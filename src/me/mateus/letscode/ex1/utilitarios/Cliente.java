package me.mateus.letscode.ex1.utilitarios;

import java.time.LocalDate;
import java.util.Objects;

public final class Cliente {

    private final ID id;
    private final Carteira carteira;
    private final Documento documento;
    public Cliente(String nome, String rg, String cpf, LocalDate nascimento) {
        this.id        = ID.create();
        this.carteira  = new Carteira();
        this.documento = new Documento(nome, rg, cpf, nascimento);
    }
    public ID getID() {return id;}
    public Carteira getCarteira() {return carteira;}
    public Documento getDocumento() {return documento;}
    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(id, cliente.id);
    }
    @Override
    public int hashCode() {return Objects.hash(id);}
    @Override
    public String toString() {return "Cliente{" + "id=" + id + ", carteira=" + carteira + ", documento=" + documento + '}';}

}