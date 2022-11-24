package me.mateus.letscode.ex1.loja.produto;

import java.util.Objects;
import me.mateus.letscode.ex1.utilitarios.ID;
import me.mateus.letscode.ex1.loja.catalogo.Mercadoria;

public abstract class Produto<Catalogo extends Mercadoria> {

    private final ID id;
    protected final Catalogo informacoes;
    public Produto(Catalogo informacoes) {
        this.id = ID.create();
        this.informacoes = informacoes;
    }
    public final ID id() {return this.id;}
    @Override
    public final boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof Produto<?> produto)) return false;
        return Objects.equals(id, produto.id);
    }
    @Override
    public final int hashCode() {return Objects.hash(id);}
    @Override
    public String toString() {return "Produto{" + "id=" + id + ", informacoes=" + this.informacoes + "}";}
    public static Mercadoria extrairCatalogo(Produto<?> produto) {return produto.informacoes;}

}