package jav.wacc;

public class PrintlnAst extends AST {
  private AST expr;

  public PrintlnAst(AST expr) {
    this.expr = expr;
  }

  @Override
  public String toString() {
    return "Println: " + expr + "\n";
  }
}
