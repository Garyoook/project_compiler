package jav.wacc;

import static java.lang.System.exit;

public class ExitAst extends AST {
  private AST expr;

  public ExitAst(AST expr) {
    this.expr = expr;
    if (!is_int(expr)) {
      System.out.println("Semantic error: Type incompatible: exit code should be an int");
      exit(200);
    }
  }

  @Override
  public String toString() {
    return "exit(" + expr + ")\n";
  }
}
