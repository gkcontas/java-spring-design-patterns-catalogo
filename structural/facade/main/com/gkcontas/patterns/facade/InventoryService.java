package com.gkcontas.patterns.facade;

import java.util.HashMap;
import java.util.Map;

public class InventoryService {

    private final Map<String, Integer> stock = new HashMap<>(Map.of("keyboard", 5, "mouse", 0));

    public boolean reserve(String item, int quantity) {
        int available = stock.getOrDefault(item, 0);
        if (available < quantity) {
            return false;
        }
        stock.put(item, available - quantity);
        return true;
    }

    public int available(String item) {
        return stock.getOrDefault(item, 0);
    }
}
