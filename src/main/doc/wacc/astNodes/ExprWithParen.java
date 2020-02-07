package doc.wacc.astNodes;

public class ExprWithParen extends AST {
  AST expr;

  public ExprWithParen(AST expr) {
    this.expr = expr;
  }

  @Override
  public String toString() {
    return expr.toString();
  }
}
