package jav.wacc;

import antlr.BasicParser;

import static java.lang.System.exit;

public class BinaryOpNode extends AST{
  BasicParser.Binary_operContext operContext;
  AST expr1;
  AST expr2;
  BinaryOpNode(BasicParser.Binary_operContext operContext, AST expr1, AST expr2) {
    this.operContext = operContext;
    this.expr1 = expr1;
    this.expr2 = expr2;

    if ((expr1 instanceof Lowest_BinaryOpNode) || (expr2 instanceof Lowest_BinaryOpNode)) {
      System.out.println("or#");  exit(200);
    }
    if (!same_type(expr1, expr2) || !same_type(expr2, expr1)) {
      System.out.println("#semantic_error#1");  exit(200);
    }

    if ((expr1 instanceof IntNode && !(expr2 instanceof IntNode))) {
      System.out.println("#semantic_error#2");  exit(200);
    }
    if (expr1 instanceof BoolNode && !(expr2 instanceof BoolNode)) {
      System.out.println("#semantic_error#3");  exit(200);
    }
    if (expr1 instanceof CharNode && !(expr2 instanceof CharNode)) {
      System.out.println("#semantic_error#4");  exit(200);
    }
    if (expr1 instanceof StringNode && !(expr2 instanceof StringNode)) {
      System.out.println("#semantic_error#5");  exit(200);
    }

  }
  @Override
  public String toString() {
    return expr1 + operContext.getText() + expr2;
  }
}
