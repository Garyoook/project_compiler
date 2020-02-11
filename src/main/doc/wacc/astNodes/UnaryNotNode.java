package doc.wacc.astNodes;

import antlr.BasicParser;
import doc.wacc.ASTVisitor;

public class UnaryNotNode extends AST{
  private BasicParser.Unary_notContext operContext;
  private AST expr;

  public UnaryNotNode(BasicParser.Unary_notContext operContext, AST expr) {
    this.operContext = operContext;
    this.expr = expr;
  }

  @Override
  public String toString() {
    return operContext.getText() + expr;
  }

  @Override
  public void Accept(ASTVisitor v) {

  }
}
