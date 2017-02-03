package com.codingmates.cildoc.lang.tree.decl;

import com.codingmates.cildoc.lang.parser.base.Span;
import com.codingmates.cildoc.lang.tree.CilNamedExpression;
import com.codingmates.cildoc.lang.tree.CilSymbol;

public final class CilNamedDeclarationExpression extends CilNamedExpression {
    private final CilNamedDeclarationType type;

    public CilNamedDeclarationExpression(CilSymbol symbol, Span span, CilNamedDeclarationType type) {
        super(symbol, span);
        this.type = type;
    }
}
