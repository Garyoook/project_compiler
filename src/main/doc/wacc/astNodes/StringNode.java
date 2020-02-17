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
    int counter = 0;

    for (int i = 0; i < value.length(); i++) {
      if (value.charAt(i) != '\\') {
        counter++;
      }
    }
    return counter - 2; // to substract the number of quote sign.
  }

  @Override
  public String toString() {
    return value;
  }
}
