package com.food.ordering.system.domain.vo;

import java.math.BigDecimal;

public class DomainTestMain {

    public static void main(String[] args) {
        Money money1 = new Money(BigDecimal.valueOf(10));
        Money money2 = new Money(BigDecimal.valueOf(10));

        System.out.println(money1.equals(money2));
    }
}
