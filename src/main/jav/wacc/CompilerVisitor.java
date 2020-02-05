package jav.wacc;

import antlr.BasicParserBaseVisitor;
import jav.wacc.Type.ArrayType;
import jav.wacc.Type.BaseType;
import jav.wacc.Type.BaseTypeKind;

import static antlr.BasicParser.*;
import static jav.wacc.AST.symbolTable;
import static jav.wacc.Type.*;

import java.util.ArrayList;

public class CompilerVisitor extends BasicParserBaseVisitor<AST> {
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitPair_liter(Pair_literContext ctx) {
    return new PairAST(null, null);
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitArray_liter(Array_literContext ctx) {
    return new ArrayAST(ctx.expr());
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
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
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitInt_sign(Int_signContext ctx) { return visitChildren(ctx); }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
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
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitArray_elem(Array_elemContext ctx) { return visitChildren(ctx); }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitBinary_oper(Binary_operContext ctx) {
    return null;

  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitUnary_oper(Unary_operContext ctx) { return visitChildren(ctx); }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitUnary_not(Unary_notContext ctx) { return visitChildren(ctx); }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitUnary_chr(Unary_chrContext ctx) { return visitChildren(ctx); }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
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
    return null;
  }

  @Override public AST visitIdent(IdentContext ctx) {
    return new IdentNode(ctx.getText());
  }

  @Override public AST visitChar_liter(Char_literContext ctx) {
    return new CharNode(ctx.getText().charAt(0));
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitString_liter(String_literContext ctx) { return new StringNode(ctx.getText());
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public Type visitPair_type(Pair_typeContext ctx) {
    return new PairType(visitPair_elem_type(ctx.pair_elem_type(0)), visitPair_elem_type(ctx.pair_elem_type(1)));
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public Type visitPair_elem_type(Pair_elem_typeContext ctx) {
    if (ctx.array_type() != null) {
      return visitArray_type(ctx.array_type());
    } else {
      return visitBase_type(ctx.base_type());
    }
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public BaseType visitBase_type(Base_typeContext ctx) {
    BaseTypeKind kind = BaseTypeKind.valueOf(ctx.getText().toUpperCase());
    return new BaseType(kind);
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public Type visitType(TypeContext ctx) {
    if (ctx.pair_type() != null) {
      return visitPair_type(ctx.pair_type());
    } else if (ctx.array_type() != null) {
      return visitArray_type(ctx.array_type());
    } else {
      return visitBase_type(ctx.base_type());
    }
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public ArrayType visitArray_type(Array_typeContext ctx) {
    if (ctx.array_type() != null) {
      return new ArrayType(visitArray_type(ctx.array_type()));
    } else {
      return new ArrayType(visitBase_type(ctx.base_type()));
    }
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitPair_elem(Pair_elemContext ctx) { return visitChildren(ctx); }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitArg_list(Arg_listContext ctx) { return visitChildren(ctx); }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitAssign_rhs(Assign_rhsContext ctx) { return visitChildren(ctx); }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitAssign_lhs(Assign_lhsContext ctx) { return visitChildren(ctx); }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitRead(ReadContext ctx) {
    return new ReadAst(ctx.assign_lhs());
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitAssignment(AssignmentContext ctx) {
    return new AssignAST(ctx.assign_lhs(), ctx.assign_rhs());
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitIfthenesle(IfthenesleContext ctx) {
    return new IfAst(visitExpr(ctx.expr()), visitStat(ctx.stat(0)), visitStat(ctx.stat(1)));
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitAskip(AskipContext ctx) {
    return new ASkipAst();
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitDeclaration(DeclarationContext ctx) {
    return new DeclarationAst(visitType(ctx.type()), ctx.IDENT().getText(), ctx.assign_rhs());
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitWhileloop(WhileloopContext ctx) {
    return new WhileAst(visitExpr(ctx.expr()), visitStat(ctx.stat()));
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitSeq_compose(Seq_composeContext ctx) {
    ArrayList<AST> seqs = new ArrayList<>();
    for (StatContext statContext:ctx.stat()) {
      seqs.add(visitStat(statContext));
    }
    return new SeqStateAst(seqs);
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitExit(ExitContext ctx) {
    return new ExitAst(visitExpr(ctx.expr()));
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitPrint(PrintContext ctx) {
    return new PrintAst(visitExpr(ctx.expr()));
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitPrintln(PrintlnContext ctx) {
    return new PrintlnAst(visitExpr(ctx.expr()));
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitBlock(BlockContext ctx) {
    return new BlockAst(visitStat(ctx.stat()));
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitFree(FreeContext ctx) {
    return new FreeAst(visitExpr(ctx.expr()));
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitReturn(ReturnContext ctx) {
    return new ReturnAst(visitExpr(ctx.expr()));
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitParam(ParamContext ctx) { return visitChildren(ctx); }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitParam_list(Param_listContext ctx) {

    return visitChildren(ctx);
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitFunc(FuncContext ctx) {
    Param_listContext params = ctx.param_list();
    for (ParamContext p: ctx.param_list().param()) {
      symbolTable.getCurrentSymbolTable().put(p.IDENT().getText(), visitType(p.type()));
    }
    return new FuncAST(ctx.type(), ctx.IDENT().getText(), params == null ? new ArrayList<>() : params.param(), visitStat(ctx.stat()));
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitProg(ProgContext ctx) {
    ArrayList<FuncAST> funcASTS = new ArrayList<>();
    for (FuncContext funcContext:ctx.func()) {
      funcASTS.add((FuncAST) visitFunc(funcContext));
    }
    return new ProgramAST(funcASTS, visitStat(ctx.stat()));
  }


  public AST visitStat(StatContext statContext) {
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
