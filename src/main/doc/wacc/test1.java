package doc.wacc;

import doc.wacc.astNodes.*;

class Test1 {
    public static void main(String args[]) {
        AST test1 = new IfAst(new BoolNode(true), new PrintAst(new ASkipAst()), new SkipAst());

        System.out.println();

        System.out.println("Test 1");
        System.out.println("------");
        System.out.println();

        test1.Accept(new TranslateVisitor());

        System.out.println();
    }
}
