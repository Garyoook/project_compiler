package doc.wacc.astNodes;

public class DerefNode extends AST {
    private String variable;
    private int dereference_num;
    public DerefNode(String variable, int dereference_num) {
        this.variable = variable;
        this.dereference_num = dereference_num;
    }

    public int getDereference_num() {
        return dereference_num;
    }

    public String getVariable() {
        return variable;
    }
}
