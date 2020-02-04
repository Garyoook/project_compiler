package jav.wacc;

public class StringNode extends AST {
  String value;
  StringNode(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return value;
  }
}
