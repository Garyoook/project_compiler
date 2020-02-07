package doc.wacc.astNodes;

import antlr.BasicParser;

import static java.lang.System.exit;

public class BinaryOpNode extends AST{
  BasicParser.Binary_operContext operContext;
  AST expr1;
  AST expr2;
  public BinaryOpNode(BasicParser.Binary_operContext operContext, AST expr1, AST expr2) {
    this.operContext = operContext;
    this.expr1 = expr1;
    this.expr2 = expr2;



    if (!(is_int(expr1) && is_int(expr2))) {
      System.out.println("Semantic error: wrong type in " + operContext.getText() +
              "\nExit code 200 returned");
      exit(200);
    }


  }
  @Override
  public String toString() {
    return expr1 + operContext.getText() + expr2;
  }
}
