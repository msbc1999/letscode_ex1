package me.mateus.letscode.ex1.loja.service;

import java.util.HashSet;
import me.mateus.letscode.ex1.api.Get;
import me.mateus.letscode.ex1.loja.catalogo.Mercadoria;
import me.mateus.letscode.ex1.loja.produto.Produto;
import static me.mateus.letscode.ex1.utils.Utils.unmodifiable;

public final class CatalogoService {

    private final Get<Produto<?>> estoque;
    public CatalogoService(Get<Produto<?>> listarEstoque) {this.estoque = listarEstoque;}
    public Get<Mercadoria> linkListarCatalogo() {
        return () -> {
            var mercadorias = new HashSet<Mercadoria>();
            for(Produto<?> produto : estoque.get()) mercadorias.add(Produto.extrairCatalogo(produto));
            return unmodifiable(mercadorias);
        };
    }

}