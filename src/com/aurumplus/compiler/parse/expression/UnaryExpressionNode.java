package com.aurumplus.compiler.parse.expression;

import com.aurumplus.compiler.lexer.Token;

public class UnaryExpressionNode extends ExpressionNode {
    public final Token operator;
    public final ExpressionNode expression;

    public UnaryExpressionNode(Token operator, ExpressionNode expression) {
        this.operator = operator;
        this.expression = expression;
    }

    @Override
    public String toString() {
        return String.format("Unary(%s, %s)", operator.getType(), expression);
    }
}
