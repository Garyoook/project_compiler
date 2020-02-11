package doc.wacc.astNodes;

import antlr.BasicParser;
import doc.wacc.ASTVisitor;

import static doc.wacc.utils.CompilerVisitor.currentCharPos;
import static doc.wacc.utils.CompilerVisitor.currentLine;
import static java.lang.System.exit;

public class Binary_BoolOpNode extends AST{
  BasicParser.Binary_bool_operContext operContext;
  AST expr1;
  AST expr2;
  public Binary_BoolOpNode(BasicParser.Binary_bool_operContext operContext, AST expr1, AST expr2) {
    this.operContext = operContext;
    this.expr1 = expr1;
    this.expr2 = expr2;
    if (!(is_bool(expr1) && is_bool(expr2) || is_int(expr1) && is_int(expr2) || is_Char(expr1) && is_Char(expr2))) {
      System.out.println("Semantic error: wrong type in " + operContext.getText() +
              " at line:" + currentLine + ":" + currentCharPos +
              "\nExit code 200 returned");
      exit(200);
    }
  }
  @Override
  public String toString() {
    return expr1 + operContext.getText() + expr2;
  }

  @Override
  public void Accept(ASTVisitor v) {

  }
}
