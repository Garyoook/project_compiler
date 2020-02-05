package jav.wacc;

import antlr.BasicParser;

import static jav.wacc.Type.*;
import static java.lang.System.exit;

public class ReadAst extends AST {
  private final BasicParser.Assign_lhsContext lhs;
  public ReadAst(BasicParser.Assign_lhsContext lhs) {
    this.lhs = lhs;
    Type type = symbolTable.getCurrentSymbolTable().get(lhs.getText());
    if (type == null) {
      System.out.println("#semantic_error#");  exit(200);
    }
    if(type.equals(intType()) && type.equals(charType())) {
      System.out.println("#semantic_error#");  exit(200);
    }
  }

  @Override
  public String toString() {
    return "reading from: " + lhs.IDENT().getText() + "\n";
  }
}
