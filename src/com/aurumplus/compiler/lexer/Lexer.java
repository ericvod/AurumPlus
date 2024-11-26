package com.aurumplus.compiler.lexer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aurumplus.compiler.pass.CompilationPass;
import com.aurumplus.utils.AurumPlusFile;
import com.aurumplus.utils.FileReader;

public class Lexer extends CompilationPass<AurumPlusFile, LexerData> {
    private FileReader reader;
    private final List<Token> tokens;
    private final Map<String, TokenType> keywords;
    private StringBuilder currentLexeme;

    public Lexer() {
        this.tokens = new ArrayList<>();
        this.keywords = new HashMap<>();
        this.currentLexeme = new StringBuilder();
        initializeKeywords();
    }

    @Override
    public LexerData pass(AurumPlusFile input) {
        resetInternalState(input);
        scanTokens();
        return new LexerData(tokens);
    }

    private void resetInternalState(AurumPlusFile file) {
        this.reader = new FileReader(file.getSource());

        tokens.clear();
        currentLexeme.setLength(0);
    }

    private void initializeKeywords() {
        keywords.put("number", TokenType.NUMBER);
        keywords.put("string", TokenType.STRING);
        keywords.put("bool", TokenType.BOOL);
        keywords.put("var", TokenType.VAR);
        keywords.put("const", TokenType.CONST);
        keywords.put("mut", TokenType.MUT);
        keywords.put("static", TokenType.STATIC);
        keywords.put("fn", TokenType.FN);
        keywords.put("class", TokenType.CLASS);
        keywords.put("and", TokenType.AND);
        keywords.put("or", TokenType.OR);
        keywords.put("not", TokenType.NOT);
        keywords.put("true", TokenType.BOOL_LITERAL_TRUE);
        keywords.put("false", TokenType.BOOL_LITERAL_FALSE);
    }

    public List<Token> scanTokens() {
        while (!reader.isAtEnd()) {
            currentLexeme = new StringBuilder();
            scanToken();
        }

        tokens.add(new Token(TokenType.EOF, "", null, reader.getCurrentPosition()));
        return tokens;
    }

    private void scanToken() {
        char c = reader.advance();
        currentLexeme.append(c);

        switch (c) {
            // Operadores simples
            case '+' -> handlePlus();
            case '-' -> handleMinus();
            case '*' -> handleStar();
            case '/' -> handleSlash();
            case '%' -> addToken(TokenType.MODULO);
            case '=' -> handleEquals();
            case '>' -> handleGreater();
            case '<' -> handleLess();
            case '!' -> handleBang();

            // Delimitadores
            case '(' -> addToken(TokenType.LPAREN);
            case ')' -> addToken(TokenType.RPAREN);
            case '{' -> addToken(TokenType.LBRACE);
            case '}' -> addToken(TokenType.RBRACE);
            case ',' -> addToken(TokenType.COMMA);
            case '.' -> addToken(TokenType.DOT);
            case ';' -> addToken(TokenType.SEMICOLON);
            case ':' -> addToken(TokenType.COLON);

            // Ignorar espaços em branco
            case ' ', '\r', '\t', '\n' -> {
            }

            // String literals
            case '"' -> handleString();

            default -> {
                if (isDigit(c)) {
                    handleNumber();
                } else if (isAlpha(c)) {
                    handleIdentifier();
                } else {
                    reportError("Invalid character: '" + c + "'");
                }
            }
        }
    }

    private void handlePlus() {
        if (match('+')) {
            addToken(TokenType.INCREMENT);
        } else {
            addToken(TokenType.PLUS);
        }
    }

    private void handleMinus() {
        if (match('-')) {
            addToken(TokenType.DECREMENT);
        } else if (match('>')) {
            addToken(TokenType.ARROW);
        } else {
            addToken(TokenType.MINUS);
        }
    }

    private void handleStar() {
        if (match('*')) {
            addToken(TokenType.POWER);
        } else {
            addToken(TokenType.MULTIPLY);
        }
    }

    private void handleSlash() {
        if (match('/')) {
            while (reader.peek() != '\n' && !reader.isAtEnd()) {
                reader.advance();
            }
            currentLexeme.setLength(0);
        } else if (match('*')) {
            handleMultiLineComment();
        } else {
            addToken(TokenType.DIVIDE);
        }
    }

    private void handleMultiLineComment() {
        boolean commentClosed = false;
        while (!reader.isAtEnd()) {
            char c = reader.advance();
            if (c == '*' && reader.peek() == '/') {
                reader.advance();
                commentClosed = true;
                break;
            }
        }

        if (!commentClosed) {
            reportError("Multi-line comment not closed");
        }
        currentLexeme.setLength(0);
    }

    private void handleEquals() {
        if (match('=')) {
            addToken(TokenType.EQUALS);
        } else {
            addToken(TokenType.ASSIGN);
        }
    }

    private void handleGreater() {
        if (match('=')) {
            addToken(TokenType.GREATER_EQUAL);
        } else {
            addToken(TokenType.GREATER_THAN);
        }
    }

    private void handleLess() {
        if (match('=')) {
            addToken(TokenType.LESS_EQUAL);
        } else {
            addToken(TokenType.LESS_THAN);
        }
    }

    private void handleBang() {
        if (match('=')) {
            addToken(TokenType.NOT_EQUALS);
        } else {
            addToken(TokenType.NOT);
        }
    }

    private void handleString() {
        while (reader.peek() != '"' && !reader.isAtEnd()) {
            char c = reader.advance();
            currentLexeme.append(c);
        }

        if (reader.isAtEnd()) {
            reportError("Unclosed string");
            return;
        }

        reader.advance();
        currentLexeme.append('"');

        String value = currentLexeme.substring(1, currentLexeme.length() - 1);
        addToken(TokenType.STRING_LITERAL, value);
    }

    private void handleNumber() {
        while (isDigit(reader.peek())) {
            currentLexeme.append(reader.advance());
        }

        if (reader.peek() == '.') {
            currentLexeme.append(reader.advance());

            if (!isDigit(reader.peek())) {
                reportError("Malformed number: expected digit after decimal point");
            }

            while (isDigit(reader.peek())) {
                currentLexeme.append(reader.advance());
            }
        }

        if (reader.peek() == 'e' || reader.peek() == 'E') {
            currentLexeme.append(reader.advance());

            if (reader.peek() == '+' || reader.peek() == '-') {
                currentLexeme.append(reader.advance());
            }

            if (!isDigit(reader.peek())) {
                reportError("Malformed number: expected digit in exponent");
            }

            while (isDigit(reader.peek())) {
                currentLexeme.append(reader.advance());
            }
        }

        if (isAlpha(reader.peek())) {
            currentLexeme.append(reader.advance());
            reportError("Malformed number: invalid character '" + reader.peek() + "' in number");
        }

        String numberString = currentLexeme.toString();

        try {
            double value = Double.parseDouble(numberString);
            addToken(TokenType.NUMBER_LITERAL, value);
        } catch (NumberFormatException e) {
            reportError("Number out of range: " + numberString);
        }
    }

    private void handleIdentifier() {
        while (isAlphaNumeric(reader.peek())) {
            currentLexeme.append(reader.advance());
        }

        String text = currentLexeme.toString();
        TokenType type = keywords.get(text);

        if (type == null) {
            addToken(TokenType.IDENTIFIER);
        } else if (type == TokenType.BOOL_LITERAL_TRUE || type == TokenType.BOOL_LITERAL_FALSE) {
            addToken(type, Boolean.parseBoolean(text));
        } else {
            addToken(type);
        }
    }

    private boolean match(char expected) {
        if (reader.isAtEnd() || reader.peek() != expected) {
            return false;
        }
        currentLexeme.append(reader.advance());
        return true;
    }

    private void reportError(String message) {
        Position position = reader.getCurrentPosition();
        System.err.println("Lexical error in " + position + ": " + message);
        System.exit(1);
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String lexeme = currentLexeme.toString();
        tokens.add(new Token(type, lexeme, literal, reader.getCurrentPosition()));
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z') ||
                c == '_';
    }

    private boolean isAlphaNumeric(char c) {
        return isDigit(c) || isAlpha(c);
    }

    @Override
    public Class<AurumPlusFile> getInputType() {
        return AurumPlusFile.class;
    }

    @Override
    public Class<LexerData> getOutputType() {
        return LexerData.class;
    }

    @Override
    public String getDebugName() {
        return "Lexer Pass";
    }
}
