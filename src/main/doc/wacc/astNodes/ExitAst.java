package doc.wacc.astNodes;

import doc.wacc.ASTVisitor;

import static doc.wacc.utils.CompilerVisitor.currentCharPos;
import static doc.wacc.utils.CompilerVisitor.currentLine;
import static java.lang.System.exit;

public class ExitAst extends AST {
  private AST expr;

  public ExitAst(AST expr) {
    this.expr = expr;

    if (!is_int(expr)) {
      System.out.println("Semantic error: Type incompatible: exit code should be an int" +
              " at line:" + currentLine + ":" + currentCharPos +
              "\nExit code 200 returned");
      exit(200);
    }
  }

  @Override
  public String toString() {
    return "exit(" + expr + ")\n";
  }

  @Override
  public void Accept(ASTVisitor v) {

  }
}
