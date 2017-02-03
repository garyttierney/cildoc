package com.codingmates.cildoc.lang.tree.decl;

import com.codingmates.cildoc.lang.tree.CilSymbol;

public class CilMacroParameter {
    private final String type;
    private final CilSymbol name;

    public CilMacroParameter(String type, CilSymbol name) {
        this.type = type;
        this.name = name;
    }
}
