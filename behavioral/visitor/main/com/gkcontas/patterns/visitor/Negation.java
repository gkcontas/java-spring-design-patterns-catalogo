package com.gkcontas.patterns.visitor;

public record Negation(Expression operand) implements Expression {

    @Override
    public <R> R accept(ExpressionVisitor<R> visitor) {
        return visitor.visitNegation(this);
    }
}
