package doc.wacc.astNodes;

public class IntNode extends AST {
  int value;
  public IntNode(int value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }
}
