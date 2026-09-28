package com.gkcontas.patterns.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

public final class ShippingStrategies {

    private ShippingStrategies() {
    }

    public static ShippingStrategy standard() {
        return (orderTotal, weightKg) ->
                BigDecimal.valueOf(weightKg * 5).setScale(2, RoundingMode.HALF_UP);
    }

    public static ShippingStrategy express() {
        return (orderTotal, weightKg) ->
                BigDecimal.valueOf(weightKg * 12).add(new BigDecimal("15.00")).setScale(2, RoundingMode.HALF_UP);
    }

    /** Free above a threshold, standard below it. */
    public static ShippingStrategy freeAbove(BigDecimal threshold) {
        return (orderTotal, weightKg) -> orderTotal.compareTo(threshold) >= 0
                ? BigDecimal.ZERO.setScale(2)
                : standard().costFor(orderTotal, weightKg);
    }

    /**
     * Selection by key, which is what replaces the switch the pattern is meant to remove.
     * In Spring the same job is done by injecting {@code Map<String, ShippingStrategy>}
     * and letting the container fill it from the bean names.
     */
    public static Map<String, ShippingStrategy> byName() {
        return Map.of(
                "STANDARD", standard(),
                "EXPRESS", express(),
                "FREE_ABOVE_200", freeAbove(new BigDecimal("200")));
    }
}
