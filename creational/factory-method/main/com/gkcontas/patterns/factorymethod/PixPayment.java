package com.gkcontas.patterns.factorymethod;

import java.math.BigDecimal;

public class PixPayment implements PaymentMethod {

    @Override
    public String charge(BigDecimal amount) {
        return "Requested %s over Pix".formatted(amount);
    }
}
