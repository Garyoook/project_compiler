package doc.wacc.astNodes;

import antlr.BasicParser;

import static antlr.BasicParser.*;

public class UnaryOpNode extends AST{
  private Unary_operContext operContext;
  private Unary_notContext unary_notContext;
  private Unary_chrContext chrContext;
  private AST expr;

  public UnaryOpNode(Unary_operContext operContext, AST expr) {
    this.operContext = operContext;
    this.expr = expr;
  }

  public UnaryOpNode(Unary_notContext operContext, AST expr) {
    this.unary_notContext = operContext;
    this.expr = expr;
  }

  public UnaryOpNode(Unary_chrContext operContext, AST expr) {
    this.chrContext = operContext;
    this.expr = expr;
  }

  public Unary_chrContext getChrContext() {
    return chrContext;
  }

  public Unary_notContext getUnary_notContext() {
    return unary_notContext;
  }

  public Unary_operContext getOperContext() {
    return operContext;
  }

  @Override
  public String toString() {
    return operContext.getText() + expr;
  }

  public AST getExpr() {
    return expr;
  }

  public boolean isNOT() {
    return unary_notContext != null;
  }
}
