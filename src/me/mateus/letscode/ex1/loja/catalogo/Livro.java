package me.mateus.letscode.ex1.loja.catalogo;

import java.util.Set;
import me.mateus.letscode.ex1.loja.regras.Restricao;
import static me.mateus.letscode.ex1.utilitarios.Carteira.cents;

public record Livro(String nome, long valor, Set<String> generos, String escritor, String editora, Set<Restricao> restricoes) implements Mercadoria {

    @Override
    public String toString() {return "Livro{" + "nome='" + nome + '\'' + ", valor=" + cents(valor) + ", generos=" + generos + ", escritor='" + escritor + '\'' + ", editora='" + editora + '\'' + ", restricoes=" + restricoes + '}';}

}