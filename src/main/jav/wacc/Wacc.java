package jav.wacc;

import antlr.BasicLexer;
import antlr.BasicParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

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
//
    BasicLexer lexer = new BasicLexer(input);

    CommonTokenStream stream = new CommonTokenStream(lexer);

    BasicParser basicParser = new BasicParser(stream);

    CompilerVisitor visitor = new CompilerVisitor();

    AST ast = visitor.visitProg(basicParser.prog());

    System.out.println(ast);

  }
}