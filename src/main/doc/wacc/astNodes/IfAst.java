package doc.wacc.astNodes;

import doc.wacc.utils.ErrorMessage;
import doc.wacc.utils.SymbolTable;

import static doc.wacc.utils.CompilerVisitor.currentCharPos;
import static doc.wacc.utils.CompilerVisitor.currentLine;
import static doc.wacc.utils.CompilerVisitor.dynamically_Typed;
import static java.lang.System.exit;

public class IfAst extends AST {
  private final AST expr;
  private AST thenbranch;
  private AST elsebranch;
  private SymbolTable thenSymbolTable;
  private SymbolTable elseSymbolTable;

  public IfAst(AST expr, AST thenbranch, AST elsebranch, SymbolTable symbolTable) {
    this.expr = expr;
    this.thenbranch = thenbranch;
    this.elsebranch = elsebranch;
    this.thenSymbolTable = symbolTable;

    if (!dynamically_Typed) {
      if (!is_bool(expr)) {
        ErrorMessage.addSemanticError("wrong type in If condition, should be bool" +
            " at line:" + currentLine + ":" + currentCharPos);
      }
    }
  }

  @Override
  public String toString() {
    return "If (" + expr + ") then " + "("
            + thenbranch + ")" +  "else " + "("
            + elsebranch + ")\n";
  }

  public AST getExpr() {
    return expr;
  }

  public AST getThenbranch() {
    return thenbranch;
  }

  public AST getElsebranch() {
    return elsebranch;
  }

  public SymbolTable getThenSymbolTable() {
    return thenSymbolTable;
  }

  public SymbolTable getElseSymbolTable() {
    return elseSymbolTable;
  }

  public void setElseSymbolTable(SymbolTable symbolTable) {
    this.elseSymbolTable = symbolTable;
  }
}
