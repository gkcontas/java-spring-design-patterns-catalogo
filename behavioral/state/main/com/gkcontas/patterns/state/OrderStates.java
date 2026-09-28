package com.gkcontas.patterns.state;

/**
 * The states, each overriding only the transitions it permits. Everything else falls
 * through to the default and is refused — so a forbidden transition is refused by
 * omission rather than by a case somebody has to remember to write.
 */
public final class OrderStates {

    public static final OrderState NEW = new OrderState() {
        @Override
        public String name() {
            return "NEW";
        }

        @Override
        public OrderState pay() {
            return PAID;
        }

        @Override
        public OrderState cancel() {
            return CANCELLED;
        }
    };

    public static final OrderState PAID = new OrderState() {
        @Override
        public String name() {
            return "PAID";
        }

        @Override
        public OrderState ship() {
            return SHIPPED;
        }

        @Override
        public OrderState cancel() {
            return CANCELLED;
        }
    };

    /** Terminal: no override at all, so every transition is refused. */
    public static final OrderState SHIPPED = () -> "SHIPPED";

    public static final OrderState CANCELLED = () -> "CANCELLED";

    private OrderStates() {
    }
}
