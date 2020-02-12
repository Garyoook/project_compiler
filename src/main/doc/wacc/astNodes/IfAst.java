package doc.wacc.astNodes;

import doc.wacc.utils.SymbolTable;

import static doc.wacc.utils.CompilerVisitor.currentCharPos;
import static doc.wacc.utils.CompilerVisitor.currentLine;
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

    if (!is_bool(expr)) {
      System.out.println("Semantic error: wrong type in If condition, should be bool" +
              " at line:" + currentLine + ":" + currentCharPos +
              "\nExit code 200 returned");
      exit(200);
    }
  }

  @Override
  public String toString() {
    return "If (" + expr + ") then " + "("
            + thenbranch + ")" +  "else " + "("
            + elsebranch + ")\n";
  }

  public SymbolTable getSymbolTable() {
    return symbolTable;
  }

  public void setElseSymbolTable(SymbolTable symbolTable) {
    this.elseSymbolTable = symbolTable;
  }
}
