package com.gkcontas.patterns.chainofresponsibility;

import java.util.List;
import java.util.Optional;

/**
 * Runs the links in order and stops at the first rejection.
 *
 * <p>Holding the chain as a list rather than each handler holding a {@code next} pointer
 * is a deliberate simplification. The linked form couples every handler to the following
 * one and makes reordering a matter of rewiring constructors; a list makes the order
 * explicit at the assembly point, where it belongs.
 */
public class ValidationChain {

    private final List<ValidationHandler> handlers;

    public ValidationChain(List<ValidationHandler> handlers) {
        this.handlers = List.copyOf(handlers);
    }

    public Optional<String> validate(OrderRequest request) {
        // Short-circuits: the first rejection wins and the rest never run. That matters
        // when a later check is expensive — or would blow up on data an earlier one rejects.
        for (ValidationHandler handler : handlers) {
            Optional<String> rejection = handler.validate(request);
            if (rejection.isPresent()) {
                return rejection;
            }
        }
        return Optional.empty();
    }
}
