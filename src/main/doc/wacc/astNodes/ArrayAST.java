package doc.wacc.astNodes;

import antlr.BasicParser;
import doc.wacc.ASTVisitor;

import java.util.List;

public class ArrayAST extends AST {
    private int size;
    private List<BasicParser.ExprContext> exprs;

    public ArrayAST(List<BasicParser.ExprContext> exprs) {
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

    @Override
    public void Accept(ASTVisitor v) {

    }
}