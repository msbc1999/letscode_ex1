package me.mateus.letscode.ex1.api;

import java.util.concurrent.atomic.AtomicInteger;
import me.mateus.letscode.ex1.utils.Utils.WrappedIterable;
import me.mateus.letscode.ex1.utils.Utils.WrappedIterator;

@FunctionalInterface
public interface Generate<T> {

    T produce();

    default Iterable<T> produce(int amount) {
        return new WrappedIterable<>(() -> {
            var current = new AtomicInteger(0);
            return new WrappedIterator<>(() -> current.get() < amount, () -> {
                current.incrementAndGet();
                return produce();
            }, () -> {throw new UnsupportedOperationException();});
        });
    }

}