package me.mateus.letscode.ex1.loja.produto;

import java.util.Set;
import me.mateus.letscode.ex1.loja.catalogo.AlbumMusica;
import me.mateus.letscode.ex1.loja.regras.Restricao;

public final class AlbumMusicaProduto extends Produto<AlbumMusica> {

    public AlbumMusicaProduto(AlbumMusica informacoes) {super(informacoes);}
    public String nome() {return this.informacoes.nome();}
    public long valor() {return this.informacoes.valor();}
    public Set<String> artistas() {return this.informacoes.artistas();}
    public Set<String> generos() {return this.informacoes.generos();}
    public Set<String> selos() {return this.informacoes.selos();}
    public Set<Restricao> restricoes() {return this.informacoes.restricoes();}

}