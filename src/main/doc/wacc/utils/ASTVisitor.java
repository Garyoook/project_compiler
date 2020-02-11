package doc.wacc.utils;

import doc.wacc.astNodes.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ASTVisitor {
  private AST ast;
  private List<String> codes = new LinkedList<>();
  private String resultReg = "r0";
  private String paramReg = "r4";

  public void getCodes() {
    codes.add(0, "main:");
    codes.add(0, ".global main");
    codes.add(0, ".text\n");
    codes.add("\tLDR r0, =0");
    codes.add("\tPOP {pc}");
    codes.add("\t.ltorg");

    for(String s: codes) {
      System.out.println(s);
    }

  }

  public void visitProgAST(AST ast) {
    codes.add("\tPUSH {lr}");
    ProgramAST past= (ProgramAST)ast;
    for (FuncAST f: past.getFunctions()) {
      visitFuncAST(f);
    }
    visitStat(past.getMainProgram());
  }

  public void visitFuncAST(FuncAST ast) {
  }

  public void visitExitAst(ExitAst ast) {
    visitExprAst(ast.getExpr());
    codes.add("\tMOV " + resultReg + ", " + paramReg);
    codes.add("\tBL exit");
  }

  public void visitExprAst(AST ast) {
    if (ast instanceof IntNode) {
      IntNode int_ast = (IntNode)ast;
      codes.add("\tLDR " + paramReg + ", =" + int_ast.getValue());
    }
  }




  public void visitStat(AST ast) {
    if (ast instanceof SkipAst) {
      visitSkipAst(ast);
    } else if (ast instanceof ExitAst) {
      visitExitAst((ExitAst)ast);
    }
  }
  public void visitSkipAst(AST ast) {
  }
}