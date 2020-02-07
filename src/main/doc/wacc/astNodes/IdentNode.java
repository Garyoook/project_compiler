package doc.wacc.astNodes;

public class IdentNode extends AST {
  private String ident;
  public IdentNode(String ident) {
    this.ident = ident;
  }

  public String getIdent() {
    return this.ident;
  }

  @Override
  public String toString() {
    return ident;
  }
}
