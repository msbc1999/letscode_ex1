package me.mateus.letscode.ex1.loja.catalogo;

import java.util.Set;
import me.mateus.letscode.ex1.loja.regras.Restricao;
import static me.mateus.letscode.ex1.utilitarios.Carteira.cents;

public record AlbumMusica(String nome, long valor, Set<String> artistas, Set<String> generos, Set<String> selos, Set<Restricao> restricoes) implements Mercadoria {

    @Override
    public String toString() {return "AlbumMusica{" + "nome='" + nome + '\'' + ", valor=" + cents(valor) + ", artistas=" + artistas + ", generos=" + generos + ", selos=" + selos + ", restricoes=" + restricoes + '}';}

}