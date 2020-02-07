package doc.wacc;

import antlr.BasicLexer;
import antlr.BasicParser;
import doc.wacc.astNodes.AST;
import doc.wacc.utils.CompilerVisitor;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;

import static java.lang.System.exit;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.BitSet;
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

    ANTLRErrorListener errorListener = new ANTLRErrorListener() {
      @Override
      public void syntaxError(Recognizer<?, ?> recognizer, Object o, int i, int i1, String s, RecognitionException e) {
        System.out.println("Syntax Error: parse error in ANTLR error listener\n" +
                "Exit code 100 returned");
        exit(100);
      }

      @Override
      public void reportAmbiguity(Parser parser, DFA dfa, int i, int i1, boolean b, BitSet bitSet, ATNConfigSet atnConfigSet) {

      }

      @Override
      public void reportAttemptingFullContext(Parser parser, DFA dfa, int i, int i1, BitSet bitSet, ATNConfigSet atnConfigSet) {

      }

      @Override
      public void reportContextSensitivity(Parser parser, DFA dfa, int i, int i1, int i2, ATNConfigSet atnConfigSet) {

      }
    };

    lexer.removeErrorListeners();
    lexer.addErrorListener(errorListener);
    CommonTokenStream stream = new CommonTokenStream(lexer);

    try {
      BasicParser basicParser = new BasicParser(stream);
      basicParser.addErrorListener(errorListener);
      CompilerVisitor visitor = new CompilerVisitor();

      System.out.println("Compiling from source: " + args[0] + ":");
      AST ast = visitor.visitProg(basicParser.prog());

//      System.out.println(ast.toString());
    } catch (NumberFormatException e) {
      System.out.println("Syntax error: Integer overflow"  +
              "\nExit code 100 returned");
      exit(100);
    }
  }
}