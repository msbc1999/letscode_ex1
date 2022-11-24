package me.mateus.letscode.ex1.loja.produto;

import java.util.Set;
import me.mateus.letscode.ex1.loja.catalogo.Filme;
import me.mateus.letscode.ex1.loja.regras.Restricao;

public final class FilmeProduto extends Produto<Filme> {

    public FilmeProduto(Filme informacoes) {super(informacoes);}
    public String nome() {return this.informacoes.nome();}
    public long valor() {return this.informacoes.valor();}
    public String estudio() {return this.informacoes.estudio();}
    public Set<String> diretores() {return this.informacoes.diretores();}
    public Set<String> generos() {return this.informacoes.generos();}
    public Set<String> produtores() {return this.informacoes.produtores();}
    public Set<Restricao> restricoes() {return this.informacoes.restricoes();}

}