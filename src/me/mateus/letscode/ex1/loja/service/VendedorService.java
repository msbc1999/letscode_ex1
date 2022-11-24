package me.mateus.letscode.ex1.loja.service;

import java.util.*;
import java.util.function.*;
import me.mateus.letscode.ex1.api.Get;
import me.mateus.letscode.ex1.api.Post;
import me.mateus.letscode.ex1.api.TakeOut;
import me.mateus.letscode.ex1.api.Trigger;
import me.mateus.letscode.ex1.loja.catalogo.Mercadoria;
import me.mateus.letscode.ex1.loja.produto.Produto;
import me.mateus.letscode.ex1.loja.regras.Promocao;
import me.mateus.letscode.ex1.loja.regras.Restricao;
import me.mateus.letscode.ex1.loja.service.VendedorService.Sessao.Carrinho;
import me.mateus.letscode.ex1.loja.service.VendedorService.Sessao.Catalogo;
import me.mateus.letscode.ex1.utilitarios.Carteira;
import me.mateus.letscode.ex1.utilitarios.Documento;
import static me.mateus.letscode.ex1.utilitarios.Carteira.cents;
import static me.mateus.letscode.ex1.utils.Utils.*;

public final class VendedorService {

    private final Carteira caixa, gorjeta;
    private final Get<Mercadoria> catalogo;
    private final Get<Promocao> promocoes;
    private final TakeOut<Mercadoria, Produto<?>> retirarEstoque;
    private final Post<Produto<?>> guardarEstoque;
    private final Trigger vendaRealizada;
    public VendedorService(Get<Mercadoria> linkCatalogo, Get<Promocao> linkPromocoes, TakeOut<Mercadoria, Produto<?>> linkRetirarProduto, Post<Produto<?>> linkDevolverEstoque, Trigger linkVendaRealizada) {
        this.caixa          = new Carteira();
        this.gorjeta        = new Carteira();
        this.catalogo       = linkCatalogo;
        this.promocoes      = linkPromocoes;
        this.retirarEstoque = linkRetirarProduto;
        this.guardarEstoque = linkDevolverEstoque;
        this.vendaRealizada = linkVendaRealizada;
    }
    public Supplier<String> linkValorEmCaixa() {
        return () -> cents(this.caixa.getCents());
    }
    public Iterable<Produto<?>> realizarAtendimento(Documento documento, Atendimento atendimento) throws ReembolsavelException {
        try(var sessao = new Sessao(documento)) {
            var resultado = atendimento.atender(sessao.getCarrinho(), sessao.getCatalogo());
            if(resultado.status() == Acao.COMPRAR) { /* O CLIENTE ESCOLHEU COMPRAR */
                validarCarrinho(sessao.getCarrinho());
                long total = sessao.getCarrinho().getValorTotal(); /* VALOR DAS COMPRAS */
                long valorPago = receberPagamento(resultado.pagamento, total);
                if(valorPago < total) {/* O CLIENTE PAGOU MENOS QUE O TOTAL DA COMPRA. COMPRA CANCELADA */
                    throw new ReembolsavelException("Pagamento insuficiente!", reembolsar(valorPago)); /* DEVOLVER O VALOR PAGO */
                }
                var produtos = new HashSet<Produto<?>>(); /* OS PRODUTOS DO CLIENTE */
                try {
                    separarProdutos(sessao.getCarrinho(), produtos::add); /* PEGA OS PRODUTOS DO ESTOQUE */
                } catch(Exception ex) { /* ALGO DEU ERRADO AO SEPARAR OS PRODUTOS. COMPRA CANCELADA */
                    guardarEstoque.post(produtos); /* DEVOLVE O QUE FOI RETIRADO DO ESTOQUE */
                    throw new ReembolsavelException("Estoque insuficiente!", reembolsar(valorPago)); /* DEVOLVER O VALOR PAGO */
                }
                if(valorPago > total) { /* CASO O CLIENTE TENHA PAGO MAIS QUE O TOTAL COLOCAR O EXTRA NA GORJETA */
                    this.caixa.transferir(this.gorjeta, valorPago - total);
                }
                this.vendaRealizada.perform(); /* ACIONA O GATILHO DE VENDA REALIZADA */
                return unmodifiable(produtos); /* RETORNA OS PROTUDOS QUE O CLIENTE COMPROU */
            }
        }
        return set(); /* O CLIENTE DESISTIU DA COMPRA */
    }
    private void validarCarrinho(Carrinho carrinho) throws RuntimeException {
        if(carrinho.getQuantMercadorias() == 0) throw new RuntimeException("O carrinho esta vazio!"); /* CARRINHO VAZIO */
    }
    private long receberPagamento(BiConsumer<Carteira, Long> pagamento, long valorCompras) {
        return this.caixa.recebimentoSeguro(caixaSeguro -> { /* PROTECAO CONTRA ROUBO */
            pagamento.accept(caixaSeguro, valorCompras); /* CLIENTE PAGANDO */
            return caixaSeguro.getCents(); /* RETORNA QUANTO O CLIENTE PAGOU */
        });
    }
    private void separarProdutos(Carrinho carrinho, Consumer<Produto<?>> destino) throws NoSuchElementException {
        carrinho.forEach((mercadoria, quantidade) -> {
            for(int i = 0; i < quantidade; i++) destino.accept(retirarEstoque.takeOut(mercadoria)); /* PEGA OS ITENS DO ESTOQUE USANDO O LINK */
        });
    }
    private Consumer<Carteira> reembolsar(long valor) {
        var transferir = this.caixa.pagamentoSeguro();
        return carteira -> transferir.accept(carteira, valor);
    }
    public interface Atendimento {

        AcaoCliente atender(Carrinho carrinho, Catalogo catalogo);

    }

    private enum Acao {COMPRAR, DESISTIR}

    public record AcaoCliente(Acao status, BiConsumer<Carteira, Long> pagamento) {

        public static AcaoCliente desistir() {return new AcaoCliente(Acao.DESISTIR, null);}
        public static AcaoCliente comprar(BiConsumer<Carteira, Long> pagamento) {return new AcaoCliente(Acao.COMPRAR, pagamento);}

    }

    public static class ReembolsavelException extends Exception {

        private final Consumer<Carteira> reembolsar;
        public ReembolsavelException(String mensagem, Consumer<Carteira> reembolsar) {
            super(mensagem);
            this.reembolsar = reembolsar;
        }
        public void reembolsarPara(Carteira destino) {
            this.reembolsar.accept(destino);
        }

    }

    public final class Sessao extends RecursoInterno {

        private final Documento documento;
        private final Carrinho carrinho;
        private final Catalogo catalogo;
        private Sessao(Documento documento) {
            this.documento = documento;
            this.carrinho  = new Carrinho();
            this.catalogo  = new Catalogo();
        }
        public Documento getDocumento() {return documento;}
        public Carrinho getCarrinho() {return carrinho;}
        public Catalogo getCatalogo() {return catalogo;}
        public final class Catalogo {

            public <C extends Mercadoria, T> Iterable<C> procurar(Function<C, T> parametro, Predicate<T> condicao) {
                Sessao.this.isClosed();
                var resultado = new HashSet<C>();
                for(Mercadoria mercadoria : VendedorService.this.catalogo.get()) try {if(condicao.test(parametro.apply(cast(mercadoria)))) resultado.add(cast(mercadoria));} catch(ClassCastException cce) {/* filtro incompativel */}
                return unmodifiable(resultado);
            }

        }

        public final class Carrinho extends AbstractMap<Mercadoria, Integer> {

            private final HashMap<Mercadoria, Integer> mercadorias = new HashMap<>();
            public void adicionar(Mercadoria mercadoria, int quantidade) {
                Sessao.this.isClosed();
                checkRestricao:
                {
                    var restricoes = mercadoria.restricoes().stream().filter(caso(getDocumento(), Restricao::permitir).negate()).map(Restricao::getDescricao).toArray(String[]::new);
                    if(restricoes.length == 0) break checkRestricao;
                    throw new IllegalArgumentException("Cliente nao atendeu ao(s) seguinte(s) criterios para esta mercadoria: " + String.join(", ", restricoes));
                }
                mercadorias.merge(mercadoria, quantidade, sum);
            }
            public long getValorMercadorias() {
                long total = 0L;
                for(var entry : this.mercadorias.entrySet()) total += entry.getKey().valor() * entry.getValue();
                return total;
            }
            public long getValorDescontos() {
                long desconto = 0L;
                for(var promocao : promocoes.get()) desconto += promocao.getDesconto(this.mercadorias);
                return desconto;
            }
            public long getValorTotal() {return getValorMercadorias() - getValorDescontos();}
            public int getQuantMercadorias() {return this.mercadorias.values().stream().mapToInt(intValue).sum();}

            @Override
            public Set<Entry<Mercadoria, Integer>> entrySet() {
                return unmodifiableSet(this.mercadorias.entrySet());
            }

        }

    }

    private static class RecursoInterno implements AutoCloseable {

        private boolean closed = false;
        public final void isClosed() {if(closed) throw new IllegalStateException();}
        @Override
        public final void close() {this.closed = true;}

    }

}