package com.codingmates.cildoc.lang.parser.cil;

import com.codingmates.cildoc.lang.diagnostic.DiagnosticLogger;
import com.codingmates.cildoc.lang.parser.base.Span;
import com.codingmates.cildoc.lang.parser.sexpr.Atom;
import com.codingmates.cildoc.lang.parser.sexpr.Sexpr;
import com.codingmates.cildoc.lang.parser.sexpr.SexprNode;
import com.codingmates.cildoc.lang.tree.CilNode;
import com.codingmates.cildoc.lang.tree.CilSymbol;
import com.codingmates.cildoc.lang.tree.decl.*;
import com.codingmates.cildoc.lang.tree.namespace.CilBlock;
import com.codingmates.cildoc.lang.tree.namespace.CilBlockAbstract;
import com.codingmates.cildoc.lang.tree.namespace.CilBlockInherit;

import java.util.ArrayList;
import java.util.List;

public class CilExpressionParser {
    private DiagnosticLogger logger;

    public CilExpressionParser(DiagnosticLogger logger) {
        this.logger = logger;
    }

    /**
     * Expect the given to be an {@link SexprNode} of a given {@code type}.  If not, throw a new diagnostic exception
     * with a message template as {@code message}.
     *
     * @param type The type of {@link SexprNode} to expect.
     * @param expr The {@link SexprNode}.
     * @param <T> The {@link SexprNode} type to cast the expected value to.
     * @return The {@link SexprNode} casted to {@code T} if it is the expected {@code type}.
     * @throws CilParserDiagnosticException If the {@link SexprNode} is not the expected {@code type}
     */
    private static <T extends SexprNode> T expect(SexprNode.Type type, SexprNode expr, CilParserDiagnosticType diagnostic)
        throws CilParserDiagnosticException {

        if (expr.getType() != type) {
            throw new CilParserDiagnosticException(diagnostic, expr);
        }

        return (T) expr;
    }

    /**
     * Similar to {@link #expect(SexprNode.Type, SexprNode, CilParserDiagnosticType)} but expects an {@link SexprNode} of a given {@code}
     * type in a {@link Sexpr}'s list (skipping any comments).
     *
     * @param sexpr The {@link Sexpr} which we expect the given node in.
     * @param position The offset in the {@link Sexpr}'s children that the node should occur.
     * @param expectedType The type of node that should occur.
     * @param <T> The {@link SexprNode} type to cast the expected value to.
     * @return The {@link SexprNode} casted to {@code T}.  Will never be {@code null}.
     * @throws CilParserDiagnosticException If the expected value is not found.
     */
    private static <T> T expectAt(Sexpr sexpr, int position, SexprNode.Type expectedType, CilParserDiagnosticType diagnostic) throws CilParserDiagnosticException {
        SexprNode node = sexpr.getChild(position);
        SexprNode.Type actualType = node.getType();

        if (actualType != expectedType) {
            throw new CilParserDiagnosticException(diagnostic, node);
        } else {
            return (T) node;
        }
    }

    private static Sexpr expectList(SexprNode expr, CilParserDiagnosticType diagnostic) {
        return expect(SexprNode.Type.LIST, expr, diagnostic);
    }

    private static Sexpr expectListAt(Sexpr expr, int position, CilParserDiagnosticType diagnostic) throws CilParserDiagnosticException {
        return expectAt(expr, position, SexprNode.Type.LIST, diagnostic);
    }

    private static Atom expectSymbol(SexprNode expr) throws CilParserDiagnosticException {
        return expect(SexprNode.Type.SYMBOL, expr, CilParserDiagnosticType.EXPECTED_IDENTIFIER);
    }

    private static Atom expectSymbolAt(Sexpr sexpr, int position) throws CilParserDiagnosticException {
        return expectAt(sexpr, position, SexprNode.Type.SYMBOL, CilParserDiagnosticType.EXPECTED_IDENTIFIER);
    }

    public CilNode parseMacroDecl(Sexpr node) throws CilParserDiagnosticException {
        Atom name = expectSymbolAt(node, 1);
        Sexpr parameterList = expectListAt(node, 2, CilParserDiagnosticType.EXPECTED_PARAMETER_LIST);
        List<CilMacroParameter> cilParameterList = new ArrayList<>();

        for (SexprNode parameterNode : parameterList.getChildren()) {
            Sexpr sexpr = expectList(parameterNode, CilParserDiagnosticType.EXPECTED_PARAMETER);
            Atom type = expectSymbolAt(sexpr, 0);
            Atom paramName = expectSymbolAt(sexpr, 1);

            cilParameterList.add(new CilMacroParameter(type.getValue(), new CilSymbol(paramName.getValue(),
                paramName.span())));
        }

        List<SexprNode> children = node.getChildren();
        List<CilNode> cilNodes = new ArrayList<>();

        for (int index = 3; index < children.size(); index++) {
            try {
                cilNodes.add(parseTopLevelExpression(children.get(index)));
            } catch (CilParserDiagnosticException ex) {
                logger.log(ex.diagnostic, ex.node);
            }
        }

        return new CilMacroDeclaration(new CilSymbol(name.getValue(), name.span()), cilParameterList, cilNodes, node.span());
    }

    public CilBlock parseNamespace(Sexpr sexpr) {
        Atom name = expectSymbolAt(sexpr, 1);
        List<CilNode> cilNodes = new ArrayList<>();
        List<SexprNode> children = sexpr.getChildren();

        // offset of 2 for the block keyword and name
        for (int index = 2; index < children.size(); index++) {
            try {
                cilNodes.add(parseTopLevelExpression(children.get(index)));
            } catch (CilParserDiagnosticException ex) {
                logger.log(ex.diagnostic, ex.node);
            }
        }

        return new CilBlock(new CilSymbol(name.getValue(), name.span()), cilNodes, sexpr.span());
    }

    public CilNode parseSymbolDeclExpression(Sexpr node, CilNamedDeclarationType type)
        throws CilParserDiagnosticException {

        return parseSymbolExpression(node, (symbol, span) ->
            new CilNamedDeclarationExpression(symbol, span, type));
    }

    public CilNode parseSymbolExpression(Sexpr node, SymbolExprFactory factory)
        throws CilParserDiagnosticException {

        Atom symbol = expectSymbolAt(node, 1);

        if (node.getChildren().size() > 2) {
            logger.log(CilParserDiagnosticType.UNCLOSED_EXPRESSION, node.getChild(2));
        }

        return factory.create(new CilSymbol(symbol.getValue(), symbol.span()), node.span());
    }

    public CilNode parseTopLevelExpression(SexprNode expr) throws CilParserDiagnosticException {
        Sexpr sexpr = expectList(expr, CilParserDiagnosticType.EXPECTED_EXPRESSION);
        Atom symbol = expectSymbolAt(sexpr, 0);

        switch (symbol.getValue().toLowerCase()) {
            case "block":
                return parseNamespace(sexpr);
            case "blockabstract":
                return parseSymbolExpression(sexpr, CilBlockAbstract::new);
            case "blockinherit":
                return parseSymbolExpression(sexpr, CilBlockInherit::new);
            case "macro":
                return parseMacroDecl(sexpr);

            case "typealiasactual":
                return parseTupleSymbolExpression(sexpr, CilTypeAliasActual::new);
            case "typealias":
            case "type":
            case "typeattribute":
            case "role":
            case "roleattribute":
            case "user":
            case "userattribute":
            case "sensitivity":
            case "category":
            case "sid":
                return parseSymbolDeclExpression(sexpr, CilNamedDeclarationType.from(symbol.getValue()));
            default:
                throw new CilParserDiagnosticException(CilParserDiagnosticType.EXPECTED_KEYWORD, symbol);
        }
    }

    public CilNode parseTupleSymbolExpression(Sexpr node, TupleSymbolExprFactory factory)
        throws CilParserDiagnosticException {

        Atom first = expectSymbolAt(node, 1);
        Atom second = expectSymbolAt(node, 2);

        return factory.create(new CilSymbol(first.getValue(), first.span()),
            new CilSymbol(second.getValue(), second.span()), node.span());
    }

    @FunctionalInterface
    private interface TupleSymbolExprFactory {
        CilNode create(CilSymbol first, CilSymbol second, Span span);
    }

    @FunctionalInterface
    private interface SymbolExprFactory {
        CilNode create(CilSymbol symbol, Span span);
    }
}
