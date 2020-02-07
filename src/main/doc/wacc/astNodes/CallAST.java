package doc.wacc.astNodes;

public class CallAST extends AST {
  private final String funcName;

  public CallAST(String funcName) {
    this.funcName = funcName;
  }
}
