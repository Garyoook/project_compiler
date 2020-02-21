package doc.wacc.utils;

import doc.wacc.astNodes.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static doc.wacc.astNodes.AST.symbolTable;
import static doc.wacc.utils.Register.createAllRegs;
import static doc.wacc.utils.Type.*;

public class ASTVisitor {
  public static final String SP = "sp";
  public static final String PC = "pc";
  public static final String LR = "lr";
  private List<String> main = new LinkedList<>();
  public static int offset = 0;
  public final ArrayList<Register> allRegs = createAllRegs();

  private List<String> variables = new LinkedList<>();
  private List<String> printcodes = new LinkedList<>();
  private final String resultReg = "r0";
  private final String paramReg = "r4";
  private int stringCounter = 0;
  private int spPosition = 0;
  private int branchCounter = 0;
  private boolean println = false;
  private boolean printint = false;
  private boolean printstring = false;
  private boolean printBool = false;

  public List<String> getCodes() {
    if (spPosition > 0) {
      main.add(ADD(SP, SP, "#" + spPosition));
    }
    main.add(LDR_value("r0", 0));
    main.add(POP("pc"));
    main.add("\t.ltorg");

    if (variables.size() > 0) {
      variables.add(0, ".data\n");
    }

    if(printstring) {
      printcodes.add("p_print_string:");
      printcodes.add(PUSH("lr"));
      printcodes.add(LDR_reg(reg_add(), resultReg));
      printcodes.add(ADD(reg_add(), resultReg, "#4"));
      printcodes.add(LDR_msg(resultReg, String.valueOf(stringCounter)));
      printcodes.add(ADD(resultReg, resultReg, "#4"));
      printcodes.add(BL("printf"));
      printcodes.add(MOV(resultReg, "#0"));
      printcodes.add(BL("fflush"));
      printcodes.add(POP("pc"));
      visitStringNode(new StringNode("\"%.*s\\0\""));
    }

    if (printint) {
      printcodes.add("p_print_int:");
      printcodes.add(PUSH("lr"));
      printcodes.add(MOV(reg_add(), resultReg));
      printcodes.add(LDR_msg(resultReg, String.valueOf(stringCounter)));
      printcodes.add(ADD(resultReg, resultReg, "#4"));
      printcodes.add(BL("printf"));
      printcodes.add(MOV(resultReg, "#0"));
      printcodes.add(BL("fflush"));
      printcodes.add(POP("pc"));
      visitStringNode(new StringNode("\"%d\\0\""));
    }

    if (printBool) {
      printcodes.add("p_print_bool:");
      printcodes.add(PUSH("lr"));
      printcodes.add(CMP_value(resultReg, 0));
      printcodes.add(LDRNE_msg(resultReg , String.valueOf(stringCounter)));
      visitStringNode(new StringNode("\"true\\0\""));
      printcodes.add(LDRNE_msg(resultReg , String.valueOf(stringCounter)));
      visitStringNode(new StringNode("\"false\\0\""));
      printcodes.add(BL("printf"));
      printcodes.add(MOV(resultReg,"#0"));
      printcodes.add(BL("fflush"));
      printcodes.add(POP("pc"));
    }

    if (println) {
      printcodes.add("p_print_ln:");
      printcodes.add(PUSH(LR));
      printcodes.add(LDR_msg(resultReg, String.valueOf(stringCounter)));
      printcodes.add(ADD(resultReg, resultReg, "#4"));
      printcodes.add(BL("puts"));
      printcodes.add(MOV(resultReg,"#0"));
      printcodes.add(BL("fflush"));
      printcodes.add(POP(PC));
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

    List<String> result = new LinkedList<>();
    result.addAll(variables);
    result.addAll(main);
    result.addAll(printcodes);

    return result;

  }

  public void visitProgAST(AST ast) {
    main.add(PUSH("lr"));
    ProgramAST past= (ProgramAST) ast;
    int k = 4;
    for (FuncAST f: past.getFunctions()) {
      LinkedList<String> codes = new LinkedList<>();

      visitFuncAST(f, codes, k);
      k++;
      main.addAll(0, codes);
    }
    visitStat(past.getMainProgram(), main, k);
    main.add(0, "main:");
    main.add(0, ".global main");
    main.add(0, "\n.text\n");
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
      visitReturnAST((ReturnAst) ast, codes, reg_counter);
    }
  }
  public void visitFuncAST(FuncAST ast, LinkedList<String> codes, int reg_counter) {

//    saveReg();
    SymbolTable symbolTabletemp = symbolTable;
    symbolTable = ast.getSymbolTable();
    LinkedList<String> funccodes = new LinkedList<>();
    visitStat(ast.getFunctionBody(), funccodes, reg_counter);
    codes.add("f_" + ast.getFuncName() + ":");
    codes.add(PUSH("lr"));
    codes.addAll(funccodes);
    codes.add(POP(PC));
    codes.add(POP(PC));
    symbolTable = symbolTabletemp;
    // TODO: 18/02/2020 saveReg restoreReg
//    restoreReg();
  }

  public void visitAssignAst(AssignAST ast, List<String> codes, int reg_counter) {
    if (!visitExprAST(ast.getRhs().getExpr(), codes, reg_counter)) {
      codes.add(MOV(resultReg , paramReg));
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
    visitExprAST(ast.getExpr(),codes, reg_counter);
    if (newAST instanceof IdentNode) {
      int x = symbolTable.getStackTable(((IdentNode) newAST).getIdent());
      if (spPosition - x == 0) {
        codes.add(LDR_reg(paramReg, SP));
      } else {
        codes.add(LDR_reg(paramReg, "sp, #" + (spPosition - x)));
      }
    } else {
      codes.add(LDR_reg(paramReg, SP));
    }
    codes.add(MOV(resultReg, paramReg));
    codes.add(BL("exit"));
  }

  public boolean visitExprAST(AST ast, List<String> codes, int reg_counter) {
    if (ast instanceof IntNode) {
      IntNode int_ast = (IntNode)ast;
      codes.add(LDR_value(paramReg, int_ast.getValue()));
      return true;
    } else if (ast instanceof BoolNode) {
      BoolNode bool_ast = (BoolNode) ast;
      codes.add(MOV(paramReg, "#" + bool_ast.getBoolValue()));
      return true;
    } else if (ast instanceof IdentNode) {
      int x = symbolTable.getStackTable(((IdentNode)ast).getIdent());
      if (spPosition - x == 0) {
        codes.add(LDR_reg(paramReg, SP));
      } else {
        codes.add(LDR_reg(paramReg, "sp, #" + (spPosition - x)));
      }
      return true;
    } else if (ast instanceof StringNode) {
      codes.add(LDR_msg(paramReg, String.valueOf(stringCounter)));
      visitStringNode((StringNode)ast);
      return true;
    } else if (ast instanceof CharNode) {
      codes.add(MOV(paramReg,"#'" + ((CharNode) ast).getCharValue() + "'"));
      return true;
    }
    else if (ast instanceof Binary_BoolOpNode) {
      visitExprAST(((Binary_BoolOpNode) ast).getExpr1(), codes, reg_counter);
      visitExprAST(((Binary_BoolOpNode) ast).getExpr2(), codes, reg_counter + 1);
      codes.add(CMP_reg( "r" + (reg_counter - 1), reg_counter));
      if (((Binary_BoolOpNode) ast).isEqual()) {
        codes.add(MOVEQ(paramReg, 1));
        codes.add(MOVNE(paramReg, 0));
      } else if (((Binary_BoolOpNode) ast).isNotEqual()) {
        codes.add(MOVNE(paramReg, 1));
        codes.add(MOVEQ(paramReg, 0));
      }
    }
    return false;
  }

  public void visitDeclaration(DeclarationAst ast, List<String> codes, int reg_counter) {
    if (isOnlyExpr(ast)) {
      CompilerVisitor visitor = new CompilerVisitor();
      AST expr = visitor.visitExpr(ast.getAssignRhsAST().getRhsContext().expr(0));
      if (expr instanceof StringNode) {
        spPosition += 4;
        codes.add(SUB(SP, SP, 4));
        codes.add(LDR_msg(paramReg, String.valueOf(stringCounter)));
        codes.add(STR(paramReg, "[sp]"));
        visitStringNode((StringNode)expr);
      } else if (expr instanceof IntNode) {
        spPosition += 4;
        codes.add(SUB(SP, SP, 4));
        codes.add(LDR_msg(paramReg, ast.getAssignRhsAST().getRhsContext().getText()));
        codes.add(STR(paramReg, "[sp]"));
      } else if (expr instanceof BoolNode) {
        codes.add(SUB(SP, SP, 1));
        spPosition += 1;
        codes.add(MOV(paramReg, ("#" + ((BoolNode) expr).getBoolValue())));
        codes.add(STRB(paramReg, "[]"));
      } else if (expr instanceof CharNode) {
        codes.add(SUB(SP, SP, 1));
        spPosition += 1;
        codes.add(MOV(paramReg, ("#'" + ((CharNode) expr).getCharValue() + "'")));
        codes.add(STRB(paramReg, "[sp]"));
      }
    } else {
      if (ast.getAssignRhsAST().getRhsContext().array_liter() != null) {
        spPosition += 4;
        codes.add(SUB(SP, SP, 4));
        codes.add(LDR_value(resultReg, (4+4*ast.getAssignRhsAST().getRhsContext().array_liter().expr().size())));
        codes.add(BL("malloc"));
        codes.add(MOV(paramReg, resultReg));
        for (int i=0; i<ast.getAssignRhsAST().getRhsContext().array_liter().expr().size(); i++) {
          CompilerVisitor compilerVisitor = new CompilerVisitor();
          visitExprAST(compilerVisitor.visitExpr(ast.getAssignRhsAST().getRhsContext().array_liter().expr(i)), codes, reg_counter);
          codes.add(STR("r5",  ("[r4, #" + ((i+1)*4) + "]")));
        }
        codes.add(LDR_value("r5" , ast.getAssignRhsAST().getRhsContext().array_liter().expr().size()));
        codes.add(STR("r5", "[r4]"));
        codes.add(STR(paramReg,"[sp]"));
      }
    }
    symbolTable.putStackTable(ast.getName(), spPosition);
  }


  private boolean isOnlyExpr(DeclarationAst ast) {
    return ast.getAssignRhsAST().getRhsContext().expr().size() == 1;
  }

  private void visitReturnAST(ReturnAst ast, List<String> codes, int reg_counter) {
    visitExprAST(ast, codes, reg_counter);
    codes.add(MOV(paramReg, resultReg));
  }

  private void visitIfAst(IfAst ast, List<String> codes, int reg_counter) {
    SymbolTable symbolTabletemp = symbolTable;
    symbolTable = ast.getThenSymbolTable();
    List<String> elseBranch = new LinkedList<>();
    visitExprAST(ast.getExpr(), codes, reg_counter);
    if (ast.getExpr() instanceof BoolNode || ast.getExpr() instanceof Binary_BoolOpNode) {
      codes.add(CMP_value(paramReg, 0));
    } else {
      codes.add(CMP_reg(paramReg, reg_counter));
    }

    //visitExpr not implemented
    codes.add("\tBEQ L" + branchCounter);
    elseBranch.add("L" + branchCounter++ + ":");
    visitStat(ast.getThenbranch(), codes, reg_counter + 1);
    symbolTable = ast.getElseSymbolTable();
    visitStat(ast.getElsebranch(), elseBranch, reg_counter + 2);
    codes.add("\tB L" + branchCounter);
    for(String s: elseBranch) {
      codes.add(s);
    }
    codes.add("L" + branchCounter++ + ":");
    symbolTable = symbolTabletemp;
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
    visitExprAST(ast.getExpr(), codes, reg_counter);
    codes.add(MOV(resultReg, paramReg));
    Type type  = null;

    if (expr instanceof IdentNode) {
      type = symbolTable.getVariable(((IdentNode) expr).getIdent());
      if (type.equals(stringType())) {
        codes.add(BL("p_print_string"));
        printstring = true;
      } else if (type.equals(intType())) {
        codes.add(BL("p_print_int"));
        printint = true;
      } else if (type.equals(charType())) {
        codes.add(BL("putchar"));
      } else if (type.equals(boolType())) {
        codes.add(BL("p_print_bool"));
        printBool = true;
      }
    }

    if (expr instanceof StringNode) {
      codes.add(BL("p_print_string"));
      printstring = true;
    } else if (expr instanceof IntNode) {
      codes.add(BL("p_print_int"));
      printint = true;
    } else if (expr instanceof CharNode) {
      codes.add(BL("putchar"));
    } else if (expr instanceof BoolNode) {
      codes.add(BL("p_print_bool"));
      printBool = true;
    }
  }

  public void visitPrintlnAst(PrintlnAst ast, List<String> codes, int reg_counter) {
    codes.add(BL("p_print_ln"));
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
    codes.add(MOV(resultReg, paramReg));
    codes.add(BL("p_read_" + readType));
    variables.add("msg_" + stringCounter + ":");
    variables.add( "\t.word " + (readType.equals("char")?4:3));
    variables.add("\t.ascii  \"" + (readType.equals("char")?" %c":"%d") + "\\0\"");
    stringCounter++;
    printcodes.add("p_read_" + readType + ":");
    printcodes.add(PUSH("lr"));
    printcodes.add(MOV("r1", resultReg));
    printcodes.add(LDR_msg(resultReg, String.valueOf((stringCounter-1))));
    printcodes.add(ADD(resultReg, resultReg, "#4"));
    printcodes.add(BL("scanf"));
    printcodes.add(POP("pc"));
  }

  public void visitWhileAST(WhileAst ast, List<String> codes, int reg_counter) {
    SymbolTable symbolTabletemp = symbolTable;
    symbolTable = ast.getSymbolTable();
    int loopLabel = branchCounter++;
    int bodyLabel = branchCounter++;
    codes.add("\tB L" + loopLabel);
    codes.add("L" + bodyLabel + ":");
    visitStat(ast.getStat(), codes, reg_counter);
    codes.add("L" + loopLabel + ":");
//    visitExprAST(ast.getExpr(), codes, reg_counter);
    if (ast.getExpr() instanceof BoolNode) {
      codes.add(MOV(paramReg, "#" + ((BoolNode) ast.getExpr()).getBoolValue()));
      codes.add(CMP_value(paramReg, 1));
    }
    codes.add("\tBEQ L" + bodyLabel);
    symbolTable = symbolTabletemp;
  }

  public ArrayList<Register> regsExcept(ArrayList<Register> except) {
    ArrayList<Register> tempRegs = allRegs;
    for (Register r : except) {
      tempRegs.remove(r);
    }
    return tempRegs;
  }

  public String SUB(String dst, String src, int size) {
    return "\tSUB " + dst + ", " + src + ", " + "#" + size;
  }

  public String LDR_msg(String dst, String content) {
    return "\tLDR " + dst + ", =msg_" + content;
  }

  public String LDR_value(String dst, int content) {
    return "\tLDR " + dst + ", =" + content;
  }

  public String LDR_reg(String dst, String srcReg) {
    return "\tLDR " + dst + ", [" + srcReg + "]";
  }

  public String LDRSB (String dst, String src) {
    return "\tLDRSB " + dst + ", " +  src;
  }

  public String LDRNE_msg(String dst, String content) {
    return "\tLDR " + dst + ", =msg_" + content;
  }

  public String STR(String dst, String addr) {
    return "\tSTR " + dst + ", " + addr;
  }

  public String STRB(String dst, String addr) {
    return "\tSTRB " + dst + ", " + addr;
  }

  public String MOV(String dst, String src) {
    return "\tMOV " + dst + ", " + src;
  }

  public String CMP_value(String dst, int value) {
    return "\tCMP " + dst + ", #" + value;
  }

  public String CMP_reg(String dst, int reg_num) {
    return "\tCMP " + dst + ", r" + reg_num;
  }

  public String MOVEQ(String dst, int value) {
    return "\tMOVEQ" + dst + ", #" + value;
  }

  public String MOVNE(String dst, int value) {
    return "\tMOVNE" + dst + ", #" + value;
  }

  public String ADD(String result, String a, String b) {
    return "\tADD " + result + ", " + a + ", " + b;
  }

  public String POP(String target) {
    return "\tPOP {" + target + "}";
  }

  public String PUSH(String target) {
    return "\tPUSH {" + target + "}";
  }

  public String BL(String target) {
    return "\tBL " + target;
  }


//  public void saveReg(int size) {
//    SUB("sp", "sp", size);
//    LDR(paramReg);
//  }
}