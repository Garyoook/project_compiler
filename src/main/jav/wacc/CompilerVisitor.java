package jav.wacc;

import antlr.BasicParser;
import antlr.BasicParserBaseVisitor;

import java.beans.Visibility;
import java.util.ArrayList;

public class CompilerVisitor extends BasicParserBaseVisitor<AST> {
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitPair_liter(BasicParser.Pair_literContext ctx) {
    return visitChildren(ctx);
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitArray_liter(BasicParser.Array_literContext ctx) { return visitChildren(ctx); }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitBool_liter(BasicParser.Bool_literContext ctx) {
    if (ctx.getText().equals("false")) {
      return new AST.BoolNode(false);
    } else {
      if (ctx.getText().equals("true")){
        return new AST.BoolNode(true);
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
  @Override public AST visitInt_sign(BasicParser.Int_signContext ctx) { return visitChildren(ctx); }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitInt_liter(BasicParser.Int_literContext ctx) {
    if (ctx.int_sign() == null) {
      return new AST.IntNode(Integer.parseInt(ctx.getText()));
    }
    if (ctx.int_sign().getText().equals("-")) {
      return new AST.IntNode(-Integer.parseInt(ctx.getText()));
    } else {
      return new AST.IntNode(Integer.parseInt(ctx.getText()));
    }
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitArray_elem(BasicParser.Array_elemContext ctx) { return visitChildren(ctx); }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitBinary_oper(BasicParser.Binary_operContext ctx) {
    return null;

  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitUnary_oper(BasicParser.Unary_operContext ctx) { return visitChildren(ctx); }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitExpr(BasicParser.ExprContext ctx) {
    if (ctx.unary_oper() != null) {
      return (new AST.Unaryop_node(ctx.unary_oper(), visitExpr(ctx.expr(0))));
    } else
    if (ctx.binary_oper() != null) {
      return (new AST.BinaryOp_node(ctx.binary_oper(), visitExpr(ctx.expr(0)), visitExpr(ctx.expr(1))));
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
    if (ctx.array_elem() != null) {
      return (visitArray_elem(ctx.array_elem()));
    } else
    if (ctx.ident() != null) {
      return (visitIdent(ctx.ident()));
    } else
    if (ctx.OPEN_PARENTHESES() != null) {
      return new AST.ExprWithParen(visitExpr(ctx.expr(0)));
    } else
    return null;
  }

  @Override public AST visitIdent(BasicParser.IdentContext ctx) {
    return new AST.IdenNode(ctx.getText());
  }

  @Override public AST visitChar_liter(BasicParser.Char_literContext ctx) {
    return new AST.CharNode(ctx.getText().charAt(0));
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitString_liter(BasicParser.String_literContext ctx) {
    return new AST.StringNode(ctx.getText());
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
  @Override public AST visitPair_type(BasicParser.Pair_typeContext ctx) { return visitChildren(ctx); }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitPair_elem_type(BasicParser.Pair_elem_typeContext ctx) { return visitChildren(ctx); }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitBase_type(BasicParser.Base_typeContext ctx) { return visitChildren(ctx); }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitType(BasicParser.TypeContext ctx) { return visitChildren(ctx); }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitArray_type(BasicParser.Array_typeContext ctx) { return visitChildren(ctx); }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitPair_elem(BasicParser.Pair_elemContext ctx) { return visitChildren(ctx); }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitArg_list(BasicParser.Arg_listContext ctx) { return visitChildren(ctx); }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitAssign_rhs(BasicParser.Assign_rhsContext ctx) { return visitChildren(ctx); }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitAssign_lhs(BasicParser.Assign_lhsContext ctx) { return visitChildren(ctx); }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitRead(BasicParser.ReadContext ctx) {
    return new AST.ReadAst(ctx.assign_lhs());
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitAssignment(BasicParser.AssignmentContext ctx) {
    return new AST.AssignAST(ctx.assign_lhs(), ctx.assign_rhs());
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitIfthenesle(BasicParser.IfthenesleContext ctx) {
    return new AST.IfAst(visitExpr(ctx.expr()), visitStat(ctx.stat(0)), visitStat(ctx.stat(1)));
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitAskip(BasicParser.AskipContext ctx) {
    return new AST.ASkipAst();
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitDeclaration(BasicParser.DeclarationContext ctx) {
    return new AST.DeclarationAst(ctx.type(), ctx.IDENT().getText(), ctx.assign_rhs());
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitWhileloop(BasicParser.WhileloopContext ctx) {
    return new AST.WhileAst(visitExpr(ctx.expr()), visitStat(ctx.stat()));
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitSeq_compose(BasicParser.Seq_composeContext ctx) {
    ArrayList<AST> seqs = new ArrayList<>();
    for (BasicParser.StatContext statContext:ctx.stat()) {
      seqs.add(visitStat(statContext));
    }
    return new AST.SeqStateAst(seqs);
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitExit(BasicParser.ExitContext ctx) {
    return new AST.ExitAst(visitExpr(ctx.expr()));
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitPrint(BasicParser.PrintContext ctx) {
    return new AST.PrintAst(visitExpr(ctx.expr()));
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitPrintln(BasicParser.PrintlnContext ctx) {
    return new AST.PrintlnAst(visitExpr(ctx.expr()));
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitBlock(BasicParser.BlockContext ctx) {
    return new AST.BlockAst(visitStat(ctx.stat()));
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitFree(BasicParser.FreeContext ctx) {
    return new AST.FreeAst(visitExpr(ctx.expr()));
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitReturn(BasicParser.ReturnContext ctx) {
    return new AST.ReturnAst(visitExpr(ctx.expr()));
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitParam(BasicParser.ParamContext ctx) { return visitChildren(ctx); }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitParam_list(BasicParser.Param_listContext ctx) { return visitChildren(ctx); }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitFunc(BasicParser.FuncContext ctx) {
    return new AST.FuncAST(ctx.type(), ctx.IDENT().getText(), ctx.param_list().param(), visitStat(ctx.stat()));
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitProg(BasicParser.ProgContext ctx) {
    ArrayList<jav.wacc.AST.FuncAST> funcASTS = new ArrayList<>();
    for (BasicParser.FuncContext funcContext:ctx.func()) {
      funcASTS.add((jav.wacc.AST.FuncAST) visitFunc(funcContext));
    }
    return new jav.wacc.AST.ProgramAST(funcASTS, visitStat(ctx.stat()));
  }


  public AST visitStat(BasicParser.StatContext statContext) {
    if (statContext != null) {
      if (statContext instanceof BasicParser.ReadContext) {
        return visitRead((BasicParser.ReadContext) statContext);
      }
      if (statContext instanceof BasicParser.AskipContext) {
        return visitAskip((BasicParser.AskipContext) statContext);
      }
      if (statContext instanceof BasicParser.DeclarationContext) {
        return visitDeclaration((BasicParser.DeclarationContext) statContext);
      }
      if (statContext instanceof BasicParser.AssignmentContext) {
        return visitAssignment((BasicParser.AssignmentContext) statContext);
      }
      if (statContext instanceof BasicParser.FreeContext) {
        return visitFree((BasicParser.FreeContext) statContext);
      }
      if (statContext instanceof BasicParser.ReturnContext) {
        return visitReturn((BasicParser.ReturnContext) statContext);
      }
      if (statContext instanceof BasicParser.ExitContext) {
        return visitExit((BasicParser.ExitContext) statContext);
      }
      if (statContext instanceof BasicParser.PrintContext) {
        return visitPrint((BasicParser.PrintContext) statContext);
      }
      if (statContext instanceof BasicParser.PrintlnContext) {
        return visitPrintln((BasicParser.PrintlnContext) statContext);
      }
      if (statContext instanceof BasicParser.IfthenesleContext) {
        return visitIfthenesle((BasicParser.IfthenesleContext) statContext);
      }
      if (statContext instanceof BasicParser.WhileloopContext) {
        return visitWhileloop((BasicParser.WhileloopContext) statContext);
      }
      if (statContext instanceof BasicParser.BlockContext) {
        return visitBlock((BasicParser.BlockContext) statContext);
      }
      if (statContext instanceof BasicParser.Seq_composeContext) {
        return visitSeq_compose((BasicParser.Seq_composeContext) statContext);
      }
    }
    return null;
  }




}
