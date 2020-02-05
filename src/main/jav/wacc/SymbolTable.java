package jav.wacc;

import antlr.BasicParser;

import java.util.HashMap;
import java.util.List;

public class SymbolTable {
    private SymbolTable encSymbolTable;
    private HashMap<String, Type> symbolTable;

    public SymbolTable(SymbolTable encSymbolTable, HashMap<String, Type> symbolTable) {
        this.encSymbolTable = encSymbolTable;
        this.symbolTable = symbolTable;
    }

    public HashMap<String, Type> getCurrentSymbolTable() {
        return symbolTable;
    }

    public SymbolTable getEncSymbolTable() {
        return encSymbolTable;
    }

    public static class TypeValue extends Type {
      boolean isFunc;
      Type type;

      TypeValue(boolean isFunc, Type type) {
        this.isFunc = isFunc;
        this.type = type;
      }

      public Type getType() {
        return type;
      }
    }

}
