package doc.wacc;

import antlr.BasicParser;

import static doc.wacc.Type.*;
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
      System.out.println("Semantic Error: Can't read in Type Bool");
      exit(200);
    }
    if (type instanceof PairType) {
      if (lhs.pair_elem() == null) {
        System.out.println("Semantic Error: Can't read into a null");
        exit(200);
      }
      if (lhs.pair_elem().fst() == null && lhs.pair_elem().snd() == null){
        System.out.println("Semantic Error: Can't read in a Pair");
        exit(200);
      }
    }
  }

  @Override
  public String toString() {
    return "reading from: " + lhs.IDENT().getText() + "\n";
  }
}
