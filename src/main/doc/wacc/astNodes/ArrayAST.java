package doc.wacc.astNodes;

import antlr.BasicParser;

import java.util.ArrayList;
import java.util.List;

public class ArrayAST extends AST {
    private int size;
    private List<AST> exprs = new ArrayList<>();

    public ArrayAST(List<AST> exprs) {
        this.exprs.addAll(exprs);
    }
//
//    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder();
//
//        for (AST exp:exprs) {
//            sb.append(exp.array_elem().getText()).append("\n");
//        }
//        return sb.toString();
//    }
}
