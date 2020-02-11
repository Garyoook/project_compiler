package doc.wacc.astNodes;

import doc.wacc.ASTVisitor;

public class CharNode extends AST {
  private char value;

  public CharNode(char value) {
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
