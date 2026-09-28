package com.gkcontas.patterns.visitor;

public record Multiplication(Expression left, Expression right) implements Expression {

    @Override
    public <R> R accept(ExpressionVisitor<R> visitor) {
        return visitor.visitMultiplication(this);
    }
}
