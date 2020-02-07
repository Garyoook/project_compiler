package doc.wacc.astNodes;

public class StringNode extends AST {
  String value;
  public StringNode(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return value;
  }
}
