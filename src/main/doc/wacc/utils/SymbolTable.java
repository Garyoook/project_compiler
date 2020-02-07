package doc.wacc.utils;

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

    public SymbolTable previousScope() {
        return encSymbolTable;
    }

    public void putVariable(String name, Type type) {
      if (symbolTable.get(name) != null){
        System.out.println("Semantic error: variable double declared : " + name +
                "\nExit code 200 returned");
        exit(200);
      } else {
        symbolTable.put(name, type);
      }
    }

    public Type getVariable(String name) {
      return sbTableHelper(name, this);
    }

    private Type sbTableHelper(String name, SymbolTable symbolTable) {
      if (symbolTable == null) {
        System.out.println("Semantic error: variable not exist " + name +
                "\nExit code 200 returned");
        exit(200);
      }
      if (symbolTable.symbolTable == null) {
        System.out.println("Semantic error: variable not exist " + name +
                "\nExit code 200 returned");
        exit(200);
      }
      if (symbolTable.symbolTable.get(name) != null) {
        return symbolTable.symbolTable.get(name);
      } else {
        return sbTableHelper(name, symbolTable.encSymbolTable);
      }
    }

    public void inheritFlags(SymbolTable s) {
      this.thenHasReturn = s.thenHasReturn;
      this.inFunction = s.inFunction;
      this.hasReturned = s.hasReturned;
      this.inIfThenElse = s.inIfThenElse;
    }
}
