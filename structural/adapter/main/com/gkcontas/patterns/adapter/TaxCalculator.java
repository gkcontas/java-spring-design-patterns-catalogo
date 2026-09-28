package com.gkcontas.patterns.adapter;

import java.math.BigDecimal;

/** The interface this application wants to depend on. */
public interface TaxCalculator {

    BigDecimal taxFor(BigDecimal amount, String state);
}
