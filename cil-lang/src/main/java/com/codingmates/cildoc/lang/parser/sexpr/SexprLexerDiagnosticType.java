package com.codingmates.cildoc.lang.parser.sexpr;

import com.codingmates.cildoc.lang.diagnostic.DiagnosticLevel;
import com.codingmates.cildoc.lang.diagnostic.DiagnosticType;

public class SexprLexerDiagnosticType implements DiagnosticType<SexprToken> {
    @Override
    public DiagnosticLevel level() {
        return DiagnosticLevel.ERROR;
    }

    @Override
    public String message(SexprToken value) {
        return null;
    }
}
