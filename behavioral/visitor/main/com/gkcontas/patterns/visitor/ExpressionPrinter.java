package com.gkcontas.patterns.visitor;

/**
 * The same job without the pattern, using Java 21.
 *
 * <p>No accept, no visit methods, no double dispatch — and because the hierarchy is
 * sealed, the switch needs no {@code default} and the compiler refuses to build if a node
 * type is added and not handled here. That is the exhaustiveness guarantee the classic
 * Visitor bought by making every node implement accept.
 *
 * <p>Record deconstruction patterns go one step further: the children are bound inline
 * rather than fetched with accessor calls.
 */
public final class ExpressionPrinter {

    private ExpressionPrinter() {
    }

    public static String print(Expression expression) {
        return switch (expression) {
            case Literal(double value) -> String.valueOf(value);
            case Addition(Expression left, Expression right) ->
                    "(%s + %s)".formatted(print(left), print(right));
            case Multiplication(Expression left, Expression right) ->
                    "(%s * %s)".formatted(print(left), print(right));
            case Negation(Expression operand) -> "-%s".formatted(print(operand));
        };
    }
}
