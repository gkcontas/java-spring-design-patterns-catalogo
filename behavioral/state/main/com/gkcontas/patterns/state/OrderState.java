package com.gkcontas.patterns.state;

/**
 * Each state is an object that knows which transitions it allows.
 *
 * <p>The alternative this replaces is a {@code switch (status)} repeated inside every
 * method — pay(), ship(), cancel() — each with its own list of cases. Adding a status
 * means finding all of them, and the one that gets missed fails in production rather than
 * at compile time.
 */
public interface OrderState {

    String name();

    default OrderState pay() {
        throw new IllegalStateException("Cannot pay an order that is " + name());
    }

    default OrderState ship() {
        throw new IllegalStateException("Cannot ship an order that is " + name());
    }

    default OrderState cancel() {
        throw new IllegalStateException("Cannot cancel an order that is " + name());
    }
}
