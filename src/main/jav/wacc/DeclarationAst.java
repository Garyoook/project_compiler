package jav.wacc;

import antlr.BasicParser;

import static java.lang.System.exit;

public class DeclarationAst extends AST {
  private final BasicParser.TypeContext type;
  private final String name;
  private final BasicParser.Assign_rhsContext rhs;



  public DeclarationAst(BasicParser.TypeContext type, String name, BasicParser.Assign_rhsContext rhs) {
    this.type = type;
    this.name = name;
    this.rhs = rhs;
    symbolTable.getCurrentSymbolTable().put(name, new SymbolTable.TypeValue(false, type));
    if (rhs.expr().size() == 1) {
      CompilerVisitor visitor = new CompilerVisitor();
      AST ast = visitor.visitExpr(rhs.expr(0));
      if ((type.base_type().BOOL() != null && !is_bool(ast)) ||
          (type.base_type().INT() != null && !is_int(ast)) ||
          (type.base_type().CHAR() != null && !is_Char(ast)) ||
          (type.base_type().STRING() != null && !is_String(ast))) {
        System.out.println("assignment type not compatible");
        exit(200);
      }
    }
    if (rhs.expr().size() == 0 && (rhs.call() != null)) {
      String s1 = rhs.IDENT().getText();
      BasicParser.TypeContext type1 = symbolTable.getCurrentSymbolTable().get(s1).getTypeContext();
      if (type1 == null) {
        System.out.println(s1 + " is not defined");
        exit(200);
      }
      if (!(type1.getText().equals(type.getText()))) {
        System.out.println("assignment type not compatible");
        exit(200);
      }
    }

  }
  @Override
  public String toString() {
    return "DECLEAR: type: " + type.getText() + " name: " + name + "\n";
  }
}
