package doc.wacc.astNodes;

import doc.wacc.utils.Type;

public class ParamNode extends AST {
  private Type type;
  private String name;


  public ParamNode(Type type, String name) {
    this.type = type;
    this.name = name;
  }

  public Type getType() {
    return type;
  }

  public String getName() {
    return name;
  }
}
