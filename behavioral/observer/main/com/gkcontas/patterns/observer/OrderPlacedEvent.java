package com.gkcontas.patterns.observer;

import java.math.BigDecimal;

public record OrderPlacedEvent(String orderId, String customerEmail, BigDecimal amount) {
}
