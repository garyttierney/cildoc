package com.codingmates.cildoc.docgen;

import com.beust.jcommander.JCommander;
import com.codingmates.cildoc.lang.CilModuleReader;
import com.codingmates.cildoc.lang.diagnostic.DiagnosticInfoCollector;
import com.codingmates.cildoc.lang.diagnostic.DiagnosticInfoPrinter;
import com.codingmates.cildoc.lang.tree.CilModule;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public final class DocumentationGenerator {
    public static void main(String[] argv) {
        DocumentationGeneratorOptions options = new DocumentationGeneratorOptions();
        JCommander commander = new JCommander(options);

        commander.setProgramName("cildocgen");
        commander.setAcceptUnknownOptions(true);
        commander.parse(argv);

        List<String> arguments = options.arguments();
        List<CilModule> modules = new ArrayList<>();

        DiagnosticInfoCollector diagnosticCollector = new DiagnosticInfoCollector();
        CilModuleReader cilModuleReader = new CilModuleReader(diagnosticCollector);

        for (String argument : arguments) {
            try {
                Path path = Paths.get(argument);
                CilModule module = cilModuleReader.read(path);
                modules.add(module);
                diagnosticCollector.addSourceMap(path, module.sourceMap);
            } catch (IOException e) {
                System.err.println("Error parsing " + argument);
                e.printStackTrace();
            }
        }

        try (PrintWriter writer = new PrintWriter(System.out)) {
            DiagnosticInfoPrinter diagnosticPrinter = new DiagnosticInfoPrinter(writer, true);
            diagnosticCollector.diagnostics().forEach(diagnosticPrinter::print);
        }
    }
}
