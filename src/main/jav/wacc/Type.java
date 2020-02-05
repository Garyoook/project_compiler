package jav.wacc;

import antlr.BasicParser;

import static java.lang.System.exit;

public class Type {
    private BasicParser.TypeContext typeContext;
    private String stringValue;
    private int intValue;
    private char charValue;

    public Type(BasicParser.TypeContext typeContext, String value) {
        this.typeContext = typeContext;
        this.stringValue = value;
    }
    public Type(BasicParser.TypeContext typeContext, int value) {
        this.typeContext = typeContext;
        this.intValue = value;
    }
    public Type(BasicParser.TypeContext typeContext, char value) {
        this.typeContext = typeContext;
        this.charValue = value;
    }

    public String getStringValue() {
        return stringValue;
    }

    public int getIntValue() {
        return intValue;
    }

    public BasicParser.TypeContext getTypeContext() {
        if (typeContext == null) {
            System.out.println("Semantic error: Variable not defined");
            exit(200);
        }
        return typeContext;
    }

    public char getCharValue() {
        return charValue;
    }
//    @Override
//    public boolean equals(Object o) {
//        if (this instanceof IntNode) {
//            return (o instanceof IntNode);
//        }
//        if (this instanceof CharNode) {
//            return (o instanceof CharNode);
//        }
//        if (this instanceof StringNode) {
//            return (o instanceof StringNode);
//        }
//        if (this instanceof PairAST) {
//            return (o instanceof PairAST);
//        }
//        if (this instanceof BoolNode) {
//            return (o instanceof BoolNode);
//        }
//        return false;
//    }
}
