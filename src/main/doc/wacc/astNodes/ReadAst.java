package doc.wacc.astNodes;

import antlr.BasicParser;
import doc.wacc.utils.ErrorMessage;
import doc.wacc.utils.CompilerVisitor;
import doc.wacc.utils.Type;

import static antlr.BasicParser.*;
import static doc.wacc.astNodes.AssignAST.*;
import static doc.wacc.utils.CompilerVisitor.currentCharPos;
import static doc.wacc.utils.CompilerVisitor.currentLine;
import static doc.wacc.utils.Type.*;
import static java.lang.System.exit;

public class ReadAst extends AST {
  private AssignLHSAST lhs;
  private Type type;

  public ReadAst(ReadContext ctx) {
    CompilerVisitor visitor = new CompilerVisitor();
    type = null;

    this.lhs = visitor.visitAssign_lhs(ctx.assign_lhs());

    if (lhs.getLhsContext().pair_elem() != null) {
      type = symbolTable.getVariable(lhs.getLhsContext().pair_elem().expr().getText());
      if (lhs.getLhsContext().pair_elem().fst() != null) {
        type = ((Type.PairType) type).getLeftType();
      } else if (lhs.getLhsContext().pair_elem().snd() != null) {
        type = ((Type.PairType) type).getRightType();
      }
    } else {
      type = symbolTable.getVariable(lhs.getLhsContext().getText());
    }
    System.out.println(lhs.getLhsContext().pair_elem().expr().getText());
    if (type == null) {
      System.out.println("Variable not defined " + lhs.getLhsContext().getText());  exit(200);
    }
    if (type.equals(boolType())) {
      ErrorMessage.addSemanticError("Can't read in Type Bool" +
              " at line:" + currentLine + ":" + currentCharPos +
              ", expected: " + type);
    }
    if (type instanceof PairType) {
      if (lhs.getLhsContext().pair_elem() == null) {
        ErrorMessage.addSemanticError("Can't read into a null" +
                " at line:" + currentLine + ":" + currentCharPos +
                ", expected: " + type);
      } else if (lhs.getLhsContext().pair_elem().fst() == null && lhs.getLhsContext().pair_elem().snd() == null){
        ErrorMessage.addSemanticError("Can't read in a Pair " +
                " at line:" + currentLine + ":" + currentCharPos +
                ", expected: " + type);
      }
    }
  }

  @Override
  public String toString() {
    return "reading from: " + lhs.getLhsContext().IDENT().getText() + "\n";
  }

  public Type getType() {
    return type;
  }
}
