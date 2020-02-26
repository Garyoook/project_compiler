package doc.wacc.utils;

import antlr.BasicParser;
import doc.wacc.astNodes.*;

import java.net.IDN;
import java.util.ArrayList;
import java.util.HashMap;
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
  public HashMap<String, Integer> functionParams = new HashMap<>();

  private List<String> variables = new LinkedList<>();
  private List<String> printcodes = new LinkedList<>();
  private final String resultReg = "r0";
  private final String paramReg = "r4";
  private final int regMax = 10;
  private int stringCounter = 0;
  private int spPosition = 0;
  private int branchCounter = 0;
  private int pushCounter = 0;
  private int mallocCounter = 0;   //for array
  private boolean println = false;
  private boolean printint = false;
  private boolean printstring = false;
  private boolean printBool = false;
  private boolean printRunTimeErr = false;
  private boolean printCheckArrayBound = false;
  private boolean printOverflowError = false;
  private boolean printReference = false;
  private boolean printFree = false;
  private boolean printDivideByZeroError = false;
  private boolean printCheckNullPointer = false;
  private boolean read_int = false;
  private boolean read_char = false;
//  private int local_variable = 0;
  private boolean in_func = false;
  private boolean return_Pop = false;

  public List<String> getCodes() {
    if (spPosition > 0) {
      while (spPosition > 1024){
        main.add(ADD(SP, SP, 1024));
        spPosition -= 1024;
      }
      main.add(ADD(SP, SP, spPosition));
    }
    main.add(LDR_value(resultReg, 0));
    main.add(POP(PC));
    main.add("\t.ltorg");

    if (printCheckNullPointer) {
      printcodes.add("p_check_null_pointer:");
      printcodes.add(PUSH(LR));
      printcodes.add(CMP_value(resultReg, 0));
      printcodes.add(LDREQ_msg(resultReg, String.valueOf(stringCounter)));
      visitStringNode(new StringNode("\"NullReferenceError: dereference a null reference\\n\\0\""));
      printcodes.add(BLEQ("p_throw_runtime_error"));
      printRunTimeErr = true;
      printcodes.add(POP(PC));
    }


    if (printCheckArrayBound) {
      printcodes.add("p_check_array_bounds:");
      printcodes.add(PUSH(LR));
      printcodes.add(CMP_value(resultReg, 0));
      printcodes.add(LDRLT_msg(resultReg, String.valueOf(stringCounter)));
      variables.add("msg_" + stringCounter++ + ":");
      variables.add( "\t.word 44");
      variables.add("\t.ascii \"ArrayIndexOutOfBoundsError: negative index\\n\\0\"");
      printcodes.add(BLLT("p_throw_runtime_error"));
      printcodes.add(LDR_reg("r1","r1"));
      printcodes.add(CMP_reg(resultReg,"r1"));
      printcodes.add(LDRCS_msg(resultReg, String.valueOf(stringCounter)));
      variables.add("msg_" + stringCounter++ + ":");
      variables.add( "\t.word 45");
      variables.add("\t.ascii \"ArrayIndexOutOfBoundsError: index too large\\n\\0\"");
      printcodes.add(BLCS("p_throw_runtime_error"));
      printRunTimeErr = true;
      printcodes.add(POP(PC));
    }

    if (printFree) {
      printcodes.add("p_free_pair:");
      printcodes.add(PUSH(LR));
      printcodes.add(CMP_value(resultReg, 0));
      printcodes.add(LDREQ_msg(resultReg, String.valueOf(stringCounter)));
      visitStringNode(new StringNode("\"NullReferenceError: dereference a null reference\\n\\0\""));
      printcodes.add("\tBEQ p_throw_runtime_error");
      printRunTimeErr = true;
      printcodes.add(PUSH(resultReg));
      printcodes.add(LDR_reg(resultReg, resultReg));
      printcodes.add(BL("free"));
      printcodes.add(LDR_reg(resultReg, SP));
      printcodes.add(LDR_reg(resultReg, (resultReg + ", #4")));
      printcodes.add(BL("free"));
      printcodes.add(POP(resultReg));
      printcodes.add(BL("free"));
      printcodes.add(POP(PC));
    }

    if (printDivideByZeroError) {
      printcodes.add("p_check_divide_by_zero:");
      printcodes.add(PUSH(LR));
      printcodes.add(CMP_value("r1", 0));
      printcodes.add(LDREQ_msg("r0", String.valueOf(stringCounter)));
      printcodes.add("\tBLEQ p_throw_runtime_error");
      printRunTimeErr = true;
      printcodes.add(POP(PC));
      variables.add("msg_" + stringCounter + ":");
      variables.add( "\t.word 45");
      variables.add("\t.ascii \"DivideByZeroError: divide or modulo by zero\\n\\0\"");
      stringCounter++;
    }


    if (printOverflowError) {
      printcodes.add("p_throw_overflow_error:");
      printcodes.add(LDR_msg(resultReg, String.valueOf(stringCounter)));
      printcodes.add(BL("p_throw_runtime_error"));
      printRunTimeErr = true;
      variables.add("msg_" + stringCounter + ":");
      variables.add( "\t.word 82");
      variables.add("\t.ascii \"OverflowError: the result is too small/large to store in a 4-byte signed-integer.\\n\"");
      stringCounter++;
    }

    if (printRunTimeErr) {
      printcodes.add("p_throw_runtime_error:");
      printcodes.add(BL("p_print_string"));
      printstring = true;
      printcodes.add(MOV(resultReg, -1));
      printcodes.add(BL("exit"));
    }

    if (printint) {
      printcodes.add("p_print_int:");
      printcodes.add(PUSH(LR));
      printcodes.add(MOV("r1", resultReg));
      printcodes.add(LDR_msg(resultReg, String.valueOf(stringCounter)));
      printcodes.add(ADD(resultReg, resultReg, "#4"));
      printcodes.add(BL("printf"));
      printcodes.add(MOV(resultReg, 0));
      printcodes.add(BL("fflush"));
      printcodes.add(POP("pc"));
      visitStringNode(new StringNode("\"%d\\0\""));
    }

    if (printBool) {
      printcodes.add("p_print_bool:");
      printcodes.add(PUSH(LR));
      printcodes.add(CMP_value(resultReg, 0));
      printcodes.add(LDRNE_msg(resultReg , String.valueOf(stringCounter)));
      visitStringNode(new StringNode("\"true\\0\""));
      printcodes.add(LDREQ_msg(resultReg, String.valueOf(stringCounter)));
      visitStringNode(new StringNode("\"false\\0\""));
      printcodes.add(ADD(resultReg, resultReg, 4));
      printcodes.add(BL("printf"));
      printcodes.add(MOV(resultReg, 0));
      printcodes.add(BL("fflush"));
      printcodes.add(POP(PC));
    }

    if (printReference) {
      printcodes.add("p_print_reference:");
      printcodes.add(PUSH(LR));
      printcodes.add(MOV("r1", resultReg));
      printcodes.add(LDR_msg(resultReg, String.valueOf(stringCounter)));
      printcodes.add(ADD(resultReg, resultReg, 4));
      printcodes.add(BL("printf"));
      printcodes.add(MOV(resultReg, 0));
      printcodes.add(BL("fflush"));
      printcodes.add(POP(PC));
      visitStringNode(new StringNode("\"%p\\0\""));
    }

    if (println) {
      printcodes.add("p_print_ln:");
      printcodes.add(PUSH(LR));
      printcodes.add(LDR_msg(resultReg, String.valueOf(stringCounter)));
      printcodes.add(ADD(resultReg, resultReg, "#4"));
      printcodes.add(BL("puts"));
      printcodes.add(MOV(resultReg, 0));
      printcodes.add(BL("fflush"));
      printcodes.add(POP(PC));
      visitStringNode(new StringNode("\"\\0\""));
    }

    if(printstring) {
      printcodes.add("p_print_string:");
      printcodes.add(PUSH(LR));
      printcodes.add(LDR_reg(reg_add(), resultReg));
      printcodes.add(ADD(reg_add(), resultReg, 4));
      printcodes.add(LDR_msg(resultReg, String.valueOf(stringCounter)));
      printcodes.add(ADD(resultReg, resultReg, 4));
      printcodes.add(BL("printf"));
      printcodes.add(MOV(resultReg, 0));
      printcodes.add(BL("fflush"));
      printcodes.add(POP(PC));
      visitStringNode(new StringNode("\"%.*s\\0\""));
    }

    if (variables.size() > 0) {
      variables.add(0, ".data\n");
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
    main.add("\n.text\n");
    main.add(".global main");

    ProgramAST past= (ProgramAST) ast;
    int k = 4;
    for (FuncAST f: past.getFunctions()) {
//      int temp = spPosition;
//      in_func = true;
      SymbolTable symbolTabletemp = symbolTable;
      symbolTable = f.getSymbolTable();

      for (ParamNode p: f.getParameters()) {
        visitParamNode(p);
      }
      functionParams.put(f.getFuncName(), symbolTable.getParamCounter());
//      visitParamFst(f, main, k);
//      in_func = false;
//      symbolTable.local_variable = 0;
      symbolTable = symbolTabletemp;
    }

    for (FuncAST f: past.getFunctions()) {
      in_func = true;
      visitFuncAST(f, main, k);
      in_func = false;
      symbolTable.local_variable = 0;
      spPosition = 0;
    }

    main.add("main:");
    main.add(PUSH(LR));
    visitStat(past.getMainProgram(), main, k);
  }

//  private void visitParamFst(FuncAST ast, List<String> codes, int reg_counter){
//    SymbolTable temp = symbolTable;
//    symbolTable = ast.getSymbolTable();
//
//    spPosition+=4;
//    for (ParamNode p: ast.getParameters()) {
//      visitParamNode(p);
//    }
//    functionParams.put(ast.getFuncName(), symbolTable.getParamCounter());
//    symbolTable = temp;
//    spPosition -= 4;
//  }

  public void visitStat(AST ast, List<String> codes, int reg_counter) {
    if (ast instanceof SkipAst) {
      visitSkipAst(ast);
    } else if (ast instanceof ExitAst) {
      visitExitAst((ExitAst)ast, codes, reg_counter);
    } else if (ast instanceof DeclarationAst) {
      visitDeclaration((DeclarationAst) ast, codes, reg_counter);
    } else if (ast instanceof SeqStateAst) {
      for (AST ast1:((SeqStateAst) ast).getSeqs()) {
        visitStat(ast1, codes, reg_counter);
      }
    } else if (ast instanceof AssignAST) {
      visitAssignAst((AssignAST)ast, codes, reg_counter);
    } else if (ast instanceof PrintAst) {
      visitPrintAst((PrintAst)ast, codes, reg_counter);
    } else if (ast instanceof PrintlnAst) {
      PrintlnAst print_ast = (PrintlnAst)ast;
      visitPrintAst(new PrintAst(((PrintlnAst) ast).getExpr()), codes, reg_counter);
      visitPrintlnAst(print_ast, codes, reg_counter);
    } else if (ast instanceof ReadAst) {
      visitReadAST((ReadAst) ast, codes, reg_counter);
    } else if (ast instanceof IfAst) {
      visitIfAst((IfAst)ast, codes, reg_counter);
    } else if (ast instanceof WhileAst) {
      visitWhileAST((WhileAst) ast, codes, reg_counter);
    } else if (ast instanceof ReturnAst) {
      visitReturnAST((ReturnAst) ast, codes, reg_counter);
    } else if (ast instanceof FreeAst) {
      visitFreeAST((FreeAst) ast, codes, reg_counter);
    } else if (ast instanceof BlockAst) {
      SymbolTable temp = symbolTable;
      symbolTable = ((BlockAst) ast).getSymbolTable();
      for (AST ast1:((BlockAst) ast).getStats()) {
        visitStat(ast1, codes, reg_counter);
      }
      symbolTable = temp;
    }
  }

  public void visitFuncAST(FuncAST ast, List<String> codes, int reg_counter) {
    SymbolTable symbolTabletemp = symbolTable;
    symbolTable = ast.getSymbolTable();
    spPosition += symbolTable.getParamCounter();

//    spPosition+=4;
//    for (ParamNode p: ast.getParameters()) {
//      Type type = p.getType();
//      if (type.equals(intType()) || type.equals(boolType()) || type instanceof ArrayType) {
//        spPosition += 4;
//      } else {
//        spPosition += 1;
//      }
//
//    }
//    functionParams.put(ast.getFuncName(), symbolTable.getParamCounter());

    codes.add("f_" + ast.getFuncName() + ":");
    codes.add(PUSH(LR));
    visitStat(ast.getFunctionBody(), codes, reg_counter);
    if (!(ast.getFunctionBody() instanceof SkipAst)) {
      if (symbolTable.local_variable != 0) {
        codes.add("\tADD sp, sp, #" + symbolTable.local_variable);
      }
      if (return_Pop) {
        codes.add(POP(PC));
        return_Pop = false;
      }
      codes.add(POP(PC));
      codes.add("\t.ltorg");
    }

    symbolTable = symbolTabletemp;
    // TODO: 18/02/2020 saveReg restoreReg
//    restoreReg();
  }

  public void visitParamNode(ParamNode param) {
    Type type = param.getType();
    if (type.equals(intType()) || type.equals(boolType()) || type instanceof ArrayType) {
      symbolTable.setParamCounter(symbolTable.getParamCounter() + 4);
//      spPosition += 4;
    } else {
      symbolTable.setParamCounter(symbolTable.getParamCounter() + 1);
//      spPosition += 1;
    }
    symbolTable.putStackTable(param.getName(), symbolTable.getParamCounter());
  }

  public void visitAssignAst(AssignAST ast, List<String> codes, int reg_counter) {
    String strcommand = "\tSTR ";
    Type type;
    if (ast.getLhs().isPair()) {
      type = symbolTable.getVariable(ast.getLhs().getLhsContext().pair_elem().expr().getText());
    } else if (ast.getLhs().isArray()) {
      type = symbolTable.getVariable(ast.getLhs().getLhsContext().array_elem().IDENT().getText());
    } else {
      type = symbolTable.getVariable(ast.getLhs().getLhsContext().getText());
    }

    if(ast.getRhs().getArrayAST() != null) {
      visitArrayLiter((ArrayType)type, ast.getRhs(), codes, reg_counter);
    } else if (ast.getRhs().call()) {
      visitCallAst(ast.getRhs().getCallAST(), codes, reg_counter);
    }

    visitExprAST(ast.getRhs().getExpr1(), codes, reg_counter);

    // assign a pair from a null content.
    if (ast.getRhs().getExpr1() instanceof PairAST) {
      if (((PairAST) ast.getRhs().getExpr1()).ident.equals("null")) {
        codes.add(LDR_value(paramReg, 0));
      }
    } else if (type instanceof PairType) {
      // calculating shifting in stack
      if (ast.getLhs().getLhsContext().pair_elem()!=null) {
        int x = symbolTable.getStackTable(ast.getLhs().getLhsContext().pair_elem().expr().getText());
        x = x != -1 ? spPosition - x : 0;
        if (x != 0) {
          codes.add(LDR_reg("r5", SP + ", #" + x));
        } else {
          codes.add(LDR_reg("r5", SP));
        }
        codes.add(MOV(resultReg, "r5"));
        codes.add(BL("p_check_null_pointer"));
        printCheckNullPointer = true;
        Type strType = type;
        if (ast.getLhs().getLhsContext().pair_elem() != null) {
          // lhs is pair elem
          if (ast.getLhs().getLhsContext().pair_elem().fst() != null) {
            strType = ((PairType) type).getLeftType();
            codes.add(LDR_reg("r5", "r5"));
          } else if (ast.getLhs().getLhsContext().pair_elem().snd() != null) {
            strType = ((PairType) type).getRightType();
            if (((PairType) type).getLeftType().equals(boolType()) || ((PairType) type).getLeftType().equals(charType())) {
              codes.add(LDR_reg("r5", "r5, #1"));
            } else {
              codes.add(LDR_reg("r5", "r5, #4"));
            }
          }
        }
        if (strType.equals(boolType()) || strType.equals(charType())) {
          codes.add(STRB("r4", "[r5]"));
        } else {
          codes.add(STR("r4", "[r5]"));
        }
      } else if (ast.getRhs().getRhsContext().pair_elem()!=null) {
        // rhs is pair elem
        int x = symbolTable.getStackTable(ast.getRhs().getRhsContext().stop.getText());
        x = x != -1 ? spPosition - x : 0;
        if (x != 0) {
          codes.add(LDR_reg("r" + reg_counter, SP + ", #" + x));
        } else {
          codes.add(LDR_reg("r" + reg_counter, SP));
        }
        codes.add(MOV(resultReg, paramReg));
        codes.add(BL("p_check_null_pointer"));
        printCheckNullPointer = true;
        codes.add(LDR_reg(paramReg, paramReg + ", #4"));
        codes.add(LDR_reg(paramReg, paramReg));
      }
    }

    if (type.equals(boolType()) || type.equals(charType())) {
      strcommand = "\tSTRB ";
      if (ast.getRhs().getPairElemNode() != null) {
        int x = symbolTable.getStackTable(ast.getRhs().getPairElemNode().getName());
        x = x != -1 ? spPosition - x : 0;
        if (x != 0) {
          codes.add(LDR_reg(paramReg, SP + ", #" + x));
        } else {
          codes.add(LDR_reg(paramReg, SP));
        }
        codes.add(MOV(resultReg, paramReg));
        codes.add(BL("p_check_null_pointer"));
        printCheckNullPointer = true;
        int oldX = x;
        x = symbolTable.getStackTable(ast.getLhs().getLhsContext().getText());
        x = x != -1 ? x - oldX : 0;
        if (x != 0) {
          codes.add(LDR_reg(paramReg, paramReg + ", #" + x));
        } else {
          codes.add(LDR_reg(paramReg, paramReg));
        }
        codes.add(LDRSB(paramReg, paramReg));
      }
    }

    if (type.equals(intType())) {
      if (ast.getRhs().getPairElemNode() != null) {
        int x = symbolTable.getStackTable(ast.getRhs().getPairElemNode().getName());
        x = x != -1 ? spPosition - x : 0;
        if (x != 0) {
          codes.add(LDR_reg("r" + reg_counter, SP + ", #" + x));
        } else {
          codes.add(LDR_reg("r" + reg_counter, SP));
        }
        codes.add(MOV(resultReg, paramReg));
        codes.add(BL("p_check_null_pointer"));
        printCheckNullPointer = true;
        x = symbolTable.getStackTable(ast.getLhs().getLhsContext().getText());
        x = x != -1 ? spPosition - x : 0;
        if (x != 0) {
          codes.add(LDR_reg(paramReg, paramReg + ", #" + x));
        } else {
          codes.add(LDR_reg(paramReg, paramReg));
        }
        codes.add(LDR_reg(paramReg, paramReg));
      }
    }

    int x = symbolTable.getStackTable(ast.getLhs().getLhsContext().getText());

    if (in_func && x <= symbolTable.getParamCounter()) {
      codes.add(strcommand + " r" + reg_counter + ", [sp, #" + (x + symbolTable.getLocal_variable()) + "]");
    } else {
      if (spPosition - x == 0) {
        codes.add(strcommand + "r" + reg_counter + ", [sp]");
      } else {
        if (!ast.getLhs().isArray()) {
          if (x != -1) {
            codes.add(strcommand + "r" + reg_counter + ", [sp, #" + (spPosition - x) + "]");
          }
        }
      }
    }

    if (ast.getLhs().isArray()) {
      visitExprAST(ast.getLhs().getArrayElem(), codes, reg_counter + 1);
      while (type instanceof ArrayType) {
        type = ((ArrayType) type).getType();
      }
      if (type.equals(boolType()) || type.equals(charType())) {
        strcommand = "\tSTRB ";
      }
      codes.add(strcommand + paramReg + ", [r" + (reg_counter + 1) + "]");
//      if (ast.getRhs().call()) {
//        if (local_variable != 0) {
//          codes.add("\tADD sp, sp, #" + local_variable);
//        }
//      }
      printCheckArrayBound = true;
    } else {
//      codes.add("\t" + strcommand + paramReg + ", [sp, #" + (spPosition - symbolTable.getStackTable(ast.getLhs().getLhsContext().getText())) + "]");
    }
  }

  public void visitExitAst(ExitAst ast, List<String> codes, int reg_counter) {
    visitExprAST(ast.getExpr(),codes, reg_counter);
    codes.add(MOV(resultReg, paramReg));
    codes.add(BL("exit"));
  }

  public boolean visitExprAST(AST ast, List<String> codes, int reg_counter) {
//    if (ast == null) {
//      // use null pair situation
//      codes.add(LDR_reg(paramReg, SP));
//      codes.add(MOV(resultReg, paramReg));
//      codes.add(BL("p_check_null_pointer"));
//      printCheckNullPointer = true;
//      codes.add(LDR_reg(paramReg, paramReg));
//      codes.add(LDR_reg(paramReg, paramReg));
//    }
    if (ast instanceof IntNode) {
      IntNode int_ast = (IntNode)ast;
      codes.add(LDR_value(("r" + reg_counter), int_ast.getValue()));
      return true;
    } else if (ast instanceof BoolNode) {
      BoolNode bool_ast = (BoolNode) ast;
      codes.add(MOV("r" + reg_counter, bool_ast.getBoolValue()));
      return true;
    } else if (ast instanceof IdentNode) {
      int x = symbolTable.getStackTable(((IdentNode)ast).getIdent());
      Type type = symbolTable.getVariable(((IdentNode) ast).getIdent());
      String loadWord = "\tLDR";
      if (type.equals(boolType()) || type.equals(charType())) {
        loadWord = "\tLDRSB";
      }
//      if (in_func) {
//        codes.add(loadWord +" r" + reg_counter + ", [sp, #" + (x - symbolTable.local_variable) + "]");
//      } else {
      System.out.println("spPsist" + spPosition);
      System.out.println("pc" + symbolTable.getParamCounter());
      System.out.println("local" + symbolTable.getLocal_variable());
      System.out.println(((IdentNode) ast).getIdent() + x);


      if (in_func && x <= symbolTable.getParamCounter()) {
        codes.add(loadWord + " r" + reg_counter + ", [sp, #" + (spPosition - symbolTable.getParamCounter() + x ) + "]");
      } else {
        if (spPosition - x == 0) {
          codes.add(loadWord + " r" + reg_counter + ", [sp]");
        } else {
          codes.add(loadWord + " r" + reg_counter + ", [sp, #" + (spPosition - x) + "]");
        }
//      }
      }
      return true;
    } else if (ast instanceof StringNode) {
      codes.add(LDR_msg("r" + reg_counter, String.valueOf(stringCounter)));
      visitStringNode((StringNode)ast);
      return true;
    } else if (ast instanceof CharNode) {
      codes.add(MOV("r" + reg_counter,"#'" + ((CharNode) ast).getCharValue() + "'"));
      return true;
    } else if (ast instanceof Binary_BoolOpNode) {
      int r1 = reg_counter;
      int r2 = reg_counter + 1;
      visitExprAST(((Binary_BoolOpNode) ast).getExpr1(), codes, r1);
      visitExprAST(((Binary_BoolOpNode) ast).getExpr2(), codes, r2);
      if (!((Binary_BoolOpNode) ast).isBinaryAnd() && !((Binary_BoolOpNode) ast).isBinaryOr()) {
        codes.add(CMP_reg("r" + reg_counter, "r" + (reg_counter + 1)));
      }
      if (((Binary_BoolOpNode) ast).isEqual()) {
        codes.add(MOVEQ("r" + reg_counter, 1));
        codes.add(MOVNE("r" + reg_counter, 0));
      } else if (((Binary_BoolOpNode) ast).isNotEqual()) {
        codes.add(MOVNE("r" + reg_counter, 1));
        codes.add(MOVEQ("r" + reg_counter, 0));
      } else if (((Binary_BoolOpNode) ast).isGreater()) {
        codes.add(MOVGT("r" + reg_counter, 1));
        codes.add(MOVLE("r" + reg_counter, 0));
      } else if (((Binary_BoolOpNode) ast).isGreaterOrEqual()) {
        codes.add(MOVGE("r" + reg_counter, 1));
        codes.add(MOVLT("r" + reg_counter, 0));
      } else if (((Binary_BoolOpNode) ast).isSmaller()) {
        codes.add(MOVLT("r" + reg_counter, 1));
        codes.add(MOVGE("r" + reg_counter, 0));
      } else if (((Binary_BoolOpNode) ast).isSmallerOrEqual()) {
        codes.add(MOVLE("r" + reg_counter, 1));
        codes.add(MOVGT("r" + reg_counter, 0));
      } else if (((Binary_BoolOpNode) ast).isBinaryAnd()) {
        codes.add("\tAND r" + r1 + ", r" + r1 + ", r" + r2);// still has other operators
      } else if (((Binary_BoolOpNode) ast).isBinaryOr()) {
        codes.add("\tORR r" + r1 + ", r" + r1 + ", r" + r2);
      }
    } else if (ast instanceof BinaryOpNode) {
      int r1 = reg_counter;
      int r2 = reg_counter + 1;
      if (mallocCounter != 0) {
        r2 = reg_counter + 1 + mallocCounter;
      }
      visitExprAST(((BinaryOpNode) ast).getExpr1(), codes, r1);
      if (r2 > 10) {
        codes.add(PUSH("r10"));
        r2 = regMax;
        pushCounter++;
      }
      visitExprAST(((BinaryOpNode) ast).getExpr2(), codes, r2);

      if (pushCounter > 0) {
        codes.add(POP("r11"));
        pushCounter--;
        r2 = 11;
      }
      if (((BinaryOpNode) ast).isDivid() || ((BinaryOpNode) ast).isMod()) {
        codes.add(MOV("r0", "r" + r1));
        codes.add(MOV("r1", "r" + r2));
        codes.add(BL("p_check_divide_by_zero"));
        if (((BinaryOpNode) ast).isDivid()) {
          codes.add(BL("__aeabi_idiv"));
          codes.add(MOV("r" + reg_counter, resultReg));
        } else {
          codes.add(BL("__aeabi_idivmod"));
          codes.add(MOV("r" + r1, "r1"));
        }
        printDivideByZeroError = true;
      } else {
        if (((BinaryOpNode) ast).isPlus()) {
          codes.add("\tADDS r" + r1 + ", r" + r1 + ", r" + r2);
        } else if (((BinaryOpNode) ast).isMinus()) {
          codes.add(SUBS("r" + r1, "r" + r1, "r" + r2));
        } else if (((BinaryOpNode) ast).isTime()) {
          codes.add("\tSMULL r" + r1 + ", r" + r2 + ", r" + r1 + ", r" + r2);
          codes.add("\tCMP r" + r2 + ", r" + r1 + ", ASR #31");
        }

        if (((BinaryOpNode) ast).isTime()) {
          codes.add("\tBLNE p_throw_overflow_error");

        } else {
          codes.add("\tBLVS p_throw_overflow_error");
        }
        printOverflowError = true;
      }
    } else if (ast instanceof UnaryOpNode) {
      visitExprAST(((UnaryOpNode) ast).getExpr(), codes, reg_counter);
      if (((UnaryOpNode) ast).isNOT()) {
        codes.add("\tEOR r" + reg_counter + ", r" + reg_counter + ", #1");
      } else if (((UnaryOpNode) ast).isMinus()) {
        codes.add("\tRSBS r" + reg_counter + ", r" + reg_counter + ", #0");
        codes.add("\tBLVS p_throw_overflow_error");
        printOverflowError = true;
      } else if (((UnaryOpNode) ast).isLen()) {
        codes.add(LDR_reg(paramReg, "r" + reg_counter));
      }
    } else if (ast instanceof CallAST) {
      visitCallAst((CallAST)ast, codes, reg_counter);
    } else if (ast instanceof ArrayElemNode) {
      int arrayIndexReg = reg_counter + mallocCounter;   /////////////////////////////
      if (((ArrayElemNode) ast).getExprs().get(0) instanceof IdentNode) {
        codes.add(ADD(("r" + reg_counter), SP, 4));   //////////
      } else {
        codes.add(ADD(new Register(reg_counter), new Register(SP), 0));   //////////
      }
      for (int i = 0; i < ((ArrayElemNode) ast).getExprs().size(); i++) {
        visitExprAST(((ArrayElemNode) ast).getExprs().get(i), codes, arrayIndexReg);
        codes.add(LDR_reg("r" + reg_counter, "r" + reg_counter));
        codes.add(MOV(resultReg, "r" + arrayIndexReg));
        codes.add(MOV("r1", "r" + reg_counter));
        codes.add(BL("p_check_array_bounds"));
        codes.add(ADD(new Register(reg_counter), new Register(reg_counter), 4));
        Type type = symbolTable.getVariable(((ArrayElemNode) ast).getName());
        ArrayType arrayType = (ArrayType)type;
        if (arrayType.getType().equals(charType()) || arrayType.getType().equals(boolType())) {
          codes.add(ADD(new Register(reg_counter), new Register(reg_counter), new Register("r" + arrayIndexReg)));
        } else {
          codes.add("\tADD r" + reg_counter + ", r" + reg_counter + ", r" + arrayIndexReg + ", LSL #2");
        }
      }
      printCheckArrayBound = true;
    } else if (ast instanceof ExprWithParen) {
      visitExprAST(((ExprWithParen) ast).getExpr(), codes, reg_counter);
    } else if (ast instanceof PairAST) {
      if (((PairAST) ast).ident.equals("null")) {
        codes.add(LDR_value("r" + reg_counter, 0));
      }
    }
    return false;
  }

  private void visitCallAst(CallAST ast, List<String> codes, int reg_counter) {
    int arg_count = 0;
    if (ast.hasArgument()) {
      for(int i = ast.getArguments().size() - 1; i >= 0; i--) {
        AST argument = ast.getArguments().get(i);
        visitExprAST(argument, codes, reg_counter);
        Type type = null;

        if (argument instanceof IdentNode) {
          type = symbolTable.getVariable(((IdentNode) argument).getIdent());
          if (type.equals(charType()) || type.equals(boolType())) {
            codes.add(STRB(paramReg, "[sp, #-1]!"));
            arg_count += 1;
            spPosition += 1;
          } else {
            codes.add(STR(paramReg, "[sp, #-4]!"));
            arg_count += 4;
            spPosition += 4;
          }
        } else if (argument instanceof CharNode || argument instanceof BoolNode)  {
          codes.add(STRB(paramReg, "[sp, #-1]!"));
          arg_count += 1;
          spPosition += 1;
        }  else {
          codes.add(STR(paramReg, "[sp, #-4]!"));
          arg_count += 4;
          spPosition += 4;
        }
      }

    }
    codes.add(BL("f_" + ast.getFuncName()));
    codes.add(ADD(SP, SP, arg_count));
    spPosition -= arg_count;
    codes.add(MOV(paramReg, resultReg));

  }

  public void visitArrayLiter(ArrayType arrayType, AssignRHSAST ast, List<String> codes, int reg_counter) {
    int array_size = ast.getArrayAST().getSize();
    String strWord = "\tSTR ";

    if (arrayType.getType().equals(charType()) || arrayType.getType().equals(boolType())) {
      codes.add(LDR_value(resultReg, (4 + array_size)));
      strWord = "\tSTRB ";

    } else {
      codes.add(LDR_value(resultReg, (4 + 4 * array_size)));
    }
    codes.add(BL("malloc"));
    mallocCounter++;
    codes.add(MOV(paramReg, resultReg));
    int array_counter = 0;
    int array_reg = reg_counter + mallocCounter;
    for (AST a : ast.getArrayAST().getExprs()) {
      visitExprAST(a, codes, array_reg);
      if (arrayType.getType().equals(charType()) || arrayType.getType().equals(boolType())) {
        codes.add(strWord + "r" + array_reg + ", [r" + reg_counter + ", #" + (4 + array_counter++) + "]");
      } else {
        codes.add(strWord + "r" + array_reg + ", [r" + reg_counter + ", #" + (4 + (array_counter++ * 4)) + "]");
      }
    }
    codes.add(LDR_value("r" + array_reg, array_size));
    codes.add(STR( "r" + array_reg, "[r" + reg_counter + "]"));
  }


  public void visitDeclaration(DeclarationAst ast, List<String> codes, int reg_counter) {
    AssignRHSAST rhs = ast.getAssignRhsAST();
    Type type = ast.getType();
    String strWord = "\tSTR ";

    if (rhs.getArrayAST() != null) {
      //array declaration
      codes.add(SUB(SP, SP, 4));
      if (in_func) {
        symbolTable.local_variable += 4;
      }
      spPosition += 4;
      ArrayType arrayType = (ArrayType)type;
      visitArrayLiter(arrayType, ast.getAssignRhsAST(), codes, reg_counter);
    } else if (type.equals(stringType()) || type.equals(intType())) {
      codes.add(SUB(SP, SP, 4));
      if (in_func) {
        symbolTable.local_variable += 4;
      }
      spPosition += 4;
    } else if (type.equals(boolType()) || type.equals(charType())) {
      codes.add(SUB(SP, SP, 1));
      if (in_func) {
        symbolTable.local_variable += 1;
      }
      spPosition += 1;
      strWord = "\tSTRB ";
    } else if (ast.getAssignRhsAST().getRhsContext().expr().size() > 0) {
      //pair declaration (not pairElemNode)
      codes.add(SUB(SP, SP, 4));
      spPosition += 4;
      if (in_func) {
        symbolTable.local_variable += 4;
        System.out.println("$$$$$" + symbolTable.getLocal_variable());
      }
      if (ast.getAssignRhsAST().getRhsContext().expr().size() == 1) {
        if (!(ast.getAssignRhsAST().getExpr1() instanceof IdentNode)) {
          // rhs is null
          codes.add(LDR_value(paramReg, 0));
        }
        // do nothing here if rhs is a declared pair
      } else {
        // rhs is a new pair
        codes.add(LDR_value(resultReg, 8));
        codes.add(BL("malloc"));
        codes.add(MOV("r" + reg_counter, resultReg));
        reg_counter++;
        visitExprAST(ast.getAssignRhsAST().getExpr1(), codes, reg_counter);
        Type lType = null;
        Type rType = null;
        if (type instanceof PairType) {
          lType = ((PairType) type).getLeftType();
          rType = ((PairType) type).getRightType();
        }
        if (lType instanceof PairType) {
          if (((PairType) lType).getLeftType() == null) {
            codes.add(LDR_value("r" + reg_counter, 0));
          }
        }
        int size = lType.equals(Type.charType()) ? 1 : 4;
        String b = lType.equals(Type.charType()) ? "B" : "";
        codes.add(LDR_value(resultReg, size));
        codes.add(BL("malloc"));
        codes.add(b != "" ? STRB("r5", "[" + resultReg + "]") : STR("r5", "[" + resultReg + "]"));
        codes.add(STR(resultReg, "[r4]"));
        int codesLength = codes.size();
        visitExprAST(ast.getAssignRhsAST().getExpr2(), codes, reg_counter);
        size = rType.equals(Type.charType()) ? 1 : 4;
        b = rType.equals(Type.charType()) ? "B" : "";
        if (codes.size()-codesLength==0) {
          if (rType instanceof PairType) {
            if (((PairType) rType).getLeftType() == null) {
              codes.add(LDR_value("r" + reg_counter, 0));
            }
          }
        }
        codes.add(LDR_value(resultReg, size));
        codes.add(BL("malloc"));
        codes.add(b != "" ? STRB("r5", "[" + resultReg + "]") : STR("r5", "[" + resultReg + "]"));
        codes.add(STR(resultReg, "[" + paramReg + ", #4]"));
      }
    } else if (ast.getAssignRhsAST().getPairElemNode() != null) {
      // pair declaration (pairElemNode)
      if (type instanceof PairType) {
        codes.add(SUB(SP, SP, 4));
        spPosition += 4;
        if (in_func) {
          symbolTable.local_variable += 4;
        }
      }
    }

    if (ast.getAssignRhsAST().call()) {
      visitCallAst(ast.getAssignRhsAST().getCallAST(), codes, reg_counter);
    } else if (ast.getAssignRhsAST().getRhsContext().expr().size()<=1) {
      if (ast.getAssignRhsAST().getExpr1()!=null) {
        // rhs is a null or all other cases
        visitExprAST(ast.getAssignRhsAST().getExpr1(), codes, reg_counter);
      } else if (ast.getAssignRhsAST().getPairElemNode() != null) {
        // rhs is a declared pair or pair elem
        int x = symbolTable.getStackTable(ast.getAssignRhsAST().getPairElemNode().getName());
        x = x != -1 ? spPosition - x : 0;
        if (x != 0) {
          codes.add(LDR_reg(paramReg, SP + ", #" + x));
        } else {
          codes.add(LDR_reg(paramReg, SP));
        }
        codes.add(MOV(resultReg, paramReg));
        codes.add(BL("p_check_null_pointer"));
        printCheckNullPointer = true;
        if (ast.getAssignRhsAST().getRhsContext().pair_elem().fst() != null) {
          codes.add(LDR_reg(paramReg, paramReg));
        } else {
          Type t = symbolTable.getVariable(ast.getAssignRhsAST().getPairElemNode().getName());
          t = ((PairType) t).getLeftType();
          if (t.equals(charType()) || t.equals(boolType())) {
            codes.add(LDR_reg(paramReg, paramReg + ", #1"));
          } else {
            codes.add(LDR_reg(paramReg, paramReg + ", #4"));
          }
        }
        if (type.equals(charType())) {
          codes.add(LDRSB(paramReg, paramReg));
        } else {
          codes.add(LDR_reg(paramReg, paramReg));
        }
      }
    }

    codes.add(strWord + paramReg + ", [sp]");

    symbolTable.putStackTable(ast.getName(), spPosition);
  }

  private void visitReturnAST(ReturnAst ast, List<String> codes, int reg_counter) {
    visitExprAST(ast.getExpr(), codes, reg_counter);
    codes.add(MOV(resultReg, paramReg));
    return_Pop = true;
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
    visitExprAST(expr, codes, reg_counter);
    if (expr instanceof ArrayElemNode) {
      codes.add(LDR_reg("r" + reg_counter, "r" + reg_counter));
    }
    if (expr instanceof PairAST) {
      if (((PairAST) expr).ident.equals("null")) {
        codes.add(LDR_value(paramReg, 0));
      }
    }
    codes.add(MOV(resultReg, paramReg));
    Type type  = null;
    if (expr instanceof Binary_BoolOpNode || expr instanceof BoolNode ||
        (expr instanceof UnaryOpNode && ((UnaryOpNode) expr).isNOT())) {
      type = boolType();
    } else if (expr instanceof StringNode) {
      type = stringType();
    } else if (expr instanceof IntNode || expr instanceof BinaryOpNode ||
        (expr instanceof UnaryOpNode && ((UnaryOpNode) expr).returnInt())) {
      type = intType();
    }  else if (expr instanceof CharNode || (expr instanceof UnaryOpNode && ((UnaryOpNode) expr).isChr())) {
      type = charType();
    } else if (expr instanceof IdentNode) {
      type = symbolTable.getVariable(((IdentNode) expr).getIdent());
      if (type instanceof ArrayType) {
        if (((ArrayType) type).getType().equals(charType())) {
          type = stringType();
        } else {
          codes.add(BL("p_print_reference"));
          printReference = true;
        }
      } else if (type instanceof PairType) {
          codes.add(BL("p_print_reference"));
          printReference = true;
      }
    } else if (expr instanceof ArrayElemNode) {
      type = symbolTable.getVariable(((ArrayElemNode) expr).getName());
      while (type instanceof ArrayType) {
        type = ((ArrayType) type).getType();
      }
    }

    if (type != null) {
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

    if (expr instanceof PairAST) {
      codes.add(BL("p_print_reference"));
      printReference = true;
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
    if (ast.getLhs().getLhsContext().IDENT()==null) {
      codes.add(LDR_reg(paramReg, SP));
      codes.add(MOV(resultReg, paramReg));
      codes.add(BL("p_check_null_pointer"));
      printCheckNullPointer = true;
      codes.add(LDR_reg(paramReg, paramReg));
    } else {
      codes.add(ADD("r4", SP , (spPosition - symbolTable.getStackTable(ast.getLhs().getLhsContext().IDENT().getText()))));
    }
    codes.add(MOV(resultReg, paramReg));
    codes.add(BL("p_read_" + readType));
    if ((!read_int && readType.equals("int")) || (!read_char && readType.equals("char"))){
      variables.add("msg_" + stringCounter + ":");
      variables.add( "\t.word " + (readType.equals("char")?4:3));
      variables.add("\t.ascii  \"" + (readType.equals("char")?" %c":"%d") + "\\0\"");
      stringCounter++;
      printcodes.add("p_read_" + readType + ":");
    }

    if (type.equals(intType())) {
      read_int = true;
    } else if (type.equals(charType())) {
      read_char = true;
    }


    printcodes.add(PUSH(LR));
    printcodes.add(MOV("r1", resultReg));
    printcodes.add(LDR_msg(resultReg, String.valueOf((stringCounter-1))));
    printcodes.add(ADD(resultReg, resultReg, "#4"));
    printcodes.add(BL("scanf"));
    printcodes.add(POP(PC));
  }

  private void visitIfAst(IfAst ast, List<String> codes, int reg_counter) {
    SymbolTable symbolTabletemp = symbolTable;
    symbolTable = ast.getThenSymbolTable();
//    symbolTable.set_local_variable(symbolTabletemp.getLocal_variable());
    symbolTable.setParamCounter(symbolTabletemp.getParamCounter());
    List<String> elseBranch = new LinkedList<>();
    visitExprAST(ast.getExpr(), codes, reg_counter);
    AST expr =ast.getExpr();
    if (expr instanceof BoolNode || expr instanceof Binary_BoolOpNode ||
        (expr instanceof IdentNode && symbolTabletemp.getVariable(((IdentNode) expr).getIdent()).equals(boolType()))) {
      codes.add(CMP_value(paramReg, 0));
    } else {
      codes.add(CMP_reg("r" + (reg_counter - 1),"r" + reg_counter));
    }
    codes.add("\tBEQ L" + branchCounter);
    elseBranch.add("L" + branchCounter++ + ":");
    visitStat(ast.getThenbranch(), codes, reg_counter);

    if (symbolTable.local_variable > 0) {
      codes.add("\tADD sp, sp, #" + symbolTable.local_variable);
    }

    if (return_Pop) {
      codes.add(POP(PC));
      return_Pop = false;
    }
    symbolTable = ast.getElseSymbolTable();
//    symbolTable.set_local_variable(symbolTabletemp.getLocal_variable());
    symbolTable.setParamCounter(symbolTabletemp.getParamCounter());
    visitStat(ast.getElsebranch(), elseBranch, reg_counter);
    codes.add("\tB L" + branchCounter);
    for(String s: elseBranch) {
      codes.add(s);
    }

    if (symbolTable.local_variable > 0) {
      codes.add("\tADD sp, sp, #" + symbolTable.local_variable);
    }

    if (return_Pop) {
      codes.add(POP(PC));
      return_Pop = false;
    }
    codes.add("L" + branchCounter++ + ":");
    symbolTable = symbolTabletemp;
  }

  public void visitWhileAST(WhileAst ast, List<String> codes, int reg_counter) {
    SymbolTable symbolTabletemp = symbolTable;
    symbolTable = ast.getSymbolTable();
//    symbolTable.set_local_variable(symbolTabletemp.getLocal_variable());
    symbolTable.setParamCounter(symbolTabletemp.getParamCounter());
    int loopLabel = branchCounter++;
    int bodyLabel = branchCounter++;
    codes.add("\tB L" + loopLabel);
    codes.add("L" + bodyLabel + ":");
    visitStat(ast.getStat(), codes, reg_counter);
    codes.add("L" + loopLabel + ":");
    AST expr;
    if (ast.getExpr() instanceof ExprWithParen) {
      expr = ((ExprWithParen) ast.getExpr()).getExpr();
    } else {
      expr = ast.getExpr();
    }
    visitExprAST(expr, codes, reg_counter);
    if (expr instanceof BoolNode || expr instanceof Binary_BoolOpNode || expr instanceof IdentNode) {
      codes.add(CMP_value(paramReg,1));
    } else {
      codes.add(CMP_reg(paramReg, "r" + reg_counter));
    }

    codes.add("\tBEQ L" + bodyLabel);
    symbolTable = symbolTabletemp;
  }

  public void visitFreeAST(FreeAst ast, List<String> codes, int reg_counter) {
    codes.add(LDR_reg(paramReg, SP));
    codes.add(MOV(resultReg, paramReg));
    codes.add(BL("p_free_pair"));
    printFree = true;
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

  public String SUBS(String dst, String src, String src2) {
    return "\tSUBS " + dst + ", " + src + ", " + src2;
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
    return "\tLDRSB " + dst + ", [" +  src + "]";
  }

  public String LDRNE_msg(String dst, String content) {
    return "\tLDRNE " + dst + ", =msg_" + content;
  }

  public String LDREQ_msg(String dst, String content) {
    return "\tLDREQ " + dst + ", =msg_" + content;
  }

  public String LDRLT_msg(String dst, String content) {
    return "\tLDRLT " + dst + ", =msg_" + content;
  }

  public String LDRCS_msg(String dst, String content) {
    return "\tLDRCS " + dst + ", =msg_" + content;
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

  public String MOV(String dst, int src) {
    return "\tMOV " + dst + ", #" + src;
  }

  public String CMP_value(String dst, int value) {
    return "\tCMP " + dst + ", #" + value;
  }

  public String CMP_reg(String dst, String reg) {
    return "\tCMP " + dst + ", " + reg;
  }

  public String MOVEQ(String dst, int value) {
    return "\tMOVEQ " + dst + ", #" + value;
  }

  public String MOVNE(String dst, int value) {
    return "\tMOVNE " + dst + ", #" + value;
  }

  public String MOVGT(String dst, int value) {
    return "\tMOVGT " + dst + ", #" + value;
  }

  public String MOVLT(String dst, int value) {
    return "\tMOVLT " + dst + ", #" + value;
  }

  public String MOVGE(String dst, int value) {
    return "\tMOVGE " + dst + ", #" + value;
  }

  public String MOVLE(String dst, int value) {
    return "\tMOVLE " + dst + ", #" + value;
  }

  public String ADD(String result, String a, String b) {
    return "\tADD " + result + ", " + a + ", " + b;
  }
  public String ADD(String result, String a, int b) {
    return "\tADD " + result + ", " + a + ", #" + b;
  }

  public String ADD(Register result, Register a, Register b) {
    return "\tADD " + result + ", " + a + ", " + b;
  }
  public String ADD(Register result, Register a, int b) {
    return "\tADD " + result + ", " + a + ", #" + b;
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

  public String BLEQ(String target) {
    return "\tBLEQ " + target;
  }

  public String BLLT(String target) {
    return "\tBLLT " + target;
  }

  public String BLCS(String target) {
    return "\tBLCS " + target;
  }


//  public void saveReg(int size) {
//    SUB("sp", "sp", size);
//    LDR(paramReg);
//  }
}