package doc.wacc.astNodes;

import doc.wacc.ASTVisitor;

import static doc.wacc.utils.CompilerVisitor.currentCharPos;
import static doc.wacc.utils.CompilerVisitor.currentLine;
import static java.lang.System.exit;

public class IfAst extends AST {
  private final AST cond;
  private AST thenbranch;
  private AST elsebranch;


  public IfAst(AST cond, AST thenbranch, AST elsebranch) {
    this.cond = cond;
    this.thenbranch = thenbranch;
    this.elsebranch = elsebranch;

    if (!is_bool(cond)) {
      System.out.println("Semantic error: wrong type in If condition, should be bool" +
              " at line:" + currentLine + ":" + currentCharPos +
              "\nExit code 200 returned");
      exit(200);
    }
  }

  public void Accept(ASTVisitor v) {
    v.visitIfThenElsenode((ExpressionAST) cond, thenbranch, elsebranch);
  }

  @Override
  public String toString() {
    return "If (" + cond + ") then " + "("
            + thenbranch + ")" +  "else " + "("
            + elsebranch + ")\n";
  }
}
