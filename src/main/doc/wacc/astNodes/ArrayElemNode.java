package doc.wacc.astNodes;

public class ArrayElemNode extends AST {
  private final String name;
  private final AST expr;

  public ArrayElemNode(String name, AST expr) {
    this.name = name;
    this.expr = expr;
  }

  public AST getExpr() {
    return expr;
  }

  public String getName() {
    return name;
  }
}
