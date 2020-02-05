package jav.wacc;

import java.util.HashMap;

import static jav.wacc.Type.*;
import static java.lang.System.exit;


public abstract class AST {
  public static SymbolTable symbolTable = new SymbolTable(null, new HashMap<String, Type>());

  public boolean is_int(AST expr1) {
    if (expr1 instanceof IdentNode) {
      if (!symbolTable.getCurrentSymbolTable().get(((IdentNode) expr1).ident).equals(intType())) {
        return false;
      }
    } else
    if (((expr1 instanceof Lowest_BinaryOpNode) ||
        (expr1 instanceof BoolNode) ||
        (expr1 instanceof CharNode) ||
        (expr1 instanceof StringNode) ||
        (expr1 instanceof Binary_BoolOpNode) ||
        (expr1 instanceof UnaryChrNode) ||
        (expr1 instanceof UnaryNotNode)
    )) {
      return false;
    }
    return true;
  }

  public boolean is_bool(AST exp) {
    if (exp instanceof IdentNode) {
      if (symbolTable.getCurrentSymbolTable().get(((IdentNode) exp).ident) == null) {
        System.out.println("semantic: variable not defined: " + ((IdentNode) exp).ident);
        exit(200);
      }
      if (!symbolTable.getCurrentSymbolTable().get(((IdentNode) exp).ident).equals(boolType())) {
        return false;
      }
    } else
    if (!(exp instanceof Lowest_BinaryOpNode ||
          exp instanceof BoolNode ||
          exp instanceof Binary_BoolOpNode ||
          exp instanceof UnaryNotNode ||
          exp instanceof Low_BinaryOpNode ||
          (exp instanceof ExprWithParen && is_bool(((ExprWithParen) exp).expr)))) {

      return false;
    }
    return true;
  }

  public boolean is_Pair(AST exp) {
//    if (exp instanceof PairAST) {
//      if (!symbolTable.getCurrentSymbolTable().get(((Pa) exp).ident).equals(charType())) {
//        return false;
//      }
//    }
    return true;
  }

  public boolean is_Char(AST exp) {
    if (exp instanceof IdentNode) {
      if (!symbolTable.getCurrentSymbolTable().get(((IdentNode) exp).ident).equals(charType())) {
        return false;
      }
    } else
    if (!(exp instanceof CharNode)) {
      return false;
    }
    return true;
  }

  public boolean is_String(AST exp) {
    if (exp instanceof IdentNode) {
      return symbolTable.getCurrentSymbolTable().get(((IdentNode) exp).ident).equals(stringType());
    }
    return true;
  }


  public boolean same_type(AST expr1, AST expr2) {
    if (expr1 instanceof IdentNode) {
      Type type1 = symbolTable.getCurrentSymbolTable().get(((IdentNode) expr1).ident);
        if (type1 == null) {
          System.out.println("Semantic error: Variable not defined:" + ((IdentNode) expr1).ident);
          exit(200);
        }
      if (expr2 instanceof IdentNode) {
        Type type2 = symbolTable.getCurrentSymbolTable().get(((IdentNode) expr2).ident);
          if (type2 == null) {
            System.out.println("Semantic error: Variable not defined:" + ((IdentNode) expr1).ident);
            exit(200);
          }
        if (!type1.equals(type2)){
          System.out.println("#semantic_error#");  exit(200);}
        } else {
        if (expr2 == null) {
          System.out.println("expr2 is a null");
        }
//        if (type1.equals(intType()) && !(expr2 instanceof IntNode)) {
//            System.out.println("#semantic_error#");  exit(200);
//          }
//          if (type1.equals(boolType()) && !(expr2 instanceof BoolNode)) {
//            System.out.println("#semantic_error#");  exit(200);
//          }
//          if (type1.equals(charType())&& !(expr2 instanceof CharNode)) {
//            System.out.println("#semantic_error#");  exit(200);
//          }
//          if (type1.equals(stringType()) && !(expr2 instanceof StringNode)) {
//            System.out.println("#semantic_error#");  exit(200);
//          }
////          if (type1.pair_type().PAIR() != null) && !(expr2 instanceof PairNode) {
////
////        }
      }
    } else {
      if (expr1 == null) {
        System.out.println("expr1 is a null");
      }
    }
    return true;
  }


}
