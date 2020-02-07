package doc.wacc;

public class
CharNode extends AST {
  char value;

  CharNode(char value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }
}
