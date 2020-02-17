package doc.wacc.utils;

public class Variable {
    Type type;
    String value;
    int stack_position;
    
    public Variable(Type type, String value, int stack_position) {
        this.type = type;
        this.value = value;
        this.stack_position = stack_position;
    }
}
