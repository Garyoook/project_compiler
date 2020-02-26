package doc.wacc.astNodes;

public class PairElemNode extends AST {
  private final String name;

  public PairElemNode(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
