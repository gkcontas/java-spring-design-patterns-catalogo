package com.gkcontas.patterns.composite;

import java.math.BigDecimal;
import java.util.List;

/** The leaf. */
public record Product(String name, BigDecimal price) implements CatalogNode {

    @Override
    public BigDecimal totalPrice() {
        return price;
    }

    @Override
    public int productCount() {
        return 1;
    }

    @Override
    public List<Product> products() {
        return List.of(this);
    }
}
