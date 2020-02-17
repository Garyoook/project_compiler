package doc.wacc.astNodes;

import antlr.BasicParser;

import static antlr.BasicParser.*;
import static doc.wacc.utils.CompilerVisitor.currentCharPos;
import static doc.wacc.utils.CompilerVisitor.currentLine;
import static java.lang.System.exit;

public class BinaryOpNode extends AST{
  private Binary_operContext operContext;
  private AST expr1;
  private AST expr2;
  private Hignp_bin_opContext hignp_bin_opContext;

  public BinaryOpNode(Binary_operContext operContext, AST expr1, AST expr2) {
    this.operContext = operContext;
    this.expr1 = expr1;
    this.expr2 = expr2;

    if (!(is_int(expr1) && is_int(expr2))) {
      System.out.println("Semantic error: wrong type in " + operContext.getText() +
              " at line:" + currentLine + ":" + currentCharPos +
              "\nExit code 200 returned");
      exit(200);
    }
  }

  public BinaryOpNode(Hignp_bin_opContext operContext, AST expr1, AST expr2) {
    this.hignp_bin_opContext = operContext;
    this.expr1 = expr1;
    this.expr2 = expr2;

    if (!(is_int(expr1) && is_int(expr2))) {
      System.out.println("Semantic error: wrong type in " + operContext.getText() +
              " at line:" + currentLine + ":" + currentCharPos +
              "\nExit code 200 returned");
      exit(200);
    }
  }

  @Override
  public String toString() {
    return expr1 + operContext.getText() + expr2;
  }
}
