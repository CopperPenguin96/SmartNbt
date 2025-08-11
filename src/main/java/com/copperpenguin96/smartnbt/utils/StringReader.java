// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT license.

package com.copperpenguin96.smartnbt.utils;

import com.copperpenguin96.smartnbt.NbtFormatException;

public class StringReader implements ImmutableStringReader {
    private static final char SYNTAX_ESCAPE = '\\';
    private static final char SYNTAX_DOUBLE_QUOTE = '"';
    private static final char SYNTAX_SINGLE_QUOTE = '\'';

    private final String string;
    private int cursor;

    public StringReader(final StringReader other) {
        this.string = other.string;
        this.cursor = other.cursor;
    }

    public StringReader(final String string) {
        this.string = string;
    }

    @Override
    public String getString() {
        return string;
    }

    public void setCursor(final int cursor) {
        this.cursor = cursor;
    }

    @Override
    public int getRemainingLength() {
        return string.length() - cursor;
    }

    @Override
    public int getTotalLength() {
        return string.length();
    }

    @Override
    public int getCursor() {
        return cursor;
    }

    @Override
    public String getRead() {
        return string.substring(0, cursor);
    }

    @Override
    public String getRemaining() {
        return string.substring(cursor);
    }

    @Override
    public boolean canRead(final int length) {
        return cursor + length <= string.length();
    }

    @Override
    public boolean canRead() {
        return canRead(1);
    }

    @Override
    public char peek() {
        return string.charAt(cursor);
    }

    @Override
    public char peek(final int offset) {
        return string.charAt(cursor + offset);
    }

    public char read() {
        return string.charAt(cursor++);
    }

    public void skip() {
        cursor++;
    }

    public static boolean isAllowedNumber(final char c) {
        return c >= '0' && c <= '9' || c == '.' || c == '-';
    }

    public static boolean isQuotedStringStart(char c) {
        return c == SYNTAX_DOUBLE_QUOTE || c == SYNTAX_SINGLE_QUOTE;
    }

    public void skipWhitespace() {
        while (canRead() && Character.isWhitespace(peek())) {
            skip();
        }
    }

    public boolean isInt() {
        String peeked = peekStringUntil('i');

        if (peeked == null) {
            return false;
        } else if (peeked.matches("^[0-9]*$")) {
            int value = Integer.parseInt(peeked);
        }
    }

    public int readInt() throws NbtFormatException {
        final int start = cursor;
        while (canRead() && isAllowedNumber(peek())) {
            skip();
        }
        final String number = string.substring(start, cursor);
        if (number.isEmpty()) {
            throw new NbtFormatException("expected integer was empty");
        }
        try {
            return Integer.parseInt(number);
        } catch (final NumberFormatException ex) {
            cursor = start;
            throw new NbtFormatException(ex);
        }
    }

    public long readLong() throws NbtFormatException {
        final int start = cursor;
        while (canRead() && isAllowedNumber(peek())) {
            skip();
        }
        final String number = string.substring(start, cursor);
        if (number.isEmpty()) {
            throw new NbtFormatException("expected long was empty");
        }
        try {
            return Long.parseLong(number);
        } catch (final NumberFormatException ex) {
            cursor = start;
            throw new NbtFormatException(ex);
        }
    }

    public double readDouble() throws NbtFormatException {
        final int start = cursor;
        while (canRead() && isAllowedNumber(peek())) {
            skip();
        }
        final String number = string.substring(start, cursor);
        if (number.isEmpty()) {
            throw new NbtFormatException("expected double was empty");
        }
        try {
            return Double.parseDouble(number);
        } catch (final NumberFormatException ex) {
            cursor = start;
            throw new NbtFormatException(ex);
        }
    }

    public float readFloat() throws NbtFormatException {
        final int start = cursor;
        while (canRead() && isAllowedNumber(peek())) {
            skip();
        }
        final String number = string.substring(start, cursor);
        if (number.isEmpty()) {
            throw new NbtFormatException("expected float was empty");
        }
        try {
            return Float.parseFloat(number);
        } catch (final NumberFormatException ex) {
            cursor = start;
            throw new NbtFormatException(ex);
        }
    }

    public static boolean isAllowedInUnquotedString(final char c) {
        return c >= '0' && c <= '9'
                || c >= 'A' && c <= 'Z'
                || c >= 'a' && c <= 'z'
                || c == '_' || c == '-'
                || c == '.' || c == '+';
    }

    public String readUnquotedString() {
        final int start = cursor;
        while (canRead() && isAllowedInUnquotedString(peek())) {
            skip();
        }
        return string.substring(start, cursor);
    }

    public String readQuotedString() throws NbtFormatException {
        if (!canRead()) {
            return "";
        }
        final char next = peek();
        if (!isQuotedStringStart(next)) {
            throw new NbtFormatException("Excepted start of quote");
        }
        skip();
        return readStringUntil(next);
    }

    public String peekStringUntil(char term) {
        StringBuilder result = new StringBuilder();
        for (int x = 0; x < getRemainingLength(); x++) {
            char c = peek(x);
            if (c == term) {
                return result.toString();
            }

            result.append(c);
        }

        return null;
    }

    public String readStringUntil(char terminator) throws NbtFormatException {
        final StringBuilder result = new StringBuilder();
        boolean escaped = false;
        while (canRead()) {
            final char c = read();
            if (escaped) {
                if (c == terminator || c == SYNTAX_ESCAPE) {
                    result.append(c);
                    escaped = false;
                } else {
                    setCursor(getCursor() - 1);
                    throw new NbtFormatException("Invalid escape sequence");
                }
            } else if (c == SYNTAX_ESCAPE) {
                escaped = true;
            } else if (c == terminator) {
                return result.toString();
            } else {
                result.append(c);
            }
        }

        throw new NbtFormatException("Expected end of quote");
    }

    public String readString() throws NbtFormatException {
        if (!canRead()) {
            return "";
        }
        final char next = peek();
        if (isQuotedStringStart(next)) {
            skip();
            return readStringUntil(next);
        }
        return readUnquotedString();
    }

    public boolean readBoolean() throws NbtFormatException {
        final int start = cursor;
        final String value = readString();
        switch (value) {
            case "" -> throw new NbtFormatException("expected boolean was empty");
            case "true" -> {
                return true;
            }
            case "false" -> {
                return false;
            }
            default -> {
                cursor = start;
                throw new NbtFormatException("invalid boolean value");
            }
        }

    }

    public void expect(final char c) throws NbtFormatException {
        if (!canRead() || peek() != c) {
            throw new NbtFormatException("unexpected symbol");
        }
        skip();
    }
}