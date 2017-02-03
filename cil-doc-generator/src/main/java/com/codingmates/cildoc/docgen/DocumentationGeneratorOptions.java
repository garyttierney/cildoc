package com.codingmates.cildoc.docgen;

import com.beust.jcommander.Parameter;

import java.util.ArrayList;
import java.util.List;

public class DocumentationGeneratorOptions {
    /**
     * Command line arguments passed to the documentation generator.
     */
    @Parameter()
    private List<String> arguments = new ArrayList<>();

    public List<String> arguments() {
        return arguments;
    }
}
