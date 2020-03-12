import antlr.BasicLexer;
import antlr.BasicParser;
import doc.wacc.astNodes.AST;
import doc.wacc.utils.ASTVisitor;
import doc.wacc.utils.CompilerVisitor;
import doc.wacc.utils.ErrorMessage;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.junit.Test;

import java.io.*;
import java.util.BitSet;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.exit;
import static org.junit.Assert.assertEquals;

public class Extension_StandardLib_test {
        @Test
        public void extension_standardLib_pow() throws IOException, InterruptedException {
            String fp = "wacc_examples/extension/standardLib_pow.wacc";
            emulator(fp);

            Runtime rt = Runtime.getRuntime();
            Process pr = rt.exec("arm-linux-gnueabi-gcc -o tempProg -mcpu=arm1176jzf-s -mtune=arm1176jzf-s charComparisonExpr.s");
            pr.waitFor();
            Process pr2 = rt.exec("qemu-arm -L /usr/arm-linux-gnueabi/ tempProg");
            pr2.waitFor();
            OutputStreamWriter osw = new OutputStreamWriter(pr2.getOutputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pr2.getInputStream()));
            int myExitCode = pr2.exitValue();

            assertEquals(myExitCode, 0);
            assertEquals(bufferedReader.readLine(), "8");
        }

        // ======================= Emulator =======================================
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
                fis.close();
                sc.close(); // closes the scanner
            } catch (IOException e) {
                e.printStackTrace();
            }
            ANTLRInputStream input = new ANTLRInputStream(sb.toString());
            BasicLexer lexer = new BasicLexer(input);

            ANTLRErrorListener errorListener = new ANTLRErrorListener() {
                @Override
                public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine,
                                        String msg, RecognitionException e) {
                    System.out.println("Syntax Error: parse error in ANTLR error listener\n" +
                            "\tat line " + line + ":" + charPositionInLine + " \n\tat " +
                            offendingSymbol + ": " + msg +
                            "\nExit code 100 returned");
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

                System.out.println("Compiling from source: " + filepath + ":");
                AST ast = visitor.visitProg(basicParser.prog());
//      System.out.println(ast);

                String[] s1 = filepath.split("/");
                String fileName = s1[s1.length - 1];
                fileName = fileName.split("\\.")[0];
                System.out.println(fileName);
                if (!ErrorMessage.hasError()) {
                    File outputFile = new File(fileName + ".s");
                    FileWriter myWriter = new FileWriter(outputFile);
                    ASTVisitor translator = new ASTVisitor();
                    translator.visitProgAST(ast);
                    List<String> result = translator.getCodes();
                    for (String s : result) {
                        myWriter.write(s + "\n");
                    }
                    myWriter.close();
//        outputFile.deleteOnExit();
                }
            } catch (NumberFormatException | IOException e) {
                ErrorMessage.addSyntaxError("Integer overflow");
            }
            ErrorMessage.errorWriter();
        }
}
