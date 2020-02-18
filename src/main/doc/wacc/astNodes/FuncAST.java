package doc.wacc.astNodes;

import antlr.BasicParser;
import doc.wacc.utils.SymbolTable;

import java.util.List;

public class FuncAST extends AST {
  private final BasicParser.TypeContext returnType;
  private final String funcName;
  private final List<BasicParser.ParamContext> parameters;
  private final AST functionBody;
  private SymbolTable symbolTable;

  public FuncAST(BasicParser.TypeContext returnType, String funcName, List<BasicParser.ParamContext> parameters, AST functionBody, SymbolTable symbolTable) {
    this.returnType = returnType;
    this.funcName = funcName;
    this.parameters = parameters;
    this.functionBody = functionBody;
    this.symbolTable = symbolTable;
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

  public SymbolTable getSymbolTable() {
    return symbolTable;
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
}
