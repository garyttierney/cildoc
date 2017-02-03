package com.codingmates.cildoc.lang.tree;

import com.codingmates.cildoc.lang.parser.base.Span;

import java.util.List;

public abstract class CilContainerNode extends CilNode {
    private final List<CilNode> children;

    public CilContainerNode(List<CilNode> children, Span span) {
        super(span);
        this.children = children;
    }

    public void add(CilNode node) {
        children.add(node);
    }
}
