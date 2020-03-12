import antlr.BasicLexer;
import antlr.BasicParser;
import doc.wacc.astNodes.AST;
import doc.wacc.astNodes.RefNode;
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

import static java.lang.System.*;
import static org.junit.Assert.assertEquals;

public class extention_pointer_test {

    @Test
    public void extension_ptrChar() throws IOException, InterruptedException {
        Result_of_execution result = exec_extension_ptr("ptrChar");
        BufferedReader myOutput = result.getBufferedReader();
        assertEquals(result.getExit_code(), 0);
        assertEquals(myOutput.readLine(), "a");
    }

    @Test
    public void extension_ptrInt() throws IOException, InterruptedException {
        Result_of_execution result = exec_extension_ptr("ptrInt");
        BufferedReader myOutput = result.getBufferedReader();
        assertEquals(result.getExit_code(), 0);
        assertEquals(myOutput.readLine(), "100");
    }

    @Test
    public void extension_ptrNext_Int() throws IOException, InterruptedException {
        Result_of_execution result = exec_extension_ptr("ptrNext_Int");
        BufferedReader myOutput = result.getBufferedReader();
        assertEquals(result.getExit_code(), 0);
        assertEquals(myOutput.readLine(), "200");
    }

    @Test
    public void extension_ptrNext_Char() throws IOException, InterruptedException {
        Result_of_execution result = exec_extension_ptr("ptrNext_Char");
        BufferedReader myOutput = result.getBufferedReader();
        assertEquals(result.getExit_code(), 0);
        assertEquals(myOutput.readLine(), "b");
    }

    @Test
    public void extension_ptrNull() throws IOException, InterruptedException {
        Result_of_execution result = exec_extension_ptr("ptrNull");
        BufferedReader myOutput = result.getBufferedReader();
        assertEquals(result.getExit_code(), 139);
    }

    @Test
    public void extension_ptrString() throws IOException, InterruptedException {
        Result_of_execution result = exec_extension_ptr("ptrStr");
        BufferedReader myOutput = result.getBufferedReader();
        assertEquals(result.getExit_code(), 0);
        assertEquals(myOutput.readLine(), "hi");
    }

    @Test
    public void extension_ptrArray() throws IOException, InterruptedException {
        Result_of_execution result = exec_extension_ptr("ptrArray");
        BufferedReader myOutput = result.getBufferedReader();
        assertEquals(result.getExit_code(), 0);
        assertEquals(myOutput.readLine(), "1");
        assertEquals(myOutput.readLine(), "2");
        assertEquals(myOutput.readLine(), "3");
    }

    @Test
    public void extension_ptrArrayChr() throws IOException, InterruptedException {
        Result_of_execution result = exec_extension_ptr("ptrArrayChr");
        BufferedReader myOutput = result.getBufferedReader();
        assertEquals(result.getExit_code(), 0);
        assertEquals(myOutput.readLine(), "a");
        assertEquals(myOutput.readLine(), "b");
        assertEquals(myOutput.readLine(), "c");
    }

    @Test
    public void extension_ptrAssignRHS() throws IOException, InterruptedException {
        Result_of_execution result = exec_extension_ptr("ptrAssignRHS");
        BufferedReader myOutput = result.getBufferedReader();
        assertEquals(result.getExit_code(), 0);
        assertEquals("1", myOutput.readLine());
    }

    @Test
    public void extension_ptrAssignLHS() throws IOException, InterruptedException {
        Result_of_execution result = exec_extension_ptr("ptrAssign");
        BufferedReader myOutput = result.getBufferedReader();
        assertEquals(result.getExit_code(), 0);
        assertEquals("5", myOutput.readLine());

    }

    @Test
    public void extension_ptrBool() throws IOException, InterruptedException {
        Result_of_execution result = exec_extension_ptr("ptrBool");
        BufferedReader myOutput = result.getBufferedReader();
        assertEquals(result.getExit_code(), 0);
        assertEquals(myOutput.readLine(), "true");
    }



    private Result_of_execution exec_extension_ptr(String filename) throws IOException, InterruptedException {
        String fp = "wacc_examples/valid/ptr_test/" + filename + ".wacc";
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

    private static class Result_of_execution {
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
