package com.codingmates.cildoc.lang.parser.sexpr;

import org.junit.Test;

import java.util.List;

import static com.codingmates.cildoc.lang.parser.sexpr.SexprTestUtils.*;

public class SexprParserTest {

    @Test
    public void parseList() throws Exception {
        Sexpr root = parse("(val)");
        SexprNode list = root.getChild(0);

        assertType(SexprNode.Type.LIST, list);
    }

    @Test
    public void parseListOfStrings() throws Exception {
        Sexpr root = parse("(\"string literal\")");
        Sexpr sexpr = (Sexpr) root.getChild(0);

        List<SexprNode> children = sexpr.getChildren();
        assertType(SexprNode.Type.QUOTED_STRING, children.get(0));
        assertAtomValue("\"string literal\"", children.get(0));
    }

    @Test
    public void parseListOfSymbols() throws Exception {
        Sexpr root = parse("(symbol1 symbol2 symbol3)");
        Sexpr sexpr = (Sexpr) root.getChild(0);

        List<SexprNode> children = sexpr.getChildren();
        SexprNode node = children.get(0);
        assertType(SexprNode.Type.SYMBOL, node);
        assertAtomValue("symbol1", node);

        node = children.get(1);
        assertType(SexprNode.Type.SYMBOL, node);
        assertAtomValue("symbol2", node);

        node = children.get(2);
        assertType(SexprNode.Type.SYMBOL, node);
        assertAtomValue("symbol3", node);
    }
}