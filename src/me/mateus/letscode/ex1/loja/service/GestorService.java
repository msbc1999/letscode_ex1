package me.mateus.letscode.ex1.loja.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import me.mateus.letscode.ex1.api.Get;
import me.mateus.letscode.ex1.api.Linker;
import me.mateus.letscode.ex1.api.Post;
import me.mateus.letscode.ex1.api.Trigger;
import me.mateus.letscode.ex1.loja.catalogo.Mercadoria;
import me.mateus.letscode.ex1.loja.produto.Produto;
import me.mateus.letscode.ex1.loja.regras.Promocao;
import static java.util.Collections.unmodifiableMap;
import static me.mateus.letscode.ex1.loja.produto.Produto.extrairCatalogo;
import static me.mateus.letscode.ex1.utils.Utils.*;

public final class GestorService {

    private final HashMap<Mercadoria, Trigger> contratos = new HashMap<>();
    private final int estoquePorMercadoria, pedidoMinimo;
    private final Get<Produto<?>> estoque;
    private final Post<Produto<?>> entrega;
    private final Get<Mercadoria> mercadorias;
    private final Linker<Mercadoria, Produto<?>> fornecer;
    private Set<Promocao> promocoes;
    public GestorService(int estoquePorMercadoria, int pedidoMinimo, Get<Produto<?>> linkEstoque, Post<Produto<?>> linkEntrega, Get<Mercadoria> linkListarMercadoriasFornecedor, Linker<Mercadoria, Produto<?>> linkFornecerProduto) {
        this.estoquePorMercadoria = estoquePorMercadoria;
        this.pedidoMinimo         = pedidoMinimo;
        this.estoque              = linkEstoque;
        this.entrega              = linkEntrega;
        this.mercadorias          = linkListarMercadoriasFornecedor;
        this.fornecer             = linkFornecerProduto;
        this.promocoes            = set();
    }
    public Trigger linkConferirEstoque() {return this::provisionarEstoque;}
    public Trigger linkConferirFornecimento() {return this::provisionarFornecimento;}
    public Get<Entry<Mercadoria, Integer>> linkContagemEstoque() {return () -> unmodifiable(this.contarMercadorias().entrySet());}
    public Get<Promocao> linkPromocoes() {return () -> unmodifiable(this.promocoes);}
    public void setPromocoes(Promocao... promocoes) {this.promocoes = set(promocoes);}
    private void provisionarFornecimento() {
        this.contratos.clear();
        for(var mercadoria : this.mercadorias.get()) this.contratos.put(mercadoria, this.fornecer.connect(mercadoria, this.entrega));
    }
    private void provisionarEstoque() {
        var diferencaEstoque = diferencaEstoque();
        var diferenca = diferencaEstoque.values().stream().mapToInt(intValue).sum();
        if(diferenca >= this.pedidoMinimo) diferencaEstoque.forEach(this::solicitarMercadoria);
    }
    private Map<Mercadoria, Integer> contarMercadorias() {
        var contagem = new HashMap<Mercadoria, Integer>();
        for(var mercadoria : this.contratos.keySet()) contagem.put(mercadoria, 0);
        for(var produto : this.estoque.get()) contagem.merge(extrairCatalogo(produto), 1, sum);
        return unmodifiableMap(contagem);
    }
    private Map<Mercadoria, Integer> diferencaEstoque() {
        var diferenca = new HashMap<>(contarMercadorias());
        diferenca.replaceAll((mercadoria, quantidade) -> this.estoquePorMercadoria - quantidade);
        return unmodifiableMap(diferenca);
    }
    private void solicitarMercadoria(Mercadoria mercadoria, int quantidade) {
        var solicitar = this.contratos.get(mercadoria);
        for(int i = 0; i < quantidade; i++) solicitar.perform();
    }

}