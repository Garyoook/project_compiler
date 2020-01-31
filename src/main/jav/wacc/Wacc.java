package jav.wacc;

import antlr.BasicLexer;
import antlr.BasicParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Wacc {
  public static void main(String[] args) throws Exception {
      StringBuilder sb = new StringBuilder();
    try {
      // the file to be opened for reading
      FileInputStream fis = new FileInputStream(args[0]);
      Scanner sc = new Scanner(fis); // file to be scanned
      // returns true if there is another line to read
      while (sc.hasNextLine()) {
          sb.append(sc.nextLine()).append("\n");
      }
      sc.close(); // closes the scanner
    } catch (IOException e) {
      e.printStackTrace();
    }
    ANTLRInputStream input = new ANTLRInputStream(sb.toString());

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
