package doc.wacc.astNodes;

import antlr.BasicParser;

import static antlr.BasicParser.*;

public class PairAST extends AST {
    private Pair_elemContext fst;
    private Pair_elemContext snd;
    public String ident;

    public PairAST(String ident, Pair_elemContext fst, Pair_elemContext snd) {
      this.ident = ident;
      this.fst = fst;
      this.snd = snd;
    }

    @Override
    public String toString() {
        return ident + "(" + fst.getText() + ", " + snd.getText() + ")";
    }


}
