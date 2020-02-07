package doc.wacc.utils;

import antlr.BasicParser;
import antlr.BasicParserBaseVisitor;
import doc.wacc.utils.Type.ArrayType;
import doc.wacc.utils.Type.BaseType;
import doc.wacc.utils.Type.BaseTypeKind;
import doc.wacc.astNodes.*;

import static antlr.BasicParser.*;
import static doc.wacc.astNodes.AST.symbolTable;
import static doc.wacc.utils.Type.*;
import static java.lang.System.exit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CompilerVisitor extends BasicParserBaseVisitor<AST> {

  public static HashMap<String, List<Type>> functionTable = new HashMap<>();
  private boolean inFunction = false;
  private String currentFuncName = null;
  private boolean hasReturned = false;
  private boolean thenHasReturn = false;

  @Override public AST visitPair_liter(Pair_literContext ctx) {
    return new PairAST(ctx.getText(),null, null);
  }

  @Override public AST visitArray_liter(Array_literContext ctx) {
    return new ArrayAST(ctx.expr());
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
    if (ctx.int_sign() == null) {
      return new IntNode(Integer.parseInt(ctx.getText()));
    }
    if (ctx.int_sign().getText().equals("-")) {
      return new IntNode(-Integer.parseInt(ctx.getText()));
    } else {
      return new IntNode(Integer.parseInt(ctx.getText()));
    }
  }

  @Override public AST visitArray_elem(Array_elemContext ctx) {
    return visitChildren(ctx);
  }

  @Override public AST visitBinary_oper(Binary_operContext ctx) {
    return null;
  }

  @Override public AST visitUnary_oper(Unary_operContext ctx) { return visitChildren(ctx); }

  @Override public AST visitUnary_not(Unary_notContext ctx) { return visitChildren(ctx); }

  @Override public AST visitUnary_chr(Unary_chrContext ctx) { return visitChildren(ctx); }

  @Override public AST visitExpr(ExprContext ctx) {
    if (ctx.unary_oper() != null) {
      return (new UnaryOpNode(ctx.unary_oper(), visitExpr(ctx.expr(0))));
    } else
    if (ctx.unary_chr() != null) {
      return (new UnaryChrNode(ctx.unary_chr(), visitExpr(ctx.expr(0))));
    } else
    if (ctx.unary_not() != null) {
      return (new UnaryNotNode(ctx.unary_not(), visitExpr(ctx.expr(0))));
    } else
      if (ctx.lowest_binbool_op() != null) {
      return (new Lowest_BinaryOpNode(ctx.lowest_binbool_op(), visitExpr(ctx.expr(0)), visitExpr(ctx.expr(1))));
    } else
    if (ctx.binary_oper() != null) {
      return (new BinaryOpNode(ctx.binary_oper(), visitExpr(ctx.expr(0)), visitExpr(ctx.expr(1))));
    } else
    if (ctx.hignp_bin_op() != null) {
      return (new High_BinaryOpNode(ctx.hignp_bin_op(), visitExpr(ctx.expr(0)), visitExpr(ctx.expr(1))));
    } else
    if (ctx.low_binbool_op() != null) {
      return (new Low_BinaryOpNode(ctx.low_binbool_op(), visitExpr(ctx.expr(0)), visitExpr(ctx.expr(1))));
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
    return new CharNode(ctx.getText().charAt(0));
  }

  @Override public AST visitString_liter(String_literContext ctx) {
    return new StringNode(ctx.getText());
  }

  @Override public Type visitPair_type(Pair_typeContext ctx) {
    return new PairType(visitPair_elem_type(ctx.pair_elem_type(0)), visitPair_elem_type(ctx.pair_elem_type(1)));
  }

  @Override public Type visitPair_elem_type(Pair_elem_typeContext ctx) {
    if (ctx.array_type() != null) {
      return visitArray_type(ctx.array_type());
    } else if (ctx.base_type() != null) {
      return visitBase_type(ctx.base_type());
    } else {
      return new PairType(null, null);
    }
  }

  @Override public BaseType visitBase_type(Base_typeContext ctx) {
    BaseTypeKind kind = BaseTypeKind.valueOf(ctx.getText().toUpperCase());
    return new BaseType(kind);
  }

  @Override public Type visitType(TypeContext ctx) {
    if (ctx.pair_type() != null) {
      return visitPair_type(ctx.pair_type());
    } else if (ctx.array_type() != null) {
      return visitArray_type(ctx.array_type());
    } else {
      return visitBase_type(ctx.base_type());
    }
  }

  @Override public ArrayType visitArray_type(Array_typeContext ctx) {
    if (ctx.array_type() != null) {
      return new ArrayType(visitArray_type(ctx.array_type()));
    } else if (ctx.pair_type() != null) {
      return new ArrayType(visitPair_type(ctx.pair_type()));
    } else {
      return new ArrayType(visitBase_type(ctx.base_type()));
    }
  }

  @Override public AST visitPair_elem(Pair_elemContext ctx) {
    return visitChildren(ctx);
  }

  @Override public AST visitArg_list(Arg_listContext ctx) { return visitChildren(ctx); }

  @Override public AST visitAssign_rhs(BasicParser.Assign_rhsContext ctx) {
    return visitChildren(ctx);
  }

  @Override public AST visitAssign_lhs(Assign_lhsContext ctx) { return visitChildren(ctx); }

  @Override public AST visitRead(ReadContext ctx) {
    return new ReadAst(ctx.assign_lhs());
  }

  @Override public AST visitAssignment(AssignmentContext ctx) {
    if (ctx.assign_rhs().call() != null) {
      visitCall(ctx.assign_rhs().call());
    }
    return new AssignAST(ctx.assign_lhs(), ctx.assign_rhs());
  }

  @Override public AST visitIfthenesle(IfthenesleContext ctx) {
    symbolTable = new SymbolTable(symbolTable, new HashMap<>()); //go to a new scope
    symbolTable.inIfThenElse = true;

    AST thenAst = visitStat(ctx.stat(0));

    if (symbolTable.hasReturned) {
      hasReturned = symbolTable.hasReturned;
    }

    SymbolTable s = symbolTable;
    symbolTable = symbolTable.previousScope();
    symbolTable = new SymbolTable(symbolTable, new HashMap<>()); //go to a new scope
    symbolTable.inheritFlags(s);

    AST elseAST = visitStat(ctx.stat(1));
    if (symbolTable.hasReturned) {
      hasReturned = symbolTable.hasReturned;
    }

    symbolTable.thenHasReturn = false;
    symbolTable = symbolTable.previousScope();
    symbolTable.thenHasReturn = false;

    return new IfAst(visitExpr(ctx.expr()),thenAst ,elseAST);
  }

  @Override public AST visitAskip(AskipContext ctx) {
    return new ASkipAst();
  }

  @Override public AST visitDeclaration(DeclarationContext ctx) {
    if (ctx.assign_rhs().call() != null) {
      List<Type> parameter = functionTable.get(ctx.assign_rhs().IDENT().getText());

      if (parameter.size() > 1) {
        int i = 0;
        for (i = 1; i < parameter.size(); i++) {
          //to check whether the number of parameter provided is smaller than the number of parameter needed
          if (ctx.assign_rhs().arg_list().expr(i-1) == null) {
            System.out.println("semantic Error: args number not matched" +
                    "\nExit code 200 returned");
            exit(200);
          }

          AST ast = visitExpr(ctx.assign_rhs().arg_list().expr(i-1));
          Type type = parameter.get(i);

          if (ctx.assign_rhs().arg_list().expr(i-1).array_elem() == null) {
            if ((is_bool(ast) && !type.equals(boolType()) ||
                is_Char(ast) && !type.equals(charType()) ||
                is_int(ast) && !type.equals(intType())) ||
                is_String(ast) && !type.equals(stringType())) {
              System.out.println("Semantic Error: Wrong type in function parameter");
              exit(200);
            }
          } else {
            Type type1 = symbolTable.getVariable(ctx.assign_rhs().arg_list().expr(i-1).array_elem().IDENT().getText());

            if ((type1.equals(boolType()) && !type.equals(boolType()) ||
                type1.equals(charType()) && !type.equals(charType()) ||
                type1.equals(intType()) && !type.equals(intType())) ||
                type1.equals(stringType()) && !type.equals(stringType())) {
              System.out.println("Semantic Error: Wrong type in function parameter" +
                      "\nExit code 200 returned");
              exit(200);
            }
          }
        }
        //to check whether the number of parameter provided is greater than the number of parameter needed
        if (ctx.assign_rhs().arg_list().expr(i-1) != null) {
          System.out.println("semantic Error: args number not matched" +
                  "\nExit code 200 returned");
          exit(200);
        }
      }
    }
    return new DeclarationAst(visitType(ctx.type()), ctx.IDENT().getText(), ctx.assign_rhs());
  }

  @Override public AST visitWhileloop(WhileloopContext ctx) {
    symbolTable = new SymbolTable(symbolTable, new HashMap<>()); //go to a new scope
    AST ast = new WhileAst(visitExpr(ctx.expr()), visitStat(ctx.stat()));
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
    if (symbolTable.inIfThenElse) {
      if (symbolTable.thenHasReturn) {hasReturned = true;} else {symbolTable.thenHasReturn = true;}
    } else {
      hasReturned = true;
    }
    return new ExitAst(visitExpr(ctx.expr()));
  }

  @Override public AST visitPrint(PrintContext ctx) {
    return new PrintAst(visitExpr(ctx.expr()));
  }

  @Override public AST visitPrintln(PrintlnContext ctx) {
    return new PrintlnAst(visitExpr(ctx.expr()));
  }

  @Override public AST visitBlock(BlockContext ctx) {
    symbolTable = new SymbolTable(symbolTable, new HashMap<>()); //go to a new scope
    AST ast = new BlockAst(visitStat(ctx.stat()));
    symbolTable = symbolTable.previousScope();
    return ast;
  }

  @Override public AST visitFree(FreeContext ctx)  {
    AST expr = visitExpr(ctx.expr());
    if (expr instanceof IdentNode) {
      Type type = symbolTable.getVariable(((IdentNode) expr).getIdent());
      if (!(type instanceof  PairType) && !(type instanceof ArrayType)) {
        System.out.println("Can only free pair or array");
        exit(200);
      }
    } else if (!(expr instanceof ArrayAST) && !is_Pair(expr)) {
        System.out.println("Can only free pair or array");
        exit(200);
    }
    return new FreeAst(visitExpr((ctx.expr())));
  }

  @Override public AST visitCall(BasicParser.CallContext ctx) {
    return visitChildren(ctx);
  }

  @Override public AST visitReturn(BasicParser.ReturnContext ctx) {
    if (!inFunction) {
      System.out.println("Return can only be used in Function");
      exit(200);
    } else {
      if (symbolTable.inIfThenElse) {
        if (symbolTable.thenHasReturn) {hasReturned = true;} else {symbolTable.thenHasReturn = true;}
      } else {
        hasReturned = true;
      }
    }

    if (currentFuncName == null) {
      System.out.println("Return can only be used in Function");
      exit(200);
    }

    Type type = functionTable.get(currentFuncName).get(0);
    AST ast = visitExpr(ctx.expr());
    if ((type.equals(boolType())  && !is_bool(ast)) ||
        (type.equals(intType())   && !is_int(ast)) ||
        (type.equals(charType())  && !is_Char(ast)) ||
        (type.equals(stringType()) && !is_String(ast))) {
      System.out.println("return type not compatible");
      exit(200);
    }
    return new ReturnAst(visitExpr(ctx.expr()));
  }

  @Override public AST visitParam(ParamContext ctx) { return visitChildren(ctx); }

  @Override public AST visitParam_list(Param_listContext ctx) {
    return visitChildren(ctx);
  }

  @Override public AST visitFunc(BasicParser.FuncContext ctx) {
    inFunction = true;
    currentFuncName = ctx.IDENT().getText();
    symbolTable = new SymbolTable(symbolTable, new HashMap<>()); //go to a new scope
    symbolTable.inFunction = true;
    BasicParser.Param_listContext params = ctx.param_list();
    List<BasicParser.ParamContext> pa =  params == null ? new ArrayList<>() : params.param();

    for (BasicParser.ParamContext p: pa) {
      symbolTable.putVariable(p.IDENT().getText(), visitType(p.type()));
    }
    AST ast = new FuncAST(ctx.type(), ctx.IDENT().getText(), pa, visitStat(ctx.stat()));
    inFunction = false;
    symbolTable.inFunction = false;
    currentFuncName = null;

    if (!hasReturned) {
      System.out.println("Syntax error: function no return" +
              "\nExit code 100 returned");
      exit(100);
    }
    symbolTable = symbolTable.previousScope();
    thenHasReturn = false;
    hasReturned = false;

    return ast;
  }

  @Override public AST visitProg(ProgContext ctx) {
    ArrayList<FuncAST> funcASTS = new ArrayList<>();
    for (BasicParser.FuncContext funcContext:ctx.func()) {
      List<Type> types = new ArrayList<>();
      types.add(visitType(funcContext.type()));
      if (funcContext.param_list() != null) {
        for (ParamContext p : funcContext.param_list().param()) {
          types.add(visitType(p.type()));
        }
      }
      if (functionTable.get(funcContext.IDENT().getText()) != null) {
        System.out.println("Semantic error: function redefined" +
                "\nExit code 200 returned");
        exit(200);
      }
      functionTable.put(funcContext.IDENT().getText(), types);
    }
    for (BasicParser.FuncContext funcContext:ctx.func()) {
      funcASTS.add((FuncAST) visitFunc(funcContext));
    }

    return new ProgramAST(funcASTS, visitStat(ctx.stat()));
  }


  public AST visitStat(StatContext statContext) {
    if (symbolTable.inFunction && hasReturned) {
      System.out.println("Syntax Error: Shouldn't be anything after return" +
              "\nExit code 100 returned");
      exit(100);
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
