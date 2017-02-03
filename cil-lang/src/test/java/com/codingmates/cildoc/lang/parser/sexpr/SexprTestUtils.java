package com.codingmates.cildoc.lang.parser.sexpr;

import com.codingmates.cildoc.lang.diagnostic.DiagnosticHandler;
import com.codingmates.cildoc.lang.diagnostic.DiagnosticLogger;
import com.codingmates.cildoc.lang.parser.base.SourceMap;
import com.google.common.base.Preconditions;

import java.io.StringReader;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class SexprTestUtils {
    public static final DiagnosticHandler NULL_LOG_HANDLER = (path, diag) -> {
    };

    /**
     * Assert that the given {@code node} is a {@link Atom} with a symbol value of {@code value}.
     *
     * @param value The symbol value to expect.
     * @param node The node to compare as a symbol.
     * @throws Exception If {@code node} is not an {@link Atom}.
     */
    public static void assertAtomValue(String value, SexprNode node) throws Exception {
        Preconditions.checkArgument(node instanceof Atom, "Given node is not an Atom");
        Atom atom = (Atom) node;

        assertEquals(value, atom.getValue());
    }

    /**
     * Assert that an {@link SexprNode} is of a given {@code type}.
     *
     * @param type The type of {@link SexprNode} to expect.
     * @param node The {@code actual} {@link SexprNode}.
     */
    public static void assertType(SexprNode.Type type, SexprNode node) {
        assertEquals(type, node.getType());
    }

    /**
     * Parse the given input string and return the root {@link Sexpr} node.
     *
     * @param input The input string to parse.
     * @return A parsed root {@link Sexpr}.
     * @throws Exception If it was not possible to parse the input.
     */
    public static Sexpr parse(String input) throws Exception {
        // Need a dummy path for diagnostic logging.
        Path dummyPath = Paths.get("input.cil");

        SourceMap.Builder builder = new SourceMap.Builder(dummyPath.toString());
        DiagnosticLogger logger = new DiagnosticLogger(dummyPath, NULL_LOG_HANDLER);

        SexprLexer lexer = new SexprLexer(new StringReader(input), builder, logger);
        SexprParser parser = new SexprParser(lexer, logger);

        return parser.parse();
    }
}
