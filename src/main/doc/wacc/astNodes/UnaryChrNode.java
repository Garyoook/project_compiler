package doc.wacc.astNodes;

import antlr.BasicParser;
import doc.wacc.ASTVisitor;

public class UnaryChrNode extends AST{
  private BasicParser.Unary_chrContext operContext;
  private AST expr;

  public UnaryChrNode(BasicParser.Unary_chrContext operContext, AST expr) {
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