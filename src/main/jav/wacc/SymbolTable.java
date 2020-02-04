package jav.wacc;

import antlr.BasicParser;

import java.util.HashMap;
import java.util.List;

public class SymbolTable {
    private int scopeLevel;
    private List<HashMap<String, BasicParser.TypeContext>> scope;
    private HashMap<String, BasicParser.TypeContext> symbolTable;

    public SymbolTable(int scopeLevel, List<HashMap<String, BasicParser.TypeContext>> scope, HashMap<String, BasicParser.TypeContext> symbolTable) {
        this.scopeLevel = scopeLevel;
        this.scope = scope;
        this.symbolTable = symbolTable;
    }

    public List<HashMap<String, BasicParser.TypeContext>> getScope() {
        return scope;
    }

    public HashMap<String, BasicParser.TypeContext> getCurrentSymbolTable() {
        return symbolTable;
    }

    public int getScopeLevel() {
        return scopeLevel;
    }
}
