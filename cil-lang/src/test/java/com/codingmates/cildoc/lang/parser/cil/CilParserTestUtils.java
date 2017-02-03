package com.codingmates.cildoc.lang.parser.cil;

import com.codingmates.cildoc.lang.diagnostic.DiagnosticLogger;
import com.codingmates.cildoc.lang.parser.sexpr.Sexpr;
import com.codingmates.cildoc.lang.parser.sexpr.SexprTestUtils;
import com.codingmates.cildoc.lang.tree.CilNode;

import java.nio.file.Paths;

import static org.junit.Assert.assertTrue;

public class CilParserTestUtils {
    /**
     * Asserts that the {@code actual} {@link CilNode} matches the {@code expected} node type.
     *
     * @param expected The expected node type.
     * @param actual The actual node.
     */
    public static void assertExpressionType(Class<? extends CilNode> expected, CilNode actual) {
        assertTrue("Expected a " + expected.getName() + ", found " + actual.getClass().getName(),
            expected.isAssignableFrom(actual.getClass()));
    }

    /**
     * Parse a single expression from {@code input} and return it from the root node.
     *
     * @param input The input expression to parse.
     * @return A parsed {@link CilNode} expression.
     * @throws Exception If it wasn't possible to parse {@code input}.
     */
    public static CilNode parseExpression(String input) throws Exception {
        DiagnosticLogger logger = new DiagnosticLogger(Paths.get("input.cil"),
            SexprTestUtils.NULL_LOG_HANDLER);

        CilExpressionParser parser = new CilExpressionParser(logger);

        Sexpr root = SexprTestUtils.parse(input);
        return parser.parseTopLevelExpression(root.getChild(0));
    }
}
