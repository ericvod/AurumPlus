package com.aurumplus.compiler.lexer;

import java.util.List;

import com.aurumplus.compiler.components.IOComponent;

public class LexerData extends IOComponent<LexerData> {
    private List<Token> tokens;

    public LexerData(List<Token> tokens) {
        this.tokens = tokens;
    }

    public List<Token> getTokens() {
        return tokens;
    }
}
