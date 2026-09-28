package com.gkcontas.patterns.chainofresponsibility;

import java.util.Optional;

/**
 * One link. Each handler answers only its own question and knows nothing about the others
 * or about its position in the chain — which is what lets the order change, or a link be
 * removed, without touching any of them.
 *
 * @return the rejection reason, or empty to pass the request along
 */
@FunctionalInterface
public interface ValidationHandler {

    Optional<String> validate(OrderRequest request);
}
