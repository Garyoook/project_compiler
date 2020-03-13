package doc.wacc.astNodes;

import doc.wacc.utils.ErrorMessage;

import static doc.wacc.utils.CompilerVisitor.currentCharPos;
import static doc.wacc.utils.CompilerVisitor.currentLine;
import static doc.wacc.utils.CompilerVisitor.dynamically_Typed;
import static java.lang.System.exit;

public class PrintAst extends AST {
  private AST expr;

  public PrintAst(AST expr) {
    if (!dynamically_Typed) {
      if (expr instanceof IdentNode) {
        if (symbolTable.getVariable(((IdentNode) expr).getIdent()) == null) {
          ErrorMessage.addSemanticError(((IdentNode) expr).getIdent() + " not defined" +
              " at line:" + currentLine + ":" + currentCharPos);
        }
      }
      if (expr instanceof ArrayAST) {
        ErrorMessage.addSemanticError(((IdentNode) expr).getIdent() + " array can't be printed" +
            " at line:" + currentLine + ":" + currentCharPos);
      }
    }
    this.expr = expr;
  }

  @Override
  public String toString() {
    return "Print: " + expr + "\n";
  }

  public AST getExpr() {
    return expr;
  }
}
