package com.aurumplus.compiler.parse;

import java.util.ArrayList;
import java.util.List;

import com.aurumplus.compiler.exception.AurumPlusRuntimeException;
import com.aurumplus.compiler.lexer.LexerData;
import com.aurumplus.compiler.lexer.Token;
import com.aurumplus.compiler.lexer.TokenType;
import com.aurumplus.compiler.parse.expression.*;
import com.aurumplus.compiler.pass.CompilationPass;

public class Parser extends CompilationPass<LexerData, ParserData> {
    private int current;
    List<Token> tokens;
    List<ExpressionNode> expressions;

    @Override
    public ParserData pass(LexerData input) {
        tokens = input.getTokens();
        expressions = new ArrayList<>();

        while (!isAtEnd()) {
            expressions.add(expression());
        }

        expressions.forEach(System.out::println);

        return new ParserData();
    }

    private ExpressionNode expression() {
        return term();
    }

    private ExpressionNode term() {
        ExpressionNode left = factor();

        while (match(TokenType.MINUS, TokenType.PLUS)) {
            Token operator = previous();
            ExpressionNode right = factor();
            left = new BinaryExpressionNode(left, operator, right);
        }

        return left;
    }

    private ExpressionNode factor() {
        ExpressionNode expression = unary();

        while (match(TokenType.MULTIPLY, TokenType.DIVIDE)) {
            Token operator = previous();
            ExpressionNode right = factor();
            expression = new BinaryExpressionNode(expression, operator, right);
        }

        return expression;
    }

    private ExpressionNode unary() {
        if (match(TokenType.INCREMENT, TokenType.DECREMENT)) {
            Token operator = previous();

            ExpressionNode right = unary();
            return new UnaryExpressionNode(operator, right);
        }

        return literal();
    }

    private ExpressionNode literal() {
        if (match(TokenType.NUMBER_LITERAL)) {
            return new LiteralExpressionNode(previous());
        }

        if (match(TokenType.LPAREN)) {
            return grouping();
        }

        throw new AurumPlusRuntimeException(
                String.format("Invalid expression at line %d. error", previous().getEndPosition(), peek().getLexeme()));
    }

    private ExpressionNode grouping() {
        Token paren = previous();
        ExpressionNode expression = expression();
        consume(TokenType.RPAREN, "Exprect ')' after group expression.");

        return new GroupExpression(paren, expression);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private boolean isAtEnd() {
        return peek().getType() == TokenType.EOF;
    }

    private void advance() {
        current++;
    }

    private void consume(TokenType type, String message) {
        if (!match(type)) {
            throw new AurumPlusRuntimeException(message);
        }
    }

    private boolean match(TokenType... types) {
        if (check(types)) {
            advance();
            return true;
        }
        return false;
    }

    private boolean check(TokenType... types) {
        for (TokenType type : types) {
            if (peek().getType() == type) {
                return true;
            }
        }
        return false;
    }

    private Token peek() {
        return tokens.get(current);
    }

    @Override
    public Class<LexerData> getInputType() {
        return LexerData.class;
    }

    @Override
    public Class<ParserData> getOutputType() {
        return ParserData.class;
    }

    @Override
    public String getDebugName() {
        return "Parse Pass";
    }
}