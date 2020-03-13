package doc.wacc.utils;

import doc.wacc.astNodes.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static doc.wacc.astNodes.AST.symbolTable;
import static doc.wacc.utils.Register.createAllRegs;
import static doc.wacc.utils.Type.*;
import static doc.wacc.utils.CompilerVisitor.dynamically_Typed;

public class ASTVisitor {
  private List<String> variables = new LinkedList<>(); // to store tje variable and their
  private List<String> main = new LinkedList<>();      // to store the generated code for main body.
  private List<String> printcodes = new LinkedList<>();

  public static final String SP = "sp";   // register for stack pointer.
  public static final String PC = "pc";   // register for program counter
  public static final String LR = "lr";   // register for load
  private final String resultReg = "r0";
  private final String paramReg = "r4";
  private final int MAX_REG = 10;
  private final int INTTYPE = 1;
  private final int BOOLTYPE = 2;
  private final int CHARTYPE = 3;
  private final int STRINGTYPE = 4;
  private final int ARRAYTYPE = 5;
  private final int PAIRTYPE = 6;


  public final ArrayList<Register> allRegs = createAllRegs();
  public HashMap<String, List<Type>> functionType = new HashMap<>();  // function names and corresponding parameter counters.
  String currentFuncName = null;

  private int stringCounter = 0;   // a counter for the amount of strings appear in code.
  private int spPosition = 0;      // position of the stack pointer.
  private int branchCounter = 0;   // for counting number of branches
  private int pushCounter = 0;     // for counting the number of pushing registers to stack
  private int mallocCounter = 0;   // for array

  private boolean println = false;   // indicate if println is called in wacc code.
                                     // if yes, print corresponding assembly code
  private boolean printint = false;  // indicate if an integer is printed in wacc code.
  private boolean printstring = false; // indicate if a string is printed in wacc code.
  private boolean printBool = false;    // indicate if a bool-type variable is printed in wacc code.
  private boolean printRunTimeErr = false;  // indicate if a runtimeErr is called wacc code.
  private boolean printCheckArrayBound = false; // indicate if a arraybound check is called wacc code.
  private boolean printOverflowError = false;   // indicate if an overFlowError is called wacc code.
  private boolean printReference = false;
  private boolean printFree = false;
  private boolean printDivideByZeroError = false;
  private boolean printCheckNullPointer = false;
  private boolean read_int = false;
  private boolean read_char = false;
  private boolean in_func = false;  // indicate the current code is in a function.
  private boolean inBlock = false;
  private boolean return_Pop = false;  // indicate a return statement is met.

  private boolean typeCheck = false;
  private boolean identNotDefined = false;
  private boolean arrayNotAccepted = false;

  public List<String> getCodes() {
    if (spPosition > 0) {
      while (spPosition > 1024) {
        main.add(ADD(SP, SP, 1024));
        spPosition -= 1024;
      }
      main.add(ADD(SP, SP, spPosition));
    }
    main.add(LDR_value(resultReg, 0));
    main.add(POP(PC));
    main.add("\t.ltorg");

    // print functions for checking errors

    if (printCheckNullPointer) {
      printcodes.add("p_check_null_pointer:");
      printcodes.add(PUSH(LR));
      printcodes.add(CMP_value(resultReg, 0));
      printcodes.add(LDREQ_msg(resultReg, stringCounter));
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
      variables.add("\t.word 44");
      variables.add("\t.ascii \"ArrayIndexOutOfBoundsError: negative index\\n\\0\"");
      printcodes.add(BLLT("p_throw_runtime_error"));
      printcodes.add(LDR_reg("r1", "r1"));
      printcodes.add(CMP_reg(resultReg, "r1"));
      printcodes.add(LDRCS_msg(resultReg, String.valueOf(stringCounter)));
      variables.add("msg_" + stringCounter++ + ":");
      variables.add("\t.word 45");
      variables.add("\t.ascii \"ArrayIndexOutOfBoundsError: index too large\\n\\0\"");
      printcodes.add(BLCS("p_throw_runtime_error"));
      printRunTimeErr = true;
      printcodes.add(POP(PC));
    }

    if (printFree) {
      printcodes.add("p_free_pair:");
      printcodes.add(PUSH(LR));
      printcodes.add(CMP_value(resultReg, 0));
      printcodes.add(LDREQ_msg(resultReg, stringCounter));
      visitStringNode(new StringNode("\"NullReferenceError: dereference a null reference\\n\\0\""));
      printcodes.add(BEQ("p_throw_runtime_error"));
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
      printcodes.add(LDREQ_msg("r0", stringCounter));
      printcodes.add(BLEQ("p_throw_runtime_error"));
      printRunTimeErr = true;
      printcodes.add(POP(PC));
      variables.add("msg_" + stringCounter + ":");
      variables.add("\t.word 45");
      variables.add("\t.ascii \"DivideByZeroError: divide or modulo by zero\\n\\0\"");
      stringCounter++;
    }

    if (printOverflowError) {
      printcodes.add("p_throw_overflow_error:");
      printcodes.add(LDR_msg(resultReg, String.valueOf(stringCounter)));
      printcodes.add(BL("p_throw_runtime_error"));
      printRunTimeErr = true;
      variables.add("msg_" + stringCounter + ":");
      variables.add("\t.word 82");
      variables.add(
          "\t.ascii \"OverflowError: the result is too small/large to store in a 4-byte signed-integer.\\n\"");
      stringCounter++;
    }

    if (typeCheck) {
      printcodes.add("p_type_check:");
      printcodes.add(PUSH(LR));
      printcodes.add(CMP_reg("r1", "r2"));
      printcodes.add(MOVEQ("r1", 1));
      printcodes.add(MOVNE("r1", 0));
      printcodes.add(CMP_value("r1", 0));
      printcodes.add(LDREQ_msg(resultReg, stringCounter));
      printcodes.add(BLEQ("p_throw_runtime_error"));
      printRunTimeErr = true;
      printcodes.add(POP(PC));
      variables.add("msg_" + stringCounter + ":");
      variables.add("\t.word 37");
      variables.add(
          "\t.ascii \"TypeError: the type does not match.\\n\\0\"");
      stringCounter++;
    }

    if (identNotDefined) {
      printcodes.add("p_ident_check:");
      printcodes.add(PUSH(LR));
      printcodes.add(LDR_value("r2", 1));
      printcodes.add(CMP_reg("r1", "r2"));
      printcodes.add(MOVEQ("r1", 1));
      printcodes.add(MOVNE("r1", 0));
      printcodes.add(CMP_value("r1", 0));
      printcodes.add(LDREQ_msg(resultReg, stringCounter));
      printcodes.add(BLEQ("p_throw_runtime_error"));
      printRunTimeErr = true;
      printcodes.add(POP(PC));
      variables.add("msg_" + stringCounter + ":");
      variables.add("\t.word 48");
      variables.add(
          "\t.ascii \"TypeError: the variable is not defined before.\\n\\0\"");
      stringCounter++;
    }

    if (arrayNotAccepted) {
      printcodes.add("p_array_check:");
      printcodes.add(PUSH(LR));
      printcodes.add(LDR_value("r2", 0));
      printcodes.add(CMP_reg("r1", "r2"));
      printcodes.add(MOVEQ("r1", 1));
      printcodes.add(MOVNE("r1", 0));
      printcodes.add(CMP_value("r1", 0));
      printcodes.add(LDREQ_msg(resultReg, stringCounter));
      printcodes.add(BLEQ("p_throw_runtime_error"));
      printRunTimeErr = true;
      printcodes.add(POP(PC));
      variables.add("msg_" + stringCounter + ":");
      variables.add("\t.word 40");
      variables.add(
          "\t.ascii \"TypeError: array type is not accepted.\\n\\0\"");
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
      printcodes.add(LDRNE_msg(resultReg, String.valueOf(stringCounter)));
      visitStringNode(new StringNode("\"true\\0\""));
      printcodes.add(LDREQ_msg(resultReg, stringCounter));
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

    if (printstring) {
      printcodes.add("p_print_string:");
      printcodes.add(PUSH(LR));
      printcodes.add(LDR_reg("r1", resultReg));
      printcodes.add(ADD("r2", resultReg, 4));
      printcodes.add(LDR_msg(resultReg, String.valueOf(stringCounter)));
      printcodes.add(ADD(resultReg, resultReg, 4));
      printcodes.add(BL("printf"));
      printcodes.add(MOV(resultReg, 0));
      printcodes.add(BL("fflush"));
      printcodes.add(POP(PC));
      visitStringNode(new StringNode("\"%.*s\\0\""));
    }

    // print data list
    if (variables.size() > 0) {
      variables.add(0, ".data\n");
    }

    // if there is printable variables, print them in order.
    for (String s : variables) {
      System.out.println(s);
    }

    // print main
    for (String s : main) {
      System.out.println(s);
    }

    // print error functions
    if (printcodes.size() > 0) {
      for (String s : printcodes) {
        System.out.println(s);
      }
    }

    List<String> result = new LinkedList<>();
    result.addAll(variables);
    result.addAll(main);
    result.addAll(printcodes);

    return result;
  }

  private Type findType(AST expr) {
    if (expr instanceof CharNode) {
      return charType();
    } else if (expr instanceof IntNode || expr instanceof BinaryOpNode) {
      return intType();
    } else if (expr instanceof BoolNode || expr instanceof Binary_BoolOpNode) {
      return boolType();
    } else if (expr instanceof StringNode) {
      return stringType();
    } else if (expr instanceof PairElemNode) {
      PairType pairType = (PairType) symbolTable.getVariable(((PairElemNode) expr).getName());
      if (((PairElemNode) expr).isFst()) {
        return pairType.getLeftType();
      } else {
        return pairType.getRightType();
      }
    } else if (expr instanceof PairAST) {
        return new PairType(errorType(), errorType());
    } else if (expr instanceof IdentNode) {
      return symbolTable.getVariable(((IdentNode) expr).getIdent());
    } else if (expr instanceof ExprWithParen) {
      return findType(((ExprWithParen) expr).getExpr());
    } else if (expr instanceof ArrayElemNode) {
      Type type = symbolTable.getVariable(((ArrayElemNode) expr).getName());
      if (type instanceof ArrayType) {
        return ((ArrayType) type).getType();
      } else {
        return errorType();
      }
    } else if (expr instanceof UnaryOpNode) {
      if (((UnaryOpNode) expr).isLen() || ((UnaryOpNode) expr).isMinus() || ((UnaryOpNode) expr).isOrd()) {
        return intType();
      } else if (((UnaryOpNode) expr).isNOT()) {
        return boolType();
      } else if (((UnaryOpNode) expr).isChr()) {
        return charType();
      }
    } else if (expr instanceof ArrayAST) {
      if (((ArrayAST) expr).getExprs().size() == 0) {
        return new ArrayType(errorType());
      } else {
        return new ArrayType(findType(((ArrayAST) expr).getExprs().get(0)));
      }
    } else if (expr instanceof AssignRHSAST) {
      if (((AssignRHSAST) expr).getArrayAST() != null) {
        return findType(((AssignRHSAST) expr).getArrayAST());
      } else if (((AssignRHSAST) expr).getPairElemNode() != null) {
        return findType(((AssignRHSAST) expr).getPairElemNode());
      } else if (((AssignRHSAST) expr).getExpr2() != null){
        return new PairType(findType(((AssignRHSAST) expr).getExpr1()), findType(((AssignRHSAST) expr).getExpr2()));
      } else if (((AssignRHSAST) expr).getCallAST() != null) {
        return functionType.get(((AssignRHSAST) expr).getCallAST().getFuncName()).get(0);
      } else {
        return findType(((AssignRHSAST) expr).getExpr1());
      }
    } else if (expr instanceof AssignLHSAST) {
      if (((AssignLHSAST) expr).isArray()) {
        return findType(((AssignLHSAST) expr).getArrayElem());
      } else if (((AssignLHSAST) expr).isPair()) {
        return findType(((AssignLHSAST) expr).getPairElemNode());
      } else {
        return symbolTable.getVariable(((AssignLHSAST) expr).getName());
      }
    }
    return errorType();
  }

  private int typeConvert(Type type) {
    if (type.equals(intType())) {
      return INTTYPE;
    } else if (type.equals(boolType())) {
      return BOOLTYPE;
    } else if (type.equals(charType())) {
      return CHARTYPE;
    } else if (type.equals(stringType())) {
      return STRINGTYPE;
    } else if (type instanceof ArrayType) {
      return ARRAYTYPE;
    } else if (type instanceof PairType) {
      return PAIRTYPE;
    }
     return -1;
  }

  private void typeCheck(int desireType, Type actualType, List<String> codes) {
    codes.add(MOV("r1", typeConvert(actualType)));
    codes.add(MOV("r2", desireType));
    codes.add(BL("p_type_check"));
    typeCheck = true;
  }

  private void identCheck(String name, List<String> codes) {
    System.out.println(name);
      boolean identExist = !symbolTable.getVariable(name).equals(errorType());
      codes.add(LDR_value("r1", identExist ? 1 : 0));

      codes.add(BL("p_ident_check"));
    identNotDefined = true;
  }

  public void visitProgAST(AST ast) {
    main.add("\n.text\n");
    main.add(".global main");

    ProgramAST past = (ProgramAST) ast;
    int k = 4;

    //    for imported library:
    //    =========================================================================
    // put parameters onto stack firstly
    if (!past.getLibraries().isEmpty()) {
      for (int i = 0; i < past.getLibraries().size(); i++) {
        for (FuncAST f : past.getLibraries().get(i).getFunctions()) {
          SymbolTable symbolTabletemp = symbolTable;
          symbolTable = f.getSymbolTable();

          for (ParamNode p : f.getParameters()) {
            visitParamNode(p);
          }

          List<Type> types = new LinkedList<>();
          types.addAll(f.getParamsType());
          types.add(0, f.getReturnType());
          functionType.put(f.getFuncName(), types);
//      functionParams.put(f.getFuncName(), symbolTable.getParamCounter());
          symbolTable = symbolTabletemp;
        }

        for (FuncAST f : past.getLibraries().get(i).getFunctions()) {
          in_func = true;
          visitFuncAST(f, main, k);
          in_func = false;
          symbolTable.local_variable = 0;
          spPosition = 0;
        }
      }
    }
    //=========================================================================

    // put parameters onto stack firstly
    for (FuncAST f : past.getFunctions()) {
      SymbolTable symbolTabletemp = symbolTable;
      symbolTable = f.getSymbolTable();

      for (ParamNode p : f.getParameters()) {
        visitParamNode(p);
      }

      List<Type> types = new LinkedList<>();
      types.addAll(f.getParamsType());
      types.add(0, f.getReturnType());
      functionType.put(f.getFuncName(), types);
//      functionParams.put(f.getFuncName(), symbolTable.getParamCounter());
      symbolTable = symbolTabletemp;
    }

    for (FuncAST f : past.getFunctions()) {
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

  public void visitStat(AST ast, List<String> codes, int reg_counter) {
    // start traversing the statementsAST
    if (ast instanceof SkipAst) {
      visitSkipAst(ast);
    } else if (ast instanceof ExitAst) {
      visitExitAst((ExitAst) ast, codes, reg_counter);
    } else if (ast instanceof DeclarationAst) {
      visitDeclaration((DeclarationAst) ast, codes, reg_counter);
    } else if (ast instanceof SeqStateAst) {
      for (AST ast1 : ((SeqStateAst) ast).getSeqs()) {
        visitStat(ast1, codes, reg_counter);
      }
    } else if (ast instanceof AssignAST) {
      visitAssignAst((AssignAST) ast, codes, reg_counter);
    } else if (ast instanceof PrintAst) {
      visitPrintAst((PrintAst) ast, codes, reg_counter);
    } else if (ast instanceof PrintlnAst) {
      PrintlnAst print_ast = (PrintlnAst) ast;
      visitPrintAst(new PrintAst(((PrintlnAst) ast).getExpr()), codes, reg_counter);
      visitPrintlnAst(print_ast, codes, reg_counter);
    } else if (ast instanceof ReadAst) {
      visitReadAST((ReadAst) ast, codes, reg_counter);
    } else if (ast instanceof IfAst) {
      visitIfAst((IfAst) ast, codes, reg_counter);
    } else if (ast instanceof WhileAst) {
      visitWhileAST((WhileAst) ast, codes, reg_counter);
    } else if (ast instanceof DoWhileAST) {
      visitDoWhileAST((DoWhileAST) ast, codes, reg_counter);
    } else if (ast instanceof ReturnAst) {
      visitReturnAST((ReturnAst) ast, codes, reg_counter);
    } else if (ast instanceof FreeAst) {
      visitFreeAST((FreeAst) ast, codes, reg_counter);
    } else if (ast instanceof BlockAst) {
      SymbolTable temp = symbolTable;
      symbolTable = ((BlockAst) ast).getSymbolTable();
      inBlock = true;
      for (AST ast1 : ((BlockAst) ast).getStats()) {
        inBlock = true;
        visitStat(ast1, codes, reg_counter);
      }
      if (symbolTable.local_variable > 0) {
        codes.add(ADD(SP, SP, symbolTable.local_variable));
      }
      spPosition -= symbolTable.local_variable;
      inBlock = false;
      symbolTable = temp;
    }
  }

  public void visitFuncAST(FuncAST ast, List<String> codes, int reg_counter) {
    SymbolTable symbolTabletemp = symbolTable;
    symbolTable = ast.getSymbolTable();
    spPosition += symbolTable.getParamCounter();   // adjust spPosition as already pushed parameters onto stack
    currentFuncName = ast.getFuncName();
    // generate codes for function
    codes.add("f_" + ast.getFuncName() + ":");
    codes.add(PUSH(LR));
    visitStat(ast.getFunctionBody(), codes, reg_counter);

    if (!(ast.getFunctionBody() instanceof SkipAst)) {
      if (symbolTable.local_variable != 0) {
        codes.add(ADD(SP, SP, symbolTable.local_variable));
      }
      if (return_Pop) {
        codes.add(POP(PC));
        return_Pop = false;
      }
      codes.add(POP(PC));
      codes.add("\t.ltorg");
    }

    symbolTable = symbolTabletemp;
    currentFuncName = null;
  }

  public void visitParamNode(ParamNode param) {
    Type type = param.getType();
    // put arguments to the stack table and record total spPosition change.
    if (type.equals(intType()) || type.equals(boolType()) || type instanceof ArrayType || type instanceof PairType) {
      symbolTable.setParamCounter(symbolTable.getParamCounter() + 4);
    } else {
      symbolTable.setParamCounter(symbolTable.getParamCounter() + 1);
    }
    if (dynamically_Typed) {
      symbolTable.putVariable(param.getName(), param.getType());
    }
    symbolTable.putStackTable(param.getName(), symbolTable.getParamCounter());
  }

  public void visitAssignAst(AssignAST ast, List<String> codes, int reg_counter) {
    AssignLHSAST lhsast = ast.getLhs();
    AssignRHSAST rhsast = ast.getRhs();

    if ( !dynamically_Typed ||
        ((!symbolTable.getVariable(ast.getLhs().getName()).equals(errorType()) && !inBlock) ||
            (inBlock && symbolTable.existInCurrentBlock(ast.getLhs().getName())))) {

      Type type;

      if (lhsast.isPair()) {
      type = symbolTable.getVariable(ast.getLhsPairElem().expr().getText());
    } else if (ast.getLhs().isArray()) {
      type = symbolTable.getVariable(ast.getLhs().getLhsContext().array_elem().IDENT().getText());
    } else {
      type = symbolTable.getVariable(ast.getLhs().getLhsContext().ident().IDENT().getText());
      if (type instanceof PtrType) {
        type = ((PtrType) type).getType();
      }
    }



      // type check

      if (dynamically_Typed) {
        Type desireType = findType(ast.getLhs());
        Type actualType = findType(ast.getRhs());
        typeCheck(typeConvert(desireType), actualType, codes);
      }

      // generate codes for rhs
      if (rhsast.getArrayAST() != null) {
        visitArrayLiter((ArrayType) type, rhsast, codes, reg_counter);
      } else if (rhsast.call()) {
        visitCallAst(rhsast.getCallAST(), codes, reg_counter);
      } else {
        visitExprAST(rhsast.getExpr1(), codes, reg_counter);
      }

      if (rhsast.getExpr1() instanceof ArrayElemNode) {
        String ldrWord = "\tLDR ";
        if (equalsCharOrBoolType(type)) {
          ldrWord = "\tLDRSB ";
        }
        codes.add(ldrWord + paramReg + ", [r" + reg_counter + "]");
      }

      if (rhsast.getExpr1() instanceof PairAST) {
        // assign a pair from a null content.
        if (((PairAST) rhsast.getExpr1()).ident.equals("null")) {
          codes.add(LDR_value(paramReg, 0));
        }
      } else if (type instanceof PairType) {
        if (ast.getLhsPairElem() != null) {
          // lhs is pair elem
          // calculating shifting in stack
          int pos = symbolTable.getStackTable(ast.getLhsPairElem().expr().getText());
          pos = pos != -1 ? spPosition - pos : 0;
          if (pos != 0) {
            codes.add(LDR_reg("r5", SP + ", #" + pos));
          } else {
            codes.add(LDR_reg("r5", SP));
          }
          codes.add(MOV(resultReg, "r5"));
          codes.add(BL("p_check_null_pointer"));
          printCheckNullPointer = true;
          Type strType = type;
          if (ast.getLhsPairElem() != null) {
            // lhs is pair elem
            if (ast.getLhsPairElem().fst() != null) {
              // lhs is first elem of pair
              strType = ((PairType) type).getLeftType();
              codes.add(LDR_reg("r5", "r5"));
            } else if (ast.getLhsPairElem().snd() != null) {
              // lhs is second elem of pair
              strType = ((PairType) type).getRightType();
              codes.add(LDR_reg("r5", "r5, #4"));
            }
          }
          if (equalsCharOrBoolType(strType)) {
            codes.add(STRB("r4", "[r5]"));
          } else {
            codes.add(STR("r4", "[r5]"));
          }
        } else if (ast.getRhsPairElem() != null) {
          // lhs is pair and rhs is pair elem
          int pos = symbolTable.getStackTable(ast.getRhs().getRhsContext().stop.getText());
          pos = pos != -1 ? spPosition - pos : 0;
          if (pos != 0) {
            codes.add(LDR_reg("r" + reg_counter, SP + ", #" + pos));
          } else {
            codes.add(LDR_reg("r" + reg_counter, SP));
          }
          codes.add(MOV(resultReg, paramReg));
          codes.add(BL("p_check_null_pointer"));
          printCheckNullPointer = true;
          if (ast.getRhsPairElem().fst() != null) {
            codes.add(LDR_reg(paramReg, paramReg));
          } else {
            codes.add(LDR_reg(paramReg, paramReg + ", #4"));
          }
          codes.add(LDR_reg(paramReg, paramReg));
        }
      }

      String strcommand = "\tSTR ";

      // generate codes for STR base on type
      if (equalsCharOrBoolType(type)) {
        strcommand = "\tSTRB ";

        if (ast.getRhs().getPairElemNode() != null) {
          int pos = symbolTable.getStackTable(ast.getRhs().getPairElemNode().getName());
          pos = pos != -1 ? spPosition - pos : 0;
          if (pos != 0) {
            codes.add(LDR_reg("r" + reg_counter, SP + ", #" + pos));
          } else {
            codes.add(LDR_reg("r" + reg_counter, SP));
          }
          codes.add(MOV(resultReg, paramReg));
          codes.add(BL("p_check_null_pointer"));
          printCheckNullPointer = true;
          int oldX = pos;
          pos = symbolTable.getStackTable(ast.getLhs().getLhsContext().getText());
          pos = pos != -1 ? pos - oldX : 0;
          if (pos != 0) {
            codes.add(LDR_reg(paramReg, paramReg + ", #" + pos));
          } else {
            codes.add(LDR_reg(paramReg, paramReg));
          }
          codes.add(LDRSB(paramReg, paramReg));
        }
      }

      if (type.equals(intType())) {
        if (ast.getRhs().getPairElemNode() != null) {
          int pos = symbolTable.getStackTable(ast.getRhs().getPairElemNode().getName());
          pos = pos != -1 ? spPosition - pos : 0;
          if (pos != 0) {
            codes.add(LDR_reg("r" + reg_counter, SP + ", #" + pos));
          } else {
            codes.add(LDR_reg("r" + reg_counter, SP));
          }
          codes.add(MOV(resultReg, paramReg));
          codes.add(BL("p_check_null_pointer"));
          printCheckNullPointer = true;
          pos = symbolTable.getStackTable(ast.getLhs().getLhsContext().getText());
          pos = pos != -1 ? spPosition - pos : 0;
          if (pos != 0) {
            codes.add(LDR_reg(paramReg, paramReg + ", #" + pos));
          } else {
            if (ast.getRhsPairElem().snd() != null) {
              codes.add(LDR_reg(paramReg, paramReg + ", #" + 4));
            } else {
              codes.add(LDR_reg(paramReg, paramReg));
            }
          }
          codes.add(LDR_reg(paramReg, paramReg));
        }
      }

    String s = lhsast.getName();
    if (lhsast.getLhsContext().ident() != null) {
      s = lhsast.getLhsContext().ident().IDENT().getText();
    }
    boolean is_ptr =false;
    if (lhsast.getLhsContext().ident() != null && lhsast.getLhsContext().ident().ptr() != null) {
      is_ptr = lhsast.getLhsContext().ident().ptr().size() > 0;
    }

    int variableStackPosition = symbolTable.getStackTable(lhsast.getLhsName());

    if (is_ptr) {
      variableStackPosition = symbolTable.getStackTable(lhsast.getLhsContext().ident().IDENT().getText());
    }

    if (variableStackPosition != -1 && ! is_ptr) {
      if (in_func && variableStackPosition <= symbolTable.getParamCounter()) {
        codes.add(
            strcommand
                + "r"
                + reg_counter
                + ", [sp, #"
                + (variableStackPosition + symbolTable.getLocal_variable())
                + "]");
      } else {
        if (spPosition - variableStackPosition == 0) {
          codes.add(strcommand + "r" + reg_counter + ", [sp]");
        } else {
          if (!lhsast.isArray()) {
            if (variableStackPosition != -1) {
              codes.add(strcommand +
                  "r" + reg_counter +
                  ", [sp, #" + (spPosition - variableStackPosition) + "]");
            }
          }
        }
      }
    }

      if (variableStackPosition!=-1 && is_ptr) {
        if (in_func && variableStackPosition <= symbolTable.getParamCounter()) {
          codes.add("LDR r"+
              (reg_counter+1) +
              ", [sp, #" +
              (variableStackPosition + symbolTable.getLocal_variable())
              + "]");
          codes.add(
              strcommand
                  + "r"
                  + reg_counter
                  + ", [r"
                  + (reg_counter+1)
                  + "]");
        } else {
          if (spPosition - variableStackPosition == 0) {
            codes.add("LDR r"+
                (reg_counter+1) +
                ", [sp]");
            codes.add(strcommand + "r" + reg_counter + ", [r" + (reg_counter+1) + "]");
          } else {
            if (!lhsast.isArray()) {
              codes.add("LDR r"+
                  (reg_counter+1) +
                  ", [sp, #" + (spPosition - variableStackPosition) + "]");
              codes.add(strcommand + "r" + reg_counter + ", [r" + (reg_counter+1) + "]");
            }
          }
        }
      }

      if (variableStackPosition!=-1 && is_ptr) {
        if (in_func && variableStackPosition <= symbolTable.getParamCounter()) {
          codes.add("LDR r"+
              (reg_counter+1) +
              ", [sp, #" +
              (variableStackPosition + symbolTable.getLocal_variable())
              + "]");
          codes.add(
              strcommand
                  + "r"
                  + reg_counter
                  + ", [r"
                  + (reg_counter+1)
                  + "]");
        } else {
          if (spPosition - variableStackPosition == 0) {
            codes.add("LDR r"+
                (reg_counter+1) +
                ", [sp]");
            codes.add(strcommand + "r" + reg_counter + ", [r" + (reg_counter+1) + "]");
          } else {
            if (!lhsast.isArray()) {
              codes.add("LDR r"+
                  (reg_counter+1) +
                  ", [sp, #" + (spPosition - variableStackPosition) + "]");
              codes.add(strcommand + "r" + reg_counter + ", [r" + (reg_counter+1) + "]");
            }
          }
        }
      }



      if (lhsast.isArray()) {
        visitExprAST(lhsast.getArrayElem(), codes, reg_counter + 1);
        while (type instanceof ArrayType) {
          type = ((ArrayType) type).getType();
        }
        if (equalsCharOrBoolType(type)) {
          strcommand = "\tSTRB ";
        }
        codes.add(strcommand + paramReg + ", [r" + (reg_counter + 1) + "]");
        printCheckArrayBound = true;
      }
    } else {
      AssignRHSAST rhs = ast.getRhs();
      Type type = findType(ast.getRhs());
      symbolTable.putVariable(lhsast.getName(), type);
      String strWord = "\tSTR ";

      // adjust spPosition base on variable type
      if (rhs.getArrayAST() != null || type instanceof ArrayType) {
        // array declaration
        codes.add(SUB(SP, SP, 4));
        if (in_func || inBlock) {
          symbolTable.local_variable += 4;
        }
        spPosition += 4;
        ArrayType arrayType = (ArrayType) type;
        if (rhs.getArrayAST() != null)
          visitArrayLiter(arrayType, rhs, codes, reg_counter);
      } else if (type.equals(stringType()) || type.equals(intType())) {
        codes.add(SUB(SP, SP, 4));
        if (in_func || inBlock) {
          symbolTable.local_variable += 4;
        }
        spPosition += 4;
      } else if (equalsCharOrBoolType(type)) {
        codes.add(SUB(SP, SP, 1));
        if (in_func || inBlock) {
          symbolTable.local_variable += 1;
        }
        spPosition += 1;
        strWord = "\tSTRB ";
      } else if (ast.rhsNotPairElemPair()
          || (type instanceof PairType)) {     // pair declaration (rhs not pairElemNode)
        codes.add(SUB(SP, SP, 4));
        spPosition += 4;
        if (in_func || inBlock) {
          symbolTable.local_variable += 4;
        }
        if (ast.rhsDeclaredPairOrNull()) {
          if (!(rhsast.getExpr1() instanceof IdentNode)) {
            // rhs is null
            codes.add(LDR_value(paramReg, 0));
          }
          // do nothing here if rhs is a declared pair
        } else {
          // rhs is a new pair
          if (rhsast.getPairElemNode() == null) {
            codes.add(LDR_value(resultReg, 8));
            codes.add(BL("malloc"));
            codes.add(MOV("r" + reg_counter, resultReg));
            reg_counter++;
            visitExprAST(rhsast.getExpr1(), codes, reg_counter);
            Type lType = null;
            Type rType = null;
            if (type instanceof PairType) {
              lType = ((PairType) type).getLeftType();
              rType = ((PairType) type).getRightType();
            }
            int size = equalsCharOrBoolType(lType) ? 1 : 4;
            String b = equalsCharOrBoolType(lType) ? "B" : "";
            codes.add(LDR_value(resultReg, size));
            codes.add(BL("malloc"));
            codes.add(!b.equals("") ? STRB("r5", "[" + resultReg + "]") : STR("r5", "[" + resultReg + "]"));
            codes.add(STR(resultReg, "[r4]"));
            int codesLength = codes.size();
            visitExprAST(rhsast.getExpr2(), codes, reg_counter);
            size = (rType.equals(Type.charType()) || rType.equals(boolType())) ? 1 : 4;
            b = rType.equals(Type.charType()) ? "B" : "";
            if (codes.size() - codesLength == 0) {
              if (rType instanceof PairType) {
                if (((PairType) rType).getLeftType() == null) {
                  codes.add(LDR_value("r" + reg_counter, 0));
                }
              }
            }
            codes.add(LDR_value(resultReg, size));
            codes.add(BL("malloc"));
            codes.add(!b.equals("") ? STRB("r5", "[" + resultReg + "]") : STR("r5", "[" + resultReg + "]"));
            codes.add(STR(resultReg, "[" + paramReg + ", #4]"));
          }
        }
      } else if (rhsast.getPairElemNode() != null) {
        // pair declaration (rhs is pairElemNode)
        if (type instanceof PairType) {
          codes.add(SUB(SP, SP, 4));
          spPosition += 4;
          if (in_func) {
            symbolTable.local_variable += 4;
          }
        }
      }

      if (rhs.call()) {
        visitCallAst(rhs.getCallAST(), codes, reg_counter);
      } else if (!ast.rhsNotPairElemPair() || ast.rhsDeclaredPairOrNull()) {
        if (rhs.getExpr1() != null) {
          // rhs is a null or all other cases
          visitExprAST(rhsast.getExpr1(), codes, reg_counter);
        } else if (rhsast.getPairElemNode() != null) {
          // rhs is a declared pair or pair elem
          int pos = symbolTable.getStackTable(rhsast.getPairElemNode().getName());
          pos = pos != -1 ? spPosition - pos : 0;
          if (pos != 0) {
            codes.add(LDR_reg(paramReg, SP + ", #" + pos));
          } else {
            codes.add(LDR_reg(paramReg, SP));
          }
          codes.add(MOV(resultReg, paramReg));
          codes.add(BL("p_check_null_pointer"));
          printCheckNullPointer = true;
          if (rhsast.getRhsContext().pair_elem().fst() != null) {
            codes.add(LDR_reg(paramReg, paramReg));
          } else {
            codes.add(LDR_reg(paramReg, paramReg + ", #4"));
          }
          if (equalsCharOrBoolType(type)) {
            codes.add(LDRSB(paramReg, paramReg));
          } else {
            codes.add(LDR_reg(paramReg, paramReg));
          }
        }
      }

      if (rhs.getExpr1() instanceof ArrayElemNode) {
        String ldrWord = "\tLDR ";
        if (equalsCharOrBoolType(type)) {
          ldrWord = "\tLDRSB ";
        }
        codes.add(ldrWord + paramReg + ", [r" + reg_counter + "]");
      }

      codes.add(strWord + paramReg + ", [sp]");
      symbolTable.putStackTable(ast.getLhs().getLhsName(), spPosition);  // record the stack position for declared variable
    }
  }

  public void visitExitAst(ExitAst ast, List<String> codes, int reg_counter) {
    if (dynamically_Typed) {
      typeCheck(INTTYPE, findType(ast.getExpr()), codes);
    }
    visitExprAST(ast.getExpr(), codes, reg_counter);
    codes.add(MOV(resultReg, paramReg));
    codes.add(BL("exit"));
  }

  public void visitExprAST(AST ast, List<String> codes, int reg_counter) {
    if (dynamically_Typed) {
      // type check
      if (ast instanceof Binary_BoolOpNode) {
        if (((Binary_BoolOpNode) ast).isLow_binbool_opContext()) {
          Type expr1Type = findType(((Binary_BoolOpNode) ast).getExpr1());
          if (expr1Type.equals(intType())) {
            typeCheck(INTTYPE, findType(((Binary_BoolOpNode) ast).getExpr2()), codes);
          } else if (expr1Type.equals(charType())) {
            typeCheck(CHARTYPE, findType(((Binary_BoolOpNode) ast).getExpr2()), codes);
          } else if (expr1Type.equals(stringType())) {
            typeCheck(STRINGTYPE, findType(((Binary_BoolOpNode) ast).getExpr2()), codes);
          } else if (expr1Type instanceof PairType) {
            typeCheck(PAIRTYPE, findType(((Binary_BoolOpNode) ast).getExpr2()), codes);
          } else if (expr1Type.equals(boolType())) {
            typeCheck(BOOLTYPE, findType(((Binary_BoolOpNode) ast).getExpr2()), codes);
          }
        } else if (((Binary_BoolOpNode) ast).isOperContext()) {
          Type expr1Type = findType(((Binary_BoolOpNode) ast).getExpr1());
          if (expr1Type.equals(intType())) {
            typeCheck(INTTYPE, findType(((Binary_BoolOpNode) ast).getExpr2()), codes);
          } else if (expr1Type.equals(boolType())) {
            typeCheck(BOOLTYPE, findType(((Binary_BoolOpNode) ast).getExpr2()), codes);
          } else {
            typeCheck(CHARTYPE, findType(((Binary_BoolOpNode) ast).getExpr2()), codes);
          }
        } else {
          typeCheck(BOOLTYPE, findType(((Binary_BoolOpNode) ast).getExpr1()), codes);
          typeCheck(BOOLTYPE, findType(((Binary_BoolOpNode) ast).getExpr2()), codes);
        }
      } else if (ast instanceof BinaryOpNode) {
        typeCheck(INTTYPE, findType(((BinaryOpNode) ast).getExpr1()), codes);
        typeCheck(INTTYPE, findType(((BinaryOpNode) ast).getExpr2()), codes);
      }
    }

    if (ast instanceof RefNode) {
      RefNode refNode = (RefNode) ast;
      int variableStackPosition = symbolTable.getStackTable(refNode.getVariable());
      Type type = symbolTable.getVariable(refNode.getVariable());
      if (in_func && variableStackPosition <= symbolTable.getParamCounter()) {
        codes.add(
            "\tADD"
                + " r"
                + reg_counter
                + ", sp, #"
                + (spPosition - symbolTable.getParamCounter() + variableStackPosition));
      } else {
        codes.add("\tADD" + " r" + reg_counter + ", sp, #" + (spPosition - variableStackPosition));

      }

    } else
    if (ast instanceof DerefNode) {
      DerefNode derefNode = (DerefNode) ast;
      int variableStackPosition = symbolTable.getStackTable(derefNode.getVariable());
      Type type = symbolTable.getVariable(derefNode.getVariable());

      String loadWord = "\tLDR";
      if (equalsCharOrBoolType(type)) {
        loadWord = "\tLDRSB";
      }

      if (in_func && variableStackPosition <= symbolTable.getParamCounter()) {
        codes.add(
            "\tLDR"
                + " r"
                + reg_counter
                + ", [sp, #"
                + (spPosition - symbolTable.getParamCounter() + variableStackPosition)
                + "]");
      } else {
        if (spPosition - variableStackPosition == 0) {
          codes.add("\tLDR" + " r" + reg_counter + ", [sp]");
        } else {
          codes.add("\tLDR" + " r" + reg_counter + ", [sp, #" + (spPosition - variableStackPosition) + "]");
        }
      }
      for (int i = 0; i < derefNode.getDereference_num(); i++) {
        codes.add(loadWord + " r" + reg_counter + ", [r" + reg_counter +"]");
      }
    } else if (ast instanceof IntNode) {
      IntNode int_ast = (IntNode) ast;
      codes.add(LDR_value(("r" + reg_counter), int_ast.getValue()));
    } else if (ast instanceof BoolNode) {
      BoolNode bool_ast = (BoolNode) ast;
      codes.add(MOV("r" + reg_counter, bool_ast.getBoolValue()));
    } else if (ast instanceof IdentNode) {
      int variableStackPosition = symbolTable.getStackTable(((IdentNode) ast).getIdent());
      Type type = symbolTable.getVariable(((IdentNode) ast).getIdent());

      String loadWord = "\tLDR";
      if (equalsCharOrBoolType(type)) {
        loadWord = "\tLDRSB";
      }

      if (in_func && variableStackPosition <= symbolTable.getParamCounter()) {
        codes.add(
            loadWord
                + " r"
                + reg_counter
                + ", [sp, #"
                + (spPosition - symbolTable.getParamCounter() + variableStackPosition)
                + "]");
      } else {
        if (spPosition - variableStackPosition == 0) {
          codes.add(loadWord + " r" + reg_counter + ", [sp]");
        } else {
          codes.add(loadWord + " r" + reg_counter + ", [sp, #" + (spPosition - variableStackPosition) + "]");
        }
      }
    } else if (ast instanceof StringNode) {
      codes.add(LDR_msg("r" + reg_counter, String.valueOf(stringCounter)));
      visitStringNode((StringNode) ast);
    } else if (ast instanceof CharNode) {
      codes.add(MOV("r" + reg_counter, "#'" + ((CharNode) ast).getCharValue() + "'"));
    } else if (ast instanceof Binary_BoolOpNode) {
      int r1 = reg_counter;       // register to store the first argument.
      int r2 = reg_counter + 1;   // register to store the second argument.

      visitExprAST(((Binary_BoolOpNode) ast).getExpr1(), codes, r1);
      visitExprAST(((Binary_BoolOpNode) ast).getExpr2(), codes, r2);

      // generate codes according to the different operator
      if (!((Binary_BoolOpNode) ast).isBinaryAnd() &&
          !((Binary_BoolOpNode) ast).isBinaryOr()) {
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
        codes.add(AND("r" + r1, "r" + r1, "r" + r2)); // still has other operators
      } else if (((Binary_BoolOpNode) ast).isBinaryOr()) {
        codes.add(ORR("r" + r1, "r" + r1, "r" + r2));
      }
    } else if (ast instanceof BinaryOpNode) {
      int r1 = reg_counter;       // register to store the first argument.
      int r2 = reg_counter + 1;   // register to store the second argument.

      if (mallocCounter != 0) {   // adjust register if malloc is called
        r2 = r2 + mallocCounter;
      }

      visitExprAST(((BinaryOpNode) ast).getExpr1(), codes, r1);

      // prevent using more than 10 register.
      if (r2 > MAX_REG) {
        codes.add(PUSH("r10"));
        r2 = MAX_REG;
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
          codes.add(ADDS("r" + r1,"r" + r1, "r" + r2));
        } else if (((BinaryOpNode) ast).isMinus()) {
          codes.add(SUBS("r" + r1, "r" + r1, "r" + r2));
        } else if (((BinaryOpNode) ast).isTime()) {
          codes.add(SMULL("r" + r1, "r" + r2, "r" + r1, "r" + r2));
          codes.add(CMP_reg("r" + r2, "r" + r1 + ", ASR #31"));
        }

        if (((BinaryOpNode) ast).isTime()) {
          codes.add(BLNE("p_throw_overflow_error"));

        } else {
          codes.add(BLVS("p_throw_overflow_error"));
        }
        printOverflowError = true;
      }
    } else if (ast instanceof UnaryOpNode) {
      visitExprAST(((UnaryOpNode) ast).getExpr(), codes, reg_counter);

      if (((UnaryOpNode) ast).isNOT()) {
        codes.add(EOR("r" + reg_counter, "r" + reg_counter, 1));
      } else if (((UnaryOpNode) ast).isMinus()) {
        codes.add(RSBS("r" + reg_counter, "r" + reg_counter, 0));
        codes.add(BLVS("p_throw_overflow_error"));
        printOverflowError = true;
      } else if (((UnaryOpNode) ast).isLen()) {
        codes.add(LDR_reg(paramReg, "r" + reg_counter));
      }
    } else if (ast instanceof CallAST) {
      visitCallAst((CallAST) ast, codes, reg_counter);
    } else if (ast instanceof ArrayElemNode && symbolTable.getVariable(((ArrayElemNode) ast).getName()) instanceof ArrayType) {
      int arrayIndexReg = reg_counter + mallocCounter;   // find out the correct register for array
      codes.add(
          ADD(
              ("r" + reg_counter),
              SP,
              (spPosition
                  - symbolTable.getStackTable(((ArrayElemNode) ast).getName()))));

      // generate codes for loading each element in the array.
      for (int i = 0; i < ((ArrayElemNode) ast).getExprs().size(); i++) {
        visitExprAST(((ArrayElemNode) ast).getExprs().get(i), codes, arrayIndexReg);
        codes.add(LDR_reg("r" + reg_counter, "r" + reg_counter));
        codes.add(MOV(resultReg, "r" + arrayIndexReg));
        codes.add(MOV("r1", "r" + reg_counter));
        codes.add(BL("p_check_array_bounds"));
        codes.add(ADD(new Register(reg_counter), new Register(reg_counter), 4));
        Type type = symbolTable.getVariable(((ArrayElemNode) ast).getName());

        ArrayType arrayType = (ArrayType) type;
        if (equalsCharOrBoolType(arrayType.getType())) {
          codes.add(
              ADD(
                  new Register(reg_counter),
                  new Register(reg_counter),
                  new Register("r" + arrayIndexReg)));
        } else {
          codes.add(ADD("r" + reg_counter, "r" + reg_counter, "r" + arrayIndexReg + ", LSL #2"));
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
  }

  private void visitCallAst(CallAST ast, List<String> codes, int reg_counter) {
    int arg_count = 0;    // to record spPosition change in loading arguments for the function.
    if (ast.hasArgument()) {
      if (dynamically_Typed) {
        // type check
        for (int i = 0; i < ast.getArguments().size(); i++) {
          Type actualType = findType(ast.getArguments().get(i));
          Type desireType = functionType.get(ast.getFuncName()).get(i + 1);
          if (desireType.equals(intType())) {
            typeCheck(INTTYPE, actualType, codes);
          } else if (desireType.equals(charType())) {
            typeCheck(CHARTYPE, actualType, codes);
          } else if (desireType.equals(stringType())) {
            typeCheck(STRINGTYPE, actualType, codes);
          } else if (desireType instanceof PairType) {
            typeCheck(PAIRTYPE, actualType, codes);
          } else if (desireType.equals(boolType())) {
            typeCheck(BOOLTYPE, actualType, codes);
          }
        }
      }

      for (int i = ast.getArguments().size() - 1; i >= 0; i--) {
        AST argument = ast.getArguments().get(i);
        visitExprAST(argument, codes, reg_counter);
        Type type = null;

        if (argument instanceof IdentNode) {
          type = symbolTable.getVariable(((IdentNode) argument).getIdent());
          if (equalsCharOrBoolType(type)) {
            codes.add(STRB(paramReg, "[sp, #-1]!"));
            arg_count += 1;
            spPosition += 1;
          } else {
            codes.add(STR(paramReg, "[sp, #-4]!"));
            arg_count += 4;
            spPosition += 4;
          }
        } else if (argument instanceof CharNode || argument instanceof BoolNode) {
          codes.add(STRB(paramReg, "[sp, #-1]!"));
          arg_count += 1;
          spPosition += 1;
        } else {
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

  public void visitArrayLiter(
      ArrayType arrayType, AssignRHSAST ast, List<String> codes, int reg_counter) {
    int array_size = ast.getArrayAST().getSize();
    String strWord = "\tSTR ";

    if (equalsCharOrBoolType(arrayType.getType())) {
      codes.add(LDR_value(resultReg, (4 + array_size)));
      strWord = "\tSTRB ";

    } else {
      codes.add(LDR_value(resultReg, (4 + 4 * array_size)));
    }
    codes.add(BL("malloc"));
    mallocCounter++;
    codes.add(MOV(paramReg, resultReg));

    int array_counter = 0;
    int array_reg = reg_counter + mallocCounter;   // find out the correct register for array
    for (AST a : ast.getArrayAST().getExprs()) {   // generate codes for every element in the array
      visitExprAST(a, codes, array_reg);
      if (equalsCharOrBoolType(arrayType.getType())) {
        codes.add(
            strWord + "r" + array_reg + ", [r" + reg_counter +
                    ", #" + (4 + array_counter++) + "]");
      } else {
        codes.add(
            strWord
                + "r"
                + array_reg
                + ", [r"
                + reg_counter
                + ", #"
                + (4 + (array_counter++ * 4))
                + "]");
      }
    }
    codes.add(LDR_value("r" + array_reg, array_size));
    codes.add(STR("r" + array_reg, "[r" + reg_counter + "]"));
  }

  public void visitDeclaration(DeclarationAst ast, List<String> codes, int reg_counter) {
    AssignRHSAST rhs = ast.getAssignRhsAST();
    Type type = ast.getType();
    String strWord = "\tSTR ";

    // adjust spPosition base on variable type
    if (rhs.getArrayAST() != null || type instanceof ArrayType) {
      // array declaration
      codes.add(SUB(SP, SP, 4));
      if (in_func || inBlock) {
        symbolTable.local_variable += 4;
      }
      spPosition += 4;
      ArrayType arrayType = (ArrayType) type;
      if (rhs.getArrayAST() != null)
        visitArrayLiter(arrayType, rhs, codes, reg_counter);
    } else if (type.equals(stringType()) || type.equals(intType()) || type instanceof PtrType) {
      codes.add(SUB(SP, SP, 4));
      if (in_func || inBlock) {
        symbolTable.local_variable += 4;
      }
      spPosition += 4;
    } else if (equalsCharOrBoolType(type)) {
      codes.add(SUB(SP, SP, 1));
      if (in_func || inBlock) {
        symbolTable.local_variable += 1;
      }
      spPosition += 1;
      strWord = "\tSTRB ";
    } else if (ast.rhsNotPairElemPair()
        || (ast.getType() instanceof PairType)) {     // pair declaration (rhs not pairElemNode)
      codes.add(SUB(SP, SP, 4));
      spPosition += 4;
      if (in_func || inBlock) {
        symbolTable.local_variable += 4;
      }
      if (ast.rhsDeclaredPairOrNull()) {
        if (!(ast.getAssignRhsAST().getExpr1() instanceof IdentNode)) {
          // rhs is null
          codes.add(LDR_value(paramReg, 0));
        }
        // do nothing here if rhs is a declared pair
      } else {
        // rhs is a new pair
        if (ast.getAssignRhsAST().getPairElemNode() == null) {
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
          int size = equalsCharOrBoolType(lType) ? 1 : 4;
          String b = equalsCharOrBoolType(lType) ? "B" : "";
          codes.add(LDR_value(resultReg, size));
          codes.add(BL("malloc"));
          codes.add(!b.equals("") ? STRB("r5", "[" + resultReg + "]") : STR("r5", "[" + resultReg + "]"));
          codes.add(STR(resultReg, "[r4]"));
          int codesLength = codes.size();
          visitExprAST(ast.getAssignRhsAST().getExpr2(), codes, reg_counter);
          size = (rType.equals(Type.charType()) || rType.equals(boolType())) ? 1 : 4;
          b = rType.equals(Type.charType()) ? "B" : "";
          if (codes.size() - codesLength == 0) {
            if (rType instanceof PairType) {
              if (((PairType) rType).getLeftType() == null) {
                codes.add(LDR_value("r" + reg_counter, 0));
              }
            }
          }
          codes.add(LDR_value(resultReg, size));
          codes.add(BL("malloc"));
          codes.add(!b.equals("") ? STRB("r5", "[" + resultReg + "]") : STR("r5", "[" + resultReg + "]"));
          codes.add(STR(resultReg, "[" + paramReg + ", #4]"));
        }
      }
    } else if (ast.getAssignRhsAST().getPairElemNode() != null) {
      // pair declaration (rhs is pairElemNode)
      if (type instanceof PairType) {
        codes.add(SUB(SP, SP, 4));
        spPosition += 4;
        if (in_func) {
          symbolTable.local_variable += 4;
        }
      }
    }

    if (rhs.call()) {
      visitCallAst(rhs.getCallAST(), codes, reg_counter);
    } else if (!ast.rhsNotPairElemPair() || ast.rhsDeclaredPairOrNull()) {
      if (rhs.getExpr1() != null) {
        // rhs is a null or all other cases
        AST rhsAstExpr = ast.getAssignRhsAST().getExpr1();
        visitExprAST(ast.getAssignRhsAST().getExpr1(), codes, reg_counter);
        if (type instanceof PtrType && rhsAstExpr instanceof IdentNode ){
          if (symbolTable.getVariable(((IdentNode) rhsAstExpr).getIdent()) instanceof ArrayType) {
            codes.add("\tADD r"+reg_counter+", r"+reg_counter+", #4");
          }
        }
      } else if (ast.getAssignRhsAST().getPairElemNode() != null) {
        // rhs is a declared pair or pair elem
        int pos = symbolTable.getStackTable(ast.getAssignRhsAST().getPairElemNode().getName());
        pos = pos != -1 ? spPosition - pos : 0;
        if (pos != 0) {
          codes.add(LDR_reg(paramReg, SP + ", #" + pos));
        } else {
          codes.add(LDR_reg(paramReg, SP));
        }
        codes.add(MOV(resultReg, paramReg));
        codes.add(BL("p_check_null_pointer"));
        printCheckNullPointer = true;
        if (ast.getAssignRhsAST().getRhsContext().pair_elem().fst() != null) {
          codes.add(LDR_reg(paramReg, paramReg));
        } else {
          codes.add(LDR_reg(paramReg, paramReg + ", #4"));
        }
        if (equalsCharOrBoolType(type)) {
          codes.add(LDRSB(paramReg, paramReg));
        } else {
          codes.add(LDR_reg(paramReg, paramReg));
        }
      }
    }

    if (rhs.getExpr1() instanceof ArrayElemNode) {
      String ldrWord = "\tLDR ";
      if (equalsCharOrBoolType(type)) {
        ldrWord = "\tLDRSB ";
      }
      codes.add(ldrWord + paramReg + ", [r" + reg_counter + "]");
    }

    codes.add(strWord + paramReg + ", [sp]");
    symbolTable.putStackTable(ast.getName(), spPosition);  // record the stack position for declared variable
  }

  private void visitReturnAST(ReturnAst ast, List<String> codes, int reg_counter) {
    if (dynamically_Typed) {
      // type check
      Type returnType = functionType.get(currentFuncName).get(0);
      if (returnType.equals(intType())) {
        typeCheck(INTTYPE, findType(ast.getExpr()), codes);
      } else if (returnType.equals(charType())) {
        typeCheck(CHARTYPE, findType(ast.getExpr()), codes);
      } else if (returnType.equals(stringType())) {
        typeCheck(STRINGTYPE, findType(ast.getExpr()), codes);
      } else if (returnType instanceof PairType) {
        typeCheck(PAIRTYPE, findType(ast.getExpr()), codes);
      } else if (returnType.equals(boolType())) {
        typeCheck(BOOLTYPE, findType(ast.getExpr()), codes);
      }
    }
    visitExprAST(ast.getExpr(), codes, reg_counter);
    codes.add(MOV(resultReg, paramReg));
    return_Pop = true;
  }

  public void visitSkipAst(AST ast) {}     // skip statement simply do nothing

  public void visitStringNode(StringNode ast) {
    // generate codes for a string
    variables.add("msg_" + stringCounter + ":");
    variables.add("\t.word " + ast.getStringLength());
    variables.add("\t.ascii  " + ast.getValue());
    stringCounter++;
  }

  public void visitPrintAst(PrintAst ast, List<String> codes, int reg_counter) {
    AST expr = ast.getExpr();
    visitExprAST(expr, codes, reg_counter);

    if (dynamically_Typed) {
      //type check
      if (expr instanceof ArrayAST) {
        codes.add(LDR_value("r1", 1));
      } else {
        codes.add(LDR_value("r1", 0));
      }
      codes.add(BL("p_array_check"));
      arrayNotAccepted = true;
    }

    // add load statement when printing an array element
    if (expr instanceof ArrayElemNode) {
      Type type = symbolTable.getVariable(((ArrayElemNode) expr).getName());
      while (type instanceof ArrayType) {
        type = ((ArrayType) type).getType();
      }
      if (equalsCharOrBoolType(type)) {
        codes.add(LDRSB("r" + reg_counter, "r" + reg_counter));
      } else {
        codes.add(LDR_reg("r" + reg_counter, "r" + reg_counter));
      }
    }

    if (expr instanceof PairAST) {
      if (((PairAST) expr).ident.equals("null")) {
        codes.add(LDR_value(paramReg, 0));
      }
    }

    codes.add(MOV(resultReg, paramReg));

    // generate print code according to different types
    Type type = null;
    if (expr instanceof Binary_BoolOpNode
        || expr instanceof BoolNode
        || (expr instanceof UnaryOpNode && ((UnaryOpNode) expr).isNOT())) {
      type = boolType();
    } else if (expr instanceof StringNode) {
      type = stringType();
    } else if (expr instanceof RefNode) {
      type = symbolTable.getVariable(((RefNode) expr).getVariable());
    } else if (expr instanceof DerefNode) {
      type = symbolTable.getVariable(((DerefNode) expr).getVariable());
      if (type instanceof PtrType) {
        type = ((PtrType) type).getType();
      }
    } else if (expr instanceof IntNode
        || expr instanceof BinaryOpNode
        || (expr instanceof UnaryOpNode && ((UnaryOpNode) expr).returnInt())) {
      type = intType();
    } else if (expr instanceof CharNode
        || (expr instanceof UnaryOpNode && ((UnaryOpNode) expr).isChr())) {
      type = charType();
    } else if (expr instanceof IdentNode) {
      if (dynamically_Typed) {
        identCheck(((IdentNode) expr).getIdent(), codes);
      }
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
      } else if (type.equals(intType()) || type instanceof PtrType) {
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

  public void visitReadAST(ReadAst ast, List<String> codes, int reg_counter) {
    if (dynamically_Typed) {
      //type check
      identCheck(ast.getLhs().getName(), codes);
    }
    if (!symbolTable.getVariable(ast.getLhs().getName()).equals(errorType()) || !dynamically_Typed) {
      Type type = null;
      if (dynamically_Typed) {
        type = symbolTable.getVariable(ast.getLhs().getName());

        if (type.equals(intType())) {
          typeCheck(INTTYPE, type, codes);
        } else {
          typeCheck(CHARTYPE, type, codes);
        }
      } else {
        type = ast.getType();
      }

      String readType = null;
      if (type.equals(intType())) {
        readType = "int";
      } else if (type.equals(charType())) {
        readType = "char";
      }
      if (ast.getLhs().getLhsContext().ident() == null) {
        codes.add(LDR_reg(paramReg, SP));
        codes.add(MOV(resultReg, paramReg));
        codes.add(BL("p_check_null_pointer"));
        printCheckNullPointer = true;
        codes.add(LDR_reg(paramReg, paramReg));
      } else {
        codes.add(
            ADD(
                "r4",
                SP,
                (spPosition
                    - symbolTable.getStackTable(ast.getLhs().getLhsContext().ident().IDENT().getText()))));
      }
      codes.add(MOV(resultReg, paramReg));
      if (readType != null) {
        codes.add(BL("p_read_" + readType));
        if ((!read_int && readType.equals("int")) || (!read_char && readType.equals("char"))) {
          variables.add("msg_" + stringCounter + ":");
          variables.add("\t.word " + (readType.equals("char") ? 4 : 3));
          variables.add("\t.ascii  \"" + (readType.equals("char") ? " %c" : "%d") + "\\0\"");
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
        printcodes.add(LDR_msg(resultReg, String.valueOf((stringCounter - 1))));
        printcodes.add(ADD(resultReg, resultReg, "#4"));
        printcodes.add(BL("scanf"));
        printcodes.add(POP(PC));
      }
    }
  }

  private void visitIfAst(IfAst ast, List<String> codes, int reg_counter) {
    if (dynamically_Typed) {
      typeCheck(BOOLTYPE, findType(ast.getExpr()), codes);
    }
    SymbolTable symbolTabletemp = symbolTable;
    symbolTable = ast.getThenSymbolTable();
    symbolTable.set_local_variable(symbolTabletemp.getLocal_variable());
    symbolTable.setParamCounter(symbolTabletemp.getParamCounter());
    List<String> elseBranch = new LinkedList<>();
    visitExprAST(ast.getExpr(), codes, reg_counter);
    AST expr = ast.getExpr();
    if (expr instanceof BoolNode
        || expr instanceof Binary_BoolOpNode
        || (expr instanceof IdentNode
            && symbolTabletemp.getVariable(((IdentNode) expr).getIdent()).equals(boolType()))) {
      codes.add(CMP_value(paramReg, 0));
    } else {
      codes.add(CMP_reg("r" + (reg_counter - 1), "r" + reg_counter));
    }
    codes.add(BEQ("L" + branchCounter));
    elseBranch.add("L" + branchCounter++ + ":");
    int oldSp = spPosition;
    visitStat(ast.getThenbranch(), codes, reg_counter);

    if (spPosition - oldSp != 0) {
      codes.add(ADD(SP, SP, spPosition - oldSp));
    }
    spPosition = oldSp;

    if (return_Pop) {
      codes.add(POP(PC));
      return_Pop = false;
    }
    symbolTable = ast.getElseSymbolTable();
    symbolTable.set_local_variable(symbolTabletemp.getLocal_variable());
    symbolTable.setParamCounter(symbolTabletemp.getParamCounter());
    oldSp = spPosition;
    visitStat(ast.getElsebranch(), elseBranch, reg_counter);
    codes.add(B("L" + branchCounter));
    codes.addAll(elseBranch);

    if (spPosition - oldSp != 0) {
      codes.add(ADD(SP, SP, spPosition - oldSp));
    }
    spPosition = oldSp;

    if (return_Pop) {
      codes.add(POP(PC));
      return_Pop = false;
    }
    codes.add("L" + branchCounter++ + ":");
    symbolTable = symbolTabletemp;
  }

  private void visitDoWhileAST(DoWhileAST ast, List<String> codes, int reg_counter) {
    SymbolTable symbolTableTemp = symbolTable;
    symbolTable = ast.getSymbolTable();
    symbolTable.setParamCounter(symbolTableTemp.getParamCounter());
    int loopLabel = branchCounter++;
    int bodyLabel = branchCounter++;
    codes.add("L" + bodyLabel + ":");
    int oldSp = spPosition;
    visitStat(ast.getStat(), codes, reg_counter);
    if (spPosition - oldSp != 0) {
      codes.add(ADD(SP, SP, spPosition - oldSp));
    }
    spPosition = oldSp;
    codes.add("L" + loopLabel + ":");
    AST expr;

    if (ast.getExpr() instanceof ExprWithParen) {
      expr = ((ExprWithParen) ast.getExpr()).getExpr();
    } else {
      expr = ast.getExpr();
    }
    visitExprAST(expr, codes, reg_counter);
    if (expr instanceof BoolNode
            || expr instanceof Binary_BoolOpNode
            || expr instanceof IdentNode) {
      codes.add(CMP_value(paramReg, 1));
    } else {
      codes.add(CMP_reg(paramReg, "r" + reg_counter));
    }

    codes.add(BEQ("L" + bodyLabel));
    symbolTable = symbolTableTemp;

  }

  public void visitWhileAST(WhileAst ast, List<String> codes, int reg_counter) {
    if (dynamically_Typed) {
      typeCheck(BOOLTYPE, findType(ast.getExpr()), codes);
    }
    SymbolTable symbolTabletemp = symbolTable;
    symbolTable = ast.getSymbolTable();
    symbolTable.set_local_variable(symbolTabletemp.getLocal_variable());
    symbolTable.setParamCounter(symbolTabletemp.getParamCounter());
    int loopLabel = branchCounter++;
    int bodyLabel = branchCounter++;
    codes.add(B("L" + loopLabel));
    codes.add("L" + bodyLabel + ":");
    int oldSp = spPosition;
    visitStat(ast.getStat(), codes, reg_counter);
    if (spPosition - oldSp != 0) {
      codes.add(ADD(SP, SP, spPosition - oldSp));
    }
    spPosition = oldSp;
    codes.add("L" + loopLabel + ":");
    AST expr;
    if (ast.getExpr() instanceof ExprWithParen) {
      expr = ((ExprWithParen) ast.getExpr()).getExpr();
    } else {
      expr = ast.getExpr();
    }
    visitExprAST(expr, codes, reg_counter);
    if (expr instanceof BoolNode
        || expr instanceof Binary_BoolOpNode
        || expr instanceof IdentNode) {
      codes.add(CMP_value(paramReg, 1));
    } else {
      codes.add(CMP_reg(paramReg, "r" + reg_counter));
    }

    codes.add(BEQ("L" + bodyLabel));
    symbolTable = symbolTabletemp;
  }

  public void visitFreeAST(FreeAst ast, List<String> codes, int reg_counter) {
    if (dynamically_Typed) {
      Type actualType = findType(ast.getExpr());
      if (actualType instanceof ArrayType) {
        typeCheck(ARRAYTYPE, actualType, codes);
      } else {
        typeCheck(PAIRTYPE, actualType, codes);
      }
    }
    codes.add(LDR_reg(paramReg, SP));
    codes.add(MOV(resultReg, paramReg));
    codes.add(BL("p_free_pair"));
    printFree = true;
  }

  // for later optimization of register allocation
  public ArrayList<Register> regsExcept(ArrayList<Register> except) {
    ArrayList<Register> tempRegs = allRegs;
    for (Register r : except) {
      tempRegs.remove(r);
    }
    return tempRegs;
  }

  private boolean equalsCharOrBoolType(Type type) {
    return type.equals(boolType()) || type.equals(charType());
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

  public String LDRSB(String dst, String src) {
    return "\tLDRSB " + dst + ", [" + src + "]";
  }

  public String LDRNE_msg(String dst, String content) {
    return "\tLDRNE " + dst + ", =msg_" + content;
  }

  public String LDREQ_msg(String dst, int content) {
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

  public String ADDS(String result, String a, String b) {
    return "\tADDS " + result + ", " + a + ", " + b;
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

  public String B(String target) {
    return "\tB " + target;
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

  public String BLNE(String target) {
    return "\tBLNE " + target;
  }

  public String BLVS(String target) {
    return "\tBLVS " + target;
  }

  public String BEQ(String target) {
    return "\tBEQ " + target;
  }

  public String EOR(String a, String b, int c) {
    return "\tEOR " + a + ", " + b + ", #" + c;
  }

  public String ORR(String a, String b, String c) {
    return "\tORR " + a + ", " + b + ", " + c;
  }

  public String AND(String a, String b, String c) {
    return "\tAND " + a + ", " + b + ", " + c;
  }

  public String RSBS(String a, String b, int c) {
    return "\tRSBS " + a + ", " + b + ", #" + c;
  }

  public String SMULL(String a, String b, String c, String d) {
    return "\tSMULL " + a + ", " + b + ", " + c + ", " + d;
  }
}
