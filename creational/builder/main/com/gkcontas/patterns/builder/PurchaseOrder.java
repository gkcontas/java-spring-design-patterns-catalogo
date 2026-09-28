package com.gkcontas.patterns.builder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * Built through a builder because the alternatives are worse at this size.
 *
 * <p>A constructor with eight parameters — several optional, several of the same type —
 * is the problem the pattern solves: {@code new PurchaseOrder(a, b, null, null, c, true,
 * false, d)} compiles happily with two arguments swapped. Telescoping constructors turn
 * that into a dozen overloads, and setters make the object mutable and briefly invalid.
 */
public final class PurchaseOrder {

    private final String orderNumber;
    private final String customerEmail;
    private final List<String> items;
    private final BigDecimal total;
    private final String couponCode;
    private final String shippingNotes;
    private final boolean giftWrapped;
    private final boolean expressShipping;

    private PurchaseOrder(Builder builder) {
        this.orderNumber = builder.orderNumber;
        this.customerEmail = builder.customerEmail;
        this.items = List.copyOf(builder.items);
        this.total = builder.total;
        this.couponCode = builder.couponCode;
        this.shippingNotes = builder.shippingNotes;
        this.giftWrapped = builder.giftWrapped;
        this.expressShipping = builder.expressShipping;
    }

    public static Builder builder(String orderNumber, String customerEmail) {
        return new Builder(orderNumber, customerEmail);
    }

    public String orderNumber() {
        return orderNumber;
    }

    public String customerEmail() {
        return customerEmail;
    }

    public List<String> items() {
        return items;
    }

    public BigDecimal total() {
        return total;
    }

    public String couponCode() {
        return couponCode;
    }

    public String shippingNotes() {
        return shippingNotes;
    }

    public boolean giftWrapped() {
        return giftWrapped;
    }

    public boolean expressShipping() {
        return expressShipping;
    }

    public static final class Builder {

        private final String orderNumber;
        private final String customerEmail;
        private List<String> items = List.of();
        private BigDecimal total = BigDecimal.ZERO;
        private String couponCode;
        private String shippingNotes;
        private boolean giftWrapped;
        private boolean expressShipping;

        /**
         * The mandatory fields are constructor parameters of the builder, not optional
         * {@code withX} calls. A builder that lets {@code build()} be reached without them
         * has given up the one guarantee it was supposed to provide.
         */
        private Builder(String orderNumber, String customerEmail) {
            this.orderNumber = Objects.requireNonNull(orderNumber, "orderNumber");
            this.customerEmail = Objects.requireNonNull(customerEmail, "customerEmail");
        }

        public Builder items(List<String> items) {
            this.items = List.copyOf(items);
            return this;
        }

        public Builder total(BigDecimal total) {
            this.total = total;
            return this;
        }

        public Builder couponCode(String couponCode) {
            this.couponCode = couponCode;
            return this;
        }

        public Builder shippingNotes(String shippingNotes) {
            this.shippingNotes = shippingNotes;
            return this;
        }

        public Builder giftWrapped(boolean giftWrapped) {
            this.giftWrapped = giftWrapped;
            return this;
        }

        public Builder expressShipping(boolean expressShipping) {
            this.expressShipping = expressShipping;
            return this;
        }

        public PurchaseOrder build() {
            // Cross-field rules belong here, where the object is complete. Checking them
            // in each setter is impossible: at that moment the object is still half built.
            if (expressShipping && giftWrapped) {
                throw new IllegalStateException("Express shipping cannot be gift wrapped");
            }
            if (items.isEmpty()) {
                throw new IllegalStateException("An order needs at least one item");
            }
            return new PurchaseOrder(this);
        }
    }
}
