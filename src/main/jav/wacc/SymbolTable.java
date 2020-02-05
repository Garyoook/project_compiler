package jav.wacc;

import antlr.BasicParser;

import java.util.HashMap;
import java.util.List;

public class SymbolTable {
    private SymbolTable encSymbolTable;
    private HashMap<String, TypeValue> symbolTable;

    public SymbolTable(SymbolTable encSymbolTable, HashMap<String,TypeValue> symbolTable) {
        this.encSymbolTable = encSymbolTable;
        this.symbolTable = symbolTable;
    }

    public HashMap<String, TypeValue> getCurrentSymbolTable() {
        return symbolTable;
    }

    public SymbolTable getEncSymbolTable() {
        return encSymbolTable;
    }

    public static class TypeValue {
      boolean isFunc;
      BasicParser.TypeContext typeContext;

      TypeValue(boolean isFunc, BasicParser.TypeContext typeContext) {
        this.isFunc = isFunc;
        this.typeContext = typeContext;
      }

      public BasicParser.TypeContext getTypeContext() {
        return typeContext;
      }
    }

}
