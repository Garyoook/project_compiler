package doc.wacc.astNodes;

import doc.wacc.ASTVisitor;

public class SkipAst extends AST {
  @Override
  public String toString() {
    return "skip\n";
  }

  @Override
  public void Accept(ASTVisitor v) {

  }
}
