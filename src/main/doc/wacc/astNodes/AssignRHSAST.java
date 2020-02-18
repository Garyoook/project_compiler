package doc.wacc.astNodes;

import antlr.BasicParser;
import doc.wacc.utils.CompilerVisitor;

public class AssignRHSAST extends AST{
    private BasicParser.Assign_rhsContext rhsContext;
    private AST expr1;
    private AST expr2;
    private ArrayAST arrayAST;
    private CallAST callAST;

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
            this.callAST = (CallAST) visitor.visitCall(rhsContext.call());
        }
    }

    public AST getExpr() {
        return expr1;
    }

    public BasicParser.Assign_rhsContext getRhsContext() {
        return rhsContext;
    }
}