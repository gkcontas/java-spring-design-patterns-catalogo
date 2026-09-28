package com.gkcontas.patterns.state;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class OrderTest {

    private final Order order = new Order();

    @Test
    void shouldWalkTheHappyPath() {
        assertThat(order.status()).isEqualTo("NEW");
        order.pay();
        assertThat(order.status()).isEqualTo("PAID");
        order.ship();
        assertThat(order.status()).isEqualTo("SHIPPED");
    }

    @Test
    void shouldRefuseATransitionTheCurrentStateDoesNotAllow() {
        // NEW does not override ship(), so it is refused by omission — nobody had to
        // remember to write a case for it.
        assertThatThrownBy(order::ship)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Cannot ship an order that is NEW");
    }

    @Test
    void shouldMakeTerminalStatesTrulyTerminal() {
        order.pay();
        order.ship();

        assertThatThrownBy(order::cancel).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(order::pay).isInstanceOf(IllegalStateException.class);
        assertThat(order.status()).isEqualTo("SHIPPED");
    }

    @Test
    void shouldKeepTheContextFreeOfConditionals() {
        order.cancel();

        // Order has no switch and no if. The rule about what may follow what lives in the
        // states, which is the whole point of moving it out of the context.
        assertThat(order.status()).isEqualTo("CANCELLED");
        assertThatThrownBy(order::pay).isInstanceOf(IllegalStateException.class);
    }
}
