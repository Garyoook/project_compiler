package jav.wacc;

public class IdenNode extends AST {
  String ident;
  IdenNode(String ident) {
    this.ident = ident;
  }

  @Override
  public String toString() {
    return ident;
  }
}
