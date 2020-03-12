package doc.wacc.astNodes;

public class FreeAst extends AST {
  private AST expr;

  public FreeAst(AST expr) {
    this.expr = expr;
  }

  public AST getExpr() {
    return expr;
  }

  @Override
  public String toString() {
    return "Free expr: " + expr + "\n";
  }
}
