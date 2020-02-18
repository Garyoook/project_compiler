package doc.wacc.astNodes;

public class CharNode extends AST {
  private String value;

  public CharNode(String value) {
    this.value = value;
  }

  public char getCharValue() {
    if (value.length() > 3) {
      return value.charAt(2);
    }
    return value.charAt(1);
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }
}
