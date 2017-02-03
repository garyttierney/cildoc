package com.codingmates.cildoc.lang.diagnostic;

import com.codingmates.cildoc.lang.parser.base.SourceMap;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * A {@link DiagnosticHandler} which collects {@link Diagnostic}s into a collection which
 * can be read back later.
 */
public class DiagnosticInfoCollector implements DiagnosticHandler {
    private final Map<Path, SourceMap> sourceMaps = new HashMap<>();
    private final List<UnresolvedDiagnosticInfo> diagnosticList = new ArrayList<>();

    public void addSourceMap(Path path, SourceMap map) {
        sourceMaps.put(path, map);
    }

    public Stream<DiagnosticInfo> diagnostics() {
        return diagnosticList.stream().map(diag -> diag.resolve(sourceMaps::get));
    }

    @Override
    public void handle(Path path, Diagnostic diagnostic) {
        diagnosticList.add(new UnresolvedDiagnosticInfo(path, diagnostic));
    }

    private static final class UnresolvedDiagnosticInfo {
        private final Path path;
        private final Diagnostic diagnostic;

        private UnresolvedDiagnosticInfo(Path path, Diagnostic diagnostic) {
            this.path = path;
            this.diagnostic = diagnostic;
        }

        DiagnosticInfo resolve(Function<Path, SourceMap> sourceMapProvider) {
            return diagnostic.resolve(sourceMapProvider.apply(path));
        }
    }
}
