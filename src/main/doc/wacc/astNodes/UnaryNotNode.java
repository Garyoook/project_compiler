package doc.wacc.astNodes;

import antlr.BasicParser;

public class UnaryNotNode extends AST{
  BasicParser.Unary_notContext operContext;
  AST expr;
  public UnaryNotNode(BasicParser.Unary_notContext operContext, AST expr) {
    this.operContext = operContext;
    this.expr = expr;
  }

  @Override
  public String toString() {
    return operContext.getText() + expr;
  }
}
