package doc.wacc.astNodes;

import antlr.BasicParser;
import doc.wacc.ASTVisitor;

import static java.lang.System.exit;

public class High_BinaryOpNode extends AST{
  private BasicParser.Hignp_bin_opContext operContext;
  private AST expr1;
  private AST expr2;

  public High_BinaryOpNode(BasicParser.Hignp_bin_opContext operContext, AST expr1, AST expr2) {
    this.operContext = operContext;
    this.expr1 = expr1;
    this.expr2 = expr2;

    if (!(is_int(expr1) && is_int(expr1))) {
      System.out.println("Semantic error: wrong type in " + operContext.getText());
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
