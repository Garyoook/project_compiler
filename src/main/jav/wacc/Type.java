package jav.wacc;

import antlr.BasicParser;

import static java.lang.System.exit;

public abstract class Type extends AST {

    public static BaseType intType() {
        return new BaseType(BaseTypeKind.INT);
    }

    public static BaseType charType() {
        return new BaseType(BaseTypeKind.CHAR);
    }
    public static BaseType boolType() {
        return new BaseType(BaseTypeKind.BOOL);
    }
    public static BaseType stringType() {
        return new BaseType(BaseTypeKind.STRING);
    }

    public enum BaseTypeKind {
        INT, CHAR, BOOL, STRING
    }

    public static class BaseType extends Type {
        private final BaseTypeKind kind;
        public BaseType(BaseTypeKind kind) {
            this.kind = kind;
        }

        public BaseTypeKind getKind() {
            return kind;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof BaseType)) {
                return false;
            } else {
                return ((BaseType) obj).getKind() == this.getKind();
            }
        }

        @Override
        public String toString() {
            return kind.toString().toLowerCase();
        }
    }

    public static class ArrayType extends Type {
        private final Type type;

        public ArrayType(Type type) {
            this.type = type;
        }

        public Type getType() {
            return type;
        }

        @Override
        public String toString() {
            return type + "[]";
        }
    }

    public static class PairType extends Type {
        private final Type leftType;
        private final Type rightType;

        public PairType(Type leftType, Type rightType) {
            this.leftType = leftType;
            this.rightType = rightType;
        }

        public Type getLeftType() {
            return leftType;
        }

        public Type getRightType() {
            return rightType;
        }

        @Override
        public String toString() {
            return "pair(" + leftType + ", " + rightType + ")";
        }
    }

//    private BasicParser.TypeContext typeContext;
//    private String stringValue;
//    private int intValue;
//    private char charValue;
//
//    public Type(BasicParser.TypeContext typeContext, String value) {
//        this.typeContext = typeContext;
//        this.stringValue = value;
//    }
//    public Type(BasicParser.TypeContext typeContext, int value) {
//        this.typeContext = typeContext;
//        this.intValue = value;
//    }
//    public Type(BasicParser.TypeContext typeContext, char value) {
//        this.typeContext = typeContext;
//        this.charValue = value;
//    }
//
//    public String getStringValue() {
//        return stringValue;
//    }
//
//    public int getIntValue() {
//        return intValue;
//    }
//
//    public BasicParser.TypeContext getTypeContext() {
//        if (typeContext == null) {
//            System.out.println("Semantic error: Variable not defined");
//            exit(200);
//        }
//        return typeContext;
//    }
//
//    public char getCharValue() {
//        return charValue;
//    }
////    @Override
////    public boolean equals(Object o) {
////        if (this instanceof IntNode) {
////            return (o instanceof IntNode);
////        }
////        if (this instanceof CharNode) {
////            return (o instanceof CharNode);
////        }
////        if (this instanceof StringNode) {
////            return (o instanceof StringNode);
////        }
////        if (this instanceof PairAST) {
////            return (o instanceof PairAST);
////        }
////        if (this instanceof BoolNode) {
////            return (o instanceof BoolNode);
////        }
////        return false;
////    }
}
