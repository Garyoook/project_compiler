package doc.wacc.astNodes;

import java.util.ArrayList;

public class SeqStateAst extends AST {
  private ArrayList<AST> seqs;
  public SeqStateAst(ArrayList<AST> seqs) {
    this.seqs = new ArrayList<>(seqs);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (AST a : seqs) {
      sb.append(a);
    }
    return sb.toString();
  }

  public ArrayList<AST> getSeqs() {
    return seqs;
  }
}
