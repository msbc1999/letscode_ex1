package me.mateus.letscode.ex1.loja.produto;

import java.util.Set;
import me.mateus.letscode.ex1.loja.catalogo.Brinquedo;
import me.mateus.letscode.ex1.loja.regras.Restricao;

public final class BrinquedoProduto extends Produto<Brinquedo> {

    public BrinquedoProduto(Brinquedo informacoes) {super(informacoes);}
    public String nome() {return this.informacoes.nome();}
    public long valor() {return this.informacoes.valor();}
    public String tipo() {return this.informacoes.tipo();}
    public Set<Restricao> restricoes() {return this.informacoes.restricoes();}

}