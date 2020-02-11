package doc.wacc.astNodes;

import doc.wacc.ASTVisitor;

public class FreeAst extends AST {
  private AST expr;

  public FreeAst(AST expr) {
    this.expr = expr;
  }

  @Override
  public String toString() {
    return "Free expr: " + expr + "\n";
  }

  @Override
  public void Accept(ASTVisitor v) {

  }
}
