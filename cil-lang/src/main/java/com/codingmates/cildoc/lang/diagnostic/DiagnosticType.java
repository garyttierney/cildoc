package com.codingmates.cildoc.lang.diagnostic;

import com.codingmates.cildoc.lang.parser.base.Spanned;

/**
 * The specification for a {@code DiagnosticType}.  Each {@code DiagnosticType} implementation
 * represents a given type of lexer or parser node, which spans a section of a source file. The
 * {@code DiagnosticType} can format the message with additional message based on the {@link Spanned}
 * value.
 *
 * @param <T> The type of {@link Spanned} value.
 */
public interface DiagnosticType<T extends Spanned> {

    /**
     * The severity of this {@code DiagnosticType}.
     *
     * @return This {@code DiagnosticType}'s severity.
     */
    DiagnosticLevel level();

    /**
     * Format and return a message for the {@link Spanned} value that had this {@code DiagnosticType}
     * reported against it.
     *
     * @param value The {@link Spanned} value which caused the diagnostic.
     * @return A diagnostic message.
     */
    String message(T value);
}
