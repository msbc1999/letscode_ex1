package me.mateus.letscode.ex1.api;

import static me.mateus.letscode.ex1.utils.Utils.iterable;

@FunctionalInterface
public interface Post<T> {

    void post(Iterable<T> t);

    default void post(T... ts) {this.post(iterable(ts));}

}