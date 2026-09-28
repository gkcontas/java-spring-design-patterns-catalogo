package com.gkcontas.patterns.factorymethod;

import java.math.BigDecimal;

public class CreditCardPayment implements PaymentMethod {

    @Override
    public String charge(BigDecimal amount) {
        return "Charged %s to a credit card".formatted(amount);
    }
}
