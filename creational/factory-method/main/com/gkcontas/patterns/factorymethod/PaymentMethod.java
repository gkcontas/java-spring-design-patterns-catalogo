package com.gkcontas.patterns.factorymethod;

import java.math.BigDecimal;

public interface PaymentMethod {

    String charge(BigDecimal amount);
}
