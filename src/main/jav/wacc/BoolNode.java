package jav.wacc;

public class BoolNode extends AST {
  boolean value;

  BoolNode(boolean value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }
}
