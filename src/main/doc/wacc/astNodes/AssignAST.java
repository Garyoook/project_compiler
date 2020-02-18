package doc.wacc.astNodes;

import doc.wacc.utils.CompilerVisitor;
import doc.wacc.utils.ErrorMessage;
import doc.wacc.utils.Type;

import static antlr.BasicParser.*;
import static doc.wacc.utils.CompilerVisitor.currentCharPos;
import static doc.wacc.utils.CompilerVisitor.currentLine;
import static doc.wacc.utils.Type.*;
import static java.lang.System.exit;

public class AssignAST extends AST {
  private AssignLHSAST lhs;
  private AssignRHSAST rhs;
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

    if (type == null) {
      ErrorMessage.addSemanticError(lhs.getLhsContext().getText() + " is not defined" +
              " at line:" + currentLine + ":" +
              currentCharPos);
    }



    //=========================================

    if (rhs.getRhsContext().expr().size() == 1) {
      CompilerVisitor visitor = new CompilerVisitor();
      AST ast = visitor.visitExpr(rhs.getRhsContext().expr(0));
      if ((type.equals(Type.boolType()) && !is_bool(ast)) ||
              (type.equals(Type.intType()) && !is_int(ast)) ||
              (type.equals(Type.charType()) && !is_Char(ast)) ||
              (type.equals(Type.stringType()) && !is_String(ast)) ) {
        ErrorMessage.addSemanticError("assignment type not compatible" +
                " at line:" + currentLine + ":" + currentCharPos +
                "expected: ");
      }
    }


  }

  public AssignRHSAST getRhs() {
    return rhs;
  }


  public AssignLHSAST getLhs() {
    return lhs;
  }

  @Override
  public String toString() {
    return "assigning from: " + lhs.getLhsContext().getText() + " to " + rhs.getRhsContext().IDENT().getText() + "\n";
  }
}
