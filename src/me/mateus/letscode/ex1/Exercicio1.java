package me.mateus.letscode.ex1;

import java.time.LocalDate;
import me.mateus.letscode.ex1.api.Trigger;
import me.mateus.letscode.ex1.utilitarios.Carteira;
import me.mateus.letscode.ex1.utilitarios.Cliente;
import me.mateus.letscode.ex1.loja.catalogo.*;
import me.mateus.letscode.ex1.loja.service.*;
import me.mateus.letscode.ex1.loja.service.VendedorService.ReembolsavelException;
import static me.mateus.letscode.ex1.loja.regras.Promocao.LIVROS_200_15_OFF;
import static me.mateus.letscode.ex1.loja.service.VendedorService.AcaoCliente.comprar;
import static me.mateus.letscode.ex1.loja.service.VendedorService.AcaoCliente.desistir;
import static me.mateus.letscode.ex1.utilitarios.Carteira.cents;
import static me.mateus.letscode.ex1.utils.Utils.*;

public class Exercicio1 {

    private static final FornecedorService fornecedor;
    private static final EstoqueService estoque;
    private static final CatalogoService catalogo;
    private static final GestorService gestor;
    private static final VendedorService vendedor;

    private static final Trigger startup;

    static {
        fornecedor = new FornecedorService();
        estoque    = new EstoqueService();
        catalogo   = new CatalogoService(//
                estoque.linkListarProdutos()//
        );
        gestor     = new GestorService(10, 5,//
                estoque.linkListarProdutos(),//
                estoque.linkArmazenarProdutos(),//
                fornecedor.linkMercadoriasDisponiveis(),//
                fornecedor.linkFornecerProduto()//
        );
        vendedor   = new VendedorService(//
                catalogo.linkListarCatalogo(),//
                gestor.linkPromocoes(),//
                estoque.linkRetirarProduto(),//
                estoque.linkArmazenarProdutos(),//
                gestor.linkConferirEstoque()//
        );

        gestor.setPromocoes(LIVROS_200_15_OFF);

        startup = Trigger.join(//
                gestor.linkConferirFornecimento(),//
                gestor.linkConferirEstoque()//
        );
    }

    public static void main(String[] args) {
        startup.perform();

        Trigger contagemEstoque = () -> {
            System.out.println("Contagem do estoque:");
            gestor.linkContagemEstoque().get().forEach(entry -> System.out.println(entry.getValue() + "x " + entry.getKey()));
            System.out.println();
        };
        Trigger caixaLoja = () -> {
            System.out.println("caixaLoja = " + vendedor.linkValorEmCaixa().get());
            System.out.println();
        };

        contagemEstoque.perform();

        var banco = Carteira.comSaldoInicial(10000000000000000_00L);

        caixaLoja.perform();
        comprasDoJoaozinho(banco);
        caixaLoja.perform();
        comprasDaMariazinha(banco);
        caixaLoja.perform();
        comprasDoPedrinho(banco);
        caixaLoja.perform();
        comprasDaAninha(banco);
        caixaLoja.perform();

        contagemEstoque.perform();
    }
    private static void comprasDoJoaozinho(Carteira banco) {
        try {
            var joaozinho = cliente("Joaozinho", 10);

            banco.transferir(joaozinho.getCarteira(), 30_00L);

            try {
                var pagamento = joaozinho.getCarteira().pagamentoSeguro();
                var produtos = vendedor.realizarAtendimento(joaozinho.getDocumento(), (carrinho, catalogo) -> {
                    var brinquedo = primeiro(catalogo.procurar(Brinquedo::valor, menor(15_00L)));

                    brinquedo.ifPresent(b -> {
                        carrinho.adicionar(b, 1);
                    });

                    var valorTotal = carrinho.getValorTotal();
                    if(joaozinho.getCarteira().possuiSaldo(valorTotal)) return comprar((loja, total) -> {
                        if(total == valorTotal) pagamento.accept(loja, total);
                    });
                    return desistir();
                });
                System.out.println("Joaozinho comprou:");
                produtos.forEach(System.out::println);
            } catch(ReembolsavelException ex) {
                System.out.println("Problemas na compra do Joaozinho! " + ex.getMessage());
                ex.reembolsarPara(joaozinho.getCarteira());
            }
        } finally {
            System.out.println("Fim das compras do Joaozinho");
        }
        System.out.println();
    }
    private static void comprasDaMariazinha(Carteira banco) {
        try {
            var mariazinha = cliente("Mariazinha", 17);

            banco.transferir(mariazinha.getCarteira(), 50_00L);

            try {
                var pagamento = mariazinha.getCarteira().pagamentoSeguro();
                var produtos = vendedor.realizarAtendimento(mariazinha.getDocumento(), (carrinho, catalogo) -> {
                    var filmes = catalogo.procurar(Filme::valor, menor(20_00L));

                    filmes.forEach(filme -> {
                        try {
                            if(mariazinha.getCarteira().possuiSaldo(carrinho.getValorTotal() + filme.valor())) carrinho.adicionar(filme, 1);
                        } catch(Exception ex) {
                            System.out.println("Problema ao adicionar item ao carrinho de Mariazinha: " + ex.getMessage() + ". Item: " + filme);
                        }
                    });

                    var valorTotal = carrinho.getValorTotal();
                    if(mariazinha.getCarteira().possuiSaldo(valorTotal)) return comprar((loja, total) -> {
                        if(total == valorTotal) pagamento.accept(loja, total);
                    });
                    return desistir();
                });
                System.out.println("Mariazinha comprou:");
                produtos.forEach(System.out::println);
            } catch(ReembolsavelException ex) {
                System.out.println("Problemas na compra da Mariazinha! " + ex.getMessage());
                ex.reembolsarPara(mariazinha.getCarteira());
            }
        } finally {
            System.out.println("Fim das compras da Mariazinha");
        }
        System.out.println();
    }
    private static void comprasDoPedrinho(Carteira banco) {
        try {
            var pedrinho = cliente("Pedrinho", 24);

            banco.transferir(pedrinho.getCarteira(), 1000000_00L);

            try {
                var produtos = vendedor.realizarAtendimento(pedrinho.getDocumento(), (carrinho, catalogo) -> {
                    catalogo.procurar(Mercadoria::getClass, igual(Jogo.class)).forEach(jogo -> carrinho.adicionar(jogo, 20));
                    return comprar(pedrinho.getCarteira()::transferir);
                });
                System.out.println("Pedrinho comprou:");
                produtos.forEach(System.out::println);
            } catch(ReembolsavelException ex) {
                System.out.println("Problemas na compra do Pedrinho! " + ex.getMessage());
                ex.reembolsarPara(pedrinho.getCarteira());
            }
        } finally {
            System.out.println("Fim das compras do Pedrinho");
        }
        System.out.println();
    }
    private static void comprasDaAninha(Carteira banco) {
        try {
            var aninha = cliente("Aninha", 19);

            banco.transferir(aninha.getCarteira(), 1000_00L);

            try {
                var produtos = vendedor.realizarAtendimento(aninha.getDocumento(), (carrinho, catalogo) -> {
                    catalogo.procurar(Livro::generos, listaContem("Programação")).forEach(livro -> carrinho.adicionar(livro, 5));
                    System.out.println("Valor das mercadorias da compra da Aninha: " + cents(carrinho.getValorMercadorias()));
                    System.out.println("Descontos para Aninha: " + cents(carrinho.getValorDescontos()));
                    System.out.println("Valor da compra da Aninha: " + cents(carrinho.getValorTotal()));
                    return comprar(aninha.getCarteira()::transferir);
                });
                System.out.println("Aninha comprou:");
                produtos.forEach(System.out::println);
            } catch(ReembolsavelException ex) {
                System.out.println("Problemas na compra da Aninha! " + ex.getMessage());
                ex.reembolsarPara(aninha.getCarteira());
            }
        } finally {
            System.out.println("Fim das compras da Aninha");
        }
        System.out.println();
    }
    private static Cliente cliente(String nome, int idade) {return new Cliente(nome, "00.000.000-0", "000.000.000-00", LocalDate.now().minusYears(idade));}

}