package jav.wacc;

import antlr.BasicParser;

import static jav.wacc.Type.*;
import static jav.wacc.Type.charType;
import static jav.wacc.Type.stringType;
import static java.lang.System.exit;

public class AssignAST extends AST {
  private final BasicParser.Assign_lhsContext lhs;
  private final BasicParser.Assign_rhsContext rhs;
  public AssignAST(BasicParser.Assign_lhsContext lhs, BasicParser.Assign_rhsContext rhs) {
    this.lhs = lhs;
    this.rhs = rhs;
    Type type = symbolTable.getVariable(lhs.getText());
    if (lhs.array_elem() != null) {
      type = symbolTable.getVariable(lhs.array_elem().IDENT().getText());
    }
    if (lhs.pair_elem() != null) {
//      System.out.println(rhs.);
//      type = rhs.expr()
    }

    if (type == null) {
      System.out.println(lhs.getText() + " is not defined");
      exit(200);
    }
    if (rhs.expr().size() == 1) {
      CompilerVisitor visitor = new CompilerVisitor();
      AST ast = visitor.visitExpr(rhs.expr(0));
      if ((type.equals(boolType()) && !is_bool(ast)) ||
              (type.equals(intType()) && !is_int(ast)) ||
              (type.equals(charType()) && !is_Char(ast)) ||
              (type.equals(stringType()) && !is_String(ast)) ) {
        System.out.println("assignment type not compatible");
        exit(200);

      }
    }
  }

  @Override
  public String toString() {
    return "assigning from: " + lhs.IDENT().getText() + " to " + rhs.IDENT().getText() + "\n";
  }
}
