package doc.wacc.astNodes;

import doc.wacc.ASTVisitor;

public class ExprWithParen extends AST {
  private AST expr;

  public ExprWithParen(AST expr) {
    this.expr = expr;
  }

  public AST getExpr() {
    return expr;
  }

  @Override
  public String toString() {
    return expr.toString();
  }

  @Override
  public void Accept(ASTVisitor v) {

  }
}
