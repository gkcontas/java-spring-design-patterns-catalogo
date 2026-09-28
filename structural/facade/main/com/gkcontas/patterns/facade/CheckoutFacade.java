package com.gkcontas.patterns.facade;

import java.math.BigDecimal;

/**
 * One call in place of three, in the right order, with the failure handled.
 *
 * <p>What a facade buys is not fewer lines: it is that the <em>sequence</em> and the
 * <em>ordering rule</em> live in one place. Reserve before charging, never the reverse;
 * do not ship what was not paid for. Spread across callers, that knowledge is duplicated
 * and eventually one copy gets it wrong.
 *
 * <p>Note what it does not do: it adds no behaviour of its own, and it does not stop a
 * caller from using the three services directly when it needs something this sequence
 * does not cover. A facade that forbids that has become a bottleneck.
 */
public class CheckoutFacade {

    private final InventoryService inventory;
    private final PaymentGateway payments;
    private final ShippingService shipping;

    public CheckoutFacade(InventoryService inventory, PaymentGateway payments, ShippingService shipping) {
        this.inventory = inventory;
        this.payments = payments;
        this.shipping = shipping;
    }

    public CheckoutResult checkout(String customer, String item, int quantity,
                                   BigDecimal amount, String address) {
        if (!inventory.reserve(item, quantity)) {
            // Failing before charging is the ordering rule the facade exists to enforce.
            return CheckoutResult.failed("out of stock: " + item);
        }
        String authorization = payments.authorize(customer, amount);
        String tracking = shipping.schedule(item, address);
        return CheckoutResult.succeeded(authorization, tracking);
    }
}
