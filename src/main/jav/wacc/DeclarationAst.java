package jav.wacc;

import antlr.BasicParser;

import static jav.wacc.CompilerVisitor.functionTable;
import static jav.wacc.Type.*;
import static java.lang.System.exit;

public class DeclarationAst extends AST {
  private final Type type;
  private final String name;
  private final BasicParser.Assign_rhsContext rhs;



  public DeclarationAst(Type type, String name, BasicParser.Assign_rhsContext rhs) {
    this.type = type;
    this.name = name;
    this.rhs = rhs;
    symbolTable.putVariable(name, type);

    if ((type.equals(boolType()) ||
         type.equals(intType()) ||
         type.equals(charType()) ||
         type.equals(stringType())) && rhs.array_liter() != null) {
      System.out.println("assignment type not compatible");
      exit(200);
    }

    if (rhs.expr().size() == 1) {
      CompilerVisitor visitor = new CompilerVisitor();
      AST ast = visitor.visitExpr(rhs.expr(0));
      if ((type.equals(boolType())  && !is_bool(ast)) ||
          (type.equals(intType())   && !is_int(ast)) ||
          (type.equals(charType())  && !is_Char(ast)) ||
          (type.equals(stringType()) && !is_String(ast))) {
        System.out.println("assignment type not compatible");
        exit(200);
      }
    }

    if (rhs.expr().size() == 0 && (rhs.call() != null)) {
      String s1 = rhs.IDENT().getText();
      Type type1 = functionTable.get(s1).get(0);
      if (type1 == null) {
        System.out.println(s1 + " is not defined");
        exit(200);
      }
      if (!type1.equals(type)) {
        System.out.println("assignment type not compatible");
        exit(200);
      }
    }

  }
  @Override
  public String toString() {
    return "DECLEAR: type: " + type + " name: " + name + "\n";
  }
}
