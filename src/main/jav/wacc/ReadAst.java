package jav.wacc;

import antlr.BasicParser;

import static java.lang.System.exit;

public class ReadAst extends AST {
  private final BasicParser.Assign_lhsContext lhs;
  public ReadAst(BasicParser.Assign_lhsContext lhs) {
    this.lhs = lhs;
    BasicParser.Base_typeContext type = symbolTable.getCurrentSymbolTable().get(lhs.getText()).getTypeContext().base_type();
    if (type == null) {
      System.out.println("#semantic_error#");  exit(200);
    }
    if(type.INT() != null && type.CHAR() != null) {
      System.out.println("#semantic_error#");  exit(200);
    }
  }

  @Override
  public String toString() {
    return "reading from: " + lhs.IDENT().getText() + "\n";
  }
}
