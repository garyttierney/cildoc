package com.codingmates.cildoc.lang.tree.namespace;

import com.codingmates.cildoc.lang.parser.base.Span;
import com.codingmates.cildoc.lang.tree.CilNamedExpression;
import com.codingmates.cildoc.lang.tree.CilSymbol;

public class CilBlockAbstract extends CilNamedExpression {
    public CilBlockAbstract(CilSymbol symbol, Span span) {
        super(symbol, span);
    }
}
