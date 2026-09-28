package com.gkcontas.patterns.strategy;

import java.math.BigDecimal;

/** The context. It holds a strategy and never asks which one it is. */
public class ShippingCalculator {

    private final ShippingStrategy strategy;

    public ShippingCalculator(ShippingStrategy strategy) {
        this.strategy = strategy;
    }

    public BigDecimal quote(BigDecimal orderTotal, double weightKg) {
        return strategy.costFor(orderTotal, weightKg);
    }
}
