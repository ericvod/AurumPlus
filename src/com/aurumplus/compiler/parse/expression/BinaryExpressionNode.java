package com.aurumplus.compiler.parse.expression;

import com.aurumplus.compiler.lexer.Token;

public class BinaryExpressionNode extends ExpressionNode {
    public final ExpressionNode left;
    public final Token operator;
    public final ExpressionNode right;

    public BinaryExpressionNode(ExpressionNode left, Token operator, ExpressionNode right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public String toString() {
        return String.format("Binary(%s, %s, %s)",left, operator.getType(), right);
    }
}
