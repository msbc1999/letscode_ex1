package me.mateus.letscode.ex1.loja.regras;

import java.time.LocalDate;
import java.util.function.Predicate;
import me.mateus.letscode.ex1.utilitarios.Documento;

public enum Restricao {
    MAIOR_18_ANOS("maior de 18 anos", documento -> documento.nascimento().until(LocalDate.now()).getYears() >= 18);
    private final String descricao;
    private final Predicate<Documento> permitir;
    Restricao(String descricao, Predicate<Documento> permitir) {
        this.descricao = descricao;
        this.permitir  = permitir;
    }
    public String getDescricao() {return descricao;}
    public boolean permitir(Documento documento) {return this.permitir.test(documento);}
    @Override
    public String toString() {return getDescricao();}
}