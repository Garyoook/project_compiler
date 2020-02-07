package doc.wacc.astNodes;

import doc.wacc.utils.SymbolTable;
import doc.wacc.utils.Type;

import java.util.HashMap;

import static java.lang.System.exit;

public class WhileAst extends AST {
  private AST expr;
  private AST stat;


  public WhileAst(AST expr, AST stat) {
    this.expr = expr;
    this.stat = stat;

    if (!is_bool(expr)) {
      System.out.println("Semantic error: wrong type in while condition");
      exit(200);
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
