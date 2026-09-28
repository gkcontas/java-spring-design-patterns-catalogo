package com.gkcontas.patterns.factorymethod;

public class CreditCardCheckout extends CheckoutProcess {

    @Override
    protected PaymentMethod createPaymentMethod() {
        return new CreditCardPayment();
    }
}
