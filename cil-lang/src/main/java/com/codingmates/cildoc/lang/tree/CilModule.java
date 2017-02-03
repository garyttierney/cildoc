package com.codingmates.cildoc.lang.tree;

import com.codingmates.cildoc.lang.parser.base.SourceMap;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class CilModule {
    public final SourceMap sourceMap;
    private final List<CilNode> rootNodes;

    public CilModule(SourceMap sourceMap, List<CilNode> rootNodes) {
        this.sourceMap = sourceMap;
        this.rootNodes = new ArrayList<>(rootNodes);
    }

    public Stream<CilNode> findNodes(Predicate<CilNode> nodePredicate) {
        return null;
    }
}
