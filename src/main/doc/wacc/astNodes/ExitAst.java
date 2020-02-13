package doc.wacc.astNodes;

import doc.wacc.utils.CompilerVisitor;
import doc.wacc.utils.ErrorMessage;

import static doc.wacc.utils.CompilerVisitor.currentCharPos;
import static doc.wacc.utils.CompilerVisitor.currentLine;
import static java.lang.System.exit;

public class ExitAst extends AST {
  private AST expr;

  public ExitAst(AST expr) {
    this.expr = expr;

    if (!is_int(expr)) {
      ErrorMessage.addSemanticError("Type incompatible: exit code should be an int" +
              " at line:" + currentLine + ":" + currentCharPos);
    }
  }

  public AST getExpr() {
    return expr;
  }

  @Override
  public String toString() {
    return "exit(" + expr + ")\n";
  }
}
