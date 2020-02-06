package jav.wacc;

import antlr.BasicParser;

import static jav.wacc.CompilerVisitor.functionTable;
import static jav.wacc.Type.*;
import static java.lang.System.exit;

import java.sql.SQLOutput;

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
      if (rhs.expr(0).array_elem() != null) {
        Type type1 = symbolTable.getVariable(rhs.expr(0).array_elem().IDENT().getText());
        type1 = ((ArrayType)type1).getType();
        if (type instanceof PairType) {
          if (!(type1 instanceof PairType)) {
            System.out.println("1assignment type not compatible " + type);
            exit(200);
          }
        } else {
          if (!type1.equals(type)) {
            System.out.println("2assignment type not compatible " + type);
            exit(200);
          }
        }
      } else {
        CompilerVisitor visitor = new CompilerVisitor();
        AST ast = visitor.visitExpr(rhs.expr(0));
        if ((type.equals(boolType())  && !is_bool(ast)) ||
            (type.equals(intType())   && !is_int(ast)) ||
            (type.equals(charType())  && !is_Char(ast)) ||
            (type.equals(stringType()) && !is_String(ast))) {
          System.out.println("assignment type not compatible " + rhs.expr(0).getText());
          exit(200);
        }
      }
    }

    if (rhs.expr().size() == 0 && (rhs.call() != null)) {
      String s1 = rhs.IDENT().getText();

      Type type1 = functionTable.get(s1).get(0);
      if (type1 == null) {
        System.out.println(s1 + " is not defined");
        exit(200);
      }
      if (type1 instanceof PairType || type instanceof PairType) {
        if (!(type1 instanceof PairType && type instanceof PairType)) {
          System.out.println("assignment type not compatible");
          exit(200);
        }
      } else {
        if (!type1.equals(type)) {
          System.out.println("assignment type not compatible");
          exit(200);
        }
      }
    }

    if (rhs.pair_elem() != null) {
      if (rhs.pair_elem().expr().getText().equals("null")){
        System.out.println("Semantic Error: Cannot call fst on a null");
        exit(200);
      }
    }

  }
  @Override
  public String toString() {
    return "DECLEAR: type: " + type + " name: " + name + " assign from: " + rhs.getText() + "\n";
  }
}
