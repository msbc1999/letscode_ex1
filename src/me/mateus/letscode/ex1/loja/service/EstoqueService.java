package me.mateus.letscode.ex1.loja.service;

import java.util.HashSet;
import java.util.NoSuchElementException;
import me.mateus.letscode.ex1.api.Get;
import me.mateus.letscode.ex1.api.Post;
import me.mateus.letscode.ex1.api.TakeOut;
import me.mateus.letscode.ex1.loja.catalogo.Mercadoria;
import me.mateus.letscode.ex1.loja.produto.Produto;
import static me.mateus.letscode.ex1.utils.Utils.unmodifiable;

public final class EstoqueService {

    private final HashSet<Produto<?>> produtos = new HashSet<>();
    public Post<Produto<?>> linkArmazenarProdutos() {return entrega -> entrega.forEach(produtos::add);}
    public Get<Produto<?>> linkListarProdutos() {return () -> unmodifiable(this.produtos);}
    public TakeOut<Mercadoria, Produto<?>> linkRetirarProduto() {
        return mercadoria -> {
            var iterator = this.produtos.iterator();
            while(iterator.hasNext()) {
                Produto<?> produto = iterator.next();
                if(Produto.extrairCatalogo(produto) == mercadoria) {
                    iterator.remove();
                    return produto;
                }
            }
            throw new NoSuchElementException();
        };
    }

}