package doc.wacc.astNodes;

public class CharNode extends AST {
  private char value;

  public CharNode(char value) {
    this.value = value;
  }

  public char getCharValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }
}
