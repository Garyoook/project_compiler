package doc.wacc.utils;

import java.util.HashMap;
import java.util.List;

import static java.lang.System.exit;

public class SymbolTable {
    private SymbolTable encSymbolTable;
    private HashMap<String, Type> symbolTable;
    private HashMap<String, Integer> stackTable;
    private int paramCounter = 0;
    public boolean inFunction = false;
    public boolean inIfThenElse = false;
    public boolean hasReturned = false;
    public boolean thenHasReturn = false;
    public int local_variable = 0;

    public SymbolTable(SymbolTable encSymbolTable, HashMap<String, Type> symbolTable) {
        this.encSymbolTable = encSymbolTable;
        this.symbolTable = symbolTable;
        this.stackTable = new HashMap<>();
    }

    public void set_local_variable(int n){
      local_variable = n;
    }

  public int getLocal_variable() {
    return local_variable;
  }

  public SymbolTable previousScope() {
        return encSymbolTable;
    }

    public void putVariable(String name, Type type) {

      if (symbolTable.get(name) != null){
        ErrorMessage.addSemanticError("Semantic error: variable double declared : " + name);
      } else {
        symbolTable.put(name, type);
      }
    }

    public void putStackTable(String name, Integer stacknum) {
        stackTable.put(name, stacknum);
    }

    public int getStackTable(String name) {
        return stackTableHelper(name, this);
    }

  private int stackTableHelper(String name, SymbolTable symbolTable) {
    if (symbolTable == null) {
//      ErrorMessage.addSemanticError("Semantic error: variable not exist " + name);
//      System.out.println("no variable");
      return -1;
    }
    if (symbolTable.symbolTable == null) {
//      ErrorMessage.addSemanticError("Semantic error: variable not exist " + name);
//      System.out.println("no variable");
      return -1;
    }
    if (symbolTable.stackTable.get(name) != null) {
      return symbolTable.stackTable.get(name);
    } else {
      return stackTableHelper(name, symbolTable.encSymbolTable);
    }
  }

    public Type getVariable(String name) {
      return sbTableHelper(name, this);
    }

    private Type sbTableHelper(String name, SymbolTable symbolTable) {
      if (symbolTable == null) {
        ErrorMessage.addSemanticError("Semantic error: variable not exist " + name);
        return Type.errorType();
      }
      if (symbolTable.symbolTable == null) {
        ErrorMessage.addSemanticError("Semantic error: variable not exist " + name);
        return Type.errorType();
      }
      if (symbolTable.symbolTable.get(name) != null) {
        return symbolTable.symbolTable.get(name);
      } else {
        return sbTableHelper(name, symbolTable.encSymbolTable);
      }
    }

  public void setParamCounter(int paramCounter) {
    this.paramCounter = paramCounter;
  }

  public int getParamCounter() {
    return paramCounter;
  }

  public void inheritFlags(SymbolTable s) {
      this.thenHasReturn = s.thenHasReturn;
      this.inFunction = s.inFunction;
      this.hasReturned = s.hasReturned;
      this.inIfThenElse = s.inIfThenElse;
    }
}
