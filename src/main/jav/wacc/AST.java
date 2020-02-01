package jav.wacc;

import antlr.BasicParser;
import org.antlr.v4.runtime.ParserRuleContext;

import javax.xml.parsers.SAXParser;

import java.util.ArrayList;
import java.util.List;

public abstract class AST {
//  ArrayList<AST> children = new ArrayList<>();
  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("this is a ");
    stringBuilder.append(getClass().getName());
    stringBuilder.append(" AST \n");
    return stringBuilder.toString();
  }

  public static class ProgramAST extends AST {
    private List<FuncAST> functions;
    private AST mainProgram;

    public ProgramAST(List<FuncAST> functions, AST mainProgram) {
      this.functions = functions;
      this.mainProgram = mainProgram;
    }

    public AST getMainProgram() {
      return mainProgram;
    }

    public List<FuncAST> getFunctions() {
      return functions;
    }

    @Override
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("there is this many funcs ");
      for (FuncAST f: functions) {
        stringBuilder.append(f.funcName);
        stringBuilder.append("  ");
      }


      stringBuilder.append("this is a ");
      stringBuilder.append(getClass().getName());
      stringBuilder.append(" AST\n");
      stringBuilder.append(mainProgram.toString());
      return stringBuilder.toString();
    }
  }


  public static class ReadAst extends AST {
    private final BasicParser.Assign_lhsContext lhs;
    public ReadAst(BasicParser.Assign_lhsContext lhs) {
      this.lhs = lhs;
    }
  }

  public static class AssignAST extends AST {
    private final BasicParser.Assign_lhsContext lhs;
    private final BasicParser.Assign_rhsContext rhs;
    public AssignAST(BasicParser.Assign_lhsContext lhs, BasicParser.Assign_rhsContext rhs) {
      this.lhs = lhs;
      this.rhs = rhs;
    }
  }

  public static class IfAst extends AST {
    private final AST expr;
    private AST thenbranch;
    private AST elsebranch;


    public IfAst(AST expr, AST thenbranch, AST elsebranch) {
      this.expr = expr;
      this.thenbranch = thenbranch;
      this.elsebranch = elsebranch;
    }
  }

  public class SkipAst extends AST { 

  }

  public static class DeclarationAst extends AST {
    private final BasicParser.TypeContext type;
    private final String name;
    private final BasicParser.Assign_rhsContext rhs;


    public DeclarationAst(BasicParser.TypeContext type, String name, BasicParser.Assign_rhsContext rhs) {
      this.type = type;
      this.name = name;
      this.rhs = rhs;
    }
  }

  public static class WhileAst extends AST {
    private AST expr;
    private AST stat;


    public WhileAst(AST expr, AST stat) {
      this.expr = expr;
      this.stat = stat;
    }
  }

  public static class SeqStateAst extends AST {
    private ArrayList<AST> seqs;
    public SeqStateAst(ArrayList<AST> seqs) {
      this.seqs = new ArrayList<>(seqs);
    }
  }

  public static class ExitAst extends AST {
    private AST expr;

    public ExitAst(AST expr) {
      this.expr = expr;
    }
  }

  public static class PrintAst extends AST {
    private AST expr;

    public PrintAst(AST expr) {
      this.expr = expr;
    }
  }

  public static class PrintlnAst extends AST {
    private AST expr;

    public PrintlnAst(AST expr) {
      this.expr = expr;
    }
  }

  public static class BlockAst extends AST {
    private AST stat;

    public BlockAst(AST stat) {
      this.stat = stat;
    }
  }

  public static class FreeAst extends AST {
    private AST expr;

    public FreeAst(AST expr) {
      this.expr = expr;
    }


  }

  public static class ReturnAst extends AST {
    private AST expr;

    public ReturnAst(AST expr) {
      this.expr = expr;
    }

  }

  public static class ASkipAst extends AST {

  }


  public static class ExprAst extends AST {
    ArrayList<AST> asts;
    ExprAst(ArrayList<AST> asts) {
      this.asts = new ArrayList<>(asts);
    }

  }

  static class FuncAST extends AST {
    private final BasicParser.TypeContext returnType;
    private final String funcName;
    private final List<BasicParser.ParamContext> parameters;
    private final AST functionBody;

    public FuncAST(BasicParser.TypeContext returnType, String funcName, List<BasicParser.ParamContext> parameters, AST functionBody) {
      this.returnType = returnType;
      this.funcName = funcName;
      this.parameters = parameters;
      this.functionBody = functionBody;
    }

    public AST getFunctionBody() {
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
  }


}
