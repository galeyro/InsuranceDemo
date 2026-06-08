package edu.softkau.dev.insurancebackend.policy.domain.model;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

public final class Money {
    private final BigDecimal amount;
    private final Currency currency;

    public Money(BigDecimal amount, Currency currency){
        Objects.requireNonNull(amount, "El monto no puede ser nulo");
        Objects.requireNonNull(currency, "La moneda no puede ser nula");

        this.amount = amount;
        this.currency = currency;
    }

    public static Money usd(double amount){
        return new Money(BigDecimal.valueOf(amount), Currency.getInstance("USD"));
    }

    public boolean isGreaterThanZero(){
        return this.amount.compareTo(BigDecimal.ZERO) > 0;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null  || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        // Usamos compareTo en BigDecimal porque .equals() compara también la escala (ej. 1.0 vs 1.00)
        return amount.compareTo(money.amount) == 0 && currency.equals(money.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }

    @Override
    public String toString() {
        return amount + " " + currency.getCurrencyCode();
    }
}
