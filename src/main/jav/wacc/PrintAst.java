package jav.wacc;

public class PrintAst extends AST {
  private AST expr;

  public PrintAst(AST expr) {
    this.expr = expr;
  }

  @Override
  public String toString() {
    return "Print: " + expr + "\n";
  }
}
