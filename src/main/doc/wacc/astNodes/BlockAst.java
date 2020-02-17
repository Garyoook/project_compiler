package doc.wacc.astNodes;

import doc.wacc.utils.SymbolTable;

import java.util.List;

public class BlockAst extends AST {
  private List<AST> stats;
  private SymbolTable symbolTable;

  public BlockAst(List<AST> stat, SymbolTable symbolTable) {
    this.stats = stat;
    this.symbolTable = symbolTable;
  }


  @Override
  public String toString() {
    return "Block {" + stats + "}\n";
  }
}
