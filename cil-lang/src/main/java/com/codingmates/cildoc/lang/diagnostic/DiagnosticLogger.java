package com.codingmates.cildoc.lang.diagnostic;

import com.codingmates.cildoc.lang.parser.base.Spanned;
import com.google.common.base.Preconditions;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class DiagnosticLogger {
    private final Path path;
    private final DiagnosticHandler handler;

    public DiagnosticLogger(Path path, DiagnosticHandler handler) {
        this.path = path;
        this.handler = handler;
    }

    public <T extends Spanned> void log(DiagnosticType<T> diagnosticType, T spanned) {
        log(new Diagnostic<>(diagnosticType, spanned, new ArrayList<>()));
    }

    public <T extends Spanned> void log(Diagnostic<T> diagnostic) {
        handler.handle(path, diagnostic);
    }

    public <T extends Spanned> DiagnosticLogBuilder with(DiagnosticType<T> diagnosticType, T spanned) {
        DiagnosticLogBuilder logBuilder = new DiagnosticLogBuilder(this);
        return logBuilder.with(diagnosticType, spanned);
    }

    public static final class DiagnosticLogBuilder {
        private final DiagnosticLogger logger;
        private final List<Diagnostic> subDiagnostics = new ArrayList<>();

        private DiagnosticType type;
        private Spanned spanned;

        private DiagnosticLogBuilder(DiagnosticLogger logger) {
            this.logger = logger;
        }

        public <T extends Spanned> DiagnosticLogBuilder add(DiagnosticType<T> type, T spanned) {
            subDiagnostics.add(new Diagnostic<>(type, spanned, new ArrayList<>()));

            return this;
        }

        public void log() {
            Preconditions.checkNotNull(type, "Primary diagnostic type must not be null");
            Preconditions.checkNotNull(spanned, "Primary diagnostic spanned value must not be null");

            logger.log(new Diagnostic<>(type, spanned, subDiagnostics));
        }

        public <T extends Spanned> DiagnosticLogBuilder with(DiagnosticType<T> type, T spanned) {
            this.type = type;
            this.spanned = spanned;

            return this;
        }
    }
}
