package me.mateus.letscode.ex1.api;

import java.util.ArrayList;
import static me.mateus.letscode.ex1.utils.Utils.unmodifiable;

@FunctionalInterface
public interface Get<R> {

    Iterable<R> get();

    static <R> Get<R> join(Get<R>... getters) {
        return () -> {
            var result = new ArrayList<R>();
            for(Get<R> get : getters) get.get().forEach(result::add);
            return unmodifiable(result);
        };
    }

}