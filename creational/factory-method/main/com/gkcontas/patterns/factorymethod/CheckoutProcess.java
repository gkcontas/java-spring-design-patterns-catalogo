package com.gkcontas.patterns.factorymethod;

import java.math.BigDecimal;

/**
 * The creator. Everything except <em>which</em> payment method to build is fixed here.
 *
 * <p>This is what separates Factory Method from a plain static factory: the decision is
 * deferred to a subclass through an overridable method, not taken by a switch inside a
 * helper. The base class calls {@link #createPaymentMethod()} without knowing, or being
 * able to know, what comes back.
 */
public abstract class CheckoutProcess {

    /** The factory method: the one thing subclasses supply. */
    protected abstract PaymentMethod createPaymentMethod();

    /** Fixed algorithm, varying only in the object the factory method produces. */
    public final String checkout(BigDecimal amount) {
        PaymentMethod paymentMethod = createPaymentMethod();
        return "%s | receipt issued".formatted(paymentMethod.charge(amount));
    }
}
