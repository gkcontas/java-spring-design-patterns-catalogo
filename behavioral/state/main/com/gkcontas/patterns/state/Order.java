package com.gkcontas.patterns.state;

/** The context. It delegates every transition and contains no conditional at all. */
public class Order {

    private OrderState state = OrderStates.NEW;

    public void pay() {
        state = state.pay();
    }

    public void ship() {
        state = state.ship();
    }

    public void cancel() {
        state = state.cancel();
    }

    public String status() {
        return state.name();
    }
}
