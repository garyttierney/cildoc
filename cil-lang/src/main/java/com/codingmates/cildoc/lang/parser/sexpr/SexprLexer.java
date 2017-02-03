// Copyright notice for some lexer utility functions
/*
 * Copyright (c) 2015 Toby Scrace
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */
package com.codingmates.cildoc.lang.parser.sexpr;

import com.codingmates.cildoc.lang.diagnostic.DiagnosticLogger;
import com.codingmates.cildoc.lang.parser.base.SourceMap;
import com.codingmates.cildoc.lang.parser.base.Span;
import com.google.common.collect.ImmutableSet;

import java.io.IOException;
import java.io.Reader;
import java.util.function.Predicate;

/**
 * Lexer for {@link Sexpr} tokens.
 */
public final class SexprLexer {
    /**
     * A set of characters which constitute whitespace characters (spacing or newlines).
     */
    private final static ImmutableSet<Character> WHITESPACE_CHARACTERS = ImmutableSet.of(' ', '\t', '\n', '\r');

    /**
     * The {@link SourceMap} builder and lexer input source.
     */
    private final SourceMap.Builder builder;

    /**
     * The {@link DiagnosticLogger} to log {@link SexprLexerDiagnosticType}s with.
     */
    private final DiagnosticLogger diagnosticLogger;

    /**
     * The {@link Reader} backing this lexer.
     */
    private final Reader reader;

    /**
     * The index into the backing {@link #reader}.
     */
    private int index = 0;

    public SexprLexer(Reader reader, SourceMap.Builder builder, DiagnosticLogger diagnosticLogger) {
        this.reader = reader;
        this.builder = builder;
        this.diagnosticLogger = diagnosticLogger;
    }

    /**
     * Check if the given {@code character} is a valid symbol character.
     *
     * @param character The {@code character} to check.
     * @return {@code true} if the {@code character} can occur in a symbol.
     */
    private static boolean isSymbolCharacter(char character) {
        return Character.isLetterOrDigit(character) || character == '_' || character == '.' || character == ':';
    }

    private int advance() throws IOException {
        int next = reader.read();

        if (next != -1) {
            builder.advance((char) next, index++);
        }

        return next;
    }

    /**
     * Create a new single character terminal symbol with the given token {@code type}.
     *
     * @param type The {@link SexprToken.Type} of the terminal symbol.
     * @return A new {@link SexprToken} with the given {@code type} and a {@link Span} which spans a single character.
     */
    private SexprToken createToken(SexprToken.Type type) {
        return new SexprToken(type, position() - 1);
    }

    /**
     * Lex the next token from the input source.
     *
     * @return The next {@link SexprToken}. Will never be {@code null}.
     * @throws IOException
     */
    public SexprToken next() throws IOException {
        int start = index;
        int next = advance();

        if (next == -1) {
            return new SexprToken(SexprToken.Type.EOF, start);
        }

        char nextCh = (char) next;

        switch (nextCh) {
            case '(':
                return createToken(SexprToken.Type.LPAREN);
            case ')':
                return createToken(SexprToken.Type.RPAREN);
            case ';':
                String comment = readWhile(new StringBuilder(Character.toString(nextCh)), ch -> ch != '\n');
                Span commentSpan = new Span(start, position());

                return new SexprToken(SexprToken.Type.COMMENT, comment, commentSpan);
            case '"':
                return readString();
            default:
                if (WHITESPACE_CHARACTERS.contains(nextCh)) {
                    return next();
                }

                if (!isSymbolCharacter(nextCh)) {
                    return createToken(SexprToken.Type.UNKNOWN);
                }

                String symbol = readWhile(new StringBuilder(Character.toString(nextCh)), SexprLexer::isSymbolCharacter);
                Span symbolSpan = new Span(start, position());

                return new SexprToken(SexprToken.Type.SYMBOL, symbol, symbolSpan);
        }
    }

    private int peek() throws IOException {
        if (!reader.markSupported()) {
            throw new IllegalStateException("The given Reader implementation does not support marking.");
        }

        reader.mark(1);
        int next = reader.read();
        reader.reset();

        return next;
    }

    private int position() {
        return index;
    }

    /**
     * Reads a string literal from the input file and returns a {@link SexprToken} representing the string (including
     * quotes).
     *
     * @return A quoted string token.
     * @implNote See copright notice from Toby Scrace's lamelang.
     */
    private SexprToken readString() throws IOException {
        StringBuilder builder = new StringBuilder("\"");
        int start = position() - 1;
        int next = advance();

        while (next != '"') {
            if (next == -1) {
                // @todo: log unterminated string literal diagnostic
            } else if (next == '\\' && peek() == '"') {
                builder.append((char) next).append((char) advance());
                next = advance();

                continue;
            }

            builder.append((char) next);
            next = advance();
        }

        builder.append('"');

        int end = this.position();
        return new SexprToken(SexprToken.Type.QUOTED_STRING, builder.toString(), new Span(start, end));
    }

    /**
     * Reads characters into the specified {@link StringBuilder} whilst the specified {@link Predicate} is satisfied by
     * the input.
     *
     * @param valueBuilder The {@link StringBuilder} to read characters into. Must not be {@code null}.
     * @param predicate The {@link Predicate} that must be satisfied to continue reading. Must not be {@code null}.
     * @return All of the read input, as a String. Will never be {@code null}.
     * @implNote See copyright notice from Toby Scrace's lamelang.
     */
    private String readWhile(StringBuilder valueBuilder, Predicate<Character> predicate) throws IOException {
        int next = peek();

        while (next != -1 && predicate.test((char) next)) {
            valueBuilder.append((char) next);
            advance();
            next = peek();
        }

        return valueBuilder.toString();
    }

}
