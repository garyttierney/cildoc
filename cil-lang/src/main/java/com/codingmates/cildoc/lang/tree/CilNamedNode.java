package com.codingmates.cildoc.lang.tree;

public interface CilNamedNode {
    default String name() {
        return nameSymbol().value;
    }

    CilSymbol nameSymbol();
}
