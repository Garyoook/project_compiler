package doc.wacc.astNodes;

import doc.wacc.utils.ErrorMessage;
import doc.wacc.utils.SymbolTable;
import doc.wacc.utils.Type;

import java.util.HashMap;

import static doc.wacc.utils.CompilerVisitor.currentCharPos;
import static doc.wacc.utils.CompilerVisitor.currentLine;
import static doc.wacc.utils.CompilerVisitor.dynamically_Typed;
import static java.lang.System.exit;

public class WhileAst extends AST {
  private AST expr;
  private AST stat;
  private SymbolTable symbolTable;


  public WhileAst(AST expr, AST stat, SymbolTable symbolTable) {
    this.expr = expr;
    this.stat = stat;
    this.symbolTable = symbolTable;

    if (!dynamically_Typed) {
      if (!is_bool(expr)) {
        ErrorMessage.addSemanticError("wrong type in while condition" +
            " at line:" + currentLine + ":" + currentCharPos);
      }
    }
  }

  public SymbolTable getSymbolTable() {
    return symbolTable;
  }

  public AST getExpr() {
    return expr;
  }

  public AST getStat() {
    return stat;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("while (").append(expr).append("): ");
    sb.append("{ ").append(stat).append("}\n");
    return sb.toString();
  }
}
