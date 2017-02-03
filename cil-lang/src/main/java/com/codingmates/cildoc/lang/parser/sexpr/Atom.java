package com.codingmates.cildoc.lang.parser.sexpr;

import com.codingmates.cildoc.lang.parser.base.Span;

/**
 * An s-expression {@code Atom}, i.e., a number, quoted string, ip address, or symbol (i.e. an
 * alphanumeric string optionally containing special characters).
 */
public final class Atom extends SexprNode {
    private final String value;

    private Atom(Type type, String value, Span span) {
        super(type, span);
        this.value = value;
    }

    public static Atom stringLiteral(String value, Span span) {
        return new Atom(Type.QUOTED_STRING, value, span);
    }

    public static Atom symbol(String value, Span span) {
        return new Atom(Type.SYMBOL, value, span);
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
