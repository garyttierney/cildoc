package com.codingmates.cildoc.lang.tree.decl;

import com.codingmates.cildoc.lang.parser.base.Span;
import com.codingmates.cildoc.lang.tree.CilNode;
import com.codingmates.cildoc.lang.tree.CilSymbol;

public class CilTypeAliasActual extends CilNode {
    private final CilSymbol alias;
    private final CilSymbol type;

    public CilTypeAliasActual(CilSymbol alias, CilSymbol type, Span span) {
        super(span);
        this.alias = alias;
        this.type = type;
    }
}
