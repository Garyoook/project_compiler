package jav.wacc;

import antlr.BasicParser;

public class UnaryOpNode extends AST{
  BasicParser.Unary_operContext operContext;
  AST expr;
  UnaryOpNode(BasicParser.Unary_operContext operContext, AST expr) {
    this.operContext = operContext;
    this.expr = expr;
  }

  @Override
  public String toString() {
    return operContext.getText() + expr;
  }
}
