package doc.wacc.astNodes;

import doc.wacc.ASTVisitor;

public class ASkipAst extends AST {

  @Override
  public String toString() {
    return "ASkip\n";
  }

  @Override
  public void Accept(ASTVisitor v) {

  }
}
