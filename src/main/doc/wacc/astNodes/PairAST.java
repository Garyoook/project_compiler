package doc.wacc.astNodes;

import antlr.BasicParser;
import doc.wacc.ASTVisitor;

public class PairAST extends AST {
    private BasicParser.Pair_elemContext fst;
    private BasicParser.Pair_elemContext snd;
    public String ident;

    public PairAST(String ident, BasicParser.Pair_elemContext fst, BasicParser.Pair_elemContext snd) {
      this.ident = ident;
      this.fst = fst;
      this.snd = snd;
    }

    @Override
    public String toString() {
        return ident + "(" + fst.getText() + ", " + snd.getText() + ")";
    }

    @Override
    public void Accept(ASTVisitor v) {

    }
}
