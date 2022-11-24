package me.mateus.letscode.ex1.utils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.*;

public final class Utils {

    private Utils() {}
    public static ToIntFunction<Integer> intValue = Integer::intValue;
    public static BiFunction<Integer, Integer, Integer> sum = Integer::sum;
    public static <Tipo> Predicate<Tipo> igual(Tipo object) {return object::equals;}
    public static Predicate<String> igual(String string) {return string::equalsIgnoreCase;}
    public static Predicate<Long> maiorIgual(long numero) {return l -> l >= numero;}
    public static Predicate<Long> menorIgual(long numero) {return l -> l <= numero;}
    public static Predicate<Long> maior(long numero) {return l -> l > numero;}
    public static Predicate<Long> menor(long numero) {return l -> l < numero;}
    public static Predicate<Iterable<String>> listaContem(String string) {
        Predicate<String> pred = contem(string);
        return iterable -> {
            for(String s : iterable) if(pred.test(s)) return true;
            return false;
        };
    }
    public static <Tipo> Predicate<Iterable<Tipo>> listaContem(Tipo object) {
        return iterable -> {
            for(Tipo t : iterable) if(object.equals(t)) return true;
            return false;
        };
    }
    public static Predicate<String> contem(String string) {return txt -> txt.toLowerCase(Locale.ROOT).contains(string.toLowerCase(Locale.ROOT));}
    public static <Tipo> Optional<Tipo> primeiro(Iterable<Tipo> iterable) {try {return Optional.ofNullable(iterable.iterator().next());} catch(Exception ex) {return Optional.empty();}}
    public static <Tipo> Tipo[] array(Tipo... itens) {return Arrays.copyOf(itens, itens.length);}
    public static <Tipo> Iterable<Tipo> iterable(Tipo... itens) {return new WrappedIterable<>(() -> iterator((i) -> itens[i], () -> itens.length));}
    public static <Tipo> Iterator<Tipo> iterator(Function<Integer, Tipo> dataSource, Supplier<Integer> dataLength) {
        var index = new AtomicInteger();
        return new WrappedIterator<>(() -> index.get() < dataLength.get(), () -> dataSource.apply(index.getAndAdd(1)), () -> {throw new UnsupportedOperationException();});
    }
    public static <Tipo> Set<Tipo> set(Tipo... itens) {return set(i -> itens[i], () -> itens.length);}
    public static <Tipo> Set<Tipo> set(Function<Integer, Tipo> dataSource, Supplier<Integer> dataLength) {
        return new WrappedSet<>(() -> {
            var index = new AtomicInteger();
            return new WrappedIterator<>(() -> index.get() < dataLength.get(), () -> dataSource.apply(index.getAndAdd(1)), () -> {throw new UnsupportedOperationException();});
        }, dataLength);
    }
    public static <T1, T2> Predicate<T1> caso(T2 t2, BiPredicate<T1, T2> predicate) {return t1 -> predicate.test(t1, t2);}
    public static <Tipo> Iterable<Tipo> unmodifiable(Iterable<Tipo> iterable) {
        return new WrappedIterable<>(() -> {
            var iterator = iterable.iterator();
            return new WrappedIterator<>(iterator::hasNext, iterator::next, () -> {});
        });
    }
    public static <Tipo> Set<Tipo> unmodifiableSet(Set<Tipo> set) {
        return new WrappedSet<>(() -> {
            var source = set.iterator();
            return new WrappedIterator<>(source::hasNext, source::next, () -> {throw new UnsupportedOperationException();});
        }, set::size);
    }
    public static <T1, T2, R> R with(T1 item, Function<T1, T2> action, BiFunction<T1, T2, R> ending) {return ending.apply(item, action.apply(item));}
    @SuppressWarnings("unchecked")
    public static <Tipo> Tipo cast(Object obj) {return (Tipo) obj;}
    public static class WrappedSet<Tipo> extends AbstractSet<Tipo> {

        private final Supplier<Iterator<Tipo>> iterator;
        private final Supplier<Integer> size;
        public WrappedSet(Supplier<Iterator<Tipo>> iterator, Supplier<Integer> size) {
            this.iterator = iterator;
            this.size     = size;
        }
        @Override
        public Iterator<Tipo> iterator() {return this.iterator.get();}
        @Override
        public int size() {return this.size.get();}

    }

    public static class WrappedIterable<Tipo> implements Iterable<Tipo> {

        private final Supplier<Iterator<Tipo>> iterator;
        public WrappedIterable(Supplier<Iterator<Tipo>> iterator) {this.iterator = iterator;}
        @Override
        public Iterator<Tipo> iterator() {return iterator.get();}

    }

    public static class WrappedIterator<Tipo> implements Iterator<Tipo> {

        private final Supplier<Boolean> hasNext;
        private final Supplier<Tipo> next;
        private final Runnable remove;
        public WrappedIterator(Supplier<Boolean> hasNext, Supplier<Tipo> next, Runnable remove) {
            this.hasNext = hasNext;
            this.next    = next;
            this.remove  = remove;
        }
        @Override
        public boolean hasNext() {return this.hasNext.get();}
        @Override
        public Tipo next() {return this.next.get();}
        @Override
        public void remove() {this.remove.run();}

    }

}