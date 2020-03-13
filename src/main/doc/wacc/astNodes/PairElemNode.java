package doc.wacc.astNodes;

import doc.wacc.utils.ErrorMessage;

import static doc.wacc.utils.CompilerVisitor.currentCharPos;
import static doc.wacc.utils.CompilerVisitor.currentLine;

public class PairElemNode extends AST {
  private boolean fst;
  private final String name;
  private boolean snd;
  private AST expr;

  public PairElemNode(boolean fst, boolean snd, AST expr) {
    this.fst = fst;
    this.snd = snd;
    this.expr = expr;
    if (expr instanceof IdentNode) {
      this.name = ((IdentNode) expr).getIdent();
    } else {
      this.name = null;
    }

    if (expr instanceof PairAST) {
      ErrorMessage.addSemanticError("Cannot call fst on a null" +
          " at line:" + currentLine + ":" + currentCharPos);
    }
  }

  public String getName() {
    return name;
  }

  public boolean isFst() {
    return fst;
  }

  public boolean isSnd() {
    return snd;
  }

  public AST getExpr() {
    return expr;
  }
}
