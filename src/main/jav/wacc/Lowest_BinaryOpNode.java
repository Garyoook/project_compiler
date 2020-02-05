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


    is_bool(expr1);
    is_bool(expr2);

    if (!same_type(expr1, expr2) || !same_type(expr2, expr1)) {
      System.out.println("#semantic_error#");
      exit(200);
    }
  }

}
