package com.aurumplus.compiler.parse.expression;

import com.aurumplus.compiler.lexer.Token;

public class LiteralExpressionNode extends ExpressionNode {
    public final Token literal;

    public LiteralExpressionNode(Token literal) {
        this.literal = literal;
    }

    @Override
    public String toString() {
        return String.format("Literal(%s)", literal.getLiteral());
    }
}
