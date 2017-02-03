package com.codingmates.cildoc.lang.tree.namespace;

import com.codingmates.cildoc.lang.parser.base.Span;
import com.codingmates.cildoc.lang.tree.CilContainerNode;
import com.codingmates.cildoc.lang.tree.CilNode;
import com.codingmates.cildoc.lang.tree.CilSymbol;

import java.util.List;

public final class CilBlock extends CilContainerNode {
    private final CilSymbol name;

    public CilBlock(CilSymbol name, List<CilNode> children, Span span) {
        super(children, span);
        this.name = name;
    }

    public String getName() {
        return name.value;
    }

    public CilSymbol getNameSymbol() {
        return name;
    }

}
