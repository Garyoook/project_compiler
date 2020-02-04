package jav.wacc;

import antlr.BasicParser;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.System.exit;


public abstract class AST {
  public static SymbolTable symbolTable = new SymbolTable(0, new ArrayList<>(), new HashMap<String, BasicParser.TypeContext>());

  public boolean same_type(AST expr1, AST expr2) {
    if (expr1 instanceof IdenNode) {
      BasicParser.TypeContext type1 = symbolTable.getCurrentSymbolTable().get(((IdenNode) expr1).ident);
        if (type1 == null) {
          System.out.println("Semantic error: Variable not defined:" + ((IdenNode) expr1).ident);
          exit(200);
        }
      if (expr2 instanceof IdenNode) {
        BasicParser.TypeContext type2 = symbolTable.getCurrentSymbolTable().get(((IdenNode) expr2).ident);
          if (type2 == null) {
            System.out.println("Semantic error: Variable not defined:" + ((IdenNode) expr1).ident);
            exit(200);
          }
        if (!type1.getText().equals(type2.getText())){
          System.out.println("#semantic_error#");  exit(200);}
        } else {
        if (expr2 == null) {
          System.out.println("expr2 is a null!!!!");
        }
        if (type1.base_type().INT() != null && !(expr2 instanceof IntNode)) {
            System.out.println("#semantic_error#");  exit(200);
          }
          if (type1.base_type().BOOL() != null && !(expr2 instanceof BoolNode)) {
            System.out.println("#semantic_error#");  exit(200);
          }
          if (type1.base_type().CHAR() != null && !(expr2 instanceof CharNode)) {
            System.out.println("#semantic_error#");  exit(200);
          }
          if (type1.base_type().STRING() != null && !(expr2 instanceof StringNode)) {
            System.out.println("#semantic_error#");  exit(200);
          }
//          if (type1.pair_type().PAIR() != null) && !(expr2 instanceof PairNode) {
//
//        }
      }
    } else {
      if (expr1 == null) {
        System.out.println("expr1 is a null");
      }
    }
    return true;
  }


}
