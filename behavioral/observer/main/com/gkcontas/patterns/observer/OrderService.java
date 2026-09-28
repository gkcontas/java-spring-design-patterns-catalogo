package com.gkcontas.patterns.observer;

import java.math.BigDecimal;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * The publisher, and the measure of the pattern: it names no listener.
 *
 * <p>Without the indirection this method would call the mailer, the warehouse and the
 * analytics collector by hand, which means it depends on all three and changes every time
 * a fourth is added. Here it depends on the event type alone.
 */
@Service
public class OrderService {

    private final ApplicationEventPublisher events;

    public OrderService(ApplicationEventPublisher events) {
        this.events = events;
    }

    public String placeOrder(String customerEmail, BigDecimal amount) {
        String orderId = "ORD-" + Math.abs(customerEmail.hashCode());
        events.publishEvent(new OrderPlacedEvent(orderId, customerEmail, amount));
        return orderId;
    }
}
