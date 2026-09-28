package com.gkcontas.patterns.builder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class PurchaseOrderTest {

    @Test
    void shouldReadAsThePropertiesItSets() {
        PurchaseOrder order = PurchaseOrder.builder("ORD-1", "ana@example.com")
                .items(List.of("keyboard", "mouse"))
                .total(new BigDecimal("230.00"))
                .expressShipping(true)
                .build();

        // The call site names every value. The eight-argument constructor it replaces
        // would compile just as happily with two of the booleans swapped.
        assertThat(order.orderNumber()).isEqualTo("ORD-1");
        assertThat(order.items()).containsExactly("keyboard", "mouse");
        assertThat(order.expressShipping()).isTrue();
        assertThat(order.giftWrapped()).isFalse();
    }

    @Test
    void shouldEnforceCrossFieldRulesOnlyWhenTheObjectIsComplete() {
        // A setter cannot check this: at the moment giftWrapped is set, expressShipping
        // may not have been yet. build() is the first point where the question is answerable.
        assertThatThrownBy(() -> PurchaseOrder.builder("ORD-2", "bruno@example.com")
                .items(List.of("mug"))
                .expressShipping(true)
                .giftWrapped(true)
                .build())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("cannot be gift wrapped");
    }

    @Test
    void shouldRefuseToBuildAnOrderWithNoItems() {
        assertThatThrownBy(() -> PurchaseOrder.builder("ORD-3", "carla@example.com").build())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("at least one item");
    }

    @Test
    void shouldProduceAnImmutableObject() {
        List<String> mutableItems = new java.util.ArrayList<>(List.of("pen"));
        PurchaseOrder order = PurchaseOrder.builder("ORD-4", "diego@example.com")
                .items(mutableItems)
                .build();

        mutableItems.add("smuggled");

        // Defensive copy on the way in and List.copyOf on the way out: the caller's later
        // edits cannot reach inside a finished order.
        assertThat(order.items()).containsExactly("pen");
        assertThatThrownBy(() -> order.items().add("nope"))
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
