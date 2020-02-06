package jav.wacc;

import static java.lang.System.exit;

public class PrintlnAst extends AST {
  private AST expr;

  public PrintlnAst(AST expr) {
    if (expr instanceof IdentNode) {
      if (symbolTable.getVariable(((IdentNode) expr).ident) == null) {
        System.out.println(("Semantic error: " + ((IdentNode) expr).ident) + " not defined");
        exit(200);
      }
    }
    if (expr instanceof ArrayAST) {
      System.out.println(("Semantic error: " + ((IdentNode) expr).ident) + " array can't be printed");
      exit(200);

    }
    this.expr = expr;

  }

  @Override
  public String toString() {
    return "Println: " + expr + "\n";
  }
}
