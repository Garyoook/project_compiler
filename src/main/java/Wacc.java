import antlr.BasicLexer;
import antlr.BasicParser;
import antlr.BasicParserBaseVisitor;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.IOException;

public class Wacc {
    public static void main(String[] args) throws IOException {
      ANTLRInputStream input = new ANTLRInputStream(System.in);

      BasicLexer lexer = new BasicLexer(input);

      CommonTokenStream stream = new CommonTokenStream(lexer);

      BasicParser basicParser = new BasicParser(stream);

      ParseTree parseTree = basicParser.prog();

//      AST ast = new AST(parseTree.);

      BasicParserBaseVisitor visitor = new BasicParserBaseVisitor();
      System.out.println(visitor.visit(parseTree));

//      for (int i = 0; i < parseTree.getChildCount(); i++) {
//        System.out.println(parseTree.getChild(i).toStringTree(basicParser));
//      }

    }
}
