package com.aurumplus.compiler.lexer;

public enum TokenType {
    // Palavras-chave
    NUMBER,
    STRING,
    BOOL,
    VAR,
    CONST,
    MUT,
    STATIC,
    FN,
    CLASS,

    // Operadores matemáticos
    PLUS, // +
    MINUS, // -
    MULTIPLY, // *
    DIVIDE, // /
    MODULO, // %
    POWER, // **
    INCREMENT, // ++
    DECREMENT, // --

    // Operadores lógicos
    AND, // and
    OR, // or
    NOT, // not, !

    // Operadores de comparação
    EQUALS, // ==
    NOT_EQUALS, // !=
    GREATER_THAN, // >
    LESS_THAN, // <
    GREATER_EQUAL, // >=
    LESS_EQUAL, // <=

    // Simbolos especiais
    ASSIGN, // =
    LPAREN, // (
    RPAREN, // )
    LBRACE, // {
    RBRACE, // }
    COMMA, // ,
    DOT, // .
    SEMICOLON, // ;
    COLON, // :
    ARROW, // ->

    // Tipos de valores
    NUMBER_LITERAL,
    STRING_LITERAL,
    BOOL_LITERAL_TRUE,
    BOOL_LITERAL_FALSE,
    IDENTIFIER,

    // Especiais
    EOF, // Fim do arquivo
}
