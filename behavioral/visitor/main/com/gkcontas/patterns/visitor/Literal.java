package com.gkcontas.patterns.visitor;

public record Literal(double value) implements Expression {

    @Override
    public <R> R accept(ExpressionVisitor<R> visitor) {
        return visitor.visitLiteral(this);
    }
}
