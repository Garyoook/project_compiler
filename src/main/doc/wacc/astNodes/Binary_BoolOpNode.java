package doc.wacc.astNodes;

import antlr.BasicParser;
import doc.wacc.utils.ErrorMessage;

import static antlr.BasicParser.*;
import static doc.wacc.utils.CompilerVisitor.currentCharPos;
import static doc.wacc.utils.CompilerVisitor.currentLine;
import static doc.wacc.utils.CompilerVisitor.dynamically_Typed;
import static java.lang.System.exit;

public class Binary_BoolOpNode extends AST{
  private Binary_bool_operContext operContext;
  private Lowest_binbool_opContext lowest_binaryOpNode;
  private Low_binbool_opContext low_binbool_opContext;
  private AST expr1;
  private AST expr2;

  public Binary_BoolOpNode(Binary_bool_operContext operContext, AST expr1, AST expr2) {
    this.operContext = operContext;
    this.expr1 = expr1;
    this.expr2 = expr2;

    if (!dynamically_Typed) {
      if (!(is_bool(expr1) && is_bool(expr2) || is_int(expr1) && is_int(expr2) || is_Char(expr1) && is_Char(expr2))) {
        ErrorMessage.addSemanticError("wrong type in " + operContext.getText() +
            " at line:" + currentLine + ":" + currentCharPos);
      }
    }
  }

  public Binary_BoolOpNode(Lowest_binbool_opContext operContext, AST expr1, AST expr2) {
    this.lowest_binaryOpNode = operContext;
    this.expr1 = expr1;
    this.expr2 = expr2;

    if (!dynamically_Typed) {
      if (!(is_bool(expr1) && is_bool(expr2))) {
        System.out.println("Semantic error: wrong type, should be Bool in" + getClass() +
            " at line:" + currentLine + ":" + currentCharPos +
            "\nExit code 200 returned");
        exit(200);
      }
    }
  }

  public Binary_BoolOpNode(Low_binbool_opContext operContext, AST expr1, AST expr2) {
    this.low_binbool_opContext = operContext;
    this.expr1 = expr1;
    this.expr2 = expr2;

    if (!dynamically_Typed) {
      if (!(is_int(expr1) && is_int(expr1) ||
          is_Char(expr1) && is_Char(expr2) ||
          is_String(expr1) && is_String(expr2) ||
          is_bool(expr1) && is_bool(expr2) ||
          is_Pair(expr1) && is_Pair(expr2))
      ) {
        System.out.println("Semantic error: wrong type in " + operContext.getText() +
            " at line:" + currentLine + ":" + currentCharPos +
            "\nExit code 200 returned");
        exit(200);
      }
    }
  }
  @Override
  public String toString() {
    return expr1 + operContext.getText() + expr2;
  }

  public AST getExpr1() {
    return expr1;
  }

  public AST getExpr2() {
    return expr2;
  }

  public boolean isLow_binbool_opContext() {
    return low_binbool_opContext != null;
  }

  public boolean isLowest_binaryOpNode() {
    return lowest_binaryOpNode != null;
  }

  public boolean isOperContext() {
    return operContext != null;
  }

  public boolean isEqual() {
    if (low_binbool_opContext != null) {
      return low_binbool_opContext.EQUAL() != null;
    }
    return false;
  }

  public boolean isNotEqual() {
    if (low_binbool_opContext != null) {
      return low_binbool_opContext.NOT_EQUAL() != null;
    }
    return false;
  }

  public boolean isBinaryAnd() {
    if(lowest_binaryOpNode != null) {
      return lowest_binaryOpNode.B_AND() != null;
    }
    return false;
  }

  public boolean isBinaryOr() {
    if(lowest_binaryOpNode != null) {
      return lowest_binaryOpNode.B_OR() != null;
    }
    return false;
  }

  public boolean isGreater() {
    if(operContext != null) {
      return operContext.GREATER() != null;
    }
    return false;
  }

  public boolean isGreaterOrEqual() {
    if(operContext != null) {
      return operContext.GREATER_E() != null;
    }
    return false;
  }

  public boolean isSmaller() {
    if(operContext != null) {
      return operContext.SMALLER() != null;
    }
    return false;
  }

  public boolean isSmallerOrEqual() {
    if(operContext != null) {
      return operContext.SMALLER_E() != null;
    }
    return false;
  }

}
