package doc.wacc.utils;

import antlr.BasicLexer;
import antlr.BasicParser;
import antlr.BasicParserBaseVisitor;
import doc.wacc.utils.Type.ArrayType;
import doc.wacc.utils.Type.BaseType;
import doc.wacc.utils.Type.BaseTypeKind;
import doc.wacc.astNodes.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;

import static antlr.BasicParser.*;
import static doc.wacc.astNodes.AST.symbolTable;
import static doc.wacc.astNodes.AssignAST.*;
import static doc.wacc.utils.Type.*;
import static java.lang.System.exit;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class CompilerVisitor extends BasicParserBaseVisitor<AST> {

  public static HashMap<String, List<Type>> functionTable = new HashMap<>();
  private boolean inFunction = false;
  private String currentFuncName = null;
  private boolean hasReturned = false;
  private boolean thenHasReturn = false;
  public static int currentLine = 0;     // global variable for reporting error message
  public static int currentCharPos = 0;  // global variable for reporting error message

  @Override public AST visitPair_liter(Pair_literContext ctx) {
    return new PairAST(ctx.getText(),null, null);
  }

  @Override public AST visitArray_liter(Array_literContext ctx) {
    List<AST> exprs = new ArrayList<>();
    for (ExprContext e : ctx.expr()) {
      exprs.add(visitExpr(e));
    }
    return new ArrayAST(exprs);
  }

  @Override public AST visitBool_liter(Bool_literContext ctx) {
    if (ctx.getText().equals("false")) {
      return new BoolNode(false);
    } else {
      if (ctx.getText().equals("true")){
        return new BoolNode(true);
      }
    }
    return null;
  }

  @Override public AST visitInt_sign(Int_signContext ctx) { return visitChildren(ctx); }

  @Override public AST visitInt_liter(Int_literContext ctx) {
    if (ctx.BINARY()!=null) {
      String binary = ctx.getText().substring(2);
      int result = 0;
      for (int i = 0; i < binary.length(); i++) {
        result += Math.pow(2, i) * Integer.parseInt(String.valueOf(binary.charAt(binary.length()-i-1)));
      }
      return new IntNode(result);
    } else if (ctx.HEXADECIMAL()!=null) {
      String hexadecimal = ctx.getText().substring(2);
      return new IntNode(Integer.parseInt(hexadecimal, 16));
    } else {
      return new IntNode(Integer.parseInt(ctx.getText()));
    }
  }

  @Override public AST visitArray_elem(Array_elemContext ctx) {
    List<AST> nestedArray = new ArrayList<>();
    for (ExprContext e: ctx.expr()) {
      nestedArray.add(visitExpr(e));
    }

    return new ArrayElemNode(ctx.IDENT().getText(), nestedArray);
  }

  @Override public AST visitBinary_oper(Binary_operContext ctx) {
    return null;
  }

  @Override public AST visitUnary_oper(Unary_operContext ctx) { return visitChildren(ctx); }

  @Override public AST visitUnary_not(Unary_notContext ctx) { return visitChildren(ctx); }

  @Override public AST visitUnary_chr(Unary_chrContext ctx) { return visitChildren(ctx); }

  @Override public AST visitExpr(ExprContext ctx) {
    currentLine = ctx.getStart().getLine();
    currentCharPos = ctx.getStart().getCharPositionInLine();
    if (ctx.unary_oper() != null) {
      return (new UnaryOpNode(ctx.unary_oper(), visitExpr(ctx.expr(0))));
    } else
    if (ctx.unary_chr() != null) {
      return (new UnaryOpNode(ctx.unary_chr(), visitExpr(ctx.expr(0))));
    } else
    if (ctx.unary_not() != null) {
      return (new UnaryOpNode(ctx.unary_not(), visitExpr(ctx.expr(0))));
    } else
      if (ctx.lowest_binbool_op() != null) {
      return (new Binary_BoolOpNode(ctx.lowest_binbool_op(), visitExpr(ctx.expr(0)), visitExpr(ctx.expr(1))));
    } else
    if (ctx.binary_oper() != null) {
      return (new BinaryOpNode(ctx.binary_oper(), visitExpr(ctx.expr(0)), visitExpr(ctx.expr(1))));
    } else
    if (ctx.hignp_bin_op() != null) {
      return (new BinaryOpNode(ctx.hignp_bin_op(), visitExpr(ctx.expr(0)), visitExpr(ctx.expr(1))));
    } else
    if (ctx.low_binbool_op() != null) {
      return (new Binary_BoolOpNode(ctx.low_binbool_op(), visitExpr(ctx.expr(0)), visitExpr(ctx.expr(1))));
    } else
    if (ctx.binary_bool_oper() != null) {
      return (new Binary_BoolOpNode(ctx.binary_bool_oper(), visitExpr(ctx.expr(0)), visitExpr(ctx.expr(1))));
    } else
    if (ctx.int_liter() != null) {
      return (visitInt_liter(ctx.int_liter()));
    } else
    if (ctx.bool_liter() != null) {
      return (visitBool_liter(ctx.bool_liter()));
    } else
    if (ctx.char_liter() != null) {
      return (visitChar_liter(ctx.char_liter()));
    } else
    if (ctx.pair_liter() != null) {
      return (visitPair_liter(ctx.pair_liter()));
    } else
    if (ctx.string_liter() != null) {
      return (visitString_liter(ctx.string_liter()));
    } else
    if (ctx.array_elem() != null) {
      return (visitArray_elem(ctx.array_elem()));
    } else
    if (ctx.string_liter() != null) {
      return (visitString_liter(ctx.string_liter()));
    } else
    if (ctx.ident() != null) {
      return (visitIdent(ctx.ident()));
    } else
    if (ctx.OPEN_PARENTHESES() != null) {
      return new ExprWithParen(visitExpr(ctx.expr(0)));
    } else
    if (ctx.array_elem() != null) {
      return visitArray_elem(ctx.array_elem());
    }
    return null;
  }

  @Override public AST visitIdent(IdentContext ctx) {
    return new IdentNode(ctx.getText());
  }

  @Override public AST visitChar_liter(Char_literContext ctx) {
    return new CharNode(ctx.getText());
  }

  @Override public AST visitString_liter(String_literContext ctx) {
    return new StringNode(ctx.getText());
  }

  @Override public Type visitPair_type(Pair_typeContext ctx) {
    currentLine = ctx.getStart().getLine();
    currentCharPos = ctx.getStart().getCharPositionInLine();
    return new PairType(visitPair_elem_type(ctx.pair_elem_type(0)), visitPair_elem_type(ctx.pair_elem_type(1)));
  }

  @Override public Type visitPair_elem_type(Pair_elem_typeContext ctx) {
    currentLine = ctx.getStart().getLine();
    currentCharPos = ctx.getStart().getCharPositionInLine();
    if (ctx.array_type() != null) {
      return visitArray_type(ctx.array_type());
    } else if (ctx.base_type() != null) {
      return visitBase_type(ctx.base_type());
    } else {
      return new PairType(null, null);
    }
  }

  @Override public BaseType visitBase_type(Base_typeContext ctx) {
    currentLine = ctx.getStart().getLine();
    currentCharPos = ctx.getStart().getCharPositionInLine();
    BaseTypeKind kind = BaseTypeKind.valueOf(ctx.getText().toUpperCase());
    return new BaseType(kind);
  }

  @Override public Type visitType(TypeContext ctx) {
    currentLine = ctx.getStart().getLine();
    currentCharPos = ctx.getStart().getCharPositionInLine();
    if (ctx.pair_type() != null) {
      return visitPair_type(ctx.pair_type());
    } else if (ctx.array_type() != null) {
      return visitArray_type(ctx.array_type());
    } else {
      return visitBase_type(ctx.base_type());
    }
  }

  @Override public ArrayType visitArray_type(Array_typeContext ctx) {
    currentLine = ctx.getStart().getLine();
    currentCharPos = ctx.getStart().getCharPositionInLine();
    if (ctx.array_type() != null) {
      return new ArrayType(visitArray_type(ctx.array_type()));
    } else if (ctx.pair_type() != null) {
      return new ArrayType(visitPair_type(ctx.pair_type()));
    } else {
      return new ArrayType(visitBase_type(ctx.base_type()));
    }
  }

  @Override public PairElemNode visitPair_elem(Pair_elemContext ctx) {
    return new PairElemNode(ctx.stop.getText());
  }

  @Override public AST visitArg_list(Arg_listContext ctx) { return visitChildren(ctx); }

  @Override public AssignRHSAST visitAssign_rhs(Assign_rhsContext ctx) {
    return new AssignRHSAST(ctx);
  }

  @Override public AssignLHSAST visitAssign_lhs(Assign_lhsContext ctx) {
    return new AssignLHSAST(ctx);
  }

  @Override public AST visitRead(ReadContext ctx) {
    currentLine = ctx.getStart().getLine();
    currentCharPos = ctx.getStart().getCharPositionInLine();
    return new ReadAst(ctx);
  }

  @Override public AST visitAssignment(AssignmentContext ctx) {
    currentLine = ctx.getStart().getLine();
    currentCharPos = ctx.getStart().getCharPositionInLine();
    if (ctx.assign_rhs().call() != null) {
      visitCall(ctx.assign_rhs().call());
    }
    return new AssignAST(visitAssign_lhs(ctx.assign_lhs()), visitAssign_rhs(ctx.assign_rhs()));
  }

  @Override public AST visitIfthenesle(IfthenesleContext ctx) {
    symbolTable = new SymbolTable(symbolTable, new HashMap<>()); //go to a new scope
    SymbolTable thenSymbolTable = symbolTable;
    currentLine = ctx.getStart().getLine();
    currentCharPos = ctx.getStart().getCharPositionInLine();
    symbolTable.inIfThenElse = true;

    AST thenAst = visitStat(ctx.stat(0));

    if (symbolTable.hasReturned) {
      hasReturned = symbolTable.hasReturned;
    }

    SymbolTable s = symbolTable;
    symbolTable = symbolTable.previousScope();
    symbolTable = new SymbolTable(symbolTable, new HashMap<>()); //go to a new scope
    SymbolTable elseSymbolTable = symbolTable;
    symbolTable.inheritFlags(s);

    AST elseAST = visitStat(ctx.stat(1));
    if (symbolTable.hasReturned) {
      hasReturned = symbolTable.hasReturned;
    }

    symbolTable.thenHasReturn = false;
    symbolTable = symbolTable.previousScope();
    symbolTable.thenHasReturn = false;

    IfAst if_Ast= new IfAst(visitExpr(ctx.expr()),thenAst ,elseAST, thenSymbolTable);
    if_Ast.setElseSymbolTable(elseSymbolTable);

    return if_Ast;
  }

  @Override public AST visitAskip(AskipContext ctx) {
    return new SkipAst();
  }

  @Override public AST visitDeclaration(DeclarationContext ctx) {
    currentLine = ctx.getStart().getLine();
    currentCharPos = ctx.getStart().getCharPositionInLine();
    if (ctx.assign_rhs().call() != null) {
      List<Type> parameter = functionTable.get(ctx.assign_rhs().IDENT().getText());

      if (parameter.size() > 1) {
        int i = 0;
        for (i = 1; i < parameter.size(); i++) {
          //to check whether the number of parameter provided is smaller than the number of parameter needed
          if (ctx.assign_rhs().arg_list().expr(i-1) == null) {
            ErrorMessage.addSemanticError("args number not matched" +
                " at line:" + currentLine + ":" +
                currentCharPos + " -- " +
                "in function " + ctx.assign_rhs().IDENT().getText() + ";");
            return new ErrorAST();
          }

          AST ast = visitExpr(ctx.assign_rhs().arg_list().expr(i-1));
          Type type = parameter.get(i);

          if (ctx.assign_rhs().arg_list().expr(i-1).array_elem() == null) {
          if ((is_bool(ast) && !type.equals(boolType()) ||
              is_Char(ast) && !type.equals(charType()) ||
              is_int(ast) && !type.equals(intType())) ||
              is_String(ast) && !type.equals(stringType())) {
            ErrorMessage.addSemanticError("Wrong type in function parameter" +
                    " at line:" + currentLine + ":" +
                    currentCharPos + " -- " +
                    "in function " + ctx.assign_rhs().IDENT().getText() + ";");
            return new ErrorAST();
          }} else {
            Type type1 = symbolTable.getVariable(ctx.assign_rhs().arg_list().expr(i-1).array_elem().IDENT().getText());

            if ((type1.equals(boolType()) && !type.equals(boolType()) ||
                type1.equals(charType()) && !type.equals(charType()) ||
                type1.equals(intType()) && !type.equals(intType())) ||
                type1.equals(stringType()) && !type.equals(stringType())) {
              ErrorMessage.addSemanticError("Wrong type in function parameter");
              return new ErrorAST();
            }
          }
        }
        //to check whether the number of parameter provided is greater than the number of parameter needed
        if (ctx.assign_rhs().arg_list().expr(i-1) != null) {
          ErrorMessage.addSemanticError("args number not matched. " +
                  " at line:" + currentLine + ":" +
                  currentCharPos + " -- " +
                  "in function " + ctx.assign_rhs().IDENT().getText() + ";");
          return new ErrorAST();
        }
      }
    }
    return new DeclarationAst(visitType(ctx.type()), ctx.IDENT().getText(), visitAssign_rhs(ctx.assign_rhs()));
  }

  @Override public AST visitWhileloop(WhileloopContext ctx) {
    symbolTable = new SymbolTable(symbolTable, new HashMap<>()); //go to a new scope
    currentLine = ctx.getStart().getLine();
    currentCharPos = ctx.getStart().getCharPositionInLine();
    AST ast = new WhileAst(visitExpr(ctx.expr()), visitStat(ctx.stat()), symbolTable);
    symbolTable = symbolTable.previousScope();
    return ast;
  }

  @Override
  public AST visitDowhileloop(DowhileloopContext ctx) {
    symbolTable = new SymbolTable(symbolTable, new HashMap<>()); //go to a new scope
    currentLine = ctx.getStart().getLine();
    currentCharPos = ctx.getStart().getCharPositionInLine();
    AST ast = new DoWhileAST(visitStat(ctx.stat()), visitExpr(ctx.expr()), symbolTable);
    symbolTable = symbolTable.previousScope();
    return ast;
  }

  @Override public AST visitSeq_compose(Seq_composeContext ctx) {
    ArrayList<AST> seqs = new ArrayList<>();
    StatContext statContext1 = ctx.stat(ctx.stat().size());

    for (StatContext statContext:ctx.stat()) {
      seqs.add(visitStat(statContext));
    }
    return new SeqStateAst(seqs);
  }

  @Override public AST visitExit(ExitContext ctx) {
    currentLine = ctx.getStart().getLine();
    currentCharPos = ctx.getStart().getCharPositionInLine();
    if (symbolTable.inIfThenElse) {
      if (symbolTable.thenHasReturn) {hasReturned = true;} else {symbolTable.thenHasReturn = true;}
    } else {
      hasReturned = true;
    }
    return new ExitAst(visitExpr(ctx.expr()));
  }

  @Override public AST visitPrint(PrintContext ctx) {
    currentLine = ctx.getStart().getLine();
    currentCharPos = ctx.getStart().getCharPositionInLine();
    return new PrintAst(visitExpr(ctx.expr()));
  }

  @Override public AST visitPrintln(PrintlnContext ctx) {
    currentLine = ctx.getStart().getLine();
    currentCharPos = ctx.getStart().getCharPositionInLine();
    return new PrintlnAst(visitExpr(ctx.expr()));
  }

  @Override public AST visitBlock(BlockContext ctx) {
    symbolTable = new SymbolTable(symbolTable, new HashMap<>()); //go to a new scope
    AST stat = visitStat(ctx.stat());
    List<AST> stats = new ArrayList<>();
    if (stat instanceof SeqStateAst) {
      stats = ((SeqStateAst) stat).getSeqs();
    } else {
      stats.add(stat);
    }
    AST ast = new BlockAst(stats, symbolTable);
    symbolTable = symbolTable.previousScope();
    return ast;
  }

  @Override public AST visitFree(FreeContext ctx)  {
    currentLine = ctx.getStart().getLine();
    currentCharPos = ctx.getStart().getCharPositionInLine();
    AST expr = visitExpr(ctx.expr());
    if (expr instanceof IdentNode) {
      Type type = symbolTable.getVariable(((IdentNode) expr).getIdent());
      if (!(type instanceof  PairType) && !(type instanceof ArrayType)) {
        ErrorMessage.addSemanticError("Can only free pair or array" +
                " at line:" + currentLine + ":" +
                currentCharPos);
        return new ErrorAST();
      }
    } else if (!(expr instanceof ArrayAST) && !is_Pair(expr)) {
      ErrorMessage.addSemanticError("Can only free pair or array" +
                " at line:" + currentLine + ":" +
                currentCharPos);
      return new ErrorAST();
    }
    return new FreeAst(visitExpr((ctx.expr())));
  }

  @Override public AST visitCall(CallContext ctx) {
    return visitChildren(ctx);
  }

  @Override public AST visitReturn(ReturnContext ctx) {
    currentLine = ctx.getStart().getLine();
    currentCharPos = ctx.getStart().getCharPositionInLine();
    if (!inFunction) {
      ErrorMessage.addSemanticError("Return can only be used in Function" +
              " at line:" + currentLine + ":" +
              currentCharPos);
      return new ErrorAST();
    } else {
      if (symbolTable.inIfThenElse) {
        if (symbolTable.thenHasReturn) {hasReturned = true;} else {symbolTable.thenHasReturn = true;}
      } else {
        hasReturned = true;
      }
    }

    if (currentFuncName == null) {
      ErrorMessage.addSemanticError("Return can only be used in Function"+
              " at line:" + currentLine + ":" +
              currentCharPos);
      return new ErrorAST();
    }

    Type type = functionTable.get(currentFuncName).get(0);
    AST ast = visitExpr(ctx.expr());
    if ((type.equals(boolType())  && !is_bool(ast)) ||
        (type.equals(intType())   && !is_int(ast)) ||
        (type.equals(charType())  && !is_Char(ast)) ||
        (type.equals(stringType()) && !is_String(ast))) {
      ErrorMessage.addSemanticError("return type not compatible"+
              " at line:" + currentLine + ":" +
              currentCharPos);
      return new ErrorAST();
    }
    return new ReturnAst(visitExpr(ctx.expr()));
  }

  @Override public AST visitParam(ParamContext ctx) { return visitChildren(ctx); }

  @Override public AST visitParam_list(Param_listContext ctx) {
    return visitChildren(ctx);
  }

  @Override
  public AST visitLibraries(LibrariesContext ctx) {
    currentLine = ctx.getStart().getLine();
    currentCharPos = ctx.getStart().getCharPositionInLine();

    ProgramAST progInLib = getProgramFromWACC("wacc_standard_libraries/" + ctx.IDENT().getText() + ".wacc");

    return new LibAST(ctx.IDENT().getText(), progInLib.getFunctions());

//    for (FuncContext funcContext:ctx.func()) {
//      List<Type> types = new ArrayList<>();
//      types.add(visitType(funcContext.type()));
//      if (funcContext.param_list() != null) {
//        for (ParamContext p : funcContext.param_list().param()) {
//          types.add(visitType(p.type()));
//        }
//      }
//      if (functionTable.get(funcContext.IDENT().getText()) != null) {
//        ErrorMessage.addSemanticError("function redefined" +
//                " at line:" + currentLine + ":" +
//                currentCharPos + " -- " +
//                "in function " + ctx.getText() + ";");
//        return new ErrorAST();
//      }
//      functionTable.put(funcContext.IDENT().getText(), types);
//    }
//    for (FuncContext funcContext:ctx.func()) {
//      AST temp = visitFunc(funcContext);
//      if (!(temp instanceof ErrorAST)) {
//        funcASTS.add((FuncAST) visitFunc(funcContext));
//      }
//    }
  }

  public ProgramAST getProgramFromWACC(String path) {
    StringBuilder sb = new StringBuilder();
    try {
      // the file to be opened for reading
      FileInputStream fis = new FileInputStream(path);
      Scanner sc = new Scanner(fis); // file to be scanned
      // returns true if there is another line to read
      while (sc.hasNextLine()) {
        sb.append(sc.nextLine()).append("\n");
      }
      sc.close(); // closes the scanner
    } catch (IOException e) {
      e.printStackTrace();
    }
    ANTLRInputStream input = new ANTLRInputStream(sb.toString());
    BasicLexer lexer = new BasicLexer(input);

    ANTLRErrorListener errorListener = new ANTLRErrorListener() {
      @Override
      public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine,
                              String msg, RecognitionException e) {
        System.out.println("Syntax Error: parse error in ANTLR error listener\n" +
                "\tat line "+line+":"+charPositionInLine+" \n\tat "+
                offendingSymbol+": "+msg +
                "\nExit code 100 returned");
        exit(100);
      }

      @Override
      public void reportAmbiguity(Parser parser, DFA dfa, int i, int i1, boolean b, BitSet bitSet, ATNConfigSet atnConfigSet) {

      }

      @Override
      public void reportAttemptingFullContext(Parser parser, DFA dfa, int i, int i1, BitSet bitSet, ATNConfigSet atnConfigSet) {

      }

      @Override
      public void reportContextSensitivity(Parser parser, DFA dfa, int i, int i1, int i2, ATNConfigSet atnConfigSet) {

      }
    };

    lexer.removeErrorListeners();
    lexer.addErrorListener(errorListener);
    CommonTokenStream stream = new CommonTokenStream(lexer);

    try {
      BasicParser basicParser = new BasicParser(stream);
      basicParser.addErrorListener(errorListener);
      CompilerVisitor visitor = new CompilerVisitor();

      System.out.println("Compiling from source: " + path + ":");
      AST ast = visitor.visitProg(basicParser.prog());
//      System.out.println(ast);
      ProgramAST progAST = (ProgramAST) ast;
      return progAST;
    } catch (NumberFormatException e) {
      ErrorMessage.addSyntaxError("Integer overflow");
    }
    ErrorMessage.errorWriter();
    return null;
  }

  @Override public AST visitFunc(FuncContext ctx) {
    currentLine = ctx.getStart().getLine();
    currentCharPos = ctx.getStart().getCharPositionInLine();
    inFunction = true;
    currentFuncName = ctx.IDENT().getText();
    symbolTable = new SymbolTable(symbolTable, new HashMap<>()); //go to a new scope
    symbolTable.inFunction = true;
    Param_listContext params = ctx.param_list();
    List<ParamContext> pa =  params == null ? new ArrayList<>() : params.param();

    for (ParamContext p: pa) {
      symbolTable.putVariable(p.IDENT().getText(), visitType(p.type()));
    }
    AST ast = new FuncAST(ctx.type(), ctx.IDENT().getText(), pa, visitStat(ctx.stat()), symbolTable);
    inFunction = false;
    symbolTable.inFunction = false;
    currentFuncName = null;

    if (!hasReturned) {
      ErrorMessage.addSyntaxError("function no return" +
              " at line:" + currentLine + ":" +
              currentCharPos);
      return new ErrorAST();
    }
    symbolTable = symbolTable.previousScope();
    thenHasReturn = false;
    hasReturned = false;

    return ast;
  }

  @Override public AST visitProg(ProgContext ctx) {
    currentLine = ctx.getStart().getLine();
    currentCharPos = ctx.getStart().getCharPositionInLine();
    ArrayList<FuncAST> funcASTS = new ArrayList<>();

    // GG's extension: adding imported libraries...
    ArrayList<LibAST> libASTS = new ArrayList<>();

    for (LibrariesContext libCxt : ctx.libraries()) {
      AST temp = visitLibraries(libCxt);
      if (!(temp instanceof ErrorAST)) {
         libASTS.add((LibAST) temp);
      }
    }

    for (FuncContext funcContext:ctx.func()) {
      List<Type> types = new ArrayList<>();
      types.add(visitType(funcContext.type()));
      if (funcContext.param_list() != null) {
        for (ParamContext p : funcContext.param_list().param()) {
          types.add(visitType(p.type()));
        }
      }
      if (functionTable.get(funcContext.IDENT().getText()) != null) {
        ErrorMessage.addSemanticError("function redefined" +
                " at line:" + currentLine + ":" +
                currentCharPos + " -- " +
                "in function " + ctx.getText() + ";");
        return new ErrorAST();
      }
      functionTable.put(funcContext.IDENT().getText(), types);
    }
    for (FuncContext funcContext:ctx.func()) {
      AST temp = visitFunc(funcContext);
      if (!(temp instanceof ErrorAST)) {
        funcASTS.add((FuncAST) visitFunc(funcContext));
      }
    }


    return new ProgramAST(libASTS, funcASTS, visitStat(ctx.stat()));
  }


  public AST visitStat(StatContext statContext) {
    currentLine = statContext.getStart().getLine();
    currentCharPos = statContext.getStart().getCharPositionInLine();
    if (symbolTable.inFunction && hasReturned) {
      ErrorMessage.addSyntaxError("Shouldn't be anything after return" +
              " at line:" + statContext.getStart().getLine() + ":" +
              statContext.getStart().getCharPositionInLine());
      return new ErrorAST();
    }

    if (statContext != null) {
      if (statContext instanceof ReadContext) {
        return visitRead((ReadContext) statContext);
      }
      if (statContext instanceof AskipContext) {
        return visitAskip((AskipContext) statContext);
      }
      if (statContext instanceof DeclarationContext) {
        return visitDeclaration((DeclarationContext) statContext);
      }
      if (statContext instanceof AssignmentContext) {
        return visitAssignment((AssignmentContext) statContext);
      }
      if (statContext instanceof FreeContext) {
        return visitFree((FreeContext) statContext);
      }
      if (statContext instanceof ReturnContext) {
        return visitReturn((ReturnContext) statContext);
      }
      if (statContext instanceof ExitContext) {
        return visitExit((ExitContext) statContext);
      }
      if (statContext instanceof PrintContext) {
        return visitPrint((PrintContext) statContext);
      }
      if (statContext instanceof PrintlnContext) {
        return visitPrintln((PrintlnContext) statContext);
      }
      if (statContext instanceof IfthenesleContext) {
        return visitIfthenesle((IfthenesleContext) statContext);
      }
      if (statContext instanceof WhileloopContext) {
        return visitWhileloop((WhileloopContext) statContext);
      }
      if (statContext instanceof DowhileloopContext) {
        return visitDowhileloop((DowhileloopContext) statContext);
      }
      if (statContext instanceof BlockContext) {
        return visitBlock((BlockContext) statContext);
      }
      if (statContext instanceof Seq_composeContext) {
        return visitSeq_compose((Seq_composeContext) statContext);
      }
    }
    return null;
  }
}
