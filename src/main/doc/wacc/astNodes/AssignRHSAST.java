package doc.wacc.astNodes;

import antlr.BasicParser;
import doc.wacc.utils.CompilerVisitor;

public class AssignRHSAST extends AST{
    private BasicParser.Assign_rhsContext rhsContext;
    private AST expr1;
    private AST expr2;
    private ArrayAST arrayAST;
    private CallAST callAST;
    private PairElemNode pairElemNode;

    public AssignRHSAST(BasicParser.Assign_rhsContext rhsContext) {
        CompilerVisitor visitor = new CompilerVisitor();
        this.rhsContext = rhsContext;
        if (rhsContext.expr().size() == 1) {
          this.expr1 = visitor.visitExpr(rhsContext.expr(0));
        } else if (rhsContext.expr().size() > 1) {
          this.expr1 = visitor.visitExpr(rhsContext.expr(0));
          this.expr2 = visitor.visitExpr(rhsContext.expr(1));
        } else if (rhsContext.array_liter() != null) {
            this.arrayAST = (ArrayAST) visitor.visitArray_liter(rhsContext.array_liter());
        } else if (rhsContext.call() != null) {
            if (rhsContext.arg_list() != null) {
                this.callAST = new CallAST(rhsContext.IDENT().getText(), rhsContext.arg_list().expr());
            } else {
                this.callAST = new CallAST(rhsContext.IDENT().getText(), null);
            }
        } else if (rhsContext.pair_elem() != null) {
            this.pairElemNode = visitor.visitPair_elem(rhsContext.pair_elem());
        }

    }

    public AST getExpr1() {
        return expr1;
    }

    public AST getExpr2() {
        return expr2;
    }

    public boolean call() {
        return rhsContext.call() != null;
    }

    public boolean newPair() {
      return rhsContext.expr().size() > 1;
    }

    public CallAST getCallAST() {
        return callAST;
    }

    public BasicParser.Assign_rhsContext getRhsContext() {
        return rhsContext;
    }

    public ArrayAST getArrayAST() {
        return arrayAST;
    }

    public PairElemNode getPairElemNode() {
        return pairElemNode;
    }
}
