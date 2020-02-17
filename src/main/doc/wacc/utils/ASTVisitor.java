package doc.wacc.utils;

import doc.wacc.astNodes.*;

import java.util.LinkedList;
import java.util.List;

public class ASTVisitor {
  private List<String> codes = new LinkedList<>();
  private List<String> variables = new LinkedList<>();
  private List<String> printCodes = new LinkedList<>();
  private String resultReg = "r0";
  private String paramReg = "r4";
  private int stringCounter = 0;
  private int spPosition = 0;
  private int registerCounter = 0;
  private boolean println = false;
  private boolean printint = false;
  private boolean printstring = false;
  private boolean printBool = false;

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

    if(printstring) {
      printCodes.add("p_print_string:");
      printCodes.add("\tPUSH {lr}");
      printCodes.add("\tLDR " + reg_add() + ", [" + resultReg + "]");
      printCodes.add("\tADD " + reg_add() + ", " + resultReg + ", #4");
      printCodes.add("\tLDR " + resultReg + ", =msg_" + stringCounter);
      printCodes.add("\tADD " + resultReg + ", " + resultReg + ", #4");
      printCodes.add("\tBL printf");
      printCodes.add("\tMOV " + resultReg + ", #0");
      printCodes.add("\tBL fflush");
      printCodes.add("\tPOP {pc}");
      visitStringNode(new StringNode("\"%.*s\\0\""));
    }

    if (printint) {
      printCodes.add("p_print_int:");
      printCodes.add("\tPUSH {lr}");
      printCodes.add("\tMOV " + reg_add() + ", " + resultReg);
      printCodes.add("\tLDR " + resultReg + ", =msg_" + stringCounter);
      printCodes.add("\tADD " + resultReg + ", " + resultReg + ", #4");
      printCodes.add("\tBL printf");
      printCodes.add("\tMOV " + resultReg + ", #0");
      printCodes.add("\tBL fflush");
      printCodes.add("\tPOP {pc}");
      visitStringNode(new StringNode("\"%d\\0\""));
    }

    if (printBool) {
      printCodes.add("p_print_bool:");
      printCodes.add("\tPUSH {lr}");
      printCodes.add("\tCMP " + resultReg + ", #0");
      printCodes.add("\tLDRNE " + resultReg + ", =msg_" + stringCounter);
      visitStringNode(new StringNode("\"true\\0\""));
      printCodes.add("\tLDRNE " + resultReg + ", =msg_" + stringCounter);
      visitStringNode(new StringNode("\"false\\0\""));
      printCodes.add("\tBL printf");
      printCodes.add("\tMOV " + resultReg + ", #0");
      printCodes.add("\tBL fflush");
      printCodes.add("\tPOP {pc}");
    }

    if (println) {
      printCodes.add("p_print_ln:");
      printCodes.add("\tPUSH {lr}");
      printCodes.add("\tLDR " + resultReg + ", =msg_" + stringCounter);
      printCodes.add("\tADD " + resultReg + ", " + resultReg + ", #4");
      printCodes.add("\tBL puts");
      printCodes.add("\tMOV " + resultReg + ", #0");
      printCodes.add("\tBL fflush");
      printCodes.add("\tPOP {pc}");
      visitStringNode(new StringNode("\"\\0\""));
    }

    for(String s: variables) {
      System.out.println(s);
    }

    for(String s: codes) {
      System.out.println(s);
    }

    if (printCodes.size() > 0) {
      for(String s: printCodes) {
        System.out.println(s);
      }
    }

  }

  public void visitProgAST(AST ast) {
    codes.add("\tPUSH {lr}");
    ProgramAST past= (ProgramAST) ast;
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

  public void visitAssignAst(AssignAST ast) {

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
        codes.add("\tSUB sp, sp, #4");
        spPosition += 4;
        codes.add("\tLDR " + paramReg + ", =msg_" + stringCounter);
        codes.add("\tSTR " + paramReg + ", [sp]");
        visitStringNode((StringNode)expr);
      } else if (expr instanceof IntNode) {
        codes.add("\tSUB sp, sp, #4");
        spPosition += 4;
        codes.add("\tLDR " + paramReg + ", =" + (ast.getRhs().getText()));
        codes.add("\tSTR " + paramReg + ", [sp]");
      } else if (expr instanceof BoolNode) {
        codes.add("\tSUB sp, sp, #1");
        spPosition += 1;
        codes.add("\tMOV " + paramReg + ", #" + ((BoolNode) expr).getBoolValue());
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
    } else if (ast instanceof AssignAST) {
      visitAssignAst((AssignAST)ast);
    } else if (ast instanceof PrintAst) {
      visitPrintAst((PrintAst)ast);
    } else if (ast instanceof PrintlnAst) {
      PrintlnAst print_ast = (PrintlnAst)ast;
      visitPrintAst(new PrintAst(((PrintlnAst) ast).getExpr()));
      visitPrintlnAst(print_ast);
    } else if (ast instanceof ReadAst) {
      visitReadAST((ReadAst) ast);
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

  public void visitPrintAst(PrintAst ast) {
    AST expr = ast.getExpr();
    if (expr instanceof StringNode) {
      codes.add("\tLDR " + paramReg + ", =msg_" + stringCounter);
      codes.add("\tMOV " + resultReg + ", " + paramReg);
      codes.add("\tBL p_print_string");

      visitStringNode((StringNode) ast.getExpr());
      printstring = true;
    } else if (expr instanceof IntNode) {
      codes.add("\tLDR " + paramReg + ", =" + ((IntNode) expr).getValue());
      codes.add("\tMOV " + resultReg + ", " + paramReg);
      codes.add("\tBL p_print_int");

      printint = true;
    } else if (expr instanceof CharNode) {
      codes.add("\tMOV " + paramReg + ", #'" + ((CharNode) expr).getCharValue() + "'");
      codes.add("\tMOV " + resultReg + ", " + paramReg);
      codes.add("\tBL putchar");
    } else if (expr instanceof BoolNode) {
      codes.add("\tMOV " + paramReg + ", #" + ((BoolNode) expr).getBoolValue());
      codes.add("\tMOV " + resultReg + ", " + paramReg);
      codes.add("\tBL p_print_bool");

      printBool = true;
    }

  }

  public void visitPrintlnAst(PrintlnAst ast) {
    codes.add("\tBL p_print_ln");
    println = true;
  }

  private String reg_add() {
    String current_reg = "r" + registerCounter;
    registerCounter++;
    return current_reg;
  }

  public void visitReadAST(ReadAst ast) {
    Type type = ast.getType();
    String readType = null;
    if (type.equals(Type.intType())) {
      readType = "int";
    } else if (type.equals(Type.charType())) {
      readType = "char";
    }
    codes.add("\tMOV " + resultReg + ", " + paramReg);
    codes.add("\tBL p_read_" + readType);
    variables.add("msg_" + stringCounter + ":");
    variables.add( "\t.word " + (readType.equals("char")?4:3));
    variables.add("\t.ascii  \"" + (readType.equals("char")?" %c":"%d") + "\\0\"");
    stringCounter++;
    printCodes.add("p_read_" + readType + ":");
    printCodes.add("\tPUSH {lr}");
    printCodes.add("\tMOV r1, " + resultReg);
    printCodes.add("\tLDR " + resultReg + ", =msg_" + (stringCounter-1));
    printCodes.add("\tADD " + resultReg + ", " + resultReg + ", #4");
    printCodes.add("\tBL scanf");
    printCodes.add("\tPOP {pc}");
  }

}