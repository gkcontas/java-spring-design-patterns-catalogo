package com.gkcontas.patterns.facade;

import java.math.BigDecimal;

public class PaymentGateway {

    public String authorize(String customer, BigDecimal amount) {
        return "AUTH-%s-%s".formatted(customer.hashCode(), amount);
    }
}
