package com.codingmates.cildoc.lang.parser.sexpr;

import com.codingmates.cildoc.lang.parser.base.Span;
import com.codingmates.cildoc.lang.parser.base.Spanned;

import java.util.Optional;

public class SexprToken implements Spanned {

    /**
     * The string value associated with this token.  May be {@code null}.
     */
    private final String stringValue;

    /**
     * The spanned location of this token.
     */
    private final Span span;

    /**
     * The type of this token.
     */
    private final Type type;

    public SexprToken(Type type, int position) {
        this.type = type;
        this.span = new Span(position, position);
        this.stringValue = null;
    }

    public SexprToken(Type type, String value, Span span) {
        this.type = type;
        this.stringValue = value;
        this.span = span;
    }

    public Span span() {
        return span;
    }

    public Optional<String> stringValue() {
        return Optional.ofNullable(stringValue);
    }

    public Type type() {
        return type;
    }

    public enum Type {
        COMMENT,
        SYMBOL,
        QUOTED_STRING,
        LPAREN,
        RPAREN,
        EOF,
        UNKNOWN;
    }

}
