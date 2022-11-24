package me.mateus.letscode.ex1.loja.catalogo;

import java.util.Set;
import me.mateus.letscode.ex1.loja.regras.Restricao;
import static me.mateus.letscode.ex1.utilitarios.Carteira.cents;

public record Filme(String nome, long valor, String estudio, Set<String> diretores, Set<String> generos, Set<String> produtores, Set<Restricao> restricoes) implements Mercadoria {

    @Override
    public String toString() {return "Filme{" + "nome='" + nome + '\'' + ", valor=" + cents(valor) + ", estudio='" + estudio + '\'' + ", diretores=" + diretores + ", generos=" + generos + ", produtores=" + produtores + ", restricoes=" + restricoes + '}';}

}