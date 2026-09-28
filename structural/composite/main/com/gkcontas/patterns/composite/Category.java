package com.gkcontas.patterns.composite;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * The branch. Its answers are its children's answers combined — which is the whole
 * pattern: the recursion lives in the structure, not in the caller.
 */
public record Category(String name, List<CatalogNode> children) implements CatalogNode {

    public Category {
        children = List.copyOf(children);
    }

    public static Category of(String name, CatalogNode... children) {
        return new Category(name, List.of(children));
    }

    @Override
    public BigDecimal totalPrice() {
        return children.stream()
                .map(CatalogNode::totalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public int productCount() {
        return children.stream().mapToInt(CatalogNode::productCount).sum();
    }

    @Override
    public List<Product> products() {
        List<Product> flattened = new ArrayList<>();
        children.forEach(child -> flattened.addAll(child.products()));
        return List.copyOf(flattened);
    }
}
