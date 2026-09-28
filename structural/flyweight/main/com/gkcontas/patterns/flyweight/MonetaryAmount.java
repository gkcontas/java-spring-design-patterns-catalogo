package com.gkcontas.patterns.flyweight;

import java.math.BigDecimal;

/**
 * Holds the extrinsic state and a reference to the shared flyweight. A million of these
 * share three Currency objects between them.
 */
public record MonetaryAmount(BigDecimal value, Currency currency) {

    public String formatted() {
        return "%s %s".formatted(currency.symbol(), value.setScale(currency.decimalPlaces()));
    }
}
