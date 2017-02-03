package com.codingmates.cildoc.lang.parser.base;

import com.codingmates.cildoc.lang.diagnostic.Diagnostic;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;

/**
 * A source map for parse trees.  Can be used to map {@link AstNode}s back to the file location and text they
 * were parsed from.
 */
public final class SourceMap {

    /**
     * The name associated with this {@link SourceMap} for i.e., {@link Diagnostic}s.
     */
    private final String name;

    /**
     * The character indices at which the lines of a file end.
     */
    private final int[] endings;

    /**
     * The {@link List} of lines of the file.
     */
    private final List<String> lines;

    /**
     * Create an {@link SourceMap} from
     *
     * @param name The name of this {@code SourceMap}.
     * @param lineEndings An array of line ending offsets indexed by the line number.
     * @param lines A list of lines in this file.
     */
    SourceMap(String name, int[] lineEndings, List<String> lines) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(lines);
        Preconditions.checkArgument(lineEndings.length >= lines.size(), "Fewer line endings than number of lines");

        this.name = name;
        this.endings = Arrays.copyOf(lineEndings, lines.size());
        this.lines = ImmutableList.copyOf(lines);
    }

    /**
     * Creates a new {@link SourceMap.Builder} with the given input source {@code name}.
     *
     * @param name The name of the input source to create the SourceMap Builder for.
     * @return A new {@link SourceMap.Builder}.
     */
    public static Builder builder(String name) {
        return new Builder(name);
    }

    /**
     * Gets the code covered by the specified {@link Span}, as a {@link ImmutableList} of lines.
     *
     * @param span The {@link Span} covering the code. Must not be {@code null}.
     * @return The {@link ImmutableList} of code.
     */
    public List<String> code(Span span) {
        int start = span.start(), end = span.end();
        int startLine = line(start), endLine = line(end);
        int firstLineStart = lineStart(start);

        if (startLine == endLine) {
            return ImmutableList.of(lines.get(startLine).substring(column(start), column(end)));
        }

        ImmutableList.Builder<String> builder = ImmutableList.builder();
        builder.add(lines.get(startLine).substring(firstLineStart));

        for (int index = startLine + 1; index < endLine; index++) {
            builder.add(lines.get(index));
        }

        builder.add(lines.get(endLine).substring(0, lineStart(end)));
        return builder.build();
    }

    /**
     * Gets the offset of the column in the line that {@code index} occurs in.
     *
     * @param index The index to search for. Must not be negative.
     * @return The column number, relative to the line it occurs on.
     */
    public int column(int index) {
        return index - endings[line(index) - 1] - 1;
    }

    /**
     * Gets the index marking the line ending of the specified line, as an {@link OptionalInt}. If the line end index
     * for the specified line is unknown, {@link OptionalInt#empty()} will be returned.
     *
     * @param line The line number to get the ending index of. Must not be negative.
     * @return The line ending, as an {@link OptionalInt}. Will never be {@code null}.
     */
    public OptionalInt ending(int line) {
        if (line >= endings.length) {
            return OptionalInt.empty();
        }

        int ending = endings[line];
        return ending == -1 ? OptionalInt.empty() : OptionalInt.of(ending);
    }

    /**
     * Gets the line number of the specified character index.
     *
     * @param index The index to search for. Must not be negative.
     * @return The line number. Will never be negative.
     */
    public int line(int index) {
        int line = Arrays.binarySearch(endings, index);
        return line >= 0 ? line : ~line;
    }

    /**
     * Gets the index of the first character on the same line as the character represented by the specified
     * {@code index}.
     *
     * @param index The index to get the starting line index of. Must not be negative.
     * @return The index of the first character on the line. Will never be negative.
     */
    public int lineStart(int index) {
        int line = line(index);
        if (line == 0) {
            return 0;
        }

        return index - endings[line - 1];
    }

    /**
     * Gets the {@link ImmutableList} of all of the lines of code at least part of the {@link Span} is on. The
     * {@link Span} <strong>must</strong> end at the line before the current one (i.e. all of the code on all of the
     * lines covered by the {@link Span} must be know to this SourceMap).
     *
     * @param span The {@link Span}. Must not be {@code null}.
     * @return The {@link ImmutableList} of lines of code. Will never be {@code null}.
     */
    public ImmutableList<String> lines(Span span) {
        int start = line(span.start());
        int end = line(span.end() - 1);

        return ImmutableList.copyOf(lines.subList(start, end + 1));
    }

    /**
     * Gets the name of the input that this {@code SourceMap} represents.
     *
     * @return The name of the input source (i.e., the filename).
     */
    public String name() {
        return name;
    }

    public static class Builder {

        /**
         * The default number of lines expected in a file.
         */
        private static final int DEFAULT_NUM_LINES = 10;

        /**
         * The {@link List} of lines of the file.
         */
        private final List<String> lines = new ArrayList<>();

        /**
         * The name of the input source this {@code SourceMap} represents.
         */
        private final String name;

        /**
         * The character indices at which the lines of a file end.
         */
        private int[] endings;

        /**
         * The current line index in the reader.
         */
        private int line = 0;

        /**
         * The contents of the line currently being read.
         */
        private StringBuilder current = new StringBuilder();

        public Builder(String name) {
            this.name = name;
            this.endings = new int[DEFAULT_NUM_LINES];
        }

        /**
         * Advance to and return the next character in the backing reader.
         *
         * @return The next available character from the reader as an integer.
         * @throws IOException If an IO error occurred reading the next character.
         */
        public int advance(char next, int index) throws IOException {
            if (next == '\n') {
                lines.add(current.toString());
                current = new StringBuilder();

                nextLine(index);
            } else {
                current.append(next);
            }

            return next;
        }

        /**
         * Freeze this builder and return a new {@link SourceMap}.
         *
         * @return A new {@link SourceMap}.
         */
        public SourceMap build() {
            return new SourceMap(name, endings, lines);
        }

        /**
         * Marks the start of a new line in the input file.
         *
         * @param index The index of the new line character. Must not be negative.
         */
        private void nextLine(int index) {
            int length = endings.length;

            if (line == length) {
                endings = Arrays.copyOf(endings, length + (length >> 1));
            }

            endings[line++] = index;
        }
    }
}
