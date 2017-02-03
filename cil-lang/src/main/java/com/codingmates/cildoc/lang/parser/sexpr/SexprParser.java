package com.codingmates.cildoc.lang.parser.sexpr;

import com.codingmates.cildoc.lang.diagnostic.DiagnosticLogger;
import com.codingmates.cildoc.lang.parser.base.Span;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class SexprParser {
    private SexprLexer lexer;
    private DiagnosticLogger logger;

    public SexprParser(SexprLexer lexer, DiagnosticLogger logger) {
        this.lexer = lexer;
        this.logger = logger;
    }

    public Sexpr parse() throws IOException {
        List<SexprNode> nodes = new ArrayList<>();
        Span span = parse(nodes::add);

        return new Sexpr(nodes, span);
    }

    private Span parse(Consumer<SexprNode> consumer) throws IOException {
        return parseWhile(consumer, type -> true);
    }

    /**
     * Parse an {@link Sexpr} as a list of other s-expression nodes.
     *
     * @param start The {@link Span} of the opening parenthesis in this list.
     * @return A {@link Sexpr} list.
     * @throws IOException If an IO error occurred during lexing.
     */
    private Sexpr parseList(Span start) throws IOException {
        List<SexprNode> children = new ArrayList<>();
        Span span = Span.join(start, parseWhile(children::add,
            type -> type != SexprToken.Type.RPAREN));

        return new Sexpr(children, span);
    }

    private Span parseWhile(Consumer<SexprNode> consumer, Predicate<SexprToken.Type> predicate)
        throws IOException {

        SexprToken next = lexer.next();
        SexprToken.Type type = next.type();
        Span startingSpan = next.span();

        while (type != SexprToken.Type.EOF && predicate.test(type)) {
            Span tokenSpan = next.span();

            switch (type) {
                case LPAREN:
                    consumer.accept(parseList(tokenSpan));
                    break;
                case RPAREN:
                    logger.log(SexprParserDiagnosticType.UNBALANCED_CLOSE_PARENS, next);
                    break;
                case COMMENT:
                    consumer.accept(new Comment(next.stringValue().orElseThrow(NullPointerException::new),
                        tokenSpan));
                    break;
                case QUOTED_STRING:
                    Atom string = Atom.stringLiteral(next.stringValue().orElseThrow(NullPointerException::new),
                        tokenSpan);

                    consumer.accept(string);
                    break;
                case SYMBOL:
                    Atom symbol = Atom.symbol(next.stringValue().orElseThrow(NullPointerException::new),
                        tokenSpan);

                    consumer.accept(symbol);
                    break;
            }

            next = lexer.next();
            type = next.type();
        }

        Span endingSpan = next.span();
        return Span.join(startingSpan, endingSpan);
    }
}
