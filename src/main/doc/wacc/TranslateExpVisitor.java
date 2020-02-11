package doc.wacc;

import doc.wacc.astNodes.ExpressionAST;

public class TranslateExpVisitor extends ExpressionASTVisitor {
    public void visitExpressionNode(int value) {
        System.out.println("Push "+value);
    }
}