package com.codingmates.cildoc.lang.diagnostic;

import java.util.List;

public class DiagnosticInfoBuilder {
    private DiagnosticLevel level;
    private String message;
    private String text;
    private String name;
    private List<DiagnosticInfo> subDiagnostics;
    private List<String> lines;
    private int startLine;
    private int endLine;
    private int startColumn;
    private int endColumn;

    public DiagnosticInfo build() {
        //@todo - preconditions
        return new DiagnosticInfo(name, level, message, text, subDiagnostics, lines, startLine, endLine, startColumn, endColumn);
    }

    public DiagnosticInfoBuilder endColumn(int endColumn) {
        this.endColumn = endColumn;
        return this;
    }

    public DiagnosticInfoBuilder endLine(int endLine) {
        this.endLine = endLine;
        return this;
    }

    public DiagnosticInfoBuilder level(DiagnosticLevel level) {
        this.level = level;
        return this;
    }

    public DiagnosticInfoBuilder lines(List<String> lines) {
        this.lines = lines;
        return this;
    }

    public DiagnosticInfoBuilder message(String message) {
        this.message = message;
        return this;
    }

    public DiagnosticInfoBuilder source(String name) {
        this.name = name;
        return this;
    }

    public DiagnosticInfoBuilder startColumn(int startColumn) {
        this.startColumn = startColumn;
        return this;
    }

    public DiagnosticInfoBuilder startLine(int startLine) {
        this.startLine = startLine;
        return this;
    }

    public DiagnosticInfoBuilder subDiagnostics(List<DiagnosticInfo> subDiagnostics) {
        this.subDiagnostics = subDiagnostics;
        return this;
    }

    public DiagnosticInfoBuilder text(String text) {
        this.text = text;
        return this;
    }
}