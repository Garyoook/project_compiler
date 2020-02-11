package doc.wacc.astNodes;

import antlr.BasicParser;
import doc.wacc.ASTVisitor;
import doc.wacc.TranslateExpVisitor;
import doc.wacc.UniqueLabel;
import doc.wacc.utils.CompilerVisitor;

import java.io.*;
import java.util.Vector;

public class TranslateVisitor extends ASTVisitor {
    public void visitExitNode(AST exitNode) {

    }

    public void visitAssignNode(BasicParser.Assign_lhsContext lhs, BasicParser.Assign_rhsContext rhs) {
        // print instructions which, when executed, will leave
        // expression value at top of stack
        CompilerVisitor visitor = new CompilerVisitor();
        ExpressionAST expr = (ExpressionAST) visitor.visitAssign_rhs(rhs);
        expr.Accept(new TranslateExpVisitor());
        System.out.println("pop " + lhs.getText());
    }

    public void visitIfThenElsenode(ExpressionAST cond, AST thenStat, AST elseStat) {
        // print instructions which, when executed, will leave
        // expression value at top of stack
        UniqueLabel skiplabel = new UniqueLabel();
        cond.Accept(new TranslateExpVisitor());
        System.out.println("JFalse " + skiplabel.toString());
        thenStat.Accept(this);
        System.out.println("Define " + skiplabel.toString());
        elseStat.Accept(this);
        System.out.println("Define " + skiplabel.toString());
    }
}