package com.codingmates.cildoc.lang.parser.cil;

public class CilParserException extends RuntimeException {
    public CilParserException(String message) {
        super(message);
    }

    public CilParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
