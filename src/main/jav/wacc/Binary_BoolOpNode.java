package jav.wacc;

import antlr.BasicParser;

import static java.lang.System.exit;

public class Binary_BoolOpNode extends AST{
  BasicParser.Binary_bool_operContext operContext;
  AST expr1;
  AST expr2;
  Binary_BoolOpNode(BasicParser.Binary_bool_operContext operContext, AST expr1, AST expr2) {
    this.operContext = operContext;
    this.expr1 = expr1;
    this.expr2 = expr2;

    is_bool(expr1);
    is_bool(expr2);
    if ((expr1 instanceof Lowest_BinaryOpNode) || (expr2 instanceof Lowest_BinaryOpNode)) {
      System.out.println("#semantic_error#");  exit(200);
    }

  }
  @Override
  public String toString() {
    return expr1 + operContext.getText() + expr2;
  }
}
