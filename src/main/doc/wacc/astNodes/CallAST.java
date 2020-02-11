package doc.wacc.astNodes;

import doc.wacc.ASTVisitor;

public class CallAST extends AST {
  private final String funcName;

  public CallAST(String funcName) {
    this.funcName = funcName;
  }

  @Override
  public void Accept(ASTVisitor v) {

  }
}
