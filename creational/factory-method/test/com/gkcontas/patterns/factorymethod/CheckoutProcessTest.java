package com.gkcontas.patterns.factorymethod;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class CheckoutProcessTest {

    private static final BigDecimal AMOUNT = new BigDecimal("199.90");

    @Test
    void shouldKeepTheAlgorithmAndVaryOnlyTheProduct() {
        assertThat(new CreditCardCheckout().checkout(AMOUNT))
                .startsWith("Charged 199.90 to a credit card")
                .endsWith("receipt issued");
        assertThat(new PixCheckout().checkout(AMOUNT))
                .startsWith("Requested 199.90 over Pix")
                .endsWith("receipt issued");
    }

    @Test
    void shouldSupportANewPaymentMethodWithoutTouchingExistingCode() {
        // The benefit, demonstrated rather than asserted in prose: a new product and a new
        // creator, and nothing that already existed had to change. A switch inside a
        // static factory would have needed a new branch.
        CheckoutProcess voucherCheckout = new CheckoutProcess() {
            @Override
            protected PaymentMethod createPaymentMethod() {
                return amount -> "Redeemed a voucher worth %s".formatted(amount);
            }
        };

        assertThat(voucherCheckout.checkout(AMOUNT)).startsWith("Redeemed a voucher worth 199.90");
    }
}
