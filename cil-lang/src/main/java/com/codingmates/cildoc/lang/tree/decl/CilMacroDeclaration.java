package com.codingmates.cildoc.lang.tree.decl;

import com.codingmates.cildoc.lang.parser.base.Span;
import com.codingmates.cildoc.lang.tree.CilContainerNode;
import com.codingmates.cildoc.lang.tree.CilNode;
import com.codingmates.cildoc.lang.tree.CilSymbol;

import java.util.List;

public class CilMacroDeclaration extends CilContainerNode {

    private final CilSymbol name;
    private final List<CilMacroParameter> parameterList;

    public CilMacroDeclaration(CilSymbol name, List<CilMacroParameter> parameterList, List<CilNode> children, Span span) {
        super(children, span);
        this.name = name;
        this.parameterList = parameterList;
    }
}
