package doc.wacc.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Register {
    private int regNum;
    private boolean isAvailable;

    public Register(int regNum, boolean isAvailable) {
        this.regNum = regNum;
        this.isAvailable = isAvailable;
    }

    public Register(int regNum) {
        this.regNum = regNum;
        this.isAvailable = true;
    }


    public static ArrayList<Register> createAllRegs() {
        ArrayList<Register> regs = new ArrayList<Register>(
                Arrays.asList(
                        new Register(0, false),
                        new Register(1),
                        new Register(2),
                        new Register(3),
                        new Register(4),
                        new Register(5),
                        new Register(6),
                        new Register(7),
                        new Register(8),
                        new Register(9),
                        new Register(10)));
        return regs;
    }
}
