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
    public static PairType pairType() {
        return new PairType(null, null);
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
        public boolean equals(Object obj) {
            if (!(obj instanceof PairType)) {
                return false;
            } else {
                return this.getLeftType().equals(((PairType) obj).getLeftType())
                        && this.getRightType().equals(((PairType) obj).getRightType());
            }
        }

        @Override
        public String toString() {
            return "pair(" + leftType + ", " + rightType + ")";
        }
    }
}
