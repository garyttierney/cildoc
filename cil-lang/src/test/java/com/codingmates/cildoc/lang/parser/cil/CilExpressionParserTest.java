package com.codingmates.cildoc.lang.parser.cil;

import com.codingmates.cildoc.lang.tree.CilNode;
import com.codingmates.cildoc.lang.tree.namespace.CilBlock;
import org.junit.Test;

import static com.codingmates.cildoc.lang.parser.cil.CilParserTestUtils.assertExpressionType;
import static com.codingmates.cildoc.lang.parser.cil.CilParserTestUtils.parseExpression;
import static org.junit.Assert.assertEquals;

public class CilExpressionParserTest {
    @Test
    public void parseBlockDefinition() throws Exception {
        CilNode block = parseExpression("(block test)");

        assertExpressionType(CilBlock.class, block);
        assertEquals(((CilBlock) block).getName(), "test");
    }
}