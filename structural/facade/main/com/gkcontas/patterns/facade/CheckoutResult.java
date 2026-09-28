package com.gkcontas.patterns.facade;

public record CheckoutResult(boolean success, String authorization, String tracking, String failureReason) {

    public static CheckoutResult succeeded(String authorization, String tracking) {
        return new CheckoutResult(true, authorization, tracking, null);
    }

    public static CheckoutResult failed(String reason) {
        return new CheckoutResult(false, null, null, reason);
    }
}
