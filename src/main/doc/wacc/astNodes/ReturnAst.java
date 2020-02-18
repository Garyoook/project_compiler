package doc.wacc.astNodes;

public class ReturnAst extends AST {
  private AST expr;

  public ReturnAst(AST expr) {
    this.expr = expr;
  }

  public AST getExpr() {
    return expr;
  }

  @Override
  public String toString() {
    return "return " + expr + "\n";
  }
}
