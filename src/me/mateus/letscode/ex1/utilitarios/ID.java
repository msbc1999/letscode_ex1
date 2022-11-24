package me.mateus.letscode.ex1.utilitarios;

import java.util.Objects;

public final class ID {

    private static long disponivel = 0L;
    private final long id;
    private ID() {this.id = disponivel++;}
    @Override
    public boolean equals(Object other) {
        if(this == other) return true;
        if(other == null || getClass() != other.getClass()) return false;
        ID otherID = (ID) other;
        return this.id == otherID.id;
    }
    @Override
    public int hashCode() {return Objects.hash(id);}
    @Override
    public String toString() {return "ID{" + id + "}";}
    public static ID create() {return new ID();}

}