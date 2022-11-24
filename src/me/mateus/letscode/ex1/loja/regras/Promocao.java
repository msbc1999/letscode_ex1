package me.mateus.letscode.ex1.loja.regras;

import java.util.Map;
import java.util.function.Function;
import me.mateus.letscode.ex1.loja.catalogo.Livro;
import me.mateus.letscode.ex1.loja.catalogo.Mercadoria;

public enum Promocao {
    LIVROS_200_15_OFF(mercadorias -> {
        long totalGeral = 0L, totalLivros = 0L;
        for(var entry : mercadorias.entrySet()) {
            long valor = entry.getKey().valor() * entry.getValue();
            totalGeral += valor;
            if(entry.getKey() instanceof Livro) totalLivros += valor;
        }
        if(totalLivros >= 200_00L) return totalGeral * 15 / 100;
        return 0L;
    });
    private final Function<Map<Mercadoria, Integer>, Long> desconto;
    Promocao(Function<Map<Mercadoria, Integer>, Long> desconto) {this.desconto = desconto;}
    public long getDesconto(Map<Mercadoria, Integer> mercadorias) {return this.desconto.apply(mercadorias);}
}