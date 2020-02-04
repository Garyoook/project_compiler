package jav.wacc;

import static java.lang.System.exit;

public class WhileAst extends AST {
  private AST expr;
  private AST stat;


  public WhileAst(AST expr, AST stat) {
    this.expr = expr;
    this.stat = stat;

    if (!(expr instanceof BoolNode) && !(expr instanceof Binary_BoolOpNode)
        && !(expr instanceof Low_BinaryOpNode) && !(expr instanceof Lowest_BinaryOpNode)) {
      System.out.println("#semantic_error#");  exit(200);
    }
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("while (").append(expr).append("): ");
    sb.append("{ ").append(stat).append("}\n");
    return sb.toString();
  }
}
