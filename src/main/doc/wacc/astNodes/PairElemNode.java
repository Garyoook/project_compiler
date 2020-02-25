package doc.wacc.astNodes;

public class PairElemNode extends AST {
  private final String position;
//  private final AST expr;

  public PairElemNode(String position) {
    this.position = position;
//    this.expr = expr1;
  }

  public String getPosition() {
    return position;
  }
}
