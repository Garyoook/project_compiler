package doc.wacc.astNodes;

import antlr.BasicParser;
import doc.wacc.ASTVisitor;

import java.util.List;

public class FuncAST extends AST {
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
            + params + "): " + this.functionBody;
  }

  @Override
  public void Accept(ASTVisitor v) {

  }
}
