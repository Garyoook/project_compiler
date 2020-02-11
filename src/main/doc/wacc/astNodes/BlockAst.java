package doc.wacc.astNodes;

import doc.wacc.ASTVisitor;

public class BlockAst extends AST {
  private AST stat;

  public BlockAst(AST stat) {
    this.stat = stat;
  }

  @Override
  public String toString() {
    return "Block {" + stat + "}\n";
  }

  @Override
  public void Accept(ASTVisitor v) {

  }
}
