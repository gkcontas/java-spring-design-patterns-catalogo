package com.gkcontas.patterns.decorator;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SalesTax extends PriceDecorator {

    private final BigDecimal rate;

    public SalesTax(PriceQuote wrapped, BigDecimal rate) {
        super(wrapped);
        this.rate = rate;
    }

    @Override
    public BigDecimal amount() {
        BigDecimal base = wrapped.amount();
        return base.add(base.multiply(rate)).setScale(2, RoundingMode.HALF_UP);
    }
}
