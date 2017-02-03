package com.codingmates.cildoc.lang.tree;

import com.codingmates.cildoc.lang.parser.base.Span;

/**
 * A CIL expression with a symbol name a its first argument.
 */
public class CilNamedExpression extends CilNode implements CilNamedNode {
    /**
     * This expressions first argument.
     */
    private final CilSymbol symbol;

    public CilNamedExpression(CilSymbol symbol, Span span) {
        super(span);
        this.symbol = symbol;
    }

    public CilSymbol nameSymbol() {
        return symbol;
    }
}
