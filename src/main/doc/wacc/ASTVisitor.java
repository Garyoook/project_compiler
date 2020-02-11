package doc.wacc;

import antlr.BasicParser;
import doc.wacc.astNodes.AST;
import doc.wacc.astNodes.ExpressionAST;
import doc.wacc.utils.CompilerVisitor;

import static antlr.BasicParser.*;

public abstract class ASTVisitor {
    public abstract void visitExitNode(AST exitNode);

    public abstract void visitAssignNode(Assign_lhsContext lhs, Assign_rhsContext rhs);

    public abstract void visitIfThenElsenode(ExpressionAST cond, AST thenStat, AST elseStat);
}
