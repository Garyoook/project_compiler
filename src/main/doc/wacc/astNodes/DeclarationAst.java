package doc.wacc.astNodes;

import antlr.BasicParser;
import doc.wacc.utils.CompilerVisitor;
import doc.wacc.utils.ErrorMessage;
import doc.wacc.utils.Type;

import static doc.wacc.utils.CompilerVisitor.currentCharPos;
import static doc.wacc.utils.CompilerVisitor.currentLine;
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
      ErrorMessage.addSemanticError("assignment type not compatible" +
              " at line:" + currentLine + ":" + currentCharPos +
              ", expected: " + type);
    }

    if (rhs.expr().size() == 1) {
      if (rhs.expr(0).array_elem() != null) {
        Type type1 = symbolTable.getVariable(rhs.expr(0).array_elem().IDENT().getText());
        type1 = ((Type.ArrayType)type1).getType();
        if (type instanceof Type.PairType) {
          if (!(type1 instanceof Type.PairType)) {
            ErrorMessage.addSemanticError("assignment type not compatible " + type +
                    " at line:" + currentLine + ":" + currentCharPos +
                    ", expected: " + type);
          }
        } else {
          if (!type1.equals(type)) {
            ErrorMessage.addSemanticError("assignment type not compatible " + type +
                    " at line:" + currentLine + ":" + currentCharPos +
                    ", expected: " + type);
          }
        }
      } else {
        CompilerVisitor visitor = new CompilerVisitor();
        AST ast = visitor.visitExpr(rhs.expr(0));
        if ((type.equals(Type.boolType())  && !is_bool(ast)) ||
            (type.equals(Type.intType())   && !is_int(ast)) ||
            (type.equals(Type.charType())  && !is_Char(ast)) ||
            (type.equals(Type.stringType()) && !is_String(ast))) {
          ErrorMessage.addSemanticError("assignment type not compatible " + rhs.expr(0).getText() +
                  " at line:" + currentLine + ":" + currentCharPos +
                  ", expected: " + type);
        }
      }
    }

    if (rhs.expr().size() == 0 && (rhs.call() != null)) {
      String s1 = rhs.IDENT().getText();
      Type type1 = CompilerVisitor.functionTable.get(s1).get(0);
      if (type1 == null) {
        ErrorMessage.addSemanticError(s1 + " is not defined" +
                " at line:" + currentLine + ":" + currentCharPos);
      }
      if (type1 instanceof Type.PairType || type instanceof Type.PairType) {
        if (!(type1 instanceof Type.PairType && type instanceof Type.PairType)) {
          ErrorMessage.addSemanticError("assignment type not compatible" +
                  " at line:" + currentLine + ":" + currentCharPos +
                  ", expected: " + type);
        }
      } else {
        if (!type1.equals(type)) {
          ErrorMessage.addSemanticError("assignment type not compatible" +
                  " at line:" + currentLine + ":" + currentCharPos +
                  ", expected: " + type);
        }
      }
    }

    if ((rhs.pair_elem() != null) && (rhs.pair_elem().expr().getText().equals("null"))) {
        ErrorMessage.addSemanticError("Cannot call fst on a null" +
                " at line:" + currentLine + ":" + currentCharPos);
      }
    }

  public BasicParser.Assign_rhsContext getRhs() {
    return rhs;
  }

  @Override
  public String toString() {
    return "DECLEAR: type: " + type + " name: " + name + " assign from: " + rhs.getText() + "\n";
  }
}
