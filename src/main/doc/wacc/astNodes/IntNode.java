package doc.wacc.astNodes;

import doc.wacc.ASTVisitor;

public class IntNode extends AST {
  private int value;

  public IntNode(int value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @Override
  public void Accept(ASTVisitor v) {

  }
}
