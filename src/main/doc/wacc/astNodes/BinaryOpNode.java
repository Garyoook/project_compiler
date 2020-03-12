package doc.wacc.astNodes;

import antlr.BasicParser;
import doc.wacc.utils.ErrorMessage;

import static antlr.BasicParser.*;
import static doc.wacc.utils.CompilerVisitor.currentCharPos;
import static doc.wacc.utils.CompilerVisitor.currentLine;
import static doc.wacc.utils.CompilerVisitor.dynamically_Typed;
import static java.lang.System.exit;
import static java.lang.System.mapLibraryName;

public class BinaryOpNode extends AST{
  private Binary_operContext operContext;
  private AST expr1;
  private AST expr2;
  private Hignp_bin_opContext hignp_bin_opContext;

  public BinaryOpNode(Binary_operContext operContext, AST expr1, AST expr2) {
    this.operContext = operContext;
    this.expr1 = expr1;
    this.expr2 = expr2;

    if (!dynamically_Typed) {
      if (!(is_int(expr1) && is_int(expr2))) {
        ErrorMessage.addSemanticError("wrong type in " + operContext.getText() +
            " at line:" + currentLine + ":" + currentCharPos);
      }
    }
  }

  public BinaryOpNode(Hignp_bin_opContext operContext, AST expr1, AST expr2) {
    this.hignp_bin_opContext = operContext;
    this.expr1 = expr1;
    this.expr2 = expr2;

    if (!dynamically_Typed) {
      if (!(is_int(expr1) && is_int(expr2))) {
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

  public boolean isOperContext() {
    return operContext != null;
  }

  public boolean isHignp_bin_opContext() {
    return hignp_bin_opContext != null;
  }

  public boolean isPlus() {
    if (operContext != null) {
      return operContext.PLUS() != null;
    }
    return false;
  }

  public boolean isMinus() {
    if (operContext != null) {
      return operContext.MINUS() != null;
    }
    return false;
  }

  public boolean isDivid() {
    if (hignp_bin_opContext != null) {
      return hignp_bin_opContext.DIVIDE() != null;
    }
    return false;
  }

  public boolean isTime() {
    if (hignp_bin_opContext != null) {
      return hignp_bin_opContext.TIME() != null;
    }
    return false;
  }

  public boolean isMod() {
    if (hignp_bin_opContext != null) {
      return hignp_bin_opContext.MOD() != null;
    }
    return false;
  }
}
