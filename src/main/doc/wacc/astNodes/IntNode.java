package doc.wacc.astNodes;

public class IntNode extends AST {
  private int value;

  public IntNode(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }
}
