package wacc;

import antlr.BasicParser;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.ArrayList;
import java.util.List;

public abstract class AST {
//  ArrayList<AST> children = new ArrayList<>();

  public static class ProgramAST extends AST {
    private List<FuncAST> functions;
    private StatAST mainProgram;

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

  // 3types of condition, only statement,
  public class StatAST extends AST {

//    private int type;
//    private AST ast;
//
//    public StatAST(int type, ParserRuleContext fst, ParserRuleContext snd, ParserRuleContext thd) {
//      switch (type) {
//        case BasicParser.ASKIP: this.ast = new SkipAst();
//        case
//      }
//    }

  }

  public class ReadAst extends StatAST {
    private final BasicParser.Assign_lhsContext lhs;
    public ReadAst(BasicParser.Assign_lhsContext lhs) {
      this.lhs = lhs;
    }
  }

  public class AssignAST extends StatAST {
    private final BasicParser.Assign_lhsContext lhs;
    private final BasicParser.Assign_rhsContext rhs;
    public AssignAST(BasicParser.Assign_lhsContext lhs, BasicParser.Assign_rhsContext rhs) {
      this.lhs = lhs;
      this.rhs = rhs;
    }
  }

  public class IfAst extends StatAST {
    private final ExprAst expr;
    private StatAST thenbranch;
    private StatAST elsebranch;


    public IfAst(ExprAst expr, StatAST thenbranch, StatAST elsebranch) {
      this.expr = expr;
      this.thenbranch = thenbranch;
      this.elsebranch = elsebranch;
    }
  }

  public class SkipAst extends StatAST { 

  }

  public class DeclarationAst extends StatAST {
    private final BasicParser.TypeContext type;
    private final String name;
    private final BasicParser.Assign_rhsContext rhs;


    public DeclarationAst(BasicParser.TypeContext type, String name, BasicParser.Assign_rhsContext rhs) {
      this.type = type;
      this.name = name;
      this.rhs = rhs;
    }
  }

  public class WhileAst extends StatAST {
    private ExprAst expr;
    private StatAST stat;


    public WhileAst(ExprAst expr, StatAST stat) {
      this.expr = expr;
      this.stat = stat;
    }
  }

  public class SeqStateAst extends StatAST {
    private StatAST fst;
    private StatAST snd;

    public SeqStateAst(StatAST fst, StatAST snd) {
      this.fst = fst;
      this.snd = snd;
    }
  }

  public class ExitAst extends StatAST {
    private ExprAst expr;

    public ExitAst(ExprAst expr) {
      this.expr = expr;
    }
  }

  public class PrintAst extends StatAST {
    private ExprAst expr;

    public PrintAst(ExprAst expr) {
      this.expr = expr;
    }
  }

  public class BlockAst extends StatAST {
    private StatAST stat;

    public BlockAst(StatAST stat) {
      this.stat = stat;
    }
  }

  public class FreeAst extends StatAST {
    private StatAST stat;

    public FreeAst(StatAST stat) {
      this.stat = stat;
    }
  }

  public class ReturnAst extends StatAST {
    private StatAST stat;

    public ReturnAst(StatAST stat) {
      this.stat = stat;
    }
  }

  public class ExprAst extends AST {
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

    public FuncAST(BasicParser.TypeContext returnType, String funcName, List<BasicParser.ParamContext> parameters, StatAST functionBody) {
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
