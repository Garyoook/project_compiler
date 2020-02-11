package doc.wacc.astNodes;

import doc.wacc.ASTVisitor;

import java.util.List;

public class ProgramAST extends AST {
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
      stringBuilder.append(f.getFuncName()).append("\n");
      stringBuilder.append(f);
      stringBuilder.append("  \n\n");
    }

    stringBuilder.append("Now start the program \n");
    stringBuilder.append(mainProgram).append("\n");
    return stringBuilder.toString();
  }

  @Override
  public void Accept(ASTVisitor v) {

  }
}
