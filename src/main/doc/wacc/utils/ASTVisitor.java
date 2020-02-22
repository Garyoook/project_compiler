package doc.wacc.utils;

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
  private final String resultReg = "r0";
  private final String paramReg = "r4";
  private final int regMax = 10;
  private int stringCounter = 0;
  private int spPosition = 0;
  private int branchCounter = 0;
  private int pushCounter = 0;
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

  public List<String> getcodes() {
    if (spPosition > 0) {
      while (spPosition > 1024){
        main.add("\tADD sp, sp, #1024");
        spPosition -= 1024;
      }
      main.add("\tADD sp, sp, #" + spPosition);
    }
    main.add("\tLDR r0, =0");
    main.add("\tPOP {pc}");
    main.add("\t.ltorg");

    if (printCheckArrayBound) {
      printcodes.add("p_check_array_bounds:");
      printcodes.add("\tPUSH {lr}");
      printcodes.add("\tCMP " + resultReg + ", #0");
      visitStringNode(new StringNode("ArrayIndexOutOfBoundsError: negative index\\n\\0"));
      printcodes.add("\tLDRLT " + resultReg + ", =msg_" + stringCounter);
      printcodes.add("\tBLLT p_throw_runtime_error");
      printcodes.add("\tLDR r1, [r1]");
      printcodes.add("\tCMP " + resultReg + ", r1");
      visitStringNode(new StringNode("ArrayIndexOutOfBoundsError: index too large\\n\\0"));
      printcodes.add("\tLDRCS " + resultReg + ", =msg_" + stringCounter);
      printcodes.add("\tBLCS p_throw_runtime_error");
      printRunTimeErr = true;
      printcodes.add("\tPOP {pc}");
      printcodes.add("\tPOP {pc}");
    }

    if (printFree) {
      printcodes.add("p_free_pair:");
      printcodes.add("\tPUSH {lr}");
      printcodes.add("\tLDREQ " + resultReg + ", =msg_" + stringCounter);
      visitStringNode(new StringNode("NullReferenceError: dereference a null reference\\n\\0"));
      printcodes.add("\tBEQ p_throw_runtime_error");
      printRunTimeErr = true;
      printcodes.add("\tPUSH {r0}");
      printcodes.add("\tLDR " + resultReg + ", [" + resultReg + "]");
      printcodes.add("\tBL free");
      printcodes.add("\tLDR " + resultReg + ", [sp]");
      printcodes.add("\tLDR " + resultReg + ", [" + resultReg + ", #4]");
      printcodes.add("\tBL free");
      printcodes.add("\tPOP {r0}");
      printcodes.add("\tBL free");
      printcodes.add("\tPOP {pc}");
    }

    if (printDivideByZeroError) {
      printcodes.add("p_check_divide_by_zero:");
      printcodes.add("\tPUSH {lr}");
      printcodes.add("\tCMP r1, #0");
      printcodes.add("\tLDREQ r0, =msg_" +stringCounter);
      printcodes.add("\tBLEQ p_throw_runtime_error");
      printRunTimeErr = true;
      printcodes.add("\tPOP {pc}");
      variables.add("msg_" + stringCounter + ":");
      variables.add( "\t.word 45");
      variables.add("\t.ascii \"DivideByZeroError: divide or modulo by zero\\n\\0\"");
      stringCounter++;
    }


    if (printOverflowError) {
      printcodes.add("p_throw_overflow_error:");
      printcodes.add("\tLDR " + resultReg + ", =msg_" + stringCounter);
      printcodes.add("\tBL p_throw_runtime_error");
      printRunTimeErr = true;
      variables.add("msg_" + stringCounter + ":");
      variables.add( "\t.word 82");
      variables.add("\t.ascii \"OverflowError: the result is too small/large to store in a 4-byte signed-integer.\\n\"");
      stringCounter++;
    }

    if (printRunTimeErr) {
      printcodes.add("p_throw_runtime_error:");
      printcodes.add("\tBL p_print_string");
      printstring = true;
      printcodes.add("\tMOV " + resultReg + ", #-1");
      printcodes.add("\tBL exit");
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
      printcodes.add("\tMOV " + "r1" + ", " + resultReg);
      printcodes.add("\tLDR " + resultReg + ", =msg_" + stringCounter);
      printcodes.add("\tADD " + resultReg + ", " + resultReg + ", #4");
      printcodes.add("\tBL printf");
      printcodes.add("\tMOV " + resultReg + ", #0");
      printcodes.add("\tBL fflush");
      printcodes.add("\tPOP {pc}");
      visitStringNode(new StringNode("\"%d\\0\""));
    }

    if (printReference) {
      printcodes.add("p_print_reference:");
      printcodes.add("\tPUSH {ld}");
      printcodes.add("\tMOV r1, " + resultReg);
      printcodes.add("\tLDR " + resultReg + ", =msg_" + stringCounter);
      visitStringNode(new StringNode("\"%p\\0\""));
      printcodes.add("\tADD " + resultReg + ", " + resultReg + ", #4");
      printcodes.add("\tBL printf");
      printcodes.add("\tMOV " + resultReg + ", #0");
      printcodes.add("\tBL fflush");
      printcodes.add("\tPOP {pc}");
    }

    if (printBool) {
      printcodes.add("p_print_bool:");
      printcodes.add("\tPUSH {lr}");
      printcodes.add("\tCMP " + resultReg + ", #0");
      printcodes.add("\tLDRNE " + resultReg + ", =msg_" + stringCounter);
      visitStringNode(new StringNode("\"true\\0\""));
      printcodes.add("\tLDREQ " + resultReg + ", =msg_" + stringCounter);
      visitStringNode(new StringNode("\"false\\0\""));
      printcodes.add("\tADD " + resultReg + ", " + resultReg + ", #4");
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
      visitFuncAST(f, main, k);
    }

    main.add("main:");
    main.add("\tPUSH {lr}");
    visitStat(past.getMainProgram(), main, k);
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
        visitStat(ast1, codes, reg_counter);
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
    } else if (ast instanceof FreeAst) {
      visitFreeAST((FreeAst) ast, codes, reg_counter);
    } else if (ast instanceof BlockAst) {
      SymbolTable temp = symbolTable;
      symbolTable = ((BlockAst) ast).getSymbolTable();
      for (AST ast1:((BlockAst) ast).getStats()) {
        visitStat(ast1, codes, reg_counter++);
      }
      symbolTable = temp;
    }
  }

  public void visitFuncAST(FuncAST ast, List<String> codes, int reg_counter) {

//    saveReg();
    SymbolTable symbolTabletemp = symbolTable;
    symbolTable = ast.getSymbolTable();

    codes.add("f_" + ast.getFuncName() + ":");
    codes.add("\tPUSH {lr}");
    visitStat(ast.getFunctionBody(), codes, reg_counter);
    codes.add("\tPOP {PC}");
    codes.add("\tPOP {PC}");
    codes.add("\t.ltorg");

    symbolTable = symbolTabletemp;
    // TODO: 18/02/2020 saveReg restoreReg
//    restoreReg();
  }

  public void visitAssignAst(AssignAST ast, List<String> codes, int reg_counter) {
    visitExprAST(ast.getRhs().getExpr1(), codes, reg_counter);
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
      codes.add("\t" + strcommand + "r" + reg_counter + ", [sp]");
    } else if (ast.getLhs().getLhsContext().array_elem() != null) {
      CompilerVisitor compilerVisitor = new CompilerVisitor();
      AST ast1 = compilerVisitor.visitArray_elem(ast.getLhs().getLhsContext().array_elem());
      codes.add("\tADD " + paramReg + ", sp, #0");
      if (((ArrayElemNode) ast1).getExpr() instanceof IntNode) {
        codes.add("\tLDR r5, =" + ((IntNode) ((ArrayElemNode) ast1).getExpr()).getValue());
      } else {
        visitExprAST(((ArrayElemNode) ast1).getExpr(), codes, reg_counter);
        codes.add("\tLDR r5, " + paramReg);
      }
      codes.add("\tLDR " + paramReg + ", [" + paramReg + "]");
      codes.add("\tMOV " + resultReg + ", r5");
      codes.add("\tMOV r1, " + paramReg);
      codes.add("\tBL p_check_array_bounds");
      codes.add("\tADD " + paramReg + ", " + paramReg + ", #4");
      codes.add("\tADD " + paramReg + ", " + paramReg + ", r5, LSL #2");
      codes.add("\tLDR " + paramReg + ", [" + paramReg + "]");
      printCheckArrayBound = true;
    } else {
//      codes.add("\t" + strcommand + paramReg + ", [sp, #" + (spPosition - symbolTable.getStackTable(ast.getLhs().getLhsContext().getText())) + "]");
    }
  }

  public void visitExitAst(ExitAst ast, List<String> codes, int reg_counter) {
    visitExprAST(ast.getExpr(),codes, reg_counter);
    codes.add("\tMOV " + resultReg + ", " + paramReg);
    codes.add("\tBL exit");
  }

  public boolean visitExprAST(AST ast, List<String> codes, int reg_counter) {
    if (ast instanceof IntNode) {
      IntNode int_ast = (IntNode)ast;
      codes.add("\tLDR r" + reg_counter + ", =" + int_ast.getValue());
      return true;
    } else if (ast instanceof BoolNode) {
      BoolNode bool_ast = (BoolNode) ast;
      codes.add("\tMOV r" + reg_counter + ", #" + bool_ast.getBoolValue());
      return true;
    } else if (ast instanceof IdentNode) {
      int x = symbolTable.getStackTable(((IdentNode)ast).getIdent());
      Type type = symbolTable.getVariable(((IdentNode) ast).getIdent());
      String loadWord = "\tLDR";
      if (type.equals(boolType()) || type.equals(charType())) {
        loadWord = "\tLDRSB";
      }
      if (spPosition - x == 0) {
        codes.add(loadWord + " r" + reg_counter + ", [sp]");
      } else {
        codes.add(loadWord +" r" + reg_counter + ", [sp, #" + (spPosition - x) + "]");
      }
      return true;
    } else if (ast instanceof StringNode) {
      codes.add("\tLDR r" + reg_counter + ", =msg_" + stringCounter);
      visitStringNode((StringNode)ast);
      return true;
    } else if (ast instanceof CharNode) {
      codes.add("\tMOV r" + reg_counter + ", #'" + ((CharNode) ast).getCharValue() + "'");
      return true;
    } else if (ast instanceof Binary_BoolOpNode) {
      int r1 = reg_counter;
      int r2 = reg_counter + 1;
      visitExprAST(((Binary_BoolOpNode) ast).getExpr1(), codes, r1);
      visitExprAST(((Binary_BoolOpNode) ast).getExpr2(), codes, r2);
      codes.add("\tCMP r" + reg_counter + ", r" + (reg_counter + 1));
      if (((Binary_BoolOpNode) ast).isEqual()) {
        codes.add("\tMOVEQ " + paramReg + ", #1");
        codes.add("\tMOVNE " + paramReg + ", #0");
      } else if (((Binary_BoolOpNode) ast).isNotEqual()) {
        codes.add("\tMOVNE " + paramReg + ", #1");
        codes.add("\tMOVEQ " + paramReg + ", #0");
      } else if (((Binary_BoolOpNode) ast).isGreater()) {
        codes.add("\tMOVGT " + paramReg + ", #1");
        codes.add("\tMOVLE " + paramReg + ", #0");
      } else if (((Binary_BoolOpNode) ast).isGreaterOrEqual()) {
        codes.add("\tMOVGE " + paramReg + ", #1");
        codes.add("\tMOVLT " + paramReg + ", #0");
      } else if (((Binary_BoolOpNode) ast).isSmaller()) {
        codes.add("\tMOVLT " + paramReg + ", #1");
        codes.add("\tMOVGE " + paramReg + ", #0");
      } else if (((Binary_BoolOpNode) ast).isSmallerOrEqual()) {
        codes.add("\tMOVLE " + paramReg + ", #1");
        codes.add("\tMOVGT " + paramReg + ", #0");
      } else if (((Binary_BoolOpNode) ast).isBinaryAnd()) {
        codes.add("\tAND r" + r1 + ", r" + r1 + ", r" + r2);// still has other operators
      } else if (((Binary_BoolOpNode) ast).isBinaryOr()) {
        codes.add("\tORR r" + r1 + ", r" + r1 + ", r" + r2);
      }
    } else if (ast instanceof BinaryOpNode) {
      int r1 = reg_counter;
      int r2 = (reg_counter + 1);
      visitExprAST(((BinaryOpNode) ast).getExpr1(), codes, r1);
      if (r2 > 10) {
        codes.add("\tPUSH {r10}");
        r2 = regMax;
        pushCounter++;
      }
      visitExprAST(((BinaryOpNode) ast).getExpr2(), codes, r2);

      if (pushCounter > 0) {
        codes.add("\tPOP {r11}");
        pushCounter--;
        r2 = 11;
      }
      if (((BinaryOpNode) ast).isDivid() || ((BinaryOpNode) ast).isMod()) {
        codes.add("\tMOV r0, r" + r1);
        codes.add("\tMOV r1, r" + r2);
        codes.add("\tBL p_check_divide_by_zero");
        if (((BinaryOpNode) ast).isDivid()) {
          codes.add("\tBL __aeabi_idiv");
        } else {
          codes.add("\tBL __aeabi_idivmod");
          codes.add("\tMOV r" + r1 + ", r1");
        }
        codes.add("\tMOV " + paramReg + ", " + resultReg);
        printDivideByZeroError = true;
      } else {
        if (((BinaryOpNode) ast).isPlus()) {
          codes.add("\tADDS r" + r1 + ", r" + r1 + ", r" + r2);
        } else if (((BinaryOpNode) ast).isMinus()) {
          codes.add("\tSUBS r" + r1 + ", r" + r1 + ", r" + r2);
        } else if (((BinaryOpNode) ast).isTime()) {
          codes.add("\tSMULL r" + r1 + ", r" + r2 + ", r" + r1 + ", r" + r2);
          codes.add("\tCMP r" + r2 + ", r" + r1 + ", ASR #31");
        }
        codes.add("\tBLVS p_throw_overflow_error");
        printOverflowError = true;
      }
    } else if (ast instanceof UnaryOpNode) {
      visitExprAST(((UnaryOpNode) ast).getExpr(), codes, reg_counter);
      if (((UnaryOpNode) ast).isNOT()) {
        codes.add("\tEOR r" + reg_counter + ", r" + reg_counter + ", #1");
      }
//      if (((UnaryOpNode) ast).getOperContext() != null) {
//        codes.add("\tLDR " + paramReg + ", [sp]");
//        codes.add("\tLDR " + paramReg + ", [" + paramReg + "]");
//      }
    } else if (ast instanceof CallAST) {
      visitCallAst((CallAST)ast, codes, reg_counter);
    } else if (ast instanceof ArrayElemNode) {
      codes.add("\tADD " + paramReg + ", sp, #0");
      if (((ArrayElemNode) ast).getExpr() instanceof IntNode) {
        codes.add("\tLDR r5, =" + ((IntNode) ((ArrayElemNode) ast).getExpr()).getValue());
      } else {
        visitExprAST(((ArrayElemNode) ast).getExpr(), codes, reg_counter);
        codes.add("\tLDR r5, " + paramReg);
      }
      codes.add("\tLDR " + paramReg + ", [" + paramReg + "]");
      codes.add("\tMOV " + resultReg + ", r5");
      codes.add("\tMOV r1, " + paramReg);
      codes.add("\tBL p_check_array_bounds");
      codes.add("\tADD " + paramReg + ", " + paramReg + ", #4");
      codes.add("\tADD " + paramReg + ", " + paramReg + ", r5, LSL #2");
      codes.add("\tLDR " + paramReg + ", [" + paramReg + "]");
      printCheckArrayBound = true;
    } else if (ast instanceof ExprWithParen) {
      visitExprAST(((ExprWithParen) ast).getExpr(), codes, reg_counter);
    }
    return false;
  }

  private void visitCallAst(CallAST ast, List<String> codes, int reg_counter) {
    if (ast.hasArgument()) {
      for (AST argument : ast.getArguments()) {
        visitExprAST(argument, codes, reg_counter++);
        if (argument instanceof CharNode || argument instanceof BoolNode)  {
          codes.add("\tSTRB " + paramReg + ", [sp, #-1]!");
        } else {
          codes.add("\tSTR " + paramReg + ", [sp, #-4]!");
        }
      }
    }
    codes.add("\tBL f_" + ast.getFuncName());
    codes.add("\tMOV " + paramReg + ", " + resultReg);
  }


  public void visitDeclaration(DeclarationAst ast, List<String> codes, int reg_counter) {
    AST expr = ast.getAssignRhsAST().getExpr1();
    Type type = ast.getType();
    String strWord = "\tSTR ";

    if (ast.getAssignRhsAST().getRhsContext().array_liter() != null) {
      codes.add("\tSUB sp, sp, #4");
      spPosition += 4;
      codes.add("\tLDR " + resultReg + ", =" + (4 + 4 * ast.getAssignRhsAST().getRhsContext().array_liter().expr().size()));
      codes.add("\tBL malloc");
      codes.add("\tMOV " + paramReg + ", " + resultReg);
      for (int i = 0; i < ast.getAssignRhsAST().getRhsContext().array_liter().expr().size(); i++) {
        CompilerVisitor compilerVisitor = new CompilerVisitor();
        visitExprAST(compilerVisitor.visitExpr(ast.getAssignRhsAST().getRhsContext().array_liter().expr(i)), codes, reg_counter);
        codes.add("\tSTR r5, [r4, #" + ((i + 1) * 4) + "]");
      }
      codes.add("\tLDR r5, =" + ast.getAssignRhsAST().getRhsContext().array_liter().expr().size());
      codes.add("\tSTR r5, [r4]");
      codes.add("\tSTR " + paramReg + ", [sp]");

    } else if (ast.getAssignRhsAST().getRhsContext().expr().size() > 1) {
      codes.add("\tSUB sp, sp, #4");
      spPosition += 4;
      codes.add("\tLDR " + resultReg + ", =8");
      codes.add("\tBL malloc");
      codes.add("\tMOV " + paramReg + ", " + resultReg);
      visitExprAST(ast.getAssignRhsAST().getExpr1(),codes,reg_counter);
      Type lType = null;
      Type rType = null;
      if (type instanceof PairType) {
        lType = ((PairType) type).getLeftType();
        rType = ((PairType) type).getRightType();
      }
      int size = lType.equals(Type.charType()) ? 1 : 4;
      String b = lType.equals(Type.charType()) ? "B" : "";
      codes.add("\tLDR " + resultReg + ", =" + size);
      codes.add("\tBL malloc");
      codes.add("\tSTR" + b + " r5, [r0]");
      codes.add("\tSTR " + resultReg + ", [r4]");
      visitExprAST(ast.getAssignRhsAST().getExpr2(),codes,reg_counter);
      size = rType.equals(Type.charType()) ? 1 : 4;
      b = rType.equals(Type.charType()) ? "B" : "";
      codes.add("\tLDR " + resultReg + ", =" + size);
      codes.add("\tBL malloc");
      codes.add("\tSTR" + b + " r5, [" + resultReg + "]");
      codes.add("\tSTR " + resultReg + ", [" + paramReg + ", #4]");
    } else if (type.equals(stringType()) || type.equals(intType())) {
      codes.add("\tSUB sp, sp, #4");
      spPosition += 4;
    } else if (type.equals(boolType()) || type.equals(charType())) {
      codes.add("\tSUB sp, sp, #1");
      spPosition += 1;
      strWord = "\tSTRB ";
    }

    if (ast.getAssignRhsAST().call()) {
      visitCallAst(ast.getAssignRhsAST().getCallAST(), codes, reg_counter);
    } else if (ast.getAssignRhsAST().getRhsContext().expr().size()<=1){
      visitExprAST(ast.getAssignRhsAST().getExpr1(), codes, reg_counter);
    }

    codes.add(strWord + paramReg + ", [sp]");
    symbolTable.putStackTable(ast.getName(), spPosition);
  }

  private void visitReturnAST(ReturnAst ast, List<String> codes, int reg_counter) {
    visitExprAST(ast.getExpr(), codes, reg_counter);
    codes.add("\tMOV " + resultReg + ", " + paramReg);
  }

  private void visitIfAst(IfAst ast, List<String> codes, int reg_counter) {
    SymbolTable symbolTabletemp = symbolTable;
    symbolTable = ast.getThenSymbolTable();
    List<String> elseBranch = new LinkedList<>();
    visitExprAST(ast.getExpr(), codes, reg_counter++);
    AST expr =ast.getExpr();
    if (expr instanceof BoolNode || expr instanceof Binary_BoolOpNode) {
      codes.add("\tCMP " + paramReg + ", #0");
    } else {
      codes.add("\tCMP r" + (reg_counter - 1) + ", r" + reg_counter);
    }
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
    codes.add("\tMOV " + resultReg + ", " + paramReg);
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
    } else if (expr instanceof ArrayElemNode) {
      type = symbolTable.getVariable(((ArrayElemNode) expr).getName());
      type = ((ArrayType) type).getType();
    }

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

    if (expr instanceof PairAST) {
      codes.add("\tBL p_print_reference");
      printReference = true;
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
    codes.add("\tADD r4, sp, #" + (spPosition - symbolTable.getStackTable(ast.getLhs().getLhsContext().IDENT().getText())));
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
    SymbolTable symbolTabletemp = symbolTable;
    symbolTable = ast.getSymbolTable();
    int loopLabel = branchCounter++;
    int bodyLabel = branchCounter++;
    codes.add("\tB L" + loopLabel);
    codes.add("L" + bodyLabel + ":");
    visitStat(ast.getStat(), codes, reg_counter);
    codes.add("L" + loopLabel + ":");
    visitExprAST(ast.getExpr(), codes, reg_counter);
    if (ast.getExpr() instanceof BoolNode || ast.getExpr() instanceof Binary_BoolOpNode) {
      codes.add("\tCMP " + paramReg + ", #1");
    } else {
      codes.add("\tCMP " + paramReg + ", r" + reg_counter);
    }
    codes.add("\tBEQ L" + bodyLabel);
    symbolTable = symbolTabletemp;
  }

  public void visitFreeAST(FreeAst ast, List<String> codes, int reg_counter) {
    codes.add("\tLDR " + paramReg + ", [sp]");
    codes.add("\tMOV " + resultReg + ", " + paramReg);
    codes.add("\tBL p_free_pair");
    printFree = true;
  }
}