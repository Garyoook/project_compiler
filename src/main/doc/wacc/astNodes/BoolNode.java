package doc.wacc.astNodes;

public class BoolNode extends AST {
  private boolean value;

  public BoolNode(boolean value) {
    this.value = value;
  }

  public int getBoolValue() {
    if (value) {
      return 1;
    } else return 0;
  }
  @Override
  public String toString() {
    return String.valueOf(value);
  }
}
