package com.codingmates.cildoc.lang.diagnostic;

import java.nio.file.Path;

public interface DiagnosticHandler {
    void handle(Path path, Diagnostic diagnostic);
}
