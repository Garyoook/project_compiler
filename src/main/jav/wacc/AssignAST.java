package jav.wacc;

import antlr.BasicParser;

public class AssignAST extends AST {
  private final BasicParser.Assign_lhsContext lhs;
  private final BasicParser.Assign_rhsContext rhs;
  public AssignAST(BasicParser.Assign_lhsContext lhs, BasicParser.Assign_rhsContext rhs) {
    this.lhs = lhs;
    this.rhs = rhs;
    if (rhs.expr().size() == 1) {
      CompilerVisitor visitor = new CompilerVisitor();
      visitor.visitExpr(rhs.expr(0));
    }
  }

  @Override
  public String toString() {
    return "assigning from: " + lhs.IDENT().getText() + " to " + rhs.IDENT().getText() + "\n";
  }
}
