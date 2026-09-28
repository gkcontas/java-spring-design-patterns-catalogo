package com.gkcontas.patterns.composite;

import java.math.BigDecimal;
import java.util.List;

/**
 * Leaf and branch behind one type, so a client can treat "a product" and "a category of
 * products" the same way.
 *
 * <p>Sealed on purpose: the set of node kinds is closed, which lets a caller switch over
 * it exhaustively without a default branch. The classic version leaves the interface open
 * and pays for it with instanceof chains that go stale when a kind is added.
 */
public sealed interface CatalogNode permits Product, Category {

    String name();

    BigDecimal totalPrice();

    int productCount();

    /** Flattened view, which is what most callers actually want from a tree. */
    List<Product> products();
}
