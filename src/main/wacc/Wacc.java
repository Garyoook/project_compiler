package wacc;

import antlr.*;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class Wacc {
    public static void main(String[] args) throws Exception {
      ANTLRInputStream input = new ANTLRInputStream(System.in);

      BasicLexer lexer = new BasicLexer(input);

      CommonTokenStream stream = new CommonTokenStream(lexer);

      BasicParser basicParser = new BasicParser(stream);

//      ParseTree parseTree = basicParser.prog();


       AST.ProgramAST past = toAST(basicParser.prog());
//      System.out.println(ast);


//      CompilerVisitor visitor = new CompilerVisitor();
//      visitor.visit(parseTree);

//      for (int i = 0; i < parseTree.getChildCount(); i++) {
        System.out.println(past);
        System.out.println(past.getFunctions());



//      }

    }

  private static AST.ProgramAST toAST(BasicParser.ProgContext prog) {
    List<AST.FuncAST> list = new ArrayList<>();
    for (BasicParser.FuncContext x : prog.func()) {
      AST.FuncAST ast = toAST(x);
      list.add(ast);
    }
    return new AST.ProgramAST(list, toAST(prog.stat()));
  }

  private static AST.StatAST toAST(BasicParser.StatContext stat) {
    return null;
  }


  private static AST.FuncAST toAST(BasicParser.FuncContext f) {
      return new AST.FuncAST(f.type(), f.IDENT().getText(), f.param_list().param(), toAST(f.stat()));
  }
}
