package doc.wacc.astNodes;

import antlr.BasicParser;
import doc.wacc.utils.CompilerVisitor;
import doc.wacc.utils.Type;

import static java.lang.System.exit;

public class AssignAST extends AST {
  private final BasicParser.Assign_lhsContext lhs;
  private final BasicParser.Assign_rhsContext rhs;

  public AssignAST(BasicParser.Assign_lhsContext lhs, BasicParser.Assign_rhsContext rhs) {
    this.lhs = lhs;
    this.rhs = rhs;
    Type type = null;

    if (lhs.pair_elem() != null) {
      type = symbolTable.getVariable(lhs.pair_elem().expr().getText());
    } else if (lhs.array_elem() != null) {
      type = symbolTable.getVariable(lhs.array_elem().IDENT().getText());
    } else {
      type = symbolTable.getVariable(lhs.getText());
    }

    if (lhs.array_elem() != null) {
      type = symbolTable.getVariable(lhs.array_elem().IDENT().getText());
    }
    if (lhs.pair_elem() != null) {
      CompilerVisitor visitor = new CompilerVisitor();
      AST ast = visitor.visitAssign_rhs(rhs);
      if (is_bool(ast)) {
        type = Type.boolType();
      }
      if (is_Pair(ast)) {
        type = Type.pairType();
      }
      if (is_Char(ast)) {
        type = Type.charType();
      }
      if ((is_int(ast))) {
        type = Type.intType();
      }
      if(is_String(ast)) {
        type = Type.stringType();
      }
    }

    if (type == null) {
      System.out.println("Semantic error: " + lhs.getText() + " is not defined");
      exit(200);
    }
    if (rhs.expr().size() == 1) {
      CompilerVisitor visitor = new CompilerVisitor();
      AST ast = visitor.visitExpr(rhs.expr(0));
      if ((type.equals(Type.boolType()) && !is_bool(ast)) ||
              (type.equals(Type.intType()) && !is_int(ast)) ||
              (type.equals(Type.charType()) && !is_Char(ast)) ||
              (type.equals(Type.stringType()) && !is_String(ast)) ) {
        System.out.println("Semantic error: assignment type not compatible");
        exit(200);
      }
    }
  }

  @Override
  public String toString() {
    return "assigning from: " + lhs.IDENT().getText() + " to " + rhs.IDENT().getText() + "\n";
  }
}
