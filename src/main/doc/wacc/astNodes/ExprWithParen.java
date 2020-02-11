package doc.wacc.astNodes;

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
}
