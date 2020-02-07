package doc.wacc;

import antlr.BasicParser;

import java.util.HashMap;

import static java.lang.System.exit;

public class SymbolTable {
    private SymbolTable encSymbolTable;
    private HashMap<String, Type> symbolTable;
    public boolean inFunction = false;
    public boolean inIfThenElse = false;
    public boolean hasReturned = false;
    public boolean thenHasReturn = false;

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

    public Type getVariable(String name) {
      return helperFunction(name, this);
    }

    public void putVariable(String name, Type type) {
      if (symbolTable.get(name) != null){
        System.out.println("Semantic error: variable double declared : " + name);
        exit(200);
      } else {
        symbolTable.put(name, type);
      }
    }

    private Type helperFunction(String name, SymbolTable symbolTable) {
      if (symbolTable == null) {
        System.out.println("Semantic error: variable not exist " + name);
        exit(200);
      }
      if (symbolTable.symbolTable == null) {
        System.out.println("Semantic error: variable not exist " + name);
        exit(200);
      }
      if (symbolTable.symbolTable.get(name) != null) {
        return symbolTable.symbolTable.get(name);
      } else {
        return helperFunction(name, symbolTable.encSymbolTable);
      }
    }

}
