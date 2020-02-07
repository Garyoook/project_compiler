package doc.wacc;

public class FreeAst extends AST {
  private AST expr;

  public FreeAst(AST expr) {
    this.expr = expr;
  }

  @Override
  public String toString() {
    return "Free expr: " + expr + "\n";
  }
}
