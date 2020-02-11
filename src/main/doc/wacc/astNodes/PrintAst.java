package doc.wacc.astNodes;

import doc.wacc.ASTVisitor;

import static doc.wacc.utils.CompilerVisitor.currentCharPos;
import static doc.wacc.utils.CompilerVisitor.currentLine;
import static java.lang.System.exit;

public class PrintAst extends AST {
  private AST expr;

  public PrintAst(AST expr) {
    if (expr instanceof IdentNode) {
      if (symbolTable.getVariable(((IdentNode) expr).getIdent()) == null) {
        System.out.println(("Semantic error: " + ((IdentNode) expr).getIdent()) + " not defined" +
                " at line:" + currentLine + ":" + currentCharPos +
                "\nExit code 200 returned");
        exit(200);
      }
    }
    if (expr instanceof ArrayAST) {
      System.out.println(("Semantic error: " + ((IdentNode) expr).getIdent()) + " array can't be printed" +
              " at line:" + currentLine + ":" + currentCharPos +
              "\nExit code 200 returned");
      exit(200);
    }
    this.expr = expr;
  }

  @Override
  public String toString() {
    return "Print: " + expr + "\n";
  }

  @Override
  public void Accept(ASTVisitor v) {

  }
}
