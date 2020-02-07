package doc.wacc.astNodes;

import doc.wacc.utils.SymbolTable;
import doc.wacc.utils.Type;

import java.util.HashMap;

import static doc.wacc.utils.CompilerVisitor.currentCharPos;
import static doc.wacc.utils.CompilerVisitor.currentLine;
import static java.lang.System.exit;


public abstract class AST {
  public static SymbolTable symbolTable = new SymbolTable(null, new HashMap<String, Type>());

  public static boolean is_int(AST expr1) {
    if (expr1 instanceof IdentNode) {
      if (!symbolTable.getVariable(((IdentNode) expr1).getIdent()).equals(Type.intType())) {
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

  public static boolean is_bool(AST exp) {
    if (exp instanceof IdentNode) {
      if (symbolTable.getVariable(((IdentNode) exp).getIdent()) == null) {
        System.out.println("Semantic error: variable not defined: " + ((IdentNode) exp).getIdent() +
                " at line:" + currentLine + ":" +
                currentCharPos +
                "\nExit code 200 returned");
        exit(200);
      }
      if (!symbolTable.getVariable(((IdentNode) exp).getIdent()).equals(Type.boolType())) {
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

  public static boolean is_Pair(AST exp) {
    if (exp instanceof IdentNode) {
      return symbolTable.getVariable(((IdentNode) exp).getIdent()) instanceof Type.PairType;
    } else {
      return exp instanceof PairAST;
    }
  }

  public static boolean is_Char(AST exp) {
    if (exp instanceof IdentNode) {
      if (!symbolTable.getVariable(((IdentNode) exp).getIdent()).equals(Type.charType())) {
        return false;
      }
    } else
    if (!(exp instanceof CharNode)) {
      return false;
    }
    return true;
  }

  public static boolean is_String(AST exp) {
    if (exp instanceof IdentNode) {
      return symbolTable.getVariable(((IdentNode) exp).getIdent()).equals(Type.stringType());
    }
    if (is_bool(exp) || is_Char(exp) || is_int(exp) ) {
      return false;
    }



    return true;
  }




}
