package com.gkcontas.patterns.decorator;

import java.math.BigDecimal;

@FunctionalInterface
public interface PriceQuote {

    BigDecimal amount();
}
