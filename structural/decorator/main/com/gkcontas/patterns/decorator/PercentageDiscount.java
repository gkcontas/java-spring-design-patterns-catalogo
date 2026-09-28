package com.gkcontas.patterns.decorator;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PercentageDiscount extends PriceDecorator {

    private final BigDecimal percentage;

    public PercentageDiscount(PriceQuote wrapped, BigDecimal percentage) {
        super(wrapped);
        this.percentage = percentage;
    }

    @Override
    public BigDecimal amount() {
        BigDecimal base = wrapped.amount();
        return base.subtract(base.multiply(percentage)).setScale(2, RoundingMode.HALF_UP);
    }
}
