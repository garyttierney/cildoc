package com.codingmates.cildoc.lang.parser.base;

/**
 * A syntactical value which {@link Span}s a range in a {@link SourceMap}.
 */
public interface Spanned {

    /**
     * Gets the character range of this {@link Spanned} value.
     *
     * @return The character range of this value.
     */
    Span span();
}
