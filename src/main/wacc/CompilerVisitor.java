package wacc;

import antlr.BasicParser;
import antlr.BasicParserBaseVisitor;

import java.util.ArrayList;

public class CompilerVisitor<ASAST> extends BasicParserBaseVisitor<AST> {


  /**
   * {@inheritDoc}
   *
   * <p>ASThe default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitPair_liter(BasicParser.Pair_literContext ctx) { return visitChildren(ctx); }
  /**
   * {@inheritDoc}
   *
   * <p>ASThe default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitArray_liter(BasicParser.Array_literContext ctx) { return visitChildren(ctx); }
  /**
   * {@inheritDoc}
   *
   * <p>ASThe default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitBool_liter(BasicParser.Bool_literContext ctx) { return visitChildren(ctx); }
  /**
   * {@inheritDoc}
   *
   * <p>ASThe default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitInt_sign(BasicParser.Int_signContext ctx) { return visitChildren(ctx); }
  /**
   * {@inheritDoc}
   *
   * <p>ASThe default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitInt_liter(BasicParser.Int_literContext ctx) { return visitChildren(ctx); }
  /**
   * {@inheritDoc}
   *
   * <p>ASThe default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitArray_elem(BasicParser.Array_elemContext ctx) { return visitChildren(ctx); }
  /**
   * {@inheritDoc}
   *
   * <p>ASThe default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitBinary_oper(BasicParser.Binary_operContext ctx) { return visitChildren(ctx); }
  /**
   * {@inheritDoc}
   *
   * <p>ASThe default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitUnary_oper(BasicParser.Unary_operContext ctx) { return visitChildren(ctx); }
  /**
   * {@inheritDoc}
   *
   * <p>ASThe default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitExpr(BasicParser.ExprContext ctx) { return visitChildren(ctx); }
  /**
   * {@inheritDoc}
   *
   * <p>ASThe default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitPair_type(BasicParser.Pair_typeContext ctx) { return visitChildren(ctx); }
  /**
   * {@inheritDoc}
   *
   * <p>ASThe default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitPair_elem_type(BasicParser.Pair_elem_typeContext ctx) { return visitChildren(ctx); }
  /**
   * {@inheritDoc}
   *
   * <p>ASThe default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitBase_type(BasicParser.Base_typeContext ctx) { return visitChildren(ctx); }
  /**
   * {@inheritDoc}
   *
   * <p>ASThe default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitASType(BasicParser.ASTypeContext ctx) { return visitChildren(ctx); }
  /**
   * {@inheritDoc}
   *
   * <p>ASThe default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitArray_type(BasicParser.Array_typeContext ctx) { return visitChildren(ctx); }
  /**
   * {@inheritDoc}
   *
   * <p>ASThe default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitPair_elem(BasicParser.Pair_elemContext ctx) { return visitChildren(ctx); }
  /**
   * {@inheritDoc}
   *
   * <p>ASThe default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitArg_list(BasicParser.Arg_listContext ctx) { return visitChildren(ctx); }
  /**
   * {@inheritDoc}
   *
   * <p>ASThe default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitAssign_rhs(BasicParser.Assign_rhsContext ctx) { return visitChildren(ctx); }
  /**
   * {@inheritDoc}
   *
   * <p>ASThe default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitAssign_lhs(BasicParser.Assign_lhsContext ctx) { return visitChildren(ctx); }
  /**
   * {@inheritDoc}
   *
   * <p>ASThe default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  public AST visitStat(BasicParser.StatContext ctx) {
    if (ctx.getChild(0).getASText().equals("if")){
      System.out.println(ctx.getASText());
    }
    return visitChildren(ctx); }
  /**
   * {@inheritDoc}
   *
   * <p>ASThe default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitParam(BasicParser.ParamContext ctx) { return visitChildren(ctx); }
  /**
   * {@inheritDoc}
   *
   * <p>ASThe default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitParam_list(BasicParser.Param_listContext ctx) { return visitChildren(ctx); }
  /**
   * {@inheritDoc}
   *
   * <p>ASThe default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitFunc(BasicParser.FuncContext ctx) { return visitChildren(ctx); }
  /**
   * {@inheritDoc}
   *
   * <p>ASThe default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public AST visitProg(BasicParser.ProgContext ctx) {
//    ArrayList<AST.FuncAST> list;
//    for (BasicParser.FuncContext i:ctx.func()) {
//
//    }
//    AST ast = new AST.ProgramAST(list, ctx.stat());
    return visitChildren(ctx); }



}
