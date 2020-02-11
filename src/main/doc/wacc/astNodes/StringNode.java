package doc.wacc.astNodes;

import doc.wacc.ASTVisitor;

public class StringNode extends AST {
  private String value;

  public StringNode(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return value;
  }

  @Override
  public void Accept(ASTVisitor v) {

  }
}
