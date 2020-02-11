package doc.wacc.astNodes;

import doc.wacc.ASTVisitor;
import doc.wacc.ExpressionASTVisitor;

public class ExpressionAST extends AST {
    private int value;
    public ExpressionAST(int value) {
        this.value = value;
    }

    public ExpressionAST() {
    }

    public void Accept(ExpressionASTVisitor v) {
        v.visitExpressionNode(value);
    }

    @Override
    public void Accept(ASTVisitor v) {

    }
}
