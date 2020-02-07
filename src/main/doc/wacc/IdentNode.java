package doc.wacc;

public class IdentNode extends AST {
  String ident;
  IdentNode(String ident) {
    this.ident = ident;
  }

  @Override
  public String toString() {
    return ident;
  }
}
