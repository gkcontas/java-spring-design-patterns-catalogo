package com.gkcontas.patterns.factorymethod;

public class PixCheckout extends CheckoutProcess {

    @Override
    protected PaymentMethod createPaymentMethod() {
        return new PixPayment();
    }
}
