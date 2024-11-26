package com.aurumplus.compiler.lexer;

public class Token {
    private final TokenType type;
    private final String lexeme;
    private final Object literal;
    private final Position startPosition;
    private final Position endPosition;

    public Token(TokenType type, String lexeme, Object literal, Position endPosition) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.startPosition = null;
        this.endPosition = endPosition;
    }

    public Token(TokenType type, String lexeme, Object literal, Position startPosition, Position endPosition) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    public TokenType getType() {
        return type;
    }

    public String getLexeme() {
        return lexeme;
    }

    public Object getLiteral() {
        return literal;
    }

    public Position getStartPosition() {
        return startPosition;
    }

    public Position getEndPosition(){
        return endPosition;
    }

    @Override
    public String toString() {
        return String.format("<type=%s, lexeme='%s', literal=%s, position=%s>",
                type, lexeme, literal, endPosition);
    }

}
