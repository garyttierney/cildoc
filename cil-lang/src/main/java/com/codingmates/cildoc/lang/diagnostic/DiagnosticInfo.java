package com.codingmates.cildoc.lang.diagnostic;

import java.util.List;

public class DiagnosticInfo {
    private final String text;
    private final int startLine;
    private final int endLine;
    private final int startColumn;
    private final int endColumn;
    private final DiagnosticLevel level;
    private final String message;
    private final List<DiagnosticInfo> subDiagnostics;
    private final List<String> lines;
    private final String source;

    public DiagnosticInfo(String source, DiagnosticLevel level, String message, String text,
                          List<DiagnosticInfo> subDiagnostics, List<String> lines,
                          int startLine, int endLine, int startColumn, int endColumn) {
        this.source = source;
        this.level = level;
        this.message = message;
        this.text = text;
        this.subDiagnostics = subDiagnostics;
        this.lines = lines;
        this.startLine = startLine;
        this.endLine = endLine;
        this.startColumn = startColumn;
        this.endColumn = endColumn;
    }

    public int getEndColumn() {
        return endColumn;
    }

    public int getEndLine() {
        return endLine;
    }

    public DiagnosticLevel getLevel() {
        return level;
    }

    public List<String> getLines() {
        return lines;
    }

    public String getMessage() {
        return message;
    }

    public String getSource() {
        return source;
    }

    public int getStartColumn() {
        return startColumn;
    }

    public int getStartLine() {
        return startLine;
    }

    public List<DiagnosticInfo> getSubDiagnostics() {
        return subDiagnostics;
    }

    public String getText() {
        return text;
    }
}
