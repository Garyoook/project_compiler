package jav.wacc;

import antlr.BasicParser;

public class UnaryChrNode extends AST{
  BasicParser.Unary_chrContext operContext;
  AST expr;
  UnaryChrNode(BasicParser.Unary_chrContext operContext, AST expr) {
    this.operContext = operContext;
    this.expr = expr;
  }

  @Override
  public String toString() {
    return operContext.getText() + expr;
  }
}