package com.codingmates.cildoc.lang.tree.decl;

import com.codingmates.cildoc.lang.parser.base.Span;
import com.codingmates.cildoc.lang.tree.CilNamedExpression;
import com.codingmates.cildoc.lang.tree.CilSymbol;

public class CilContextDeclaration extends CilNamedExpression {
    private final CilContext context;

    public CilContextDeclaration(CilSymbol name, CilContext context, Span span) {
        super(name, span);
        this.context = context;
    }
}
