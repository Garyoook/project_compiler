package doc.wacc.astNodes;

import doc.wacc.ASTVisitor;

public class ReturnAst extends AST {
  private AST expr;

  public ReturnAst(AST expr) {
    this.expr = expr;
  }

  @Override
  public String toString() {
    return "return " + expr + "\n";
  }

  @Override
  public void Accept(ASTVisitor v) {

  }
}
