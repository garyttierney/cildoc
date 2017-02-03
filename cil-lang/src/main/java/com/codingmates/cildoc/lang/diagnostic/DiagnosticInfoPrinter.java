package com.codingmates.cildoc.lang.diagnostic;

import com.google.common.base.Strings;

import java.io.PrintWriter;
import java.util.List;

public final class DiagnosticInfoPrinter {
    private final PrintWriter writer;
    private final boolean useColor;

    public DiagnosticInfoPrinter(PrintWriter writer, boolean useColor) {
        this.writer = writer;
        this.useColor = useColor;
    }

    /**
     * Print the {@link DiagnosticInfo} information for a given diagnostic, including any
     * sub-diagnostics.
     *
     * @param info The diagnostic information to print (code, level, location, suggestions, context).
     */
    public void print(DiagnosticInfo info) {
        int startLine = info.getStartLine() + 1; // + 1 because line numbers should start from 1

        int indentation = info.getStartColumn();
        String message = info.getLevel() + " at " + startLine + ',' + indentation;
        int offset = message.length();

        message += ": " + info.getMessage() + " ";
        message += "in " + info.getSource() + "\n";

        List<String> lines = info.getLines();

        for (String line : lines) {
            String number = Integer.toString(startLine++);

            String spaces = Strings.repeat(" ", offset - number.length() - "Line ".length());
            message += spaces + "Line " + number + ": " + line + '\n';
        }

        if (lines.size() == 1) {
            int spaces = offset + indentation + ": ".length();
            int tildes = info.getText().chars().map(Character::charCount).sum() - 1;

            message += Strings.repeat(" ", spaces) + '^' + Strings.repeat("~", tildes);
        }

        System.out.println(message);

        info.getSubDiagnostics().forEach(this::print);
    }
}
