package com.gkcontas.patterns.chainofresponsibility;

import java.math.BigDecimal;

public record OrderRequest(String customerEmail, String item, int quantity, BigDecimal amount) {
}
