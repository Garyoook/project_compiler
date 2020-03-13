package doc.wacc.utils;

public class OpCode {
    public String name;

    public OpCode(String name) {
        this.name = name;
    }

    public String execOpCode(String s0, String s1, String s2) {
        if (name.equals("SUB")) {
            return "\tSUB " + s0 + ", " + s1 + ", " + "#" + s2;
        }

        if (name.equals("SUBS")) {
            return "\tSUBS " + s0 + ", " + s1 + ", " + s2;
        }

        if (name.equals("")) {
            
        }
        return "";
    }
}
