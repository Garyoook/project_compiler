package doc.wacc.astNodes;

import antlr.BasicParser;
import doc.wacc.utils.CompilerVisitor;

public class AssignLHSAST extends AST {

    private BasicParser.Assign_lhsContext lhsContext;
    private String name;
    private ArrayElemNode arrayElemNode;
    private PairElemNode pairElemNode;


    public AssignLHSAST(BasicParser.Assign_lhsContext lhsContext) {
        CompilerVisitor visitor = new CompilerVisitor();
        this.lhsContext = lhsContext;

        if (lhsContext.array_elem() != null) {
            this.arrayElemNode = (ArrayElemNode)visitor.visitArray_elem(lhsContext.array_elem());
            this.name = arrayElemNode.getName();
        } else if (lhsContext.pair_elem() != null) {
            this.pairElemNode = visitor.visitPair_elem(lhsContext.pair_elem());
            this.name = pairElemNode.getName();
        } else {
            this.name = lhsContext.getText();
        }
    }

    public BasicParser.Assign_lhsContext getLhsContext() {
        return lhsContext;
    }

    public String getName() {
        return name;
    }

    public String getLhsName() {
        return lhsContext.getText();
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

    public PairElemNode getPairElemNode() {
        return pairElemNode;
    }

    public AST getArrayElem() {
        return arrayElemNode;
    }

}
