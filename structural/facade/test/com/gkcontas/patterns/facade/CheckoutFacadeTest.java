package com.gkcontas.patterns.facade;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class CheckoutFacadeTest {

    private final InventoryService inventory = new InventoryService();
    private final CheckoutFacade facade =
            new CheckoutFacade(inventory, new PaymentGateway(), new ShippingService());

    @Test
    void shouldRunTheWholeSequenceFromOneCall() {
        CheckoutResult result = facade.checkout(
                "ana", "keyboard", 1, new BigDecimal("150.00"), "Rua A, 100");

        assertThat(result.success()).isTrue();
        assertThat(result.authorization()).startsWith("AUTH-");
        assertThat(result.tracking()).startsWith("SHIP-");
        assertThat(inventory.available("keyboard")).isEqualTo(4);
    }

    @Test
    void shouldNotChargeWhenTheStockCheckFails() {
        CheckoutResult result = facade.checkout(
                "bruno", "mouse", 1, new BigDecimal("80.00"), "Rua B, 200");

        // The ordering rule, enforced in one place instead of remembered at each call site.
        assertThat(result.success()).isFalse();
        assertThat(result.failureReason()).contains("out of stock");
        assertThat(result.authorization()).isNull();
    }

    @Test
    void shouldLeaveTheSubsystemsReachableForCallersThatNeedThem() {
        // A facade simplifies the common path; it does not seal the subsystem off. One
        // that forbids direct access has stopped being a convenience and become a
        // bottleneck that every new requirement has to be threaded through.
        assertThat(inventory.available("keyboard")).isEqualTo(5);
    }
}
