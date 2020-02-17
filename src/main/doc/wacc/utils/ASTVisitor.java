package doc.wacc.utils;

import antlr.BasicParser;
import doc.wacc.astNodes.*;

import java.util.LinkedList;
import java.util.List;

public class ASTVisitor {
  private List<String> main = new LinkedList<>();
  private List<String> variables = new LinkedList<>();
  private List<String> printcodes = new LinkedList<>();
  private String resultReg = "r0";
  private String paramReg = "r4";
  private int stringCounter = 0;
  private int spPosition = 0;
  private int registerCounter = 0;
  private int branchCounter = 0;
  private boolean println = false;
  private boolean printint = false;
  private boolean printstring = false;
  private boolean printBool = false;

  public void getcodes() {
    if (spPosition > 0) {
      main.add("\tADD sp, sp, #" + spPosition);
    }
    main.add("\tLDR r0, =0");
    main.add("\tPOP {pc}");
    main.add("\t.ltorg");

    if (variables.size() > 0) {
      variables.add(0, ".data\n");
    }

    if(printstring) {
      printcodes.add("p_print_string:");
      printcodes.add("\tPUSH {lr}");
      printcodes.add("\tLDR " + reg_add() + ", [" + resultReg + "]");
      printcodes.add("\tADD " + reg_add() + ", " + resultReg + ", #4");
      printcodes.add("\tLDR " + resultReg + ", =msg_" + stringCounter);
      printcodes.add("\tADD " + resultReg + ", " + resultReg + ", #4");
      printcodes.add("\tBL printf");
      printcodes.add("\tMOV " + resultReg + ", #0");
      printcodes.add("\tBL fflush");
      printcodes.add("\tPOP {pc}");
      visitStringNode(new StringNode("\"%.*s\\0\""));
    }

    if (printint) {
      printcodes.add("p_print_int:");
      printcodes.add("\tPUSH {lr}");
      printcodes.add("\tMOV " + reg_add() + ", " + resultReg);
      printcodes.add("\tLDR " + resultReg + ", =msg_" + stringCounter);
      printcodes.add("\tADD " + resultReg + ", " + resultReg + ", #4");
      printcodes.add("\tBL printf");
      printcodes.add("\tMOV " + resultReg + ", #0");
      printcodes.add("\tBL fflush");
      printcodes.add("\tPOP {pc}");
      visitStringNode(new StringNode("\"%d\\0\""));
    }

    if (printBool) {
      printcodes.add("p_print_bool:");
      printcodes.add("\tPUSH {lr}");
      printcodes.add("\tCMP " + resultReg + ", #0");
      printcodes.add("\tLDRNE " + resultReg + ", =msg_" + stringCounter);
      visitStringNode(new StringNode("\"true\\0\""));
      printcodes.add("\tLDRNE " + resultReg + ", =msg_" + stringCounter);
      visitStringNode(new StringNode("\"false\\0\""));
      printcodes.add("\tBL printf");
      printcodes.add("\tMOV " + resultReg + ", #0");
      printcodes.add("\tBL fflush");
      printcodes.add("\tPOP {pc}");
    }

    if (println) {
      printcodes.add("p_print_ln:");
      printcodes.add("\tPUSH {lr}");
      printcodes.add("\tLDR " + resultReg + ", =msg_" + stringCounter);
      printcodes.add("\tADD " + resultReg + ", " + resultReg + ", #4");
      printcodes.add("\tBL puts");
      printcodes.add("\tMOV " + resultReg + ", #0");
      printcodes.add("\tBL fflush");
      printcodes.add("\tPOP {pc}");
      visitStringNode(new StringNode("\"\\0\""));
    }

    for(String s: variables) {
      System.out.println(s);
    }

    for(String s: main) {
      System.out.println(s);
    }

    if (printcodes.size() > 0) {
      for(String s: printcodes) {
        System.out.println(s);
      }
    }

  }

  public void visitProgAST(AST ast) {
    main.add("\tPUSH {lr}");
    ProgramAST past= (ProgramAST) ast;
    for (FuncAST f: past.getFunctions()) {
      visitFuncAST(f);
    }
    visitStat(past.getMainProgram(), main);
    main.add(0, "main:");
    main.add(0, ".global main");
    main.add(0, ".text\n");
  }

  public void visitFuncAST(FuncAST ast) {
  }

  public void visitAssignAst(AssignAST ast) {

  }

  public void visitExitAst(ExitAst ast, List<String> codes) {
    visitExprAst(ast.getExpr(), codes);
    codes.add("\tLDR " + paramReg + ", [sp]");
    codes.add("\tMOV " + resultReg + ", " + paramReg);
    codes.add("\tBL exit");
  }

  public void visitExprAst(AST ast, List<String> codes) {
    if (ast instanceof IntNode) {
      IntNode int_ast = (IntNode)ast;
      codes.add("\tLDR " + paramReg + ", =" + int_ast.getValue());
    }
  }

  public void visitDeclaration(DeclarationAst ast, List<String> codes) {
    if (isOnlyExpr(ast)) {
      CompilerVisitor visitor = new CompilerVisitor();
      AST expr = visitor.visitExpr(ast.getAssignRhsAST().getRhsContext().expr(0));
      if (expr instanceof StringNode) {
        codes.add("\tSUB sp, sp, #4");
        spPosition += 4;
        codes.add("\tLDR " + paramReg + ", =msg_" + stringCounter);
        codes.add("\tSTR " + paramReg + ", [sp]");
        visitStringNode((StringNode)expr);
      } else if (expr instanceof IntNode) {
        codes.add("\tSUB sp, sp, #4");
        spPosition += 4;
        codes.add("\tLDR " + paramReg + ", =" + (ast.getAssignRhsAST().getRhsContext().getText()));
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
      ast.getAssignRhsAST().getRhsContext().array_liter();
    }

  }


  private boolean isOnlyExpr(DeclarationAst ast) {
    return ast.getAssignRhsAST().getRhsContext().expr().size() == 1;
  }

  public void visitStat(AST ast, List<String> codes) {
    if (ast instanceof SkipAst) {
      visitSkipAst(ast);
    } else if (ast instanceof ExitAst) {
      visitExitAst((ExitAst)ast, codes);
    } else if (ast instanceof DeclarationAst) {
      visitDeclaration((DeclarationAst) ast, codes);
    } else if (ast instanceof SeqStateAst) {
      for (AST ast1:((SeqStateAst) ast).getSeqs()) {
        visitStat(ast1, codes);
      }
    } else if (ast instanceof AssignAST) {
      visitAssignAst((AssignAST)ast);
    } else if (ast instanceof PrintAst) {
      visitPrintAst((PrintAst)ast, codes);
    } else if (ast instanceof PrintlnAst) {
      PrintlnAst print_ast = (PrintlnAst)ast;
      visitPrintAst(new PrintAst(((PrintlnAst) ast).getExpr()), codes);
      visitPrintlnAst(print_ast, codes);
    } else if (ast instanceof ReadAst) {
      visitReadAST((ReadAst) ast, codes);
    } else if (ast instanceof IfAst) {
      visitIfAst((IfAst)ast, codes);
    }
  }

  private void visitIfAst(IfAst ast, List<String> codes) {
    List<String> elseBranch = new LinkedList<>();
    if (ast.getExpr() instanceof BoolNode) {
      codes.add("\tMOV " + paramReg + ", #" + ((BoolNode) ast.getExpr()).getBoolValue());
      codes.add("\tCMP " + paramReg + ", #0");
    }
    //visitExpr not implemented
    codes.add("\tBEQ L" + branchCounter);
    elseBranch.add("L" + branchCounter++ + ":");
    visitStat(ast.getThenbranch(), codes);
    visitStat(ast.getElsebranch(), elseBranch);
    codes.add("\tB L" + branchCounter);
    for(String s: elseBranch) {
      codes.add(s);
    }
    codes.add("L" + branchCounter++ + ":");
  }

  public void visitSkipAst(AST ast) {
  }

  public void visitStringNode(StringNode ast) {
    variables.add("msg_" + stringCounter + ":");
    variables.add( "\t.word " + ast.getStringLength());
    variables.add("\t.ascii  " + ast.getValue());
    stringCounter++;
  }

  public void visitPrintAst(PrintAst ast, List<String> codes) {
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

  public void visitPrintlnAst(PrintlnAst ast, List<String> codes) {
    codes.add("\tBL p_print_ln");
    println = true;
  }

  private String reg_add() {
    String current_reg = "r" + registerCounter;
    registerCounter++;
    return current_reg;
  }

  public void visitReadAST(ReadAst ast, List<String> codes) {
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
    printcodes.add("p_read_" + readType + ":");
    printcodes.add("\tPUSH {lr}");
    printcodes.add("\tMOV r1, " + resultReg);
    printcodes.add("\tLDR " + resultReg + ", =msg_" + (stringCounter-1));
    printcodes.add("\tADD " + resultReg + ", " + resultReg + ", #4");
    printcodes.add("\tBL scanf");
    printcodes.add("\tPOP {pc}");
  }

}