package doc.wacc.astNodes;

import antlr.BasicParser;
import doc.wacc.utils.CompilerVisitor;

import java.util.ArrayList;
import java.util.List;

public class CallAST extends AST {
  private final String funcName;
  private final List<BasicParser.ExprContext> arguments;

  public CallAST(String funcName, List<BasicParser.ExprContext> arguments) {
    this.funcName = funcName;
    this.arguments = arguments;
  }

  public String getFuncName() {
    return funcName;
  }

  public boolean hasArgument() {
    return arguments != null;
  }

  public List<AST> getArguments() {
    if (arguments != null) {
      List<AST> args = new ArrayList<>();
      CompilerVisitor visitor = new CompilerVisitor();
      for (BasicParser.ExprContext a : arguments) {
        args.add(visitor.visitExpr(a));
      }
      return args;
    } else {
      return null;
    }
  }
}

