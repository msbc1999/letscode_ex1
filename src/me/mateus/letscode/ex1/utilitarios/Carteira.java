package me.mateus.letscode.ex1.utilitarios;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.Function;

public final class Carteira {

    private final ID id = ID.create();
    private long cents = 0L;
    public ID getID() {return id;}
    public long getCents() {return cents;}
    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Carteira carteira = (Carteira) o;
        return Objects.equals(id, carteira.id);
    }
    @Override
    public int hashCode() {return Objects.hash(id);}
    @Override
    public String toString() {return "Carteira{" + "id=" + id + ", cents=" + cents + '}';}
    public boolean possuiSaldo(long quant) {return this.cents >= quant;}
    public void transferir(Carteira carteira, long quant) {
        if(quant <= 0) throw new IllegalArgumentException("Valor menor ou igual a 0");
        if(!possuiSaldo(quant)) throw new IllegalArgumentException("Carteira com saldo insuficiente");
        this.cents -= quant;
        carteira.cents += quant;
    }
    public void transferirTudo(Carteira carteira) {if(this.cents > 0) transferir(carteira, this.cents);}
    public <Retorno> Retorno recebimentoSeguro(Function<Carteira, Retorno> protegido) {
        var seguro = new Carteira();
        try {return protegido.apply(seguro);} finally {seguro.transferirTudo(this);}
    }
    public BiConsumer<Carteira, Long> pagamentoSeguro() {
        var permitir = new AtomicBoolean(true);
        return (carteira, quant) -> {
            if(permitir.getAndSet(false)) this.transferir(carteira, quant);
            else throw new IllegalStateException("Apenas um pagamento permitido");
        };
    }
    public static Carteira comSaldoInicial(long cents) {
        final var carteira = new Carteira();
        carteira.cents = cents;
        return carteira;
    }
    public static String cents(long cents) {
        return BigDecimal.valueOf(cents, 2).toPlainString();
    }

}