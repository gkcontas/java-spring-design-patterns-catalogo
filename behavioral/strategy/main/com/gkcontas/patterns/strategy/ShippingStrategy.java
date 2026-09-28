package com.gkcontas.patterns.strategy;

import java.math.BigDecimal;

/**
 * One method, no state — which is the shape that makes this pattern nearly free in modern
 * Java. Being a functional interface means any lambda is already a strategy.
 */
@FunctionalInterface
public interface ShippingStrategy {

    BigDecimal costFor(BigDecimal orderTotal, double weightKg);
}
