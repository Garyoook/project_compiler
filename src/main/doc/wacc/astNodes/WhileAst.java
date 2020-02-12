package doc.wacc.astNodes;

import doc.wacc.utils.SymbolTable;
import doc.wacc.utils.Type;

import java.util.HashMap;

import static doc.wacc.utils.CompilerVisitor.currentCharPos;
import static doc.wacc.utils.CompilerVisitor.currentLine;
import static java.lang.System.exit;

public class WhileAst extends AST {
  private AST expr;
  private AST stat;
  private SymbolTable symbolTable;


  public WhileAst(AST expr, AST stat, SymbolTable symbolTable) {
    this.expr = expr;
    this.stat = stat;
    this.symbolTable = symbolTable;

    if (!is_bool(expr)) {
      System.out.println("Semantic error: wrong type in while condition" +
              " at line:" + currentLine + ":" + currentCharPos +
              "\nExit code 200 returned");
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
