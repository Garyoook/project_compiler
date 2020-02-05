package jav.wacc;

import antlr.BasicParser;

public class PairAST extends AST {
    private BasicParser.Pair_elemContext fst;
    private BasicParser.Pair_elemContext snd;

    public PairAST(BasicParser.Pair_elemContext fst, BasicParser.Pair_elemContext snd) {
        this.fst = fst;
        this.snd = snd;
    }

    public void addFst(BasicParser.Pair_elemContext fst) {
      //////////////////////////////
    }

  public void addSnd(BasicParser.Pair_elemContext snd) {
      ////////////////////////////
  }

    @Override
    public String toString() {
        return "Pair: (" + fst.getText() + ", " + snd.getText() + ")";
    }
}
