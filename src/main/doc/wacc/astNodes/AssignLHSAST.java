package doc.wacc.astNodes;

import antlr.BasicParser;

public class AssignLHSAST extends AST {

    private BasicParser.Assign_lhsContext lhsContext;


    public AssignLHSAST(BasicParser.Assign_lhsContext lhsContext) {
        this.lhsContext = lhsContext;

    }

    public BasicParser.Assign_lhsContext getLhsContext() {
        return lhsContext;
    }

}
