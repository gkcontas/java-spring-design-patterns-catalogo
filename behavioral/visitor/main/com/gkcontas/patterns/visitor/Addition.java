package com.gkcontas.patterns.visitor;

public record Addition(Expression left, Expression right) implements Expression {

    @Override
    public <R> R accept(ExpressionVisitor<R> visitor) {
        return visitor.visitAddition(this);
    }
}
