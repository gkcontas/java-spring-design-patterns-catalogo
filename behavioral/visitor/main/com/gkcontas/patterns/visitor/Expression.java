package com.gkcontas.patterns.visitor;

/**
 * A tiny expression tree, sealed.
 *
 * <p>Sealed changes what this pattern is for. The classic Visitor exists to add an
 * operation over a fixed hierarchy without touching every class in it — the accept/visit
 * double dispatch is a workaround for a language with no exhaustive switch over types.
 * Java 21 has one, so the workaround is no longer needed: see {@link ExpressionPrinter}
 * next to {@link ExpressionEvaluator} for the two side by side.
 */
public sealed interface Expression permits Literal, Addition, Multiplication, Negation {

    /** Kept so the classic double dispatch can be shown working. */
    <R> R accept(ExpressionVisitor<R> visitor);
}
