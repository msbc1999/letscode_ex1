package me.mateus.letscode.ex1.loja.catalogo;

import java.util.Set;
import me.mateus.letscode.ex1.loja.regras.Restricao;

public interface Mercadoria {

    String nome();

    long valor();

    Set<Restricao> restricoes();

}