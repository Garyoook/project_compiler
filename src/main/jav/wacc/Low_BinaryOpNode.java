package jav.wacc;

import antlr.BasicParser;

import static java.lang.System.exit;

public class Low_BinaryOpNode extends AST{
  BasicParser.Low_binbool_opContext operContext;
  AST expr1;
  AST expr2;
  Low_BinaryOpNode(BasicParser.Low_binbool_opContext operContext, AST expr1, AST expr2) {
    this.operContext = operContext;
    this.expr1 = expr1;
    this.expr2 = expr2;

    if (! (is_int(expr1) && is_int(expr1) || is_Char(expr1) && is_Char(expr2))) {
      System.out.println("wrong type in " + operContext.getText());
      exit(200);
    }

  }
  @Override
  public String toString() {
    return expr1 + operContext.getText() + expr2;
  }
}
