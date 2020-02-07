package jav.wacc;

import antlr.BasicParser;

import static java.lang.System.exit;

public class Lowest_BinaryOpNode extends AST {
  BasicParser.Lowest_binbool_opContext oper;
  AST expr1;
  AST expr2;

  Lowest_BinaryOpNode(BasicParser.Lowest_binbool_opContext operContext, AST expr1, AST expr2) {
    this.oper = operContext;
    this.expr1 = expr1;
    this.expr2 = expr2;

    if (!(is_bool(expr1) && is_bool(expr2))) {
      System.out.println("Semantic error: wrong type, should be Bool in" + getClass());
      exit(200);
    }

  }

}
