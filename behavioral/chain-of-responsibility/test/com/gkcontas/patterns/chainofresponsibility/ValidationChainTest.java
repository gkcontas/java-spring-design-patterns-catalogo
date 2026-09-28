package com.gkcontas.patterns.chainofresponsibility;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.Test;

class ValidationChainTest {

    private final ValidationChain chain = new ValidationChain(List.of(
            OrderValidators.emailPresent(),
            OrderValidators.positiveQuantity(),
            OrderValidators.amountWithinLimit(new BigDecimal("10000"))));

    @Test
    void shouldPassARequestThatSatisfiesEveryLink() {
        OrderRequest request = new OrderRequest("ana@example.com", "keyboard", 2, new BigDecimal("300"));

        assertThat(chain.validate(request)).isEmpty();
    }

    @Test
    void shouldStopAtTheFirstRejection() {
        OrderRequest request = new OrderRequest("not-an-email", "keyboard", -1, new BigDecimal("99999"));

        // Three rules broken, one reason reported: the first. Collecting all of them would
        // be a different pattern with a different name.
        assertThat(chain.validate(request)).contains("customer email is not valid");
    }

    @Test
    void shouldNotRunLaterLinksOnceOneRejects() {
        AtomicBoolean laterLinkRan = new AtomicBoolean();
        ValidationChain shortCircuiting = new ValidationChain(List.of(
                OrderValidators.positiveQuantity(),
                request -> {
                    laterLinkRan.set(true);
                    return Optional.empty();
                }));

        shortCircuiting.validate(new OrderRequest("ana@example.com", "keyboard", 0, BigDecimal.ONE));

        // The short circuit matters when a later check is expensive, or would break on
        // data an earlier one already rejected.
        assertThat(laterLinkRan).isFalse();
    }

    @Test
    void shouldLetTheOrderChangeWithoutTouchingAnyHandler() {
        ValidationChain reordered = new ValidationChain(List.of(
                OrderValidators.positiveQuantity(),
                OrderValidators.emailPresent()));

        // Same handlers, different order, different first rejection — and not one line
        // inside a handler had to change.
        assertThat(reordered.validate(new OrderRequest("not-an-email", "x", -1, BigDecimal.ONE)))
                .contains("quantity must be positive");
    }
}
