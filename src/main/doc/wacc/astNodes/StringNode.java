package doc.wacc.astNodes;

public class StringNode extends AST {
  private String value;

  public StringNode(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public int getStringLength() {
    return value.length() - 2; // to substract the number of quote sign.
  }

  @Override
  public String toString() {
    return value;
  }
}
