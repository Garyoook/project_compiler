package doc.wacc.astNodes;

import doc.wacc.utils.ErrorMessage;
import doc.wacc.utils.SymbolTable;
import doc.wacc.utils.Type;

import java.util.HashMap;

import static doc.wacc.utils.CompilerVisitor.currentCharPos;
import static doc.wacc.utils.CompilerVisitor.currentLine;
import static java.lang.System.*;


public abstract class AST {
  public static SymbolTable symbolTable = new SymbolTable(null, new HashMap<String, Type>());

  public static boolean is_int(AST expr1) {
    if (expr1 instanceof ArrayElemNode) {
      Type type = symbolTable.getVariable(((ArrayElemNode) expr1).getName());
      while (type instanceof Type.ArrayType) {
        type = ((Type.ArrayType) type).getType();
      }
      if (!type.equals(Type.intType())) {
        return false;
      }
    } else
    if (expr1 instanceof IdentNode) {
      Type t = symbolTable.getVariable(((IdentNode) expr1).getIdent());
      if (!(t.equals(Type.intType()) || t instanceof Type.PtrType)) {
        return false;
      }
    } else
    if (((expr1 instanceof BoolNode) ||
        (expr1 instanceof CharNode) ||
        (expr1 instanceof StringNode) ||
        (expr1 instanceof Binary_BoolOpNode) ||
        (expr1 instanceof UnaryOpNode && ((UnaryOpNode) expr1).getChrContext() != null
                && ((UnaryOpNode) expr1).getUnary_notContext() != null)
    )) {
      return false;
    }
    return true;
  }

  public static boolean is_bool(AST exp) {
    if (exp instanceof ArrayElemNode) {
      Type type = symbolTable.getVariable(((ArrayElemNode) exp).getName());
      while (type instanceof Type.ArrayType) {
        type = ((Type.ArrayType) type).getType();
      }
      if (!type.equals(Type.boolType())) {
        return false;
      }
    } else
    if (exp instanceof IdentNode) {
      if (symbolTable.getVariable(((IdentNode) exp).getIdent()) == null) {
        ErrorMessage.addSemanticError("variable not defined: " + ((IdentNode) exp).getIdent() +
                " at line:" + currentLine + ":" +
                currentCharPos);
        return false;
      }
      if (!symbolTable.getVariable(((IdentNode) exp).getIdent()).equals(Type.boolType())) {
        return false;
      }
    } else
    if (!(exp instanceof BoolNode ||
          exp instanceof Binary_BoolOpNode ||
          exp instanceof UnaryOpNode && ((UnaryOpNode) exp).getUnary_notContext() != null ||
          (exp instanceof ExprWithParen && is_bool(((ExprWithParen) exp).getExpr())))) {

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

  public static boolean is_Array(AST exp) {
    if (exp instanceof IdentNode) {
      return symbolTable.getVariable(((IdentNode) exp).getIdent()) instanceof Type.ArrayType;
    } else {
      return exp instanceof Type.ArrayType;
    }
  }

  public static boolean is_Char(AST exp) {
    if (exp instanceof ArrayElemNode) {
      Type type = symbolTable.getVariable(((ArrayElemNode) exp).getName());
      while (type instanceof Type.ArrayType) {
        type = ((Type.ArrayType) type).getType();
      }
      if (!type.equals(Type.charType())) {
        return false;
      }
    } else if (exp instanceof IdentNode) {
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
