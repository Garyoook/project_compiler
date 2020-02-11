package doc.wacc.astNodes;

import antlr.BasicParser;

import static doc.wacc.utils.CompilerVisitor.currentCharPos;
import static doc.wacc.utils.CompilerVisitor.currentLine;
import static java.lang.System.exit;

public class Low_BinaryOpNode extends AST{
  private BasicParser.Low_binbool_opContext operContext;
  private AST expr1;
  private AST expr2;

  public Low_BinaryOpNode(BasicParser.Low_binbool_opContext operContext, AST expr1, AST expr2) {
    this.operContext = operContext;
    this.expr1 = expr1;
    this.expr2 = expr2;

    if (!(is_int(expr1) && is_int(expr1) ||
            is_Char(expr1) && is_Char(expr2) ||
            is_String(expr1) && is_String(expr2) ||
            is_bool(expr1) && is_bool(expr2) ||
            is_Pair(expr1) && is_Pair(expr2))
            ) {
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
}
