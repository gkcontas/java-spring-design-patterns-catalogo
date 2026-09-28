package com.gkcontas.patterns.decorator;

import java.math.BigDecimal;

public record BasePrice(BigDecimal amount) implements PriceQuote {
}
