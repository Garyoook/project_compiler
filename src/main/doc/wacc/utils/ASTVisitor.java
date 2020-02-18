package doc.wacc.utils;

import antlr.BasicParser;
import doc.wacc.astNodes.*;

import java.util.LinkedList;
import java.util.List;

import static doc.wacc.astNodes.AST.symbolTable;
import static doc.wacc.utils.Type.*;

public class ASTVisitor {
  private List<String> main = new LinkedList<>();
  public static int offset = 0;

  private List<String> variables = new LinkedList<>();
  private List<String> printcodes = new LinkedList<>();
  private String resultReg = "r0";
  private String paramReg = "r4";
  private int stringCounter = 0;
  private int spPosition = 0;
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
    main.add(0, "main:");
    for (FuncAST f: past.getFunctions()) {
      LinkedList<String> codes = new LinkedList<>();

      visitFuncAST(f, codes);
      main.addAll(0, codes);
    }
    visitStat(past.getMainProgram(), main, 4);
    main.add(0, "main:");
    main.add(0, ".global main");
    main.add(0, "\n.text\n");
  }

  public void visitFuncAST(FuncAST ast, LinkedList<String> codes) {

//    saveReg();
    SymbolTable symbolTabletemp = symbolTable;
    symbolTable = ast.getSymbolTable();
    LinkedList<String> funccodes = new LinkedList<>();
    visitStat(ast.getFunctionBody(), funccodes);
    codes.add("f_" + ast.getFuncName() + ":");
    codes.add("\tPUSH {lr}");
    codes.addAll(funccodes);
    codes.add("\tPOP {PC}");
    codes.add("\tPOP {PC}");
    symbolTable = symbolTabletemp;
    // TODO: 18/02/2020 saveReg restoreReg
//    restoreReg();
  }

  public void visitAssignAst(AssignAST ast, List<String> codes, int reg_counter) {
    if (!visitExpr(ast.getRhs().getExpr(), codes, reg_counter)) {
      codes.add("\tMOV " + resultReg + ", " + paramReg);
    }
    String strcommand = "STR ";
    if ((spPosition - symbolTable.getStackTable(ast.getLhs().getLhsContext().getText())) == 0){
      Type type;
      if (ast.getLhs().getLhsContext().pair_elem() != null) {
        type = symbolTable.getVariable(ast.getLhs().getLhsContext().pair_elem().expr().getText());
      } else if (ast.getLhs().getLhsContext().array_elem() != null) {
        type = symbolTable.getVariable(ast.getLhs().getLhsContext().array_elem().IDENT().getText());
      } else {
        type = symbolTable.getVariable(ast.getLhs().getLhsContext().getText());
      }
      if (type.equals(boolType()) || type.equals(charType())) {
        strcommand = "STRB ";
      }
      codes.add("\t" + strcommand + paramReg + ", [sp]");
    } else {
      codes.add("\t" + strcommand + paramReg + ", [sp, #" + (spPosition - symbolTable.getStackTable(ast.getLhs().getLhsContext().getText())) + "]");
    }
  }

  public void visitExitAst(ExitAst ast, List<String> codes, int reg_counter) {
    AST newAST = ast.getExpr();
    visitExpr(ast.getExpr(),codes, reg_counter);
    if (newAST instanceof IdentNode) {
      int x = symbolTable.getStackTable(((IdentNode) newAST).getIdent());
      if (spPosition - x == 0) {
        codes.add("\tLDR " + paramReg + ", [sp]");
      } else {
        codes.add("\tLDR " + paramReg + ", [sp, #" + (spPosition - x) + "]");
      }
    } else {
      codes.add("\tLDR " + paramReg + ", [sp]");
    }
    codes.add("\tMOV " + resultReg + ", " + paramReg);
    codes.add("\tBL exit");
  }

  public boolean visitExpr(AST ast, List<String> codes, int reg_counter) {
    if (ast instanceof IntNode) {
      IntNode int_ast = (IntNode)ast;
      codes.add("\tLDR " + paramReg + ", =" + int_ast.getValue());
      return true;
    } else if (ast instanceof BoolNode) {
      BoolNode bool_ast = (BoolNode) ast;
      codes.add("\tMOV " + paramReg + ", #" + bool_ast.getBoolValue());
      return true;
    } else if (ast instanceof IdentNode) {
      int x = symbolTable.getStackTable(((IdentNode)ast).getIdent());
      if (spPosition - x == 0) {
        codes.add("\tLDR " + paramReg + ", [sp]");
      } else {
        codes.add("\tLDR " + paramReg + ", [sp, #" + (spPosition - x) + "]");
      }
      return true;
    } else if (ast instanceof StringNode) {
      codes.add("\tLDR " + paramReg + ", =msg_" + stringCounter);
      visitStringNode((StringNode)ast);
      return true;
    } else if (ast instanceof CharNode) {
      codes.add("\tMOV " + paramReg + ", #'" + ((CharNode) ast).getCharValue() + "'");
      return true;
    }
    else if (ast instanceof Binary_BoolOpNode) {
      visitExpr(((Binary_BoolOpNode) ast).getExpr1(), codes, reg_counter);
      visitExpr(((Binary_BoolOpNode) ast).getExpr2(), codes, reg_counter + 1);
      codes.add("\tCMP r" + (reg_counter - 1) + ", r" + reg_counter);
      if (((Binary_BoolOpNode) ast).isEqual()) {
        codes.add("\tMOVEQ " + paramReg + ", #1");
        codes.add("\tMOVNE " + paramReg + ", #0");
      } else if (((Binary_BoolOpNode) ast).isNotEqual()) {
        codes.add("\tMOVNE " + paramReg + ", #1");
        codes.add("\tMOVEQ " + paramReg + ", #0");
      }
    }
    return false;
  }

  public void visitDeclaration(DeclarationAst ast, List<String> codes, int reg_counter) {
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
      } else if (expr instanceof CharNode) {
        codes.add("\tSUB sp, sp, #1");
        spPosition += 1;
        codes.add("\tMOV " + paramReg + ", #'" + ((CharNode) expr).getCharValue() + "'");
        codes.add("\tSTRB " + paramReg + ", [sp]");
        }
    } else {
      ast.getAssignRhsAST().getRhsContext().array_liter();
    }
    symbolTable.putStackTable(ast.getName(), spPosition);
  }


  private boolean isOnlyExpr(DeclarationAst ast) {
    return ast.getAssignRhsAST().getRhsContext().expr().size() == 1;
  }

  public void visitStat(AST ast, List<String> codes, int reg_counter) {
    if (ast instanceof SkipAst) {
      visitSkipAst(ast);
    } else if (ast instanceof ExitAst) {
      visitExitAst((ExitAst)ast, codes, reg_counter);
    } else if (ast instanceof DeclarationAst) {
      visitDeclaration((DeclarationAst) ast, codes, reg_counter);
    } else if (ast instanceof SeqStateAst) {
      for (AST ast1:((SeqStateAst) ast).getSeqs()) {
        visitStat(ast1, codes, reg_counter++);
      }
    } else if (ast instanceof AssignAST) {
      visitAssignAst((AssignAST)ast, codes, reg_counter);
    } else if (ast instanceof PrintAst) {
      visitPrintAst((PrintAst)ast, codes, reg_counter);
    } else if (ast instanceof PrintlnAst) {
      PrintlnAst print_ast = (PrintlnAst)ast;
      visitPrintAst(new PrintAst(((PrintlnAst) ast).getExpr()), codes, reg_counter);
      visitPrintlnAst(print_ast, codes, reg_counter + 1);
    } else if (ast instanceof ReadAst) {
      visitReadAST((ReadAst) ast, codes, reg_counter);
    } else if (ast instanceof IfAst) {
      visitIfAst((IfAst)ast, codes, reg_counter);
    } else if (ast instanceof WhileAst) {
      visitWhileAST((WhileAst) ast, codes, reg_counter);
    } else if (ast instanceof ReturnAst) {
      visitReturnAST((ReturnAst) ast, codes);
    }
  }

  private void visitReturnAST(ReturnAst ast, List<String> codes) {
    visitExprAst(ast, codes);
    codes.add("\tMOV " + resultReg + ", " + paramReg);
  }

  private void visitIfAst(IfAst ast, List<String> codes, int reg_counter) {
    List<String> elseBranch = new LinkedList<>();
    visitExpr(ast.getExpr(), codes, reg_counter);
    if (ast.getExpr() instanceof BoolNode || ast.getExpr() instanceof Binary_BoolOpNode) {
      codes.add("\tCMP " + paramReg + ", #0");
    } else {
      codes.add("\tCMP " + paramReg + ", r" + reg_counter);
    }

    //visitExpr not implemented
    codes.add("\tBEQ L" + branchCounter);
    elseBranch.add("L" + branchCounter++ + ":");
    visitStat(ast.getThenbranch(), codes, reg_counter + 1);
    visitStat(ast.getElsebranch(), elseBranch, reg_counter + 2);
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

  public void visitPrintAst(PrintAst ast, List<String> codes, int reg_counter) {
    AST expr = ast.getExpr();
    visitExpr(ast.getExpr(), codes, reg_counter);
    codes.add("\tMOV " + resultReg + ", " + paramReg);
    Type type  = null;

    if (expr instanceof IdentNode) {
      type = symbolTable.getVariable(((IdentNode) expr).getIdent());
      if (type.equals(stringType())) {
        codes.add("\tBL p_print_string");
        printstring = true;
      } else if (type.equals(intType())) {
        codes.add("\tBL p_print_int");
        printint = true;
      } else if (type.equals(charType())) {
        codes.add("\tBL putchar");
      } else if (type.equals(boolType())) {
        codes.add("\tBL p_print_bool");
        printBool = true;
      }
    }

    if (expr instanceof StringNode) {
      codes.add("\tBL p_print_string");
      printstring = true;
    } else if (expr instanceof IntNode) {
      codes.add("\tBL p_print_int");
      printint = true;
    } else if (expr instanceof CharNode) {
      codes.add("\tBL putchar");
    } else if (expr instanceof BoolNode) {
      codes.add("\tBL p_print_bool");
      printBool = true;
    }
  }

  public void visitPrintlnAst(PrintlnAst ast, List<String> codes, int reg_counter) {
    codes.add("\tBL p_print_ln");
    println = true;
  }

  int counter_start = 1;
  private String reg_add() {
    String current_reg = "r" + counter_start;
    counter_start++;
    return current_reg;
  }

  public void visitReadAST(ReadAst ast, List<String> codes, int reg_counter) {
    Type type = ast.getType();
    String readType = null;
    if (type.equals(intType())) {
      readType = "int";
    } else if (type.equals(charType())) {
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

  public void visitWhileAST(WhileAst ast, List<String> codes, int reg_counter) {
    int loopLabel = branchCounter++;
    int bodyLabel = branchCounter++;
    codes.add("\tB L" + loopLabel);
    codes.add("L" + bodyLabel + ":");
    visitStat(ast.getStat(), codes, reg_counter);
    codes.add("L" + loopLabel + ":");
    //expr not implemented
//    visitExprAst(ast.getExpr(), codes);
    if (ast.getExpr() instanceof BoolNode) {
      codes.add("\tMOV " + paramReg + ", #" + ((BoolNode) ast.getExpr()).getBoolValue());
      codes.add("\tCMP " + paramReg + ", #1");
    }
    codes.add("\tBEQ L" + bodyLabel);
  }

}