package com.gkcontas.patterns.composite;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class CatalogNodeTest {

    private final CatalogNode catalog = Category.of("store",
            Category.of("peripherals",
                    new Product("keyboard", new BigDecimal("150.00")),
                    new Product("mouse", new BigDecimal("80.00"))),
            Category.of("displays",
                    Category.of("ultrawide", new Product("34 inch", new BigDecimal("2500.00"))),
                    new Product("24 inch", new BigDecimal("900.00"))),
            new Product("cable", new BigDecimal("30.00")));

    @Test
    void shouldTreatALeafAndABranchThroughTheSameInterface() {
        CatalogNode leaf = new Product("cable", new BigDecimal("30.00"));

        // The caller asks the same questions of both, which is the entire benefit.
        assertThat(leaf.totalPrice()).isEqualByComparingTo("30.00");
        assertThat(leaf.productCount()).isEqualTo(1);
        assertThat(catalog.productCount()).isEqualTo(5);
    }

    @Test
    void shouldAggregateThroughArbitraryDepthWithoutTheCallerRecursing() {
        // 150 + 80 + 2500 + 900 + 30 — including the category nested two levels down.
        // No loop, no recursion and no knowledge of the shape at the call site.
        assertThat(catalog.totalPrice()).isEqualByComparingTo("3660.00");
    }

    @Test
    void shouldFlattenTheTree() {
        assertThat(catalog.products()).extracting(Product::name)
                .containsExactly("keyboard", "mouse", "34 inch", "24 inch", "cable");
    }

    @Test
    void shouldAllowExhaustiveHandlingBecauseTheHierarchyIsSealed() {
        // No default branch, and the compiler enforces that every kind is covered. Adding
        // a third node type would break this switch at compile time rather than at runtime.
        String describe = switch (catalog) {
            case Product product -> "leaf " + product.name();
            case Category category -> "branch with %d children".formatted(category.children().size());
        };

        assertThat(describe).isEqualTo("branch with 3 children");
    }
}
