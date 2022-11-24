package me.mateus.letscode.ex1.loja.produto;

import java.util.Set;
import me.mateus.letscode.ex1.loja.catalogo.Jogo;
import me.mateus.letscode.ex1.loja.regras.Restricao;

public final class JogoProduto extends Produto<Jogo> {

    public JogoProduto(Jogo informacoes) {super(informacoes);}
    public String nome() {return this.informacoes.nome();}
    public long valor() {return this.informacoes.valor();}
    public String distribuidora() {return this.informacoes.distribuidora();}
    public Set<String> generos() {return this.informacoes.generos();}
    public String estudio() {return this.informacoes.estudio();}
    public Set<Restricao> restricoes() {return this.informacoes.restricoes();}

}