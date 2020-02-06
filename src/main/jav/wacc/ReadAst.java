package jav.wacc;

import antlr.BasicParser;

import static jav.wacc.Type.*;
import static java.lang.System.exit;

public class ReadAst extends AST {
  private final BasicParser.Assign_lhsContext lhs;
  public ReadAst(BasicParser.Assign_lhsContext lhs) {
    this.lhs = lhs;
    Type type = symbolTable.getVariable(lhs.getText());
    if (type == null) {
      System.out.println("Variable not defined " + lhs.getText());  exit(200);
    }
    if (type.equals(boolType())) {
      System.out.println("Semantic Error: Can't read in Type Bool");
      exit(200);
    }
    if (type instanceof PairType) {
      System.out.println("Semantic Error: Can't read in a Pair");
      exit(200);
    }
  }

  @Override
  public String toString() {
    return "reading from: " + lhs.IDENT().getText() + "\n";
  }
}
