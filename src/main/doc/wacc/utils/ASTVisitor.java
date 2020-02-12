package doc.wacc.utils;

import doc.wacc.astNodes.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static doc.wacc.astNodes.AST.is_String;

public class ASTVisitor {
  private AST ast;
  private List<String> codes = new LinkedList<>();
  private List<String> variables = new LinkedList<>();
  private String resultReg = "r0";
  private String paramReg = "r4";
  private int stringCounter = 0;
  private int spPosition = 0;

  public void getCodes() {
    if (spPosition > 0) {
      codes.add("\tADD sp, sp, #" + spPosition);
    }
    codes.add("\tLDR r0, =0");
    codes.add("\tPOP {pc}");
    codes.add("\t.ltorg");

    if (variables.size() > 0) {
      variables.add(0, ".data");
    }
    for(String s: variables) {
      System.out.println(s);
    }

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
    codes.add(0, "main:");
    codes.add(0, ".global main");
    codes.add(0, ".text\n");
  }

  public void visitFuncAST(FuncAST ast) {
  }

  public void visitExitAst(ExitAst ast) {
    visitExprAst(ast.getExpr());
    codes.add("\tLDR " + paramReg + ", [sp]");
    codes.add("\tMOV " + resultReg + ", " + paramReg);
    codes.add("\tBL exit");
  }

  public void visitExprAst(AST ast) {
    if (ast instanceof IntNode) {
      IntNode int_ast = (IntNode)ast;
      codes.add("\tLDR " + paramReg + ", =" + int_ast.getValue());
    }
  }

  public void visitDeclaration(DeclarationAst ast) {
    if (isOnlyExpr(ast)) {
      CompilerVisitor visitor = new CompilerVisitor();
      AST expr = visitor.visitExpr(ast.getRhs().expr(0));
      if (expr instanceof StringNode) {
        visitStringNode((StringNode)expr);
        codes.add("\tSUB sp, sp, #4");
        spPosition += 4;
        codes.add("\tLDR " + paramReg + ", =msg_" + (stringCounter - 1));
        codes.add("\tSTR " + paramReg + ", [sp]");
      } else if (expr instanceof IntNode) {
        codes.add("\tSUB sp, sp, #4");
        spPosition += 4;
        codes.add("\tLDR " + paramReg + ", =" + (ast.getRhs().getText()));
        codes.add("\tSTR " + paramReg + ", [sp]");
      } else if (expr instanceof BoolNode) {
        codes.add("\tSUB sp, sp, #1");
        spPosition += 1;
        codes.add("\tMOV " + paramReg + ", #" + (!((BoolNode) expr).getBoolValue() ? 0 : 1));
        codes.add("\tSTRB " + paramReg + ", [sp]");
        codes.add("\tADD sp, sp, #1");
      } else if (expr instanceof CharNode) {
        codes.add("\tSUB sp, sp, #1");
        spPosition += 1;
        codes.add("\tMOV " + paramReg + ", #'" + ((CharNode) expr).getCharValue() + "'");
        codes.add("\tSTRB " + paramReg + ", [sp]");
//        codes.add("\tADD sp, sp, #1");
        }
    } else {
      ast.getRhs().array_liter();
    }

  }


  private boolean isOnlyExpr(DeclarationAst ast) {
    return ast.getRhs().expr().size() == 1;
  }

  public void visitStat(AST ast) {
    if (ast instanceof SkipAst) {
      visitSkipAst(ast);
    } else if (ast instanceof ExitAst) {
      visitExitAst((ExitAst)ast);
    } else if (ast instanceof DeclarationAst) {
      visitDeclaration((DeclarationAst) ast);
    } else if (ast instanceof SeqStateAst) {
      for (AST ast1:((SeqStateAst) ast).getSeqs()) {
        visitStat(ast1);
      }
    }
  }

  public void visitSkipAst(AST ast) {
  }

  public void visitStringNode(StringNode ast) {
    variables.add("msg_" + stringCounter + ":");
    variables.add( "\t.word " + ast.getStringLength());
    variables.add("\t.ascii  " + ast.getValue());
    stringCounter++;
  }



}