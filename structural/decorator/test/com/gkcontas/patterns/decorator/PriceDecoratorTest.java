package com.gkcontas.patterns.decorator;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class PriceDecoratorTest {

    private final PriceQuote base = new BasePrice(new BigDecimal("1000.00"));

    @Test
    void shouldAccumulateThroughStackedDecorators() {
        PriceQuote quote = new SalesTax(
                new ShippingFee(
                        new PercentageDiscount(base, new BigDecimal("0.10")),
                        new BigDecimal("50.00")),
                new BigDecimal("0.18"));

        // 1000 -10% = 900, +50 shipping = 950, +18% tax = 1121.00
        assertThat(quote.amount()).isEqualByComparingTo("1121.00");
    }

    @Test
    void shouldGiveADifferentResultInADifferentOrder() {
        // Discount, then shipping, then tax: 1000 -10% = 900, +50 = 950, +18% = 1121.00
        PriceQuote discountFirst = new SalesTax(
                new ShippingFee(
                        new PercentageDiscount(base, new BigDecimal("0.10")),
                        new BigDecimal("50.00")),
                new BigDecimal("0.18"));

        // Shipping, then discount, then tax: 1000 +50 = 1050, -10% = 945, +18% = 1115.10
        PriceQuote shippingFirst = new SalesTax(
                new PercentageDiscount(
                        new ShippingFee(base, new BigDecimal("50.00")),
                        new BigDecimal("0.10")),
                new BigDecimal("0.18"));

        // Nearly six reais apart, from the same three decorators in a different order.
        //
        // Note it takes a fixed amount to show this: two percentages alone commute, so
        // discount-then-tax and tax-then-discount agree and the problem stays hidden. That
        // is what makes the edge dangerous — a stack that looks order-independent stops
        // being so the moment a flat fee joins it, and nothing in the type system says a word.
        assertThat(discountFirst.amount()).isEqualByComparingTo("1121.00");
        assertThat(shippingFirst.amount()).isEqualByComparingTo("1115.10");
        assertThat(discountFirst.amount()).isNotEqualByComparingTo(shippingFirst.amount());
    }

    @Test
    void shouldComposeWithoutAnyDecoratorKnowingTheStackDepth() {
        PriceQuote quote = base;
        for (int round = 0; round < 3; round++) {
            quote = new ShippingFee(quote, new BigDecimal("10.00"));
        }

        assertThat(quote.amount()).isEqualByComparingTo("1030.00");
    }
}
