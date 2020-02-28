
import antlr.BasicLexer;
import antlr.BasicParser;
import com.igormaznitsa.jute.annotations.JUteTest;
import doc.wacc.astNodes.AST;
import doc.wacc.utils.CompilerVisitor;
import doc.wacc.utils.ErrorMessage;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.BitSet;
import java.util.Scanner;

import static java.lang.System.exit;
import static org.junit.Assert.assertEquals;

public class FrontEndTests {
    // do not run in IDE, run all the tests using commandline:
    // ```mvn test:jute```
    // to run all the tests

    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    @Test
    public void valid_testif1() {
        String fp = "wacc_examples/valid/if/if1.wacc";
        emulator(fp);
    }

    @Test
    public void valid_testIf2()
    {
        String fp = "wacc_examples/valid/if/if2.wacc";
        emulator(fp);
    }

    @Test
    public void valid_testIf3() {
        String fp = "wacc_examples/valid/if/if3.wacc";
        emulator(fp);
    }

    @Test
    public void valid_testIf4() {
        String fp = "wacc_examples/valid/if/if4.wacc";
        emulator(fp);
    }

    @Test
    public void valid_testIf5() {
        String fp = "wacc_examples/valid/if/if5.wacc";
        emulator(fp);
    }

    @Test
    public void valid_testIf6() {
        String fp = "wacc_examples/valid/if/if6.wacc";
        emulator(fp);
    }

    @Test
    public void valid_testIfBasic() {
        String fp = "wacc_examples/valid/if/ifBasic.wacc";
        emulator(fp);
    }

    @Test
    public void valid_testIfFalse() {
        String fp = "wacc_examples/valid/if/ifFalse.wacc";
        emulator(fp);
    }

    @Test
    public void valid_testIfTrue() {
        String fp = "wacc_examples/valid/if/ifTrue.wacc";
        emulator(fp);
    }

    @Test
    public void valid_testWhitespace() {
        String fp = "wacc_examples/valid/if/whitespace.wacc";
        emulator(fp);
    }

    @Test
    public void testSemanticErr() {
        String fp = "wacc_examples/invalid/semanticErr/function/functionAssign.wacc";
        ProcessBuilder p = new ProcessBuilder();
        p.command("./compile " + fp);
        exit.expectSystemExitWithStatus(200);
        emulator(fp);
    }

    @Test
    public void testSyntaxErr() {
        exit.expectSystemExitWithStatus(100);
        String fp = "wacc_examples/invalid/syntaxErr/function/functionLateDefine.wacc";
        emulator(fp);
    }

    public void emulator(String filepath) {
        StringBuilder sb = new StringBuilder();
        try {
            // the file to be opened for reading
            FileInputStream fis = new FileInputStream(filepath);
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

        ANTLRErrorListener errorListener =
                new ANTLRErrorListener() {
                    @Override
                    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine,
                                            String msg, RecognitionException e) {
                      System.out.println("Syntax Error: parse error in ANTLR error listener\n" +
                          "at line "+line+":"+charPositionInLine+" \nat "+
                          offendingSymbol+": "+msg +
                          "\nExit code 100 returned");
                      exit(100);
                    }

                    @Override
                    public void reportAmbiguity(
                            Parser parser,
                            DFA dfa,
                            int i,
                            int i1,
                            boolean b,
                            BitSet bitSet,
                            ATNConfigSet atnConfigSet) {
                    }

                    @Override
                    public void reportAttemptingFullContext(
                            Parser parser, DFA dfa, int i, int i1, BitSet bitSet, ATNConfigSet atnConfigSet) {
                    }

                    @Override
                    public void reportContextSensitivity(
                            Parser parser, DFA dfa, int i, int i1, int i2, ATNConfigSet atnConfigSet) {
                    }
                };

        lexer.removeErrorListeners();

        lexer.addErrorListener(errorListener);

        CommonTokenStream stream = new CommonTokenStream(lexer);
        try {
            BasicParser basicParser = new BasicParser(stream);
            basicParser.addErrorListener(errorListener);

            CompilerVisitor visitor = new CompilerVisitor();

            AST ast = visitor.visitProg(basicParser.prog());

            ErrorMessage.errorWriter();

            //      System.out.println(ast.toString());
        } catch (NumberFormatException e) {
          ErrorMessage.addSyntaxError("Integer overflow");
        }
    }
}
