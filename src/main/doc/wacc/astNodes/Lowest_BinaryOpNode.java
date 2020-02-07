package doc.wacc.astNodes;

import antlr.BasicParser;

import static doc.wacc.utils.CompilerVisitor.currentCharPos;
import static doc.wacc.utils.CompilerVisitor.currentLine;
import static java.lang.System.exit;

public class Lowest_BinaryOpNode extends AST {
  BasicParser.Lowest_binbool_opContext oper;
  AST expr1;
  AST expr2;

  public Lowest_BinaryOpNode(BasicParser.Lowest_binbool_opContext operContext, AST expr1, AST expr2) {
    this.oper = operContext;
    this.expr1 = expr1;
    this.expr2 = expr2;

    if (!(is_bool(expr1) && is_bool(expr2))) {
      System.out.println("Semantic error: wrong type, should be Bool in" + getClass()+
              " at line:" + currentLine + ":" + currentCharPos +
              "\nExit code 200 returned");
      exit(200);
    }

  }

}
