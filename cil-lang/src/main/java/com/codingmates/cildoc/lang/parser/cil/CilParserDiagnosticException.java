package com.codingmates.cildoc.lang.parser.cil;

import com.codingmates.cildoc.lang.parser.sexpr.SexprNode;

public class CilParserDiagnosticException extends RuntimeException {
    protected final CilParserDiagnosticType diagnostic;
    protected final SexprNode node;

    public CilParserDiagnosticException(CilParserDiagnosticType diagnostic, SexprNode node) {
        this.diagnostic = diagnostic;
        this.node = node;
    }
}
