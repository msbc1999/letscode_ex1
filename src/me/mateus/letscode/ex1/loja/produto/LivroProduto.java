package me.mateus.letscode.ex1.loja.produto;

import java.util.Set;
import me.mateus.letscode.ex1.loja.catalogo.Livro;
import me.mateus.letscode.ex1.loja.regras.Restricao;

public final class LivroProduto extends Produto<Livro> {

    public LivroProduto(Livro informacoes) {super(informacoes);}
    public String nome() {return this.informacoes.nome();}
    public long valor() {return this.informacoes.valor();}
    public Set<String> generos() {return this.informacoes.generos();}
    public String escritor() {return this.informacoes.escritor();}
    public String editora() {return this.informacoes.editora();}
    public Set<Restricao> restricoes() {return this.informacoes.restricoes();}

}