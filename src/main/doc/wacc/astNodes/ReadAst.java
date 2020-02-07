package doc.wacc.astNodes;

import antlr.BasicParser;
import doc.wacc.utils.Type;

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
      System.out.println("Semantic Error: Can't read in Type Bool" +
              "\nExit code 200 returned");
      exit(200);
    }
    if (type instanceof PairType) {
      if (lhs.pair_elem() == null) {
        System.out.println("Semantic Error: Can't read into a null" +
                "\nExit code 200 returned");
        exit(200);
      }
      if (lhs.pair_elem().fst() == null && lhs.pair_elem().snd() == null){
        System.out.println("Semantic Error: Can't read in a Pair " +
                "\nExit code 200 returned");
        exit(200);
      }
    }
  }

  @Override
  public String toString() {
    return "reading from: " + lhs.IDENT().getText() + "\n";
  }
}
