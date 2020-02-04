package jav.wacc;

import static java.lang.System.exit;

public class IfAst extends AST {
  private final AST expr;
  private AST thenbranch;
  private AST elsebranch;


  public IfAst(AST expr, AST thenbranch, AST elsebranch) {
    this.expr = expr;
    this.thenbranch = thenbranch;
    this.elsebranch = elsebranch;
    if (!(expr instanceof BoolNode) && !(expr instanceof Binary_BoolOpNode)
        && !(expr instanceof Low_BinaryOpNode) && !(expr instanceof Lowest_BinaryOpNode)) {
      System.out.println("#semantic_error#");  exit(200);
    }
  }

  @Override
  public String toString() {
    return "If (" + expr + ") then " + "("
            + thenbranch + ")" +  "else " + "("
            + elsebranch + ")\n";
  }
}
