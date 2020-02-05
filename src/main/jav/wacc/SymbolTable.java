package jav.wacc;

import antlr.BasicParser;

import java.util.HashMap;
import java.util.List;

public class SymbolTable {
    private SymbolTable encSymbolTable;
    private HashMap<String, BasicParser.TypeContext> symbolTable;

    public SymbolTable(SymbolTable encSymbolTable, HashMap<String, BasicParser.TypeContext> symbolTable) {
        this.encSymbolTable = encSymbolTable;
        this.symbolTable = symbolTable;
    }

    public HashMap<String, BasicParser.TypeContext> getCurrentSymbolTable() {
        return symbolTable;
    }

    public SymbolTable getEncSymbolTable() {
        return encSymbolTable;
    }

}
