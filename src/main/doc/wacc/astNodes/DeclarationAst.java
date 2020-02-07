package doc.wacc.astNodes;

import antlr.BasicParser;
import doc.wacc.utils.CompilerVisitor;
import doc.wacc.utils.Type;

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

    if ((type.equals(Type.boolType()) ||
         type.equals(Type.intType()) ||
         type.equals(Type.charType()) ||
         type.equals(Type.stringType())) && rhs.array_liter() != null) {
      System.out.println("Semantic error: assignment type not compatible" +
              "\nExit code 200 returned");
      exit(200);
    }

    if (rhs.expr().size() == 1) {
      if (rhs.expr(0).array_elem() != null) {
        Type type1 = symbolTable.getVariable(rhs.expr(0).array_elem().IDENT().getText());
        type1 = ((Type.ArrayType)type1).getType();
        if (type instanceof Type.PairType) {
          if (!(type1 instanceof Type.PairType)) {
            System.out.println("Semantic error: assignment type not compatible " + type +
                    "\nExit code 200 returned");
            exit(200);
          }
        } else {
          if (!type1.equals(type)) {
            System.out.println("Semantic error: assignment type not compatible " + type +
                    "\nExit code 200 returned");
            exit(200);
          }
        }
      } else {
        CompilerVisitor visitor = new CompilerVisitor();
        AST ast = visitor.visitExpr(rhs.expr(0));
        if ((type.equals(Type.boolType())  && !is_bool(ast)) ||
            (type.equals(Type.intType())   && !is_int(ast)) ||
            (type.equals(Type.charType())  && !is_Char(ast)) ||
            (type.equals(Type.stringType()) && !is_String(ast))) {
          System.out.println("Semantic error: assignment type not compatible " + rhs.expr(0).getText() +
                  "\nExit code 200 returned");
          exit(200);
        }
      }
    }

    if (rhs.expr().size() == 0 && (rhs.call() != null)) {
      String s1 = rhs.IDENT().getText();

      Type type1 = CompilerVisitor.functionTable.get(s1).get(0);
      if (type1 == null) {
        System.out.println("Semantic error: " + s1 + " is not defined" +
                "\nExit code 200 returned");
        exit(200);
      }
      if (type1 instanceof Type.PairType || type instanceof Type.PairType) {
        if (!(type1 instanceof Type.PairType && type instanceof Type.PairType)) {
          System.out.println("Semantic error: assignment type not compatible" +
                  "\nExit code 200 returned");
          exit(200);
        }
      } else {
        if (!type1.equals(type)) {
          System.out.println("Semantic error: assignment type not compatible" +
                  "\nExit code 200 returned");
          exit(200);
        }
      }
    }

    if (rhs.pair_elem() != null) {
      if (rhs.pair_elem().expr().getText().equals("null")){
        System.out.println("Semantic Error: Cannot call fst on a null" +
                "\nExit code 200 returned");
        exit(200);
      }
    }

  }
  @Override
  public String toString() {
    return "DECLEAR: type: " + type + " name: " + name + " assign from: " + rhs.getText() + "\n";
  }
}
