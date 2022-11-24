package me.mateus.letscode.ex1.loja.catalogo;

import java.util.Set;
import me.mateus.letscode.ex1.loja.regras.Restricao;
import static me.mateus.letscode.ex1.utilitarios.Carteira.cents;

public record Brinquedo(String nome, long valor, String tipo, Set<Restricao> restricoes) implements Mercadoria {

    @Override
    public String toString() {return "Brinquedo{" + "nome='" + nome + '\'' + ", valor=" + cents(valor) + ", tipo='" + tipo + '\'' + ", restricoes=" + restricoes + '}';}

}