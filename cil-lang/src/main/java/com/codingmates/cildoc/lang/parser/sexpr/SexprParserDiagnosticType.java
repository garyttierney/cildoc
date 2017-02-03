package com.codingmates.cildoc.lang.parser.sexpr;

import com.codingmates.cildoc.lang.diagnostic.DiagnosticLevel;
import com.codingmates.cildoc.lang.diagnostic.DiagnosticType;

public enum SexprParserDiagnosticType implements DiagnosticType<SexprToken> {
    UNBALANCED_CLOSE_PARENS("Found a closing `)` without a matching opening `(`"),
    UNEXPECTED_STRING_LITERAL("Found an unexpected string literal, expected a comment or s-expression"),
    UNEXPECTED_SYMBOL("Found an unexpected symbol, expected a comment or s-expression");

    private String msg;

    SexprParserDiagnosticType(String message) {
        this.msg = message;
    }

    public DiagnosticLevel level() {
        return DiagnosticLevel.ERROR;
    }

    public String message(SexprToken token) {
        return msg;
    }
}
