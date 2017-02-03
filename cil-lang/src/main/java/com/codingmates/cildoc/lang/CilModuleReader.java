package com.codingmates.cildoc.lang;

import com.codingmates.cildoc.lang.diagnostic.DiagnosticHandler;
import com.codingmates.cildoc.lang.diagnostic.DiagnosticLogger;
import com.codingmates.cildoc.lang.parser.base.SourceMap;
import com.codingmates.cildoc.lang.parser.cil.CilParser;
import com.codingmates.cildoc.lang.parser.sexpr.Sexpr;
import com.codingmates.cildoc.lang.parser.sexpr.SexprLexer;
import com.codingmates.cildoc.lang.parser.sexpr.SexprParser;
import com.codingmates.cildoc.lang.tree.CilModule;
import com.codingmates.cildoc.lang.tree.CilNode;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * A factory that creates {@link CilModule}s from filesystem paths and passes any diagnostics off
 * to a {@link DiagnosticHandler}.
 */
public final class CilModuleReader {
    private DiagnosticHandler diagnosticHandler;

    public CilModuleReader(DiagnosticHandler diagnosticHandler) {
        this.diagnosticHandler = diagnosticHandler;
    }

    /**
     * Parse and return the {@link CilModule} at the given path if one exists.
     *
     * @param path The path of the {@link CilModule} on the filesystem.
     * @return A parsed {@link CilModule}.
     * @throws IOException If there was an error reading the module, or the filesystem path does not exist.
     */
    public CilModule read(Path path) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            DiagnosticLogger diagnosticLogger = new DiagnosticLogger(path, diagnosticHandler);

            SourceMap.Builder builder = new SourceMap.Builder(path.toString());
            SexprLexer sexprLexer = new SexprLexer(reader, builder, diagnosticLogger);
            SexprParser sexprParser = new SexprParser(sexprLexer, diagnosticLogger);

            Sexpr root = sexprParser.parse();
            SourceMap sourceMap = builder.build();

            CilParser cilParser = new CilParser(diagnosticLogger);
            List<CilNode> cilNodes = cilParser.parse(root);

            return new CilModule(sourceMap, cilNodes);
        }
    }
}
