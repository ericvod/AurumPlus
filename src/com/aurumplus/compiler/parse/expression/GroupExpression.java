package com.aurumplus.compiler.parse.expression;

import com.aurumplus.compiler.lexer.Token;

public class GroupExpression extends ExpressionNode {
    public final Token paren;
    public final ExpressionNode expression;

    public GroupExpression(Token paren, ExpressionNode expression) {
        this.paren = paren;
        this.expression = expression;
    }

    @Override
    public String toString() {
        return String.format("Grouping(%s)", expression);
    }
}
