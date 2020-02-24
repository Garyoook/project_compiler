package doc.wacc.astNodes;

import java.util.List;

public class ArrayElemNode extends AST {
  private final String name;
  private final List<AST> exprs;

  public ArrayElemNode(String name, List<AST> expr) {
    this.name = name;
    this.exprs = expr;
  }

  public List<AST> getExprs() {
    return exprs;
  }

  public String getName() {
    return name;
  }

}
