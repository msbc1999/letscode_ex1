package me.mateus.letscode.ex1.api;

@FunctionalInterface
public interface Linker<T, D> {

    Trigger connect(T t, Post<D> p);

}