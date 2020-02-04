package jav.wacc;

public class IntNode extends AST {
  int value;
  IntNode(int value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }
}
