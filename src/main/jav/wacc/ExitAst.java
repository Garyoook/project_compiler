package jav.wacc;

public class ExitAst extends AST {
  private AST expr;

  public ExitAst(AST expr) {
    this.expr = expr;
  }

  @Override
  public String toString() {
    return "exit(" + expr + ")\n";
  }
}
