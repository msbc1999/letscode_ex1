package me.mateus.letscode.ex1.utilitarios;

import java.time.LocalDate;

public record Documento(String nome, String rg, String cpf, LocalDate nascimento) {}