package java;

import antlr.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.*;

public class Wacc {

    public static void main(String[] args) throws Exception{
        ANTLRInputStream input = new ANTLRInputStream(System.in);
        BasicLexer lexer = new BasicLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        BasicParser parser = new BasicParser(tokens);

        ParseTree tree = parser.prog();

        System.out.println("===============");
        BasicParserBaseVisitor visitor = new BasicParserBaseVisitor();
        visitor.visit(tree);
        System.out.println("===============");
    }
}