package jav.wacc;

import antlr.BasicParser;

import java.util.List;

public abstract class AST {
//  ArrayList<AST> children = new ArrayList<>();

  public static class ProgramAST extends AST {
    private final List<FuncAST> functions;
    private final StatAST mainProgram;

    public ProgramAST(List<FuncAST> functions, StatAST mainProgram) {
      this.functions = functions;
      this.mainProgram = mainProgram;
    }

    public StatAST getMainProgram() {
      return mainProgram;
    }

    public List<FuncAST> getFunctions() {
      return functions;
    }
  }

  public class StatAST {
  }
//  public AST(BasicParser.StatContext thisTree) throws Exception {
//    int i = 0;
//    while (thisTree instanceof BasicParser.DeclarationContext) {
//      System.out.println(((BasicParser.DeclarationContext) thisTree).type());
//      if (thisTree.stat(i).getRuleIndex() == BasicParser.IF) {
//        System.out.println("wow its if");
//        children.add(new IfAST(thisTree.stat(i)));
//      } else if (thisTree.stat(i).getRuleIndex() == BasicParser.PRINTLN) {
//        System.out.println("wow its println");
//        children.add(new PrintAST(thisTree.stat(i).stat(1)));
//      }
//      i++;
//    }
//  }

  static class FuncAST extends AST {
    private final BasicParser.TypeContext returnType;
    private final String funcName;
    private final List<BasicParser.ParamContext> parameters;
    private final StatAST functionBody;

    FuncAST(BasicParser.TypeContext returnType, String funcName, List<BasicParser.ParamContext> parameters, StatAST functionBody) {
      this.returnType = returnType;
      this.funcName = funcName;
      this.parameters = parameters;
      this.functionBody = functionBody;
    }

    public StatAST getFunctionBody() {
      return functionBody;
    }

    public List<BasicParser.ParamContext> getParameters() {
      return parameters;
    }

    public BasicParser.TypeContext getReturnType() {
      return returnType;
    }

    public String getFuncName() {
      return funcName;
    }

    @Override
    public String toString() {
      return funcName;
    }
  }

//  private class IfAST extends AST {
//    private BasicParser.ExprContext expr;
//    private AST statThen;
//    private AST statElse;
//
//    public IfAST(BasicParser.StatContext stat) throws Exception {
//      super(stat);
//      expr = stat.expr();
//      statThen = new AST(stat.stat(0));
//      statElse = new AST(stat.stat(1));
//      System.out.println("expr:BOOL :" + expr.bool_liter());
////      if (!((expr.getRuleIndex() == BasicParser.RULE_bool_liter))) {
////        throw new Exception("This should be Bool\n");
////      }
//    }
//  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

//    for (AST child : children) {
//      sb.append(child.toString()).append("\n");
//    }

    return sb.toString();
  }

}
