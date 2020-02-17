package doc.wacc.astNodes;

import antlr.BasicParser;
import doc.wacc.utils.CompilerVisitor;

public class AssignRHSAST extends AST{
    private BasicParser.Assign_rhsContext rhsContext;
    private AST expr;

    public AssignRHSAST(BasicParser.Assign_rhsContext rhsContext) {
        CompilerVisitor visitor = new CompilerVisitor();
        this.rhsContext = rhsContext;
        this.expr = visitor.visitExpr(rhsContext.expr(0));
    }

    public AST getExpr() {
        return expr;
    }

    public BasicParser.Assign_rhsContext getRhsContext() {
        return rhsContext;
    }
}
