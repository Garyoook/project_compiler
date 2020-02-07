package doc.wacc;

public class CallAST extends AST {
  final String funcName;


  public CallAST(String funcName) {
    this.funcName = funcName;
  }
}
