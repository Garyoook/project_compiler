package doc.wacc.astNodes;

import static java.lang.System.exit;

public class IfAst extends AST {
  private final AST expr;
  private AST thenbranch;
  private AST elsebranch;


  public IfAst(AST expr, AST thenbranch, AST elsebranch) {
    this.expr = expr;
    this.thenbranch = thenbranch;
    this.elsebranch = elsebranch;

    if (!is_bool(expr)) {
      System.out.println("Semantic error: wrong type in If condition, should be bool" +
              "\nExit code 200 returned");
      exit(200);
    }
  }

  @Override
  public String toString() {
    return "If (" + expr + ") then " + "("
            + thenbranch + ")" +  "else " + "("
            + elsebranch + ")\n";
  }
}
