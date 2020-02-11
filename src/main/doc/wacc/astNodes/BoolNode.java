package doc.wacc.astNodes;

public class BoolNode extends ExpressionAST {
  private boolean value;

  public BoolNode(boolean value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }
}
