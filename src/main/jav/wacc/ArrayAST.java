package jav.wacc;

import antlr.BasicParser;

import java.util.List;

public class ArrayAST extends AST {
    private int size;
    private List<BasicParser.ExprContext> exprs;

    public ArrayAST(List<BasicParser.ExprContext> exprs) {
        assert this.exprs != null;
        this.exprs.addAll(exprs);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (BasicParser.ExprContext exp:exprs) {
            sb.append(exp.array_elem().getText()).append("\n");
        }
        return sb.toString();
    }
}
