package com.gkcontas.patterns.decorator;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ShippingFee extends PriceDecorator {

    private final BigDecimal fee;

    public ShippingFee(PriceQuote wrapped, BigDecimal fee) {
        super(wrapped);
        this.fee = fee;
    }

    @Override
    public BigDecimal amount() {
        return wrapped.amount().add(fee).setScale(2, RoundingMode.HALF_UP);
    }
}
