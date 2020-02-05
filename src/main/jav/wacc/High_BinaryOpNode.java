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
   if (!same_type(expr1, expr2) || !same_type(expr2, expr1)) {
      System.out.println("#semantic_error#");  exit(200);
    }
  }
  @Override
  public String toString() {
    return expr1 + operContext.getText() + expr2;
  }
}
