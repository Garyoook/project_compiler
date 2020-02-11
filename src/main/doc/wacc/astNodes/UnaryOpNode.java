package doc.wacc.astNodes;

import antlr.BasicParser;
import doc.wacc.ASTVisitor;

public class UnaryOpNode extends AST{
  private BasicParser.Unary_operContext operContext;
  private AST expr;

  public UnaryOpNode(BasicParser.Unary_operContext operContext, AST expr) {
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
