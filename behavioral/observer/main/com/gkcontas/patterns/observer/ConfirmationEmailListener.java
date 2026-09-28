package com.gkcontas.patterns.observer;

import java.util.ArrayList;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ConfirmationEmailListener {

    private final List<String> sent = new ArrayList<>();

    @EventListener
    public void onOrderPlaced(OrderPlacedEvent event) {
        sent.add("confirmation to " + event.customerEmail());
    }

    public List<String> sent() {
        return List.copyOf(sent);
    }

    public void clear() {
        sent.clear();
    }
}
