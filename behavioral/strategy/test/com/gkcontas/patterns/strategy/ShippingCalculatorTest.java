package com.gkcontas.patterns.strategy;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ShippingCalculatorTest {

    @Test
    void shouldQuoteAccordingToTheStrategyItWasGiven() {
        BigDecimal total = new BigDecimal("100.00");

        assertThat(new ShippingCalculator(ShippingStrategies.standard()).quote(total, 2))
                .isEqualByComparingTo("10.00");
        assertThat(new ShippingCalculator(ShippingStrategies.express()).quote(total, 2))
                .isEqualByComparingTo("39.00");
    }

    @Test
    void shouldAcceptANewStrategyWithoutTouchingExistingCode() {
        // Open/closed, demonstrated: a strategy that did not exist when the calculator was
        // written, added as a lambda, and not a line of ShippingCalculator changed.
        ShippingStrategy flatRate = (orderTotal, weightKg) -> new BigDecimal("19.90");

        assertThat(new ShippingCalculator(flatRate).quote(new BigDecimal("5000"), 40))
                .isEqualByComparingTo("19.90");
    }

    @Test
    void shouldSelectByKeyInsteadOfSwitching() {
        Map<String, ShippingStrategy> strategies = ShippingStrategies.byName();

        // The map is what replaces the switch the pattern exists to remove. In Spring the
        // container fills this map from the bean names.
        assertThat(strategies.get("EXPRESS").costFor(new BigDecimal("100"), 1))
                .isEqualByComparingTo("27.00");
        assertThat(strategies).containsOnlyKeys("STANDARD", "EXPRESS", "FREE_ABOVE_200");
    }

    @Test
    void shouldLetAStrategyDependOnTheInputItReceives() {
        ShippingStrategy freeAbove200 = ShippingStrategies.freeAbove(new BigDecimal("200"));

        assertThat(freeAbove200.costFor(new BigDecimal("250"), 3)).isEqualByComparingTo("0.00");
        assertThat(freeAbove200.costFor(new BigDecimal("150"), 3)).isEqualByComparingTo("15.00");
    }
}
