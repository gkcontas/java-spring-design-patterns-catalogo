package com.gkcontas.patterns.facade;

public class ShippingService {

    public String schedule(String item, String address) {
        return "SHIP-%s-%d".formatted(item, address.length());
    }
}
