package doc.wacc.astNodes;

public class RefNode extends AST{
    private String variable;
    public RefNode(String variable){
        this.variable = variable;
    }

    public String getVariable() {
        return variable;
    }
}
