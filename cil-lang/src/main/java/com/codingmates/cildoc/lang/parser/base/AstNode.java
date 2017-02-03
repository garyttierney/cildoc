package com.codingmates.cildoc.lang.parser.base;

public abstract class AstNode implements Spanned {
    private Span span;

    public AstNode(Span span) {
        this.span = span;
    }

    public Span span() {
        return span;
    }
}
