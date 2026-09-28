package com.gkcontas.patterns.visitor;

/**
 * One method per node type. Adding an operation is a new implementation of this interface;
 * adding a node type breaks every implementation — which is the trade the pattern makes,
 * and the reason it fits closed hierarchies and hurts on open ones.
 */
public interface ExpressionVisitor<R> {

    R visitLiteral(Literal literal);

    R visitAddition(Addition addition);

    R visitMultiplication(Multiplication multiplication);

    R visitNegation(Negation negation);
}
