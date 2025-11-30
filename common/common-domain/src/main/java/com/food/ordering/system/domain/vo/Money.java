package com.food.ordering.system.domain.vo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;


@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Money {
    private final BigDecimal amount;

    public boolean isGreaterThanZero() {
        return this.amount != null && this.amount.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isGreaterThan(Money money) {
        return this.amount != null && this.amount.compareTo(money.getAmount()) > 0;
    }

    public Money add(Money money) {
        BigDecimal addAmount = this.amount.add(money.getAmount());
        return new Money(setScale(addAmount));
    }

    public Money subtract(Money money) {
        BigDecimal subtract = this.amount.subtract(money.getAmount());
        return new Money(setScale(subtract));
    }

    public Money multiply(int multiplier) {
        BigDecimal multiplyAmount = this.amount.multiply(new BigDecimal(multiplier));
        return new Money(setScale(multiplyAmount));
    }

    private BigDecimal setScale(BigDecimal input) {
        return input.setScale(2, RoundingMode.HALF_EVEN);
    }
}