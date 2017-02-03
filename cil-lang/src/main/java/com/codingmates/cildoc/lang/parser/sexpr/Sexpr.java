package com.codingmates.cildoc.lang.parser.sexpr;

import com.codingmates.cildoc.lang.parser.base.AstNode;
import com.codingmates.cildoc.lang.parser.base.Span;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An s-expression list containing {@link SexprNode}s.
 */
public class Sexpr extends SexprNode {
    /**
     * The children belonging to this s-expression.
     */
    private final List<SexprNode> children;

    /**
     * @param children
     * @see AstNode#AstNode(Span)
     */
    public Sexpr(List<SexprNode> children, Span span) {
        super(Type.LIST, span);
        this.children = ImmutableList.copyOf(children);
    }

    public SexprNode getChild(int position) {
        return getChild(position, false);
    }

    public SexprNode getChild(int position, boolean includeComments) {
        if (includeComments) {
            return children.get(position);
        }

        List<SexprNode> sexprOrAtomList = children.stream()
            .filter(node -> node.getType() != Type.COMMENT)
            .collect(Collectors.toList());

        return sexprOrAtomList.get(position);
    }

    /**
     * Get the {@link SexprNode}s in this {@code list} excluding {@link Comment}s.
     *
     * @return A list of {@link SexprNode}s excluding comments.
     */
    public List<SexprNode> getChildren() {
        return children.stream()
            .filter(node -> node.getType() != SexprNode.Type.COMMENT)
            .collect(Collectors.toList());
    }

    /**
     * Get a list {@link Comment}s which occur directly before the given {@code child}.
     *
     * @param child The child node to find comments for.
     * @return A list of comments occurring before the child node.
     */
    public List<Comment> getLeadingComments(SexprNode child) {
        LinkedList<Comment> comments = new LinkedList<>();

        int index = children.indexOf(child);
        if (index == -1) {
            throw new IllegalArgumentException("The given SexprNode does not exist in this Sexpr");
        } else if (index == 0) {
            return comments;
        }

        while (--index >= 0) {
            SexprNode node = children.get(index);

            if (node.getType() != SexprNode.Type.COMMENT) {
                break;
            }

            comments.addFirst((Comment) node);
        }

        return comments;
    }

    public boolean hasChild(int position, boolean includeComments) {
        if (includeComments) {
            return position < children.size();
        } else {
            return position < getChildren().size();
        }
    }

    public boolean hasChild(int position) {
        return hasChild(position, false);
    }

    @Override
    public String toString() {
        return "(" + Joiner.on(' ').join(Lists.transform(children, SexprNode::toString)) + ")";
    }
}
