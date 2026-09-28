package com.gkcontas.patterns.visitor;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ExpressionTest {

    // -(2 + 3) * 4
    private final Expression expression = new Multiplication(
            new Negation(new Addition(new Literal(2), new Literal(3))),
            new Literal(4));

    @Test
    void classicVisitorShouldEvaluateTheTree() {
        assertThat(expression.accept(new ExpressionEvaluator())).isEqualTo(-20.0);
    }

    @Test
    void patternMatchingShouldDoTheSameJobWithoutTheCeremony() {
        // Same traversal, no accept, no visit methods, no double dispatch.
        assertThat(ExpressionPrinter.print(expression)).isEqualTo("(-(2.0 + 3.0) * 4.0)");
    }

    @Test
    void shouldAddAnOperationWithoutTouchingAnyNodeType() {
        // This is what Visitor is for: a new operation over a hierarchy that does not
        // change. Not one node class was modified to add it.
        ExpressionVisitor<Integer> nodeCounter = new ExpressionVisitor<>() {
            @Override
            public Integer visitLiteral(Literal literal) {
                return 1;
            }

            @Override
            public Integer visitAddition(Addition addition) {
                return 1 + addition.left().accept(this) + addition.right().accept(this);
            }

            @Override
            public Integer visitMultiplication(Multiplication multiplication) {
                return 1 + multiplication.left().accept(this) + multiplication.right().accept(this);
            }

            @Override
            public Integer visitNegation(Negation negation) {
                return 1 + negation.operand().accept(this);
            }
        };

        assertThat(expression.accept(nodeCounter)).isEqualTo(6);
    }

    @Test
    void bothFormsShouldAgree() {
        Expression simple = new Addition(new Literal(1), new Multiplication(new Literal(2), new Literal(3)));

        assertThat(simple.accept(new ExpressionEvaluator())).isEqualTo(7.0);
        assertThat(ExpressionPrinter.print(simple)).isEqualTo("(1.0 + (2.0 * 3.0))");
    }
}
