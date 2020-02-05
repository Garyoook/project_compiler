package jav.wacc;

import antlr.BasicParser;

import static java.lang.System.exit;

public class DeclarationAst extends AST {
  private final Type type;
  private final String name;
  private final BasicParser.Assign_rhsContext rhs;



  public DeclarationAst(Type type, String name, BasicParser.Assign_rhsContext rhs) {
    this.type = type;
    this.name = name;
    this.rhs = rhs;
    symbolTable.getCurrentSymbolTable().put(name, type);
    if (rhs.expr().size() == 1) {
      CompilerVisitor visitor = new CompilerVisitor();
      visitor.visitExpr(rhs.expr(0));

      if (rhs.expr(0).hignp_bin_op() != null || rhs.expr(0).binary_bool_oper() != null) {
        if (type.getTypeContext().base_type().INT() != null) {
          System.out.println("#semantic_error#");
          exit(200);
        } else if (type.getTypeContext().base_type().BOOL() != null) {
          System.out.println("#semantic_error#");
          exit(200);
        }
      }
    }
  }
  @Override
  public String toString() {
    return "DECLEAR: type: " + type.getTypeContext().getText() + " name: " + name + "\n";
  }
}
