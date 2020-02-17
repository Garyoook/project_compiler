package doc.wacc.astNodes;

import antlr.BasicParser;
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

  public ReadAst(ReadContext ctx) {
    CompilerVisitor visitor = new CompilerVisitor();

    this.lhs = visitor.visitAssign_lhs(ctx.assign_lhs());
    Type type;

    if (lhs.getLhsContext().pair_elem() != null) {
      type = symbolTable.getVariable(lhs.getLhsContext().pair_elem().expr().getText());
    } else {
      type = symbolTable.getVariable(lhs.getLhsContext().getText());
    }
    if (type == null) {
      System.out.println("Variable not defined " + lhs.getLhsContext().getText());  exit(200);
    }
    if (type.equals(boolType())) {
      System.out.println("Semantic Error: Can't read in Type Bool" +
              " at line:" + currentLine + ":" + currentCharPos +
              ", expected: " + type +
              "\nExit code 200 returned");
      exit(200);
    }
    if (type instanceof PairType) {
      if (lhs.getLhsContext().pair_elem() == null) {
        System.out.println("Semantic Error: Can't read into a null" +
                " at line:" + currentLine + ":" + currentCharPos +
                ", expected: " + type +
                "\nExit code 200 returned");
        exit(200);
      }
      if (lhs.getLhsContext().pair_elem().fst() == null && lhs.getLhsContext().pair_elem().snd() == null){
        System.out.println("Semantic Error: Can't read in a Pair " +
                " at line:" + currentLine + ":" + currentCharPos +
                ", expected: " + type +
                "\nExit code 200 returned");
        exit(200);
      }
    }
  }

  @Override
  public String toString() {
    return "reading from: " + lhs.getLhsContext().IDENT().getText() + "\n";
  }
}
