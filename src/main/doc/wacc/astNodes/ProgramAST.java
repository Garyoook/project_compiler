package doc.wacc.astNodes;

import java.util.List;

public class ProgramAST extends AST {
  private List<LibAST> libraries;
  private List<FuncAST> functions;
  private AST mainProgram;

  public ProgramAST(
          List<LibAST> libraries,
          List<FuncAST> functions, AST mainProgram) {
    this.libraries = libraries;
    this.functions = functions;
    this.mainProgram = mainProgram;
  }

  public AST getMainProgram() {
    return mainProgram;
  }

  public List<FuncAST> getFunctions() {
    return functions;
  }

  public List<LibAST> getLibraries() {
    return libraries;
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("*import libraries: \n");
    for (LibAST l : libraries) {
      stringBuilder.append(l.getImporteeName()).append(".wacc\n\n");
    }

    stringBuilder.append("*this program has these functions: \n");
    for (FuncAST f: functions) {
      stringBuilder.append(f.getFuncName()).append("\n");
      stringBuilder.append(f);
      stringBuilder.append("  \n\n");
    }

    stringBuilder.append("Now start the program \n");
    stringBuilder.append(mainProgram).append("\n");
    return stringBuilder.toString();
  }
}
