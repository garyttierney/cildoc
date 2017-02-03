package com.codingmates.cildoc.lang.parser.cil;


import com.codingmates.cildoc.lang.diagnostic.DiagnosticLogger;
import com.codingmates.cildoc.lang.parser.sexpr.Sexpr;
import com.codingmates.cildoc.lang.parser.sexpr.SexprNode;
import com.codingmates.cildoc.lang.parser.sexpr.SexprParser;
import com.codingmates.cildoc.lang.tree.CilNode;

import java.util.ArrayList;
import java.util.List;

/**
 * A parser which builds an AST of CIL expressions from a tree of {@code s-expressions}.
 *
 * @see SexprParser
 */
public final class CilParser {
    private final CilExpressionParser expressionParser;
    private final DiagnosticLogger diagnosticLogger;

    public CilParser(DiagnosticLogger diagnosticLogger) {
        this.diagnosticLogger = diagnosticLogger;
        this.expressionParser = new CilExpressionParser(diagnosticLogger);
    }

    /**
     * Parse and return a list of top-level CIL nodes from the given {@link Sexpr} tree.
     *
     * @param root The root s-expression node to construct a CIL ast from.
     * @return A list of parsed {@link CilNode}s.
     */
    public List<CilNode> parse(Sexpr root) {
        List<CilNode> parsed = new ArrayList<>();

        for (SexprNode sexpr : root.getChildren()) {
            try {
                parsed.add(expressionParser.parseTopLevelExpression(sexpr));
            } catch (CilParserDiagnosticException ex) {
                diagnosticLogger.log(ex.diagnostic, ex.node);
            }
        }

        return parsed;
    }
}
