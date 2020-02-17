package doc.wacc.astNodes;

import doc.wacc.utils.CompilerVisitor;
import doc.wacc.utils.Type;

import static antlr.BasicParser.*;
import static doc.wacc.utils.CompilerVisitor.currentCharPos;
import static doc.wacc.utils.CompilerVisitor.currentLine;
import static doc.wacc.utils.Type.*;
import static java.lang.System.exit;

public class AssignAST extends AST {
  private AssignLHSAST lhs;
  private static AssignRHSAST rhs;
  public static Type type;

  public AssignAST(AssignLHSAST lhs, AssignRHSAST rhs) {
    this.lhs = lhs;
    this.rhs = rhs;


    if (lhs.getLhsContext().pair_elem() != null) {
      type = symbolTable.getVariable(lhs.getLhsContext().pair_elem().expr().getText());
    } else if (lhs.getLhsContext().array_elem() != null) {
      type = symbolTable.getVariable(lhs.getLhsContext().array_elem().IDENT().getText());

    } else {
      type = symbolTable.getVariable(lhs.getLhsContext().getText());
    }
    if (lhs.getLhsContext().array_elem() != null) {
      type = symbolTable.getVariable(lhs.getLhsContext().array_elem().IDENT().getText());
    }

    if (type == null) {
      System.out.println("Semantic error: " + lhs.getLhsContext().getText() + " is not defined" +
              " at line:" + currentLine + ":" +
              currentCharPos +
              "\nExit code 200 returned");
      exit(200);
    }



    //=========================================

    if (rhs.getRhsContext().expr().size() == 1) {
      CompilerVisitor visitor = new CompilerVisitor();

      System.out.println(type);

      System.out.println(rhs.getRhsContext().expr(0).getText());

      AST ast = visitor.visitExpr(rhs.getRhsContext().expr(0));
      if ((type.equals(boolType()) && !is_bool(ast)) ||
              (type.equals(intType()) && !is_int(ast)) ||
              (type.equals(charType()) && !is_Char(ast)) ||
              (type.equals(stringType()) && !is_String(ast)) ) {
        System.out.println("Semantic error: assignment type not compatible" +
                " at line:" + currentLine + ":" + currentCharPos +
                "expected: " +
                "\nExit code 200 returned");
        exit(200);
      }
    }


  }

  public static class AssignLHSAST extends AST{
    private Assign_lhsContext lhsContext;


    public AssignLHSAST(Assign_lhsContext lhsContext) {
      this.lhsContext = lhsContext;

    }

    public Assign_lhsContext getLhsContext() {
      return lhsContext;
    }
  }

  public static class AssignRHSAST extends AST{
    private Assign_rhsContext rhsContext;

    public AssignRHSAST(Assign_rhsContext rhsContext) {
      this.rhsContext = rhsContext;


    }

    public Assign_rhsContext getRhsContext() {
      return rhsContext;
    }
  }
  @Override
  public String toString() {
    return "assigning from: " + lhs.getLhsContext().getText() + " to " + rhs.getRhsContext().IDENT().getText() + "\n";
  }
}
