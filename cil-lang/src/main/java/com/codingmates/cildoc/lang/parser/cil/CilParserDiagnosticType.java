package com.codingmates.cildoc.lang.parser.cil;

import com.codingmates.cildoc.lang.diagnostic.DiagnosticLevel;
import com.codingmates.cildoc.lang.diagnostic.DiagnosticType;
import com.codingmates.cildoc.lang.parser.sexpr.SexprNode;

public enum CilParserDiagnosticType implements DiagnosticType<SexprNode> {
    UNOPENED_EXPRESSION("Expected to find `(` to begin an expression, not `%actual`"),
    UNCLOSED_EXPRESSION("Expected to find a `)` closing an expression, not `%actual`"),
    EXPECTED_IDENTIFIER("Expected to find an identifier, not `%actual`"),
    EXPECTED_CONTEXT_EXPRESSION("Expected to find a security context expression, not `%actual`"),
    EXPECTED_CONTEXT_EXPRESSION_OR_ID("Expected to find an anonymous security context or identifier, not `%actual"),
    EXPECTED_KEYWORD("Expected a valid expression keyword, not `%actual`"),
    EXPECTED_EXPRESSION("Expected an S-expression, not `%actual`"),
    EXPECTED_PARAMETER("Expected macro parameter, not `%actual`"),
    EXPECTED_PARAMETER_LIST("Expected macro parameter list, not `%actual`");

    private String format;

    CilParserDiagnosticType(String format) {
        this.format = format;
    }

    @Override
    public DiagnosticLevel level() {
        return DiagnosticLevel.ERROR;
    }

    @Override
    public String message(SexprNode value) {
        return format.replace("%actual", value.toString());
    }
}
