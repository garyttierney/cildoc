package com.codingmates.cildoc.lang.tree;

import com.codingmates.cildoc.lang.parser.base.Span;

public class CilSymbol {
    public final String value;
    private final Span span;

    public CilSymbol(String value, Span span) {
        this.value = value;
        this.span = span;
    }
}
