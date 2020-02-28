import antlr.BasicLexer;
import antlr.BasicParser;
import doc.wacc.astNodes.AST;
import doc.wacc.utils.ASTVisitor;
import doc.wacc.utils.CompilerVisitor;
import doc.wacc.utils.ErrorMessage;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;

import java.io.*;
import java.util.BitSet;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.exit;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class backEndTests {
    // do not run in IDE, run all the tests using commandline:
    // ```mvn test:jute``` or just ```mvn test ```
    // to run all the tests

    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    @Test
    public void backend_if1() throws IOException, InterruptedException {
        String fp = "wacc_examples/valid/if/if1.wacc";
        emulator(fp);
        ProcessBuilder pb = new ProcessBuilder();

        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec("arm-linux-gnueabi-gcc -o tempProg -mcpu=arm1176jzf-s -mtune=arm1176jzf-s if1.s");
        pr.waitFor();
        Process pr2 = rt.exec("qemu-arm -L /usr/arm-linux-gnueabi/ tempProg");
        pr2.waitFor();
        OutputStreamWriter osw = new OutputStreamWriter(pr2.getOutputStream());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pr2.getInputStream()));
        String myOutput = bufferedReader.readLine();
        int myExitCode = pr2.exitValue();

        assertEquals(myExitCode, 0);
        assertEquals(myOutput, "correct");
    }

    @Test
    public void backend_if2() throws IOException, InterruptedException {
        String fp = "wacc_examples/valid/if/if2.wacc";
        emulator(fp);
        ProcessBuilder pb = new ProcessBuilder();

        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec("arm-linux-gnueabi-gcc -o tempProg -mcpu=arm1176jzf-s -mtune=arm1176jzf-s if2.s");
        pr.waitFor();
        Process pr2 = rt.exec("qemu-arm -L /usr/arm-linux-gnueabi/ tempProg");
        pr2.waitFor();
        OutputStreamWriter osw = new OutputStreamWriter(pr2.getOutputStream());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pr2.getInputStream()));
        String myOutput = bufferedReader.readLine();
        int myExitCode = pr2.exitValue();

        assertEquals(myExitCode, 0);
        assertEquals(myOutput, "correct");
    }

    @Test
    public void backend_array() throws IOException, InterruptedException {
        String fp = "wacc_examples/valid/array/array.wacc";
        emulator(fp);
        ProcessBuilder pb = new ProcessBuilder();

        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec("arm-linux-gnueabi-gcc -o tempProg -mcpu=arm1176jzf-s -mtune=arm1176jzf-s array.s");
        pr.waitFor();
        Process pr2 = rt.exec("qemu-arm -L /usr/arm-linux-gnueabi/ tempProg");
        pr2.waitFor();
        OutputStreamWriter osw = new OutputStreamWriter(pr2.getOutputStream());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pr2.getInputStream()));
        String myOutput = bufferedReader.readLine();
        int myExitCode = pr2.exitValue();

        assertEquals(myExitCode, 0);
        myOutput = myOutput.split("=")[1];
        assertEquals(myOutput, " {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}");
    }

    @Test
    public void backend_arrayBasic() throws IOException, InterruptedException {
        String fp = "wacc_examples/valid/array/arrayBasic.wacc";
        emulator(fp);
        ProcessBuilder pb = new ProcessBuilder();

        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec("arm-linux-gnueabi-gcc -o tempProg -mcpu=arm1176jzf-s -mtune=arm1176jzf-s arrayBasic.s");
        pr.waitFor();
        Process pr2 = rt.exec("qemu-arm -L /usr/arm-linux-gnueabi/ tempProg");
        pr2.waitFor();
        OutputStreamWriter osw = new OutputStreamWriter(pr2.getOutputStream());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pr2.getInputStream()));
        String myOutput = bufferedReader.readLine();
        int myExitCode = pr2.exitValue();

        assertEquals(myExitCode, 0);
        assertNull(myOutput);
    }

    @Test
    public void backend_arrayEmpty() throws IOException, InterruptedException {
        String fp = "wacc_examples/valid/array/arrayEmpty.wacc";
        emulator(fp);
        ProcessBuilder pb = new ProcessBuilder();

        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec("arm-linux-gnueabi-gcc -o tempProg -mcpu=arm1176jzf-s -mtune=arm1176jzf-s arrayEmpty.s");
        pr.waitFor();
        Process pr2 = rt.exec("qemu-arm -L /usr/arm-linux-gnueabi/ tempProg");
        pr2.waitFor();
        OutputStreamWriter osw = new OutputStreamWriter(pr2.getOutputStream());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pr2.getInputStream()));
        String myOutput = bufferedReader.readLine();
        int myExitCode = pr2.exitValue();

        assertEquals(myExitCode, 0);
        assertNull(myOutput);
    }

    @Test
    public void backend_arrayLength() throws IOException, InterruptedException {
        String fp = "wacc_examples/valid/array/arrayLength.wacc";
        emulator(fp);
        ProcessBuilder pb = new ProcessBuilder();

        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec("arm-linux-gnueabi-gcc -o tempProg -mcpu=arm1176jzf-s -mtune=arm1176jzf-s arrayLength.s");
        pr.waitFor();
        Process pr2 = rt.exec("qemu-arm -L /usr/arm-linux-gnueabi/ tempProg");
        pr2.waitFor();
        OutputStreamWriter osw = new OutputStreamWriter(pr2.getOutputStream());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pr2.getInputStream()));
        String myOutput = bufferedReader.readLine();
        int myExitCode = pr2.exitValue();

        assertEquals(myExitCode, 0);
        assertEquals(myOutput, "4");
    }

    @Test
    public void backend_arrayLookup() throws IOException, InterruptedException {
        String fp = "wacc_examples/valid/array/arrayLookup.wacc";
        emulator(fp);
        ProcessBuilder pb = new ProcessBuilder();

        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec("arm-linux-gnueabi-gcc -o tempProg -mcpu=arm1176jzf-s -mtune=arm1176jzf-s arrayLookup.s");
        pr.waitFor();
        Process pr2 = rt.exec("qemu-arm -L /usr/arm-linux-gnueabi/ tempProg");
        pr2.waitFor();
        OutputStreamWriter osw = new OutputStreamWriter(pr2.getOutputStream());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pr2.getInputStream()));
        String myOutput = bufferedReader.readLine();
        int myExitCode = pr2.exitValue();

        assertEquals(myExitCode, 0);
        assertEquals(myOutput, "43");
    }

    @Test
    public void backend_arrayNested() throws IOException, InterruptedException {
        String fp = "wacc_examples/valid/array/arrayNested.wacc";
        emulator(fp);
        ProcessBuilder pb = new ProcessBuilder();

        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec("arm-linux-gnueabi-gcc -o tempProg -mcpu=arm1176jzf-s -mtune=arm1176jzf-s arrayNested.s");
        pr.waitFor();
        Process pr2 = rt.exec("qemu-arm -L /usr/arm-linux-gnueabi/ tempProg");
        pr2.waitFor();
        OutputStreamWriter osw = new OutputStreamWriter(pr2.getOutputStream());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pr2.getInputStream()));
        String myOutput = bufferedReader.readLine();
        int myExitCode = pr2.exitValue();

        assertEquals(myExitCode, 0);
        assertEquals(myOutput, "3");
    }

    @Test
    public void backend_arrayPrint() throws IOException, InterruptedException {
        String fp = "wacc_examples/valid/array/arrayPrint.wacc";
        emulator(fp);
        ProcessBuilder pb = new ProcessBuilder();

        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec("arm-linux-gnueabi-gcc -o tempProg -mcpu=arm1176jzf-s -mtune=arm1176jzf-s arrayPrint.s");
        pr.waitFor();
        Process pr2 = rt.exec("qemu-arm -L /usr/arm-linux-gnueabi/ tempProg");
        pr2.waitFor();
        OutputStreamWriter osw = new OutputStreamWriter(pr2.getOutputStream());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pr2.getInputStream()));
        String myOutput = bufferedReader.readLine();
        int myExitCode = pr2.exitValue();

        assertEquals(myExitCode, 0);
        myOutput = myOutput.split("=")[1];
        assertEquals(myOutput, " {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}");
    }

    @Test
    public void backend_arraySimple() throws IOException, InterruptedException {
        String fp = "wacc_examples/valid/array/arraySimple.wacc";
        emulator(fp);
        ProcessBuilder pb = new ProcessBuilder();

        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec("arm-linux-gnueabi-gcc -o tempProg -mcpu=arm1176jzf-s -mtune=arm1176jzf-s arraySimple.s");
        pr.waitFor();
        Process pr2 = rt.exec("qemu-arm -L /usr/arm-linux-gnueabi/ tempProg");
        pr2.waitFor();
        OutputStreamWriter osw = new OutputStreamWriter(pr2.getOutputStream());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pr2.getInputStream()));
        String myOutput = bufferedReader.readLine();
        int myExitCode = pr2.exitValue();

        assertEquals(myExitCode, 0);
        assertEquals(myOutput, "42");
    }

    @Test
    public void backend_modifyString() throws IOException, InterruptedException {
        String fp = "wacc_examples/valid/array/modifyString.wacc";
        emulator(fp);
        ProcessBuilder pb = new ProcessBuilder();

        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec("arm-linux-gnueabi-gcc -o tempProg -mcpu=arm1176jzf-s -mtune=arm1176jzf-s modifyString.s");
        pr.waitFor();
        Process pr2 = rt.exec("qemu-arm -L /usr/arm-linux-gnueabi/ tempProg");
        pr2.waitFor();
        OutputStreamWriter osw = new OutputStreamWriter(pr2.getOutputStream());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pr2.getInputStream()));
        String myOutput0 = bufferedReader.readLine();
        String myOutput1 = bufferedReader.readLine();
        String myOutput2 = bufferedReader.readLine();
        int myExitCode = pr2.exitValue();

        assertEquals(myExitCode, 0);
        assertEquals(myOutput0, "hello world!");
        assertEquals(myOutput1, "Hello world!");
        assertEquals(myOutput2, "Hi!");
    }

    @Test
    public void backend_printRef() throws IOException, InterruptedException {
        String fp = "wacc_examples/valid/array/printRef.wacc";
        emulator(fp);
        ProcessBuilder pb = new ProcessBuilder();

        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec("arm-linux-gnueabi-gcc -o tempProg -mcpu=arm1176jzf-s -mtune=arm1176jzf-s printRef.s");
        pr.waitFor();
        Process pr2 = rt.exec("qemu-arm -L /usr/arm-linux-gnueabi/ tempProg");
        pr2.waitFor();
        OutputStreamWriter osw = new OutputStreamWriter(pr2.getOutputStream());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pr2.getInputStream()));
        String myOutput = bufferedReader.readLine();
        int myExitCode = pr2.exitValue();

        assertEquals(myExitCode, 0);
        myOutput = myOutput.split("0x")[0];
        assertEquals(myOutput, "Printing an array variable gives an address, such as ");
    }

    @Test
    public void backend_expression_charComp() throws IOException, InterruptedException {
        String fp = "wacc_examples/valid/expressions/charComparisonExpr.wacc";
        emulator(fp);
        ProcessBuilder pb = new ProcessBuilder();

        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec("arm-linux-gnueabi-gcc -o tempProg -mcpu=arm1176jzf-s -mtune=arm1176jzf-s charComparisonExpr.s");
        pr.waitFor();
        Process pr2 = rt.exec("qemu-arm -L /usr/arm-linux-gnueabi/ tempProg");
        pr2.waitFor();
        OutputStreamWriter osw = new OutputStreamWriter(pr2.getOutputStream());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pr2.getInputStream()));
        int myExitCode = pr2.exitValue();

        assertEquals(myExitCode, 0);
        assertEquals(bufferedReader.readLine(), "false");
        assertEquals(bufferedReader.readLine(), "true");
        assertEquals(bufferedReader.readLine(), "true");
        assertEquals(bufferedReader.readLine(), "true");
        assertEquals(bufferedReader.readLine(), "false");
    }

    @Test
    public void backend_expression_longExpr() throws IOException, InterruptedException {
        String fp = "wacc_examples/valid/expressions/longExpr.wacc";
        emulator(fp);
        ProcessBuilder pb = new ProcessBuilder();

        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec("arm-linux-gnueabi-gcc -o tempProg -mcpu=arm1176jzf-s -mtune=arm1176jzf-s longExpr.s");
        pr.waitFor();
        Process pr2 = rt.exec("qemu-arm -L /usr/arm-linux-gnueabi/ tempProg");
        pr2.waitFor();
        OutputStreamWriter osw = new OutputStreamWriter(pr2.getOutputStream());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pr2.getInputStream()));
        int myExitCode = pr2.exitValue();
        String myOutput = bufferedReader.readLine();

        assertEquals(myExitCode, 153);
        assertNull(myOutput);
    }

    @Test
    public void backend_expression_divExpr() throws IOException, InterruptedException {
        String fp = "wacc_examples/valid/expressions/divExpr.wacc";
        emulator(fp);
        ProcessBuilder pb = new ProcessBuilder();

        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec("arm-linux-gnueabi-gcc -o tempProg -mcpu=arm1176jzf-s -mtune=arm1176jzf-s divExpr.s");
        pr.waitFor();
        Process pr2 = rt.exec("qemu-arm -L /usr/arm-linux-gnueabi/ tempProg");
        pr2.waitFor();
        OutputStreamWriter osw = new OutputStreamWriter(pr2.getOutputStream());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pr2.getInputStream()));
        int myExitCode = pr2.exitValue();
        String myOutput = bufferedReader.readLine();
        assertEquals(myExitCode, 0);
        assertEquals(myOutput, "1");
    }

    @Test
    public void backend_expression_greaterEqExpr() throws IOException, InterruptedException {
        String fp = "wacc_examples/valid/expressions/greaterEqExpr.wacc";
        emulator(fp);
        ProcessBuilder pb = new ProcessBuilder();

        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec("arm-linux-gnueabi-gcc -o tempProg -mcpu=arm1176jzf-s -mtune=arm1176jzf-s greaterEqExpr.s");
        pr.waitFor();
        Process pr2 = rt.exec("qemu-arm -L /usr/arm-linux-gnueabi/ tempProg");
        pr2.waitFor();
        OutputStreamWriter osw = new OutputStreamWriter(pr2.getOutputStream());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pr2.getInputStream()));
        int myExitCode = pr2.exitValue();
        assertEquals(bufferedReader.readLine(), "false");
        assertEquals(bufferedReader.readLine(), "true");
        assertEquals(bufferedReader.readLine(), "true");
        assertEquals(myExitCode, 0);
    }

    @Test
    public void backend_expression_negDividendMod() throws IOException, InterruptedException {
        String fp = "wacc_examples/valid/expressions/negDividendMod.wacc";
        emulator(fp);
        ProcessBuilder pb = new ProcessBuilder();

        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec("arm-linux-gnueabi-gcc -o tempProg -mcpu=arm1176jzf-s -mtune=arm1176jzf-s negDividendMod.s");
        pr.waitFor();
        Process pr2 = rt.exec("qemu-arm -L /usr/arm-linux-gnueabi/ tempProg");
        pr2.waitFor();
        OutputStreamWriter osw = new OutputStreamWriter(pr2.getOutputStream());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pr2.getInputStream()));
        int myExitCode = pr2.exitValue();
        assertEquals(bufferedReader.readLine(), "-2");
        assertEquals(myExitCode, 0);
    }

    @Test
    public void backend_expression_multExpr() throws IOException, InterruptedException {
        String fp = "wacc_examples/valid/expressions/multExpr.wacc";
        emulator(fp);
        ProcessBuilder pb = new ProcessBuilder();

        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec("arm-linux-gnueabi-gcc -o tempProg -mcpu=arm1176jzf-s -mtune=arm1176jzf-s multExpr.s");
        pr.waitFor();
        Process pr2 = rt.exec("qemu-arm -L /usr/arm-linux-gnueabi/ tempProg");
        pr2.waitFor();
        OutputStreamWriter osw = new OutputStreamWriter(pr2.getOutputStream());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pr2.getInputStream()));
        int myExitCode = pr2.exitValue();
        assertEquals(bufferedReader.readLine(), "15");
        assertEquals(myExitCode, 0);
    }

    @Test
    public void backend_expression_negBothDiv() throws IOException, InterruptedException {
        String fp = "wacc_examples/valid/expressions/negBothDiv.wacc";
        emulator(fp);
        ProcessBuilder pb = new ProcessBuilder();

        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec("arm-linux-gnueabi-gcc -o tempProg -mcpu=arm1176jzf-s -mtune=arm1176jzf-s negBothDiv.s");
        pr.waitFor();
        Process pr2 = rt.exec("qemu-arm -L /usr/arm-linux-gnueabi/ tempProg");
        pr2.waitFor();
        OutputStreamWriter osw = new OutputStreamWriter(pr2.getOutputStream());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pr2.getInputStream()));
        int myExitCode = pr2.exitValue();
        assertEquals(bufferedReader.readLine(), "2");
        assertEquals(myExitCode, 0);
    }

    @Test
    public void backend_expression_negBothModExpr() throws IOException, InterruptedException {
        Result_of_execution result = exec("negBothMod");
        BufferedReader myOutput = result.getBufferedReader();
        assertEquals(myOutput.readLine(), "-2");
        assertEquals(result.getExit_code(), 0);
    }


    private Result_of_execution exec(String filename) throws IOException, InterruptedException {
        String fp = "wacc_examples/valid/expressions/" + filename + ".wacc";
        emulator(fp);
        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec("arm-linux-gnueabi-gcc -o tempProg -mcpu=arm1176jzf-s -mtune=arm1176jzf-s " + filename + ".s");
        pr.waitFor();
        Process pr2 = rt.exec("qemu-arm -L /usr/arm-linux-gnueabi/ tempProg");
        pr2.waitFor();
        OutputStreamWriter osw = new OutputStreamWriter(pr2.getOutputStream());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pr2.getInputStream()));
        return new Result_of_execution(bufferedReader, pr2.exitValue());
    }

    private class Result_of_execution {
        private BufferedReader bufferedReader;
        private int exit_code;


        private Result_of_execution(BufferedReader bufferedReader, int exit_code) {
            this.bufferedReader = bufferedReader;
            this.exit_code = exit_code;
        }

        public BufferedReader getBufferedReader() {
            return bufferedReader;
        }

        public int getExit_code() {
            return exit_code;
        }
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
