package com.gkcontas.patterns.visitor;

/** The classic form: an operation as a visitor, reached by double dispatch. */
public class ExpressionEvaluator implements ExpressionVisitor<Double> {

    @Override
    public Double visitLiteral(Literal literal) {
        return literal.value();
    }

    @Override
    public Double visitAddition(Addition addition) {
        return addition.left().accept(this) + addition.right().accept(this);
    }

    @Override
    public Double visitMultiplication(Multiplication multiplication) {
        return multiplication.left().accept(this) * multiplication.right().accept(this);
    }

    @Override
    public Double visitNegation(Negation negation) {
        return -negation.operand().accept(this);
    }
}
