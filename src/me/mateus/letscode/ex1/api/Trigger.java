package me.mateus.letscode.ex1.api;

@FunctionalInterface
public interface Trigger {

    void perform();

    static Trigger join(Trigger... triggers) {return () -> {for(Trigger trigger : triggers) trigger.perform();};}

    static <T> Trigger pump(Get<T> from, Post<T> to) {return () -> to.post(from.get());}

}