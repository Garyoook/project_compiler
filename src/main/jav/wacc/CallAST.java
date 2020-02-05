package jav.wacc;

public class CallAST extends AST {
  final String funcName;


  public CallAST(String funcName) {
    this.funcName = funcName;
  }
}
