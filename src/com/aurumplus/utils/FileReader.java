package com.aurumplus.utils;

import com.aurumplus.compiler.lexer.Position;

public class FileReader {
    private String source;
    private int position = 0;
    private int line = 1;
    private int column = 0;

    public FileReader(String source) {
        this.source = source;
    }

    public char peek() {
        if (isAtEnd()) {
            return '\0';
        }
        return source.charAt(position);
    }

    public char advance() {
        char current = peek();
        position++;
        if (current == '\n') {
            line++;
            column = 0;
        } else {
            column++;
        }
        return current;
    }

    public void rewind() {
        if (position > 0) {
            position--;
            if (source.charAt(position) == '\n') {
                line--;
                int temp = position - 1;
                while (temp >= 0 && source.charAt(temp) != '\n') {
                    temp--;
                }
                column = position - temp - 1;
            } else {
                column--;
            }
        }
    }

    public boolean isAtEnd() {
        return position >= source.length();
    }

    public Position getCurrentPosition() {
        return new Position(line, column);
    }
}
