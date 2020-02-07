package doc.wacc.astNodes;

public class CallAST extends AST {
  final String funcName;


  public CallAST(String funcName) {
    this.funcName = funcName;
  }
}
