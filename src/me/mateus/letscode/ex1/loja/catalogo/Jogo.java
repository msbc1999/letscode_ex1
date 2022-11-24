package me.mateus.letscode.ex1.loja.catalogo;

import java.util.Set;
import me.mateus.letscode.ex1.loja.regras.Restricao;
import static me.mateus.letscode.ex1.utilitarios.Carteira.cents;

public record Jogo(String nome, long valor, String distribuidora, Set<String> generos, String estudio, Set<Restricao> restricoes) implements Mercadoria {

    @Override
    public String toString() {return "Jogo{" + "nome='" + nome + '\'' + ", valor=" + cents(valor) + ", distribuidora='" + distribuidora + '\'' + ", generos=" + generos + ", estudio='" + estudio + '\'' + ", restricoes=" + restricoes + '}';}

}