package jav.wacc;

import antlr.BasicParser;

import static java.lang.System.exit;

public class High_BinaryOpNode extends AST{
  BasicParser.Hignp_bin_opContext operContext;
  AST expr1;
  AST expr2;
  High_BinaryOpNode(BasicParser.Hignp_bin_opContext operContext, AST expr1, AST expr2) {
    this.operContext = operContext;
    this.expr1 = expr1;
    this.expr2 = expr2;

   is_int(expr1);
   is_int(expr2);
  }
  @Override
  public String toString() {
    return expr1 + operContext.getText() + expr2;
  }
}
