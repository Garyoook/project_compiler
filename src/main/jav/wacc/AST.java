package jav.wacc;

import antlr.BasicParser;
import org.antlr.v4.runtime.ParserRuleContext;

import javax.xml.parsers.SAXParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.System.exit;


public abstract class AST {
  public static HashMap<String, BasicParser.TypeContext> symbolTable = new HashMap<>();
//  ArrayList<AST> children = new ArrayList<>();
//  @Override
//  public String toString() {
//    StringBuilder stringBuilder = new StringBuilder();
//    stringBuilder.append("this is a ");
//    stringBuilder.append(getClass().getName());
//    stringBuilder.append(" AST \n");
//    return stringBuilder.toString();
//  }

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
      stringBuilder.append("this program has these functions: \n");
      for (FuncAST f: functions) {
        stringBuilder.append(f.funcName).append("\n");
        stringBuilder.append(f.toString());
        stringBuilder.append("  \n\n");
      }

      stringBuilder.append("Now start the program \n");
      stringBuilder.append(mainProgram.toString()).append("\n");
      return stringBuilder.toString();
    }
  }


  public static class ReadAst extends AST {
    private final BasicParser.Assign_lhsContext lhs;
    public ReadAst(BasicParser.Assign_lhsContext lhs) {
      this.lhs = lhs;
    }

    @Override
    public String toString() {
      return "reading from: " + lhs.IDENT().getText() + "\n";
    }
  }

  public static class AssignAST extends AST {
    private final BasicParser.Assign_lhsContext lhs;
    private final BasicParser.Assign_rhsContext rhs;
    public AssignAST(BasicParser.Assign_lhsContext lhs, BasicParser.Assign_rhsContext rhs) {
      this.lhs = lhs;
      this.rhs = rhs;
      if (rhs.expr().size() == 1) {
        CompilerVisitor visitor = new CompilerVisitor();
        visitor.visitExpr(rhs.expr(0));
      }
    }

    @Override
    public String toString() {
      return "assigning from: " + lhs.IDENT().getText() + " to " + rhs.IDENT().getText() + "\n";
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

    @Override
    public String toString() {
      return "If (" + expr.toString() + ") then " + "("
              + thenbranch.toString() + ")" +  "else " + "("
              + elsebranch.toString() + ")\n";
    }
  }

  public class SkipAst extends AST {
    @Override
    public String toString() {
      return "skip\n";
    }
  }

  public static class DeclarationAst extends AST {
    private final BasicParser.TypeContext type;
    private final String name;
    private final BasicParser.Assign_rhsContext rhs;



    public DeclarationAst(BasicParser.TypeContext type, String name, BasicParser.Assign_rhsContext rhs) {
      this.type = type;
      this.name = name;
      this.rhs = rhs;
      symbolTable.put(name, type);
      if (rhs.expr().size() == 1) {
        CompilerVisitor visitor = new CompilerVisitor();
        visitor.visitExpr(rhs.expr(0));
      }
    }

    @Override
    public String toString() {
      return "DECLEAR: type: " + type.getText() + " name: " + name + "\n";
    }
  }

  public static class WhileAst extends AST {
    private AST expr;
    private AST stat;


    public WhileAst(AST expr, AST stat) {
      this.expr = expr;
      this.stat = stat;
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("while (").append(expr.toString()).append("): ");
      sb.append("{ ").append(stat.toString()).append("}\n");
      return sb.toString();
    }
  }

  public static class SeqStateAst extends AST {
    private ArrayList<AST> seqs;
    public SeqStateAst(ArrayList<AST> seqs) {
      this.seqs = new ArrayList<>(seqs);
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      for (AST a : seqs) {
        sb.append(a.toString());
      }
      return sb.toString();
    }
  }

  public static class ExitAst extends AST {
    private AST expr;

    public ExitAst(AST expr) {
      this.expr = expr;
    }

    @Override
    public String toString() {
      return "exit(" + expr.toString() + ")\n";
    }
  }

  public static class PrintAst extends AST {
    private AST expr;

    public PrintAst(AST expr) {
      this.expr = expr;
    }

    @Override
    public String toString() {
      return "Print: " + expr.toString() + "\n";
    }
  }

  public static class PrintlnAst extends AST {
    private AST expr;

    public PrintlnAst(AST expr) {
      this.expr = expr;
    }

    @Override
    public String toString() {
      return "Println: " + expr.toString() + "\n";
    }
  }

  public static class BlockAst extends AST {
    private AST stat;

    public BlockAst(AST stat) {
      this.stat = stat;
    }

    @Override
    public String toString() {
      return "Block {" + stat.toString() + "}\n";
    }
  }

  public static class FreeAst extends AST {
    private AST expr;

    public FreeAst(AST expr) {
      this.expr = expr;
    }

    @Override
    public String toString() {
      return "Free expr: " + expr.toString() + "\n";
    }
  }

  public static class ReturnAst extends AST {
    private AST expr;

    public ReturnAst(AST expr) {
      this.expr = expr;
    }

    @Override
    public String toString() {
      return "return " + expr.toString() + "\n";
    }
  }

  public static class ASkipAst extends AST {

    @Override
    public String toString() {
      return "ASkip\n";
    }
  }


  public static class IntNode extends AST {
    int value;
    IntNode(int value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
  }

  public static class StringNode extends AST {
    String value;
    StringNode(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return value;
    }
  }

  public static class BoolNode extends AST {
    boolean value;
    BoolNode(boolean value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
  }

  public static class IdenNode extends AST {
    String ident;
    IdenNode(String ident) {
      this.ident = ident;
    }

    @Override
    public String toString() {
      return ident;
    }
  }

  public static class Unaryop_node extends AST{
    BasicParser.Unary_operContext operContext;
    AST expr;
    Unaryop_node(BasicParser.Unary_operContext operContext, AST expr) {
      this.operContext = operContext;
      this.expr = expr;
    }

    @Override
    public String toString() {
      return operContext.getText() + expr.toString();
    }
  }

  public static class BinaryOp_node extends AST{
    BasicParser.Binary_operContext operContext;
    AST expr1;
    AST expr2;
    BinaryOp_node(BasicParser.Binary_operContext operContext, AST expr1, AST expr2) {
      this.operContext = operContext;
      this.expr1 = expr1;
      this.expr2 = expr2;
      if (!same_type(expr1, expr2) || !same_type(expr2, expr1)) {
        System.out.println("Semantic error: type doesn't matched");  exit(200);
      }
      if (expr1 instanceof IntNode && !(expr2 instanceof IntNode)) {
        System.out.println("Semantic error: type doesn't matched");  exit(200); 
      }
      if (expr1 instanceof BoolNode && !(expr2 instanceof BoolNode)) {
        System.out.println("Semantic error: type doesn't matched");  exit(200); 
      }
      if (expr1 instanceof CharNode && !(expr2 instanceof CharNode)) {
        System.out.println("Semantic error: type doesn't matched");  exit(200); 
      }
      if (expr1 instanceof StringNode && !(expr2 instanceof StringNode)) {
        System.out.println("Semantic error: type doesn't matched");  exit(200); 
      }
    }

    private boolean same_type(AST expr1, AST expr2) {
      if (expr1 instanceof IdenNode) {
        BasicParser.TypeContext type1 = symbolTable.get(((IdenNode) expr1).ident);
//        if (type1 == null) {
//          System.out.println("Semantic error: Variable not defined:" + ((IdenNode) expr1).ident);
//          exit(200);
//        }
        if (expr2 instanceof IdenNode) {
//
          BasicParser.TypeContext type2 = symbolTable.get(((IdenNode) expr2).ident);
//          if (type2 == null) {
//            System.out.println("Semantic error: Variable not defined:" + ((IdenNode) expr1).ident);
//            exit(200);
//          }

//          if (!type1.equals(type2)){
//            System.out.println("Semantic error: type doesn't matched");  exit(200);}
//        } else {
//          if (type1.base_type().INT() != null && !(expr2 instanceof IntNode)) {
//
//            System.out.println("Semantic error: type doesn't matched");  exit(200);
//          }
//          if (type1.base_type().BOOL() != null && !(expr2 instanceof BoolNode)) {
//            System.out.println("Semantic error: type doesn't matched");  exit(200);
//          }
//          if (type1.base_type().CHAR() != null && !(expr2 instanceof CharNode)) {
//            System.out.println("Semantic error: type doesn't matched");  exit(200);
//          }
//          if (type1.base_type().STRING() != null && !(expr2 instanceof StringNode)) {
//            System.out.println("Semantic error: type doesn't matched");  exit(200);
//          }
        }
      }
      return true;
    }

    @Override
    public String toString() {
      return expr1.toString() + operContext.getText() + expr2.toString();
    }
  }

  public static class CharNode extends AST {
    char value;
    CharNode(char value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
  }

  public static class ExprWithParen extends AST {
    AST expr;

    public ExprWithParen(AST expr) {
      this.expr = expr;
    }

    @Override
    public String toString() {
      return expr.toString();
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

    @Override
    public String toString() {
      StringBuilder params = new StringBuilder();
      for (BasicParser.ParamContext p : parameters) {
        params.append(p.type().getText()).append(" ").append(p.IDENT()
                .getText()).append(", ");
      }
      return "function: " + this.getReturnType().getText() + " " +this.getFuncName()+"("
              + params.toString() + "): " + this.functionBody.toString();
    }
  }


}
