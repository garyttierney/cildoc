package com.codingmates.cildoc.lang.parser.base;

/**
 * A span of text with a starting and ending offset.
 */
public class Span {
    private final int start;
    private final int end;

    public Span(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public static Span join(Span start, Span end) {
        return new Span(
            Math.min(start.start, end.start),
            Math.max(start.end, end.end)
        );
    }

    public int end() {
        return end;
    }

    public int start() {
        return start;
    }
}
