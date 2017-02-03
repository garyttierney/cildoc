package com.codingmates.cildoc.lang.parser.sexpr;

import com.codingmates.cildoc.lang.parser.base.Span;

public class Comment extends SexprNode {

    private final String comment;

    public Comment(String comment, Span span) {
        super(Type.COMMENT, span);
        this.comment = comment;
    }

    //@todo - remove any formatting in the comment and leading semicolons
    public String getComment() {
        return comment;
    }

    @Override
    public String toString() {
        return comment;
    }
}
