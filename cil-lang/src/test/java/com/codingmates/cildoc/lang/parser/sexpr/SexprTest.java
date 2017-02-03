package com.codingmates.cildoc.lang.parser.sexpr;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

import static com.codingmates.cildoc.lang.parser.sexpr.SexprTestUtils.*;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link Sexpr} ast nodes.
 */
public class SexprTest {

    @Test
    public void atomLeadingComments() throws Exception {
        Sexpr root = parse("(\n" +
            ";comment1\n" +
            ";comment2\n" +
            "symbol\n" +
            ")"
        );

        Sexpr sexpr = (Sexpr) root.getChild(0);
        SexprNode symbol = sexpr.getChild(0);

        List<String> leadingComments = Lists.transform(sexpr.getLeadingComments(symbol), Comment::getComment);
        assertEquals(2, leadingComments.size());
        assertEquals(";comment1", leadingComments.get(0));
        assertEquals(";comment2", leadingComments.get(1));
        assertAtomValue("symbol", symbol);
    }

    @Test
    public void listLeadingComments() throws Exception {
        Sexpr root = parse("(\n" +
            ";comment1\n" +
            ";comment2\n" +
            "(list)\n" +
            ")"
        );

        Sexpr sexpr = (Sexpr) root.getChild(0);
        SexprNode list = sexpr.getChild(0);

        List<String> leadingComments = Lists.transform(sexpr.getLeadingComments(list), Comment::getComment);
        assertEquals(2, leadingComments.size());
        assertEquals(";comment1", leadingComments.get(0));
        assertEquals(";comment2", leadingComments.get(1));
        assertType(SexprNode.Type.LIST, list);
    }
}