package com.gkcontas.patterns.observer;

import java.util.ArrayList;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class WarehouseListener {

    private final List<String> picked = new ArrayList<>();

    @EventListener
    public void onOrderPlaced(OrderPlacedEvent event) {
        picked.add(event.orderId());
    }

    public List<String> picked() {
        return List.copyOf(picked);
    }

    public void clear() {
        picked.clear();
    }
}
