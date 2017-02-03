package com.codingmates.cildoc.lang.diagnostic;

import com.codingmates.cildoc.lang.parser.base.SourceMap;
import com.codingmates.cildoc.lang.parser.base.Span;
import com.codingmates.cildoc.lang.parser.base.Spanned;
import com.google.common.collect.Lists;

import java.util.List;

public class Diagnostic<T extends Spanned> {
    private final T spanned;
    private final DiagnosticType<T> type;
    private final List<Diagnostic> subDiagnostics;

    public Diagnostic(DiagnosticType<T> diagnosticType, T spanned, List<Diagnostic> subDiagnostics) {
        this.type = diagnosticType;
        this.spanned = spanned;
        this.subDiagnostics = subDiagnostics;
    }

    /**
     * Resolve this {@code Diagnostic} to {@link DiagnosticInfo} which can be printed to a user
     * or parsed by tools.
     *
     * @param sourceMap The {@link SourceMap} used to map {@link Span}s back to code and locations.
     * @return Reportable information for this {@code Diagnostic}.
     */
    public DiagnosticInfo resolve(SourceMap sourceMap) {
        Span span = spanned.span();
        int startIndex = span.start();
        int endIndex = span.end();

        DiagnosticInfoBuilder builder = new DiagnosticInfoBuilder();
        builder.source(sourceMap.name());
        builder.text(sourceMap.code(span).get(0));
        builder.startLine(sourceMap.line(startIndex));
        builder.endLine(sourceMap.line(endIndex));
        builder.startColumn(sourceMap.column(startIndex));
        builder.endColumn(sourceMap.column(endIndex));
        builder.message(type.message(spanned));
        builder.level(type.level());
        builder.lines(sourceMap.lines(span));
        builder.subDiagnostics(Lists.transform(subDiagnostics, diag -> diag.resolve(sourceMap)));

        return builder.build();
    }
}
