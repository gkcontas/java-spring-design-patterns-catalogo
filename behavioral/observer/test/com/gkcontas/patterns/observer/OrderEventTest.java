package com.gkcontas.patterns.observer;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {OrderService.class, ConfirmationEmailListener.class, WarehouseListener.class})
class OrderEventTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ConfirmationEmailListener emailListener;

    @Autowired
    private WarehouseListener warehouseListener;

    @BeforeEach
    void reset() {
        emailListener.clear();
        warehouseListener.clear();
    }

    @Test
    void shouldReachEveryListenerWithoutThePublisherNamingAnyOfThem() {
        orderService.placeOrder("ana@example.com", new BigDecimal("300.00"));

        // Two listeners reacted. OrderService imports neither of them — adding a third
        // would not change one line of it.
        assertThat(emailListener.sent()).containsExactly("confirmation to ana@example.com");
        assertThat(warehouseListener.picked()).hasSize(1);
    }

    @Test
    void shouldRunListenersSynchronouslyOnTheCallersThread() {
        orderService.placeOrder("bruno@example.com", new BigDecimal("50.00"));

        // The default is synchronous, and the surprise is what that implies: a slow
        // listener slows the publisher, and a listener that throws propagates back into
        // it. @Async or @TransactionalEventListener(AFTER_COMMIT) change that, on purpose.
        assertThat(emailListener.sent()).isNotEmpty();
    }
}
