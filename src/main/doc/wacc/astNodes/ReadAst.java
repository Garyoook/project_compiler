package doc.wacc.astNodes;

import antlr.BasicParser;
import doc.wacc.utils.ErrorMessage;
import doc.wacc.utils.Type;

import static doc.wacc.utils.CompilerVisitor.currentCharPos;
import static doc.wacc.utils.CompilerVisitor.currentLine;
import static doc.wacc.utils.Type.*;
import static java.lang.System.exit;

public class ReadAst extends AST {
  private final BasicParser.Assign_lhsContext lhs;

  public ReadAst(BasicParser.Assign_lhsContext lhs) {
    this.lhs = lhs;
    Type type = null;

    if (lhs.pair_elem() != null) {
      type = symbolTable.getVariable(lhs.pair_elem().expr().getText());
    } else {
      type = symbolTable.getVariable(lhs.getText());
    }
    if (type == null) {
      System.out.println("Variable not defined " + lhs.getText());  exit(200);
    }
    if (type.equals(boolType())) {
      ErrorMessage.addSemanticError("Can't read in Type Bool" +
              " at line:" + currentLine + ":" + currentCharPos +
              ", expected: " + type);
    }
    if (type instanceof PairType) {
      if (lhs.pair_elem() == null) {
        ErrorMessage.addSemanticError("Can't read into a null" +
                " at line:" + currentLine + ":" + currentCharPos +
                ", expected: " + type);
      } else if (lhs.pair_elem().fst() == null && lhs.pair_elem().snd() == null){
        ErrorMessage.addSemanticError("Can't read in a Pair " +
                " at line:" + currentLine + ":" + currentCharPos +
                ", expected: " + type);
      }
    }
  }

  @Override
  public String toString() {
    return "reading from: " + lhs.IDENT().getText() + "\n";
  }
}
