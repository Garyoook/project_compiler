package jav.wacc;

import antlr.BasicParser;

import static java.lang.System.exit;

public class AssignAST extends AST {
  private final BasicParser.Assign_lhsContext lhs;
  private final BasicParser.Assign_rhsContext rhs;
  public AssignAST(BasicParser.Assign_lhsContext lhs, BasicParser.Assign_rhsContext rhs) {
    this.lhs = lhs;
    this.rhs = rhs;
    if (symbolTable.getCurrentSymbolTable().get(lhs.getText()).isFunc) {
      System.out.println(lhs.getText() + " Function can't be assigned");
      exit(200);
    }
    BasicParser.TypeContext type = symbolTable.getCurrentSymbolTable().get(lhs.getText()).getTypeContext();
    if (type == null) {
      System.out.println(lhs.getText() + " is not defined");
      exit(200);
    }
    if (rhs.expr().size() == 1) {
      CompilerVisitor visitor = new CompilerVisitor();
      AST ast = visitor.visitExpr(rhs.expr(0));
      if ((type.base_type().BOOL() != null && !is_bool(ast)) ||
         (type.base_type().INT() != null && !is_int(ast)) ||
          (type.base_type().CHAR() != null && !is_Char(ast)) ||
             (type.base_type().STRING() != null && !is_String(ast))) {
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
