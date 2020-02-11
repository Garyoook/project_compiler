package doc.wacc;

public class UniqueLabel {
    static int NextUnusedId = 0;
    int MyId;
    public UniqueLabel() {
        MyId = NextUnusedId;
        NextUnusedId += 1;
    }
    public String toString() {
        return("L"+MyId);
    }
}
