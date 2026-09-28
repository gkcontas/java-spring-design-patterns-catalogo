package com.gkcontas.patterns.chainofresponsibility;

import java.math.BigDecimal;
import java.util.Optional;

public final class OrderValidators {

    private OrderValidators() {
    }

    public static ValidationHandler emailPresent() {
        return request -> request.customerEmail() == null || !request.customerEmail().contains("@")
                ? Optional.of("customer email is not valid")
                : Optional.empty();
    }

    public static ValidationHandler positiveQuantity() {
        return request -> request.quantity() <= 0
                ? Optional.of("quantity must be positive")
                : Optional.empty();
    }

    public static ValidationHandler amountWithinLimit(BigDecimal limit) {
        return request -> request.amount().compareTo(limit) > 0
                ? Optional.of("amount exceeds the limit of " + limit)
                : Optional.empty();
    }
}
