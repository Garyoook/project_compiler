package doc.wacc.astNodes;

import antlr.BasicParser;
import doc.wacc.utils.CompilerVisitor;

public class AssignLHSAST extends AST {

    private BasicParser.Assign_lhsContext lhsContext;


    public AssignLHSAST(BasicParser.Assign_lhsContext lhsContext) {
        this.lhsContext = lhsContext;

    }

    public BasicParser.Assign_lhsContext getLhsContext() {
        return lhsContext;
    }


    public boolean isArray() {
        return lhsContext.array_elem() != null;
    }

    public boolean isPair() {
        return lhsContext.pair_elem() != null;
    }


    public AST getArrayIndex() {
        CompilerVisitor visitor = new CompilerVisitor();
        return visitor.visitExpr(lhsContext.array_elem().expr(0));
    }

}
