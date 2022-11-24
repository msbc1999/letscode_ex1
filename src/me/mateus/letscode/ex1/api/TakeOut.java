package me.mateus.letscode.ex1.api;

import java.util.NoSuchElementException;

@FunctionalInterface
public interface TakeOut<T, R> {

    R takeOut(T t) throws NoSuchElementException;

    static <T, R> TakeOut<T, R> join(TakeOut<T, R>... takeOuts) {
        return t -> {
            try {for(var takeOut : takeOuts) return takeOut.takeOut(t);} catch(NoSuchElementException ignored) {}
            throw new NoSuchElementException();
        };
    }

}