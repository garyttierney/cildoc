package com.codingmates.cildoc.lang.parser.sexpr;

import com.codingmates.cildoc.lang.parser.base.AstNode;
import com.codingmates.cildoc.lang.parser.base.Span;

/**
 * An abstract syntax tree node for s-expressions.
 */
public abstract class SexprNode extends AstNode {
    /**
     * The type of this node.
     */
    private final Type type;

    /**
     * Create a new s-expression node.
     *
     * @param type The type of this s-expression node.e
     * @see AstNode#AstNode(Span)
     */
    public SexprNode(Type type, Span span) {
        super(span);
        this.type = type;
    }

    /**
     * Gets the type of this node.
     *
     * @return The type of this node.
     */
    public Type getType() {
        return type;
    }

    public abstract String toString();

    /**
     * The types of {@link SexprNode}s which can occur in an s-expression tree.
     */
    public static enum Type {
        LIST,
        SYMBOL,
        QUOTED_STRING, COMMENT
    }
}
