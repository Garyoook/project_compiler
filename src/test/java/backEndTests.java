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
import static java.lang.System.out;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.*;

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
        Result_of_execution result = exec_expression("negBothMod");
        BufferedReader myOutput = result.getBufferedReader();
        assertEquals(myOutput.readLine(), "-2");
        assertEquals(result.getExit_code(), 0);
    }

    @Test
    public void backend_while_fibonacciIterative() throws IOException, InterruptedException {
        Result_of_execution result = exec_while("fibonacciIterative");
        BufferedReader myOutput = result.getBufferedReader();
        assertEquals(myOutput.readLine(), "The first 20 fibonacci numbers are:");
        assertEquals(myOutput.readLine(), "0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610, 987, 1597, 2584, 4181, ...");
        assertEquals(result.getExit_code(), 0);
    }

    @Test
    public void backend_while_loopCharCond() throws IOException, InterruptedException {
        Result_of_execution result = exec_while("loopCharCondition");
        BufferedReader myOutput = result.getBufferedReader();
        assertEquals(myOutput.readLine(), "Change c");
        assertEquals(myOutput.readLine(), "Should print \"Change c\" once before.");
        assertEquals(result.getExit_code(), 0);
    }

    @Test
    public void backend_while_loopIntCondition() throws IOException, InterruptedException {
        Result_of_execution result = exec_while("loopIntCondition");
        BufferedReader myOutput = result.getBufferedReader();
        assertEquals(myOutput.readLine(), "Change n");
        assertEquals(myOutput.readLine(), "Should print \"Change n\" once before.");
        assertEquals(result.getExit_code(), 0);
    }

    @Test
    public void backend_while_max() throws IOException, InterruptedException {
        Result_of_execution result = exec_while("max");
        BufferedReader myOutput = result.getBufferedReader();
        assertEquals(myOutput.readLine(), "max value = 17");
        assertEquals(result.getExit_code(), 0);
    }

    @Test
    public void backend_while_min() throws IOException, InterruptedException {
        Result_of_execution result = exec_while("min");
        BufferedReader myOutput = result.getBufferedReader();
        assertEquals(myOutput.readLine(), "min value = 10");
        assertEquals(result.getExit_code(), 0);
    }

    @Test
    public void backend_while_rmStyleAdd() throws IOException, InterruptedException {
        Result_of_execution result = exec_while("rmStyleAdd");
        BufferedReader myOutput = result.getBufferedReader();
        assertEquals(myOutput.readLine(), "initial value of x: 3");
        assertEquals(myOutput.readLine(), "(+)(+)(+)(+)(+)(+)(+)");
        assertEquals(myOutput.readLine(), "final value of x: 10");
        assertEquals(result.getExit_code(), 0);
    }

    @Test
    public void backend_while_boolFlip() throws IOException, InterruptedException {
        Result_of_execution result = exec_while("whileBoolFlip");
        BufferedReader myOutput = result.getBufferedReader();
        assertEquals(myOutput.readLine(), "flip b!");
        assertEquals(myOutput.readLine(), "end of loop");
        assertEquals(result.getExit_code(), 0);
    }

    @Test
    public void backend_while_whileCount() throws IOException, InterruptedException {
        Result_of_execution result = exec_while("whileCount");
        BufferedReader myOutput = result.getBufferedReader();
        assertEquals(myOutput.readLine(), "Can you count to 10?");
        assertEquals(myOutput.readLine(), "1");
        assertEquals(myOutput.readLine(), "2");
        assertEquals(myOutput.readLine(), "3");
        assertEquals(myOutput.readLine(), "4");
        assertEquals(myOutput.readLine(), "5");
        assertEquals(myOutput.readLine(), "6");
        assertEquals(myOutput.readLine(), "7");
        assertEquals(myOutput.readLine(), "8");
        assertEquals(myOutput.readLine(), "9");
        assertEquals(myOutput.readLine(), "10");
        assertEquals(result.getExit_code(), 0);
    }

    @Test
    public void backend_variables_varNames() throws IOException, InterruptedException {
        Result_of_execution result = exec_variable("_VarNames");
        BufferedReader myOutput = result.getBufferedReader();
        assertNull(myOutput.readLine());
        assertEquals(result.getExit_code(), 19);
    }

    @Test
    public void backend_variables_capCharDeclaration() throws IOException, InterruptedException {
        Result_of_execution result = exec_variable("capCharDeclaration");
        BufferedReader myOutput = result.getBufferedReader();
        assertNull(myOutput.readLine());
        assertEquals(result.getExit_code(), 0);
    }

    @Test
    public void backend_variables_charDeclaration() throws IOException, InterruptedException {
        Result_of_execution result = exec_variable("charDeclaration");
        BufferedReader myOutput = result.getBufferedReader();
        assertNull(myOutput.readLine());
        assertEquals(result.getExit_code(), 0);
    }

    @Test
    public void backend_variables_charDeclaration2() throws IOException, InterruptedException {
        Result_of_execution result = exec_variable("charDeclaration2");
        BufferedReader myOutput = result.getBufferedReader();
        assertNull(myOutput.readLine());
        assertEquals(result.getExit_code(), 0);
    }

    @Test
    public void backend_variables_intDeclaration() throws IOException, InterruptedException {
        Result_of_execution result = exec_variable("intDeclaration");
        BufferedReader myOutput = result.getBufferedReader();
        assertNull(myOutput.readLine());
        assertEquals(result.getExit_code(), 0);
    }

    @Test
    public void backend_variables_boolDeclaration() throws IOException, InterruptedException {
        Result_of_execution result = exec_variable("boolDeclaration");
        BufferedReader myOutput = result.getBufferedReader();
        assertNull(myOutput.readLine());
        assertEquals(result.getExit_code(), 0);
    }

    @Test
    public void backend_variables_boolDeclaration2() throws IOException, InterruptedException {
        Result_of_execution result = exec_variable("boolDeclaration2");
        BufferedReader myOutput = result.getBufferedReader();
        assertNull(myOutput.readLine());
        assertEquals(result.getExit_code(), 0);
    }

    @Test
    public void backend_variables_emptyStringDeclaration2() throws IOException, InterruptedException {
        Result_of_execution result = exec_variable("emptyStringDeclaration");
        BufferedReader myOutput = result.getBufferedReader();
        assertNull(myOutput.readLine());
        assertEquals(result.getExit_code(), 0);
    }

    @Test
    public void backend_variables_longVarNames() throws IOException, InterruptedException {
        Result_of_execution result = exec_variable("longVarNames");
        BufferedReader myOutput = result.getBufferedReader();
        assertNull(myOutput.readLine());
        assertEquals(result.getExit_code(), 5);
    }

    @Test
    public void backend_variables_manyVar() throws IOException, InterruptedException {
        Result_of_execution result = exec_variable("manyVariables");
        BufferedReader myOutput = result.getBufferedReader();
        assertNull(myOutput.readLine());
        assertEquals(result.getExit_code(), 0);
    }

    @Test
    public void backend_variables_negIntDeclaration() throws IOException, InterruptedException {
        Result_of_execution result = exec_variable("negIntDeclaration");
        BufferedReader myOutput = result.getBufferedReader();
        assertNull(myOutput.readLine());
        assertEquals(result.getExit_code(), 0);
    }

    @Test
    public void backend_variables_puncCharDeclaration() throws IOException, InterruptedException {
        Result_of_execution result = exec_variable("puncCharDeclaration");
        BufferedReader myOutput = result.getBufferedReader();
        assertNull(myOutput.readLine());
        assertEquals(result.getExit_code(), 0);
    }

    @Test
    public void backend_variables_stringDeclaration() throws IOException, InterruptedException {
        Result_of_execution result = exec_variable("stringDeclaration");
        BufferedReader myOutput = result.getBufferedReader();
        assertNull(myOutput.readLine());
        assertEquals(result.getExit_code(), 0);
    }

    @Test
    public void backend_variables_zeroIntDeclaration() throws IOException, InterruptedException {
        Result_of_execution result = exec_variable("zeroIntDeclaration");
        BufferedReader myOutput = result.getBufferedReader();
        assertNull(myOutput.readLine());
        assertEquals(result.getExit_code(), 0);
    }

    @Test
    public void backend_sequence_basicSeq() throws IOException, InterruptedException {
        Result_of_execution result = exec_sequence("basicSeq");
        BufferedReader myOutput = result.getBufferedReader();
        assertNull(myOutput.readLine());
        assertEquals(result.getExit_code(), 0);
    }

    @Test
    public void backend_sequence_basicSeq2() throws IOException, InterruptedException {
        Result_of_execution result = exec_sequence("basicSeq2");
        BufferedReader myOutput = result.getBufferedReader();
        assertNull(myOutput.readLine());
        assertEquals(result.getExit_code(), 0);
    }

    @Test
    public void backend_sequence_boolAssignment() throws IOException, InterruptedException {
        Result_of_execution result = exec_sequence("boolAssignment");
        BufferedReader myOutput = result.getBufferedReader();
        assertNull(myOutput.readLine());
        assertEquals(result.getExit_code(), 0);
    }

    @Test
    public void backend_sequence_charAssignment() throws IOException, InterruptedException {
        Result_of_execution result = exec_sequence("charAssignment");
        BufferedReader myOutput = result.getBufferedReader();
        assertNull(myOutput.readLine());
        assertEquals(result.getExit_code(), 0);
    }

    @Test
    public void backend_sequence_exitSimple() throws IOException, InterruptedException {
        Result_of_execution result = exec_sequence("exitSimple");
        BufferedReader myOutput = result.getBufferedReader();
        assertNull(myOutput.readLine());
        assertEquals(result.getExit_code(), 42);
    }

    @Test
    public void backend_sequence_intAssignment() throws IOException, InterruptedException {
        Result_of_execution result = exec_sequence("intAssignment");
        BufferedReader myOutput = result.getBufferedReader();
        assertNull(myOutput.readLine());
        assertEquals(result.getExit_code(), 20);
    }

    @Test
    public void backend_sequence_intLeadingZeros() throws IOException, InterruptedException {
        Result_of_execution result = exec_sequence("intLeadingZeros");
        BufferedReader myOutput = result.getBufferedReader();
        assertEquals(myOutput.readLine(), "42");
        assertEquals(myOutput.readLine(), "0");
        assertEquals(result.getExit_code(), 0);
    }

    @Test
    public void backend_sequence_stringAssignment() throws IOException, InterruptedException {
        Result_of_execution result = exec_sequence("stringAssignment");
        BufferedReader myOutput = result.getBufferedReader();
        assertNull(myOutput.readLine());
        assertEquals(result.getExit_code(), 0);
    }

    @Test
    public void backend_scope_ifNested1() throws IOException, InterruptedException {
        Result_of_execution result = exec_scope("ifNested1");
        BufferedReader myOutput = result.getBufferedReader();
        assertEquals(myOutput.readLine(), "correct");
        assertEquals(result.getExit_code(), 0);
    }

    @Test
    public void backend_scope_ifNested2() throws IOException, InterruptedException {
        Result_of_execution result = exec_scope("ifNested2");
        BufferedReader myOutput = result.getBufferedReader();
        assertEquals(myOutput.readLine(), "correct");
        assertEquals(result.getExit_code(), 0);
    }

    @Test
    public void backend_scope_indentation() throws IOException, InterruptedException {
        Result_of_execution result = exec_scope("indentationNotImportant");
        BufferedReader myOutput = result.getBufferedReader();
        assertNull(myOutput.readLine());
        assertEquals(result.getExit_code(), 0);
    }

    @Test
    public void backend_scope_intsAndKeywords() throws IOException, InterruptedException {
        Result_of_execution result = exec_scope("intsAndKeywords");
        BufferedReader myOutput = result.getBufferedReader();
        assertNull(myOutput.readLine());
        assertEquals(result.getExit_code(), 0);
    }

    @Test
    public void backend_scope_printAllTypes() throws IOException, InterruptedException {
        Result_of_execution result = exec_scope("printAllTypes");
        BufferedReader myOutput = result.getBufferedReader();
        assertEquals(myOutput.readLine(), "( [1, 2, 3] , [a, b, c] )");
        myOutput.readLine();
        assertEquals(myOutput.readLine(), "1, 2");
        assertEquals(myOutput.readLine(), "array, of, strings");
        assertEquals(myOutput.readLine(), "true, false, true");
        assertEquals(myOutput.readLine(), "xyz");
        assertEquals(myOutput.readLine(), "1, 2, 3");
        assertEquals(myOutput.readLine(), "this is a string");
        assertEquals(myOutput.readLine(), "true");
        assertEquals(myOutput.readLine(), "x");
        assertEquals(myOutput.readLine(), "5");
        assertEquals(result.getExit_code(), 0);
    }

    @Test
    public void backend_scope_scope() throws IOException, InterruptedException {
        Result_of_execution result = exec_scope("scope");
        BufferedReader myOutput = result.getBufferedReader();
        assertNull(myOutput.readLine());
        assertEquals(result.getExit_code(), 0);
    }

    @Test
    public void backend_scope_scopebasic() throws IOException, InterruptedException {
        Result_of_execution result = exec_scope("scopeBasic");
        BufferedReader myOutput = result.getBufferedReader();
        assertNull(myOutput.readLine());
        assertEquals(result.getExit_code(), 0);
    }

    @Test
    public void backend_scope_scopeRedefine() throws IOException, InterruptedException {
        Result_of_execution result = exec_scope("scopeRedefine");
        BufferedReader myOutput = result.getBufferedReader();
        assertEquals(myOutput.readLine(), "true");
        assertEquals(myOutput.readLine(), "2");
        assertEquals(result.getExit_code(), 0);
    }

    @Test
    public void backend_scope_scopeSimlpleRedefine() throws IOException, InterruptedException {
        Result_of_execution result = exec_scope("scopeSimpleRedefine");
        BufferedReader myOutput = result.getBufferedReader();
        assertEquals(myOutput.readLine(), "true");
        assertEquals(myOutput.readLine(), "12");
        assertEquals(result.getExit_code(), 0);
    }

    @Test
    public void backend_scope_scopeVars() throws IOException, InterruptedException {
        Result_of_execution result = exec_scope("scopeVars");
        BufferedReader myOutput = result.getBufferedReader();
        assertEquals(myOutput.readLine(), "2");
        assertEquals(myOutput.readLine(), "4");
        assertEquals(myOutput.readLine(), "2");
        assertEquals(result.getExit_code(), 0);
    }

    private Result_of_execution exec_expression(String filename) throws IOException, InterruptedException {
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

    @Test
    public void backend_expression_notExpr() throws IOException, InterruptedException {
        Result_of_execution result = exec_expression("notExpr");
        BufferedReader myOutput = result.getBufferedReader();
        assertEquals(myOutput.readLine(), "false");
        assertEquals(myOutput.readLine(), "true");
        assertEquals(result.getExit_code(), 0);
    }

    @Test
    public void backend_expression_negDivisorModExpr() throws IOException, InterruptedException {
        Result_of_execution result = exec_expression("negDivisorMod");
        BufferedReader myOutput = result.getBufferedReader();
        assertEquals(myOutput.readLine(), "2");
        assertEquals(result.getExit_code(), 0);
    }

    @Test
    public void backend_expression_negExpr() throws IOException, InterruptedException {
        Result_of_execution result = exec_expression("negExpr");
        BufferedReader myOutput = result.getBufferedReader();
        assertEquals(myOutput.readLine(), "-42");
        assertEquals(result.getExit_code(), 0);
    }

    @Test
    public void backend_expression_plusExpr() throws IOException, InterruptedException {
        Result_of_execution result = exec_expression("plusExpr");
        BufferedReader myOutput = result.getBufferedReader();
        assertEquals(myOutput.readLine(), "35");
        assertEquals(result.getExit_code(), 0);
    }

    @Test
    public void backend_expression_plusMinusExpr() throws IOException, InterruptedException {
        Result_of_execution result = exec_expression("plusMinusExpr");
        BufferedReader myOutput = result.getBufferedReader();
        assertEquals(myOutput.readLine(), "-1");
        assertEquals(result.getExit_code(), 0);
    }

    @Test
    public void backend_expression_stringEqualsExpr() throws IOException, InterruptedException {
        Result_of_execution result = exec_expression("stringEqualsExpr");
        BufferedReader myOutput = result.getBufferedReader();
        assertEquals(myOutput.readLine(), "true");
        assertEquals(myOutput.readLine(), "false");
        assertEquals(myOutput.readLine(), "false");
        assertEquals(result.getExit_code(), 0);
    }

    @Test
    public void backend_expression_sequentialCount() throws IOException, InterruptedException {
        Result_of_execution result = exec_expression("sequentialCount");
        BufferedReader myOutput = result.getBufferedReader();
        assertEquals(myOutput.readLine(), "Can you count to 10?");
        assertEquals(myOutput.readLine(), "1");
        assertEquals(myOutput.readLine(), "2");
        assertEquals(myOutput.readLine(), "3");
        assertEquals(myOutput.readLine(), "4");
        assertEquals(myOutput.readLine(), "5");
        assertEquals(myOutput.readLine(), "6");
        assertEquals(myOutput.readLine(), "7");
        assertEquals(myOutput.readLine(), "8");
        assertEquals(myOutput.readLine(), "9");
        assertEquals(result.getExit_code(), 0);
    }

    @Test
    public void backend_expression_plusPlusExpr() throws IOException, InterruptedException {
        Result_of_execution result = exec_expression("plusPlusExpr");
        BufferedReader myOutput = result.getBufferedReader();
        assertEquals(myOutput.readLine(), "3");
        assertEquals(result.getExit_code(), 0);
    }

    @Test
    public void backend_expression_ordAndchrExpr() throws IOException, InterruptedException {
        Result_of_execution result = exec_expression("ordAndchrExpr");
        BufferedReader myOutput = result.getBufferedReader();
        assertEquals(myOutput.readLine(), "a is 97");
        assertEquals(myOutput.readLine(), "99 is c");
        assertEquals(result.getExit_code(), 0);
    }

    @Test
    public void backend_notequalsExpr() throws IOException, InterruptedException {
        Result_of_execution result = exec_expression("notequalsExpr");
        BufferedReader myOutput = result.getBufferedReader();
        assertEquals(myOutput.readLine(), "true");
        assertEquals(myOutput.readLine(), "true");
        assertEquals(myOutput.readLine(), "false");
        assertEquals(result.getExit_code(), 0);
    }

    @Test
    public void backend_incFunction() throws IOException, InterruptedException {
        Result_of_execution result = exec_simple_functions("incFunction");
        BufferedReader myOutput = result.getBufferedReader();
        assertEquals(myOutput.readLine(), "1");
        assertEquals(myOutput.readLine(), "4");
        assertEquals(result.getExit_code(), 0);
    }

    @Test
    public void backend_negFunction() throws IOException, InterruptedException {
        Result_of_execution result = exec_simple_functions("negFunction");
        BufferedReader myOutput = result.getBufferedReader();
        assertEquals(myOutput.readLine(), "true");
        assertEquals(myOutput.readLine(), "false");
        assertEquals(myOutput.readLine(), "true");
        assertEquals(result.getExit_code(), 0);
    }

    @Test
    public void backend_functionUpdateParameter() throws IOException, InterruptedException {
        Result_of_execution result = exec_simple_functions("functionUpdateParameter");
        BufferedReader myOutput = result.getBufferedReader();
        assertEquals(myOutput.readLine(), "y is 1");
        assertEquals(myOutput.readLine(), "x is 1");
        assertEquals(myOutput.readLine(), "x is now 5");
        assertEquals(myOutput.readLine(), "y is still 1");
        assertEquals(result.getExit_code(), 0);
    }

    @Test
    public void backend_sameArgName2() throws IOException, InterruptedException {
        Result_of_execution result = exec_simple_functions("sameArgName2");
        BufferedReader myOutput = result.getBufferedReader();
        assertEquals(myOutput.readLine(), "99");
        assertEquals(result.getExit_code(), 0);
    }

    @Test
    public void backend_sameNameAsVar() throws IOException, InterruptedException {
        Result_of_execution result = exec_simple_functions("sameNameAsVar");
        BufferedReader myOutput = result.getBufferedReader();
        assertEquals(myOutput.readLine(), "5");
        assertEquals(result.getExit_code(), 0);
    }

    @Test
    public void backend_functionSimpleLoop() throws IOException, InterruptedException {
        Result_of_execution result = exec_simple_functions("functionSimpleLoop");
        BufferedReader myOutput = result.getBufferedReader();
        assertEquals(myOutput.readLine(), "10");
        assertEquals(result.getExit_code(), 0);
    }

    @Test
    public void backend_functionReturnPair() throws IOException, InterruptedException {
        Result_of_execution result = exec_simple_functions("functionReturnPair");
        BufferedReader myOutput = result.getBufferedReader();
        assertEquals(myOutput.readLine(), "10");
        assertEquals(result.getExit_code(), 0);
    }

    @Test
    public void backend_functionManyArguments() throws IOException, InterruptedException {
        Result_of_execution result = exec_simple_functions("functionManyArguments");
        BufferedReader myOutput = result.getBufferedReader();
        assertEquals(myOutput.readLine(), "a is 42");
        assertEquals(myOutput.readLine(), "b is true");
        assertEquals(myOutput.readLine(), "c is u");
        assertEquals(myOutput.readLine(), "d is hello");
        myOutput.readLine();
        myOutput.readLine();
        assertEquals(myOutput.readLine(), "answer is g");
        assertEquals(result.getExit_code(), 0);
    }

    @Test
    public void backend_arrayOutOfBounds() throws IOException, InterruptedException {
        String fp = "wacc_examples/valid/runtimeErr/arrayOutOfBounds/arrayOutOfBounds.wacc";
        emulator(fp);
        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec("arm-linux-gnueabi-gcc -o tempProg -mcpu=arm1176jzf-s -mtune=arm1176jzf-s " + "arrayOutOfBounds" + ".s");
        pr.waitFor();
        Process pr2 = rt.exec("qemu-arm -L /usr/arm-linux-gnueabi/ tempProg");
        pr2.waitFor();
        OutputStreamWriter osw = new OutputStreamWriter(pr2.getOutputStream());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pr2.getInputStream()));
//        assertTrue(bufferedReader.readLine().contains("runtime_error"));
        assertEquals(pr2.exitValue(), 255);
    }

    @Test
    public void backend_arrayNegBounds() throws IOException, InterruptedException {
        String fp = "wacc_examples/valid/runtimeErr/arrayOutOfBounds/arrayNegBounds.wacc";
        emulator(fp);
        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec("arm-linux-gnueabi-gcc -o tempProg -mcpu=arm1176jzf-s -mtune=arm1176jzf-s " + "arrayNegBounds" + ".s");
        pr.waitFor();
        Process pr2 = rt.exec("qemu-arm -L /usr/arm-linux-gnueabi/ tempProg");
        pr2.waitFor();
        OutputStreamWriter osw = new OutputStreamWriter(pr2.getOutputStream());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pr2.getInputStream()));
//        assertTrue(bufferedReader.readLine().contains("runtime_error"));
        assertEquals(pr2.exitValue(), 255);
    }

    @Test
    public void backend_arrayOutOfBoundsWrite() throws IOException, InterruptedException {
        String fp = "wacc_examples/valid/runtimeErr/arrayOutOfBounds/arrayOutOfBoundsWrite.wacc";
        emulator(fp);
        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec("arm-linux-gnueabi-gcc -o tempProg -mcpu=arm1176jzf-s -mtune=arm1176jzf-s " + "arrayOutOfBoundsWrite" + ".s");
        pr.waitFor();
        Process pr2 = rt.exec("qemu-arm -L /usr/arm-linux-gnueabi/ tempProg");
        pr2.waitFor();
        OutputStreamWriter osw = new OutputStreamWriter(pr2.getOutputStream());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pr2.getInputStream()));
//        assertTrue(bufferedReader.readLine().contains("runtime_error"));
        assertEquals(pr2.exitValue(), 255);
    }

    @Test
    public void backend_freeNull() throws IOException, InterruptedException {
        String fp = "wacc_examples/valid/runtimeErr/nullDereference/freeNull.wacc";
        emulator(fp);
        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec("arm-linux-gnueabi-gcc -o tempProg -mcpu=arm1176jzf-s -mtune=arm1176jzf-s " + "freeNull" + ".s");
        pr.waitFor();
        Process pr2 = rt.exec("qemu-arm -L /usr/arm-linux-gnueabi/ tempProg");
        pr2.waitFor();
        OutputStreamWriter osw = new OutputStreamWriter(pr2.getOutputStream());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pr2.getInputStream()));
//        assertTrue(bufferedReader.readLine().contains("runtime_error"));
        assertEquals(pr2.exitValue(), 255);
    }

    @Test
    public void backend_readNull() throws IOException, InterruptedException {
        String fp = "wacc_examples/valid/runtimeErr/nullDereference/readNull1.wacc";
        emulator(fp);
        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec("arm-linux-gnueabi-gcc -o tempProg -mcpu=arm1176jzf-s -mtune=arm1176jzf-s " + "readNull1" + ".s");
        pr.waitFor();
        Process pr2 = rt.exec("qemu-arm -L /usr/arm-linux-gnueabi/ tempProg");
        pr2.waitFor();
        OutputStreamWriter osw = new OutputStreamWriter(pr2.getOutputStream());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pr2.getInputStream()));
//        assertTrue(bufferedReader.readLine().contains("runtime_error"));
        assertEquals(pr2.exitValue(), 255);
    }

    @Test
    public void backend_setNull() throws IOException, InterruptedException {
        String fp = "wacc_examples/valid/runtimeErr/nullDereference/setNull1.wacc";
        emulator(fp);
        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec("arm-linux-gnueabi-gcc -o tempProg -mcpu=arm1176jzf-s -mtune=arm1176jzf-s " + "setNull1" + ".s");
        pr.waitFor();
        Process pr2 = rt.exec("qemu-arm -L /usr/arm-linux-gnueabi/ tempProg");
        pr2.waitFor();
        OutputStreamWriter osw = new OutputStreamWriter(pr2.getOutputStream());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pr2.getInputStream()));
//        assertTrue(bufferedReader.readLine().contains("runtime_error"));
        assertEquals(pr2.exitValue(), 255);
    }

    @Test
    public void backend_useNull() throws IOException, InterruptedException {
        String fp = "wacc_examples/valid/runtimeErr/nullDereference/useNull1.wacc";
        emulator(fp);
        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec("arm-linux-gnueabi-gcc -o tempProg -mcpu=arm1176jzf-s -mtune=arm1176jzf-s " + "useNull1" + ".s");
        pr.waitFor();
        Process pr2 = rt.exec("qemu-arm -L /usr/arm-linux-gnueabi/ tempProg");
        pr2.waitFor();
        OutputStreamWriter osw = new OutputStreamWriter(pr2.getOutputStream());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pr2.getInputStream()));
//        assertTrue(bufferedReader.readLine().contains("runtime_error"));
        assertEquals(pr2.exitValue(), 255);
    }

    @Test
    public void backend_IntegerOverflow() throws IOException, InterruptedException {
        String fp = "wacc_examples/valid/runtimeErr/integerOverflow/intJustOverflow.wacc";
        emulator(fp);
        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec("arm-linux-gnueabi-gcc -o tempProg -mcpu=arm1176jzf-s -mtune=arm1176jzf-s " + "intJustOverflow" + ".s");
        pr.waitFor();
        Process pr2 = rt.exec("qemu-arm -L /usr/arm-linux-gnueabi/ tempProg");
        pr2.waitFor();
        OutputStreamWriter osw = new OutputStreamWriter(pr2.getOutputStream());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pr2.getInputStream()));
//        assertTrue(bufferedReader.readLine().contains("runtime_error"));
        assertEquals(pr2.exitValue(), 255);
    }


    @Test
    public void backend_IntegermulOverflow() throws IOException, InterruptedException {
        String fp = "wacc_examples/valid/runtimeErr/integerOverflow/intmultOverflow.wacc";
        emulator(fp);
        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec("arm-linux-gnueabi-gcc -o tempProg -mcpu=arm1176jzf-s -mtune=arm1176jzf-s " + "intmultOverflow" + ".s");
        pr.waitFor();
        Process pr2 = rt.exec("qemu-arm -L /usr/arm-linux-gnueabi/ tempProg");
        pr2.waitFor();
        OutputStreamWriter osw = new OutputStreamWriter(pr2.getOutputStream());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pr2.getInputStream()));
//        assertTrue(bufferedReader.readLine().contains("runtime_error"));
        assertEquals(pr2.exitValue(), 255);
    }

    @Test
    public void backend_IntegerNegateOverflow() throws IOException, InterruptedException {
        String fp = "wacc_examples/valid/runtimeErr/integerOverflow/intnegateOverflow.wacc";
        emulator(fp);
        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec("arm-linux-gnueabi-gcc -o tempProg -mcpu=arm1176jzf-s -mtune=arm1176jzf-s " + "intnegateOverflow" + ".s");
        pr.waitFor();
        Process pr2 = rt.exec("qemu-arm -L /usr/arm-linux-gnueabi/ tempProg");
        pr2.waitFor();
        OutputStreamWriter osw = new OutputStreamWriter(pr2.getOutputStream());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pr2.getInputStream()));
//        assertTrue(bufferedReader.readLine().contains("runtime_error"));
        assertEquals(pr2.exitValue(), 255);
    }

    @Test
    public void backend_IntegerUnderflow() throws IOException, InterruptedException {
        String fp = "wacc_examples/valid/runtimeErr/integerOverflow/intUnderflow.wacc";
        emulator(fp);
        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec("arm-linux-gnueabi-gcc -o tempProg -mcpu=arm1176jzf-s -mtune=arm1176jzf-s " + "intUnderflow" + ".s");
        pr.waitFor();
        Process pr2 = rt.exec("qemu-arm -L /usr/arm-linux-gnueabi/ tempProg");
        pr2.waitFor();
        OutputStreamWriter osw = new OutputStreamWriter(pr2.getOutputStream());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pr2.getInputStream()));
//        assertTrue(bufferedReader.readLine().contains("runtime_error"));
        assertEquals(pr2.exitValue(), 255);
    }


    @Test
    public void backend_IntegerwayOverflow() throws IOException, InterruptedException {
        String fp = "wacc_examples/valid/runtimeErr/integerOverflow/intWayOverflow.wacc";
        emulator(fp);
        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec("arm-linux-gnueabi-gcc -o tempProg -mcpu=arm1176jzf-s -mtune=arm1176jzf-s " + "intWayOverflow" + ".s");
        pr.waitFor();
        Process pr2 = rt.exec("qemu-arm -L /usr/arm-linux-gnueabi/ tempProg");
        pr2.waitFor();
        OutputStreamWriter osw = new OutputStreamWriter(pr2.getOutputStream());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pr2.getInputStream()));
//        assertTrue(bufferedReader.readLine().contains("runtime_error"));
        assertEquals(pr2.exitValue(), 255);
    }

    @Test
    public void backend_divideByZero() throws IOException, InterruptedException {
        String fp = "wacc_examples/valid/runtimeErr/divideByZero/divideByZero.wacc";
        emulator(fp);
        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec("arm-linux-gnueabi-gcc -o tempProg -mcpu=arm1176jzf-s -mtune=arm1176jzf-s " + "divideByZero" + ".s");
        pr.waitFor();
        Process pr2 = rt.exec("qemu-arm -L /usr/arm-linux-gnueabi/ tempProg");
        pr2.waitFor();
        OutputStreamWriter osw = new OutputStreamWriter(pr2.getOutputStream());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pr2.getInputStream()));
//        assertTrue(bufferedReader.readLine().contains("runtime_error"));
        assertEquals(pr2.exitValue(), 255);
    }

    @Test
    public void backend_divZero() throws IOException, InterruptedException {
        String fp = "wacc_examples/valid/runtimeErr/divideByZero/divZero.wacc";
        emulator(fp);
        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec("arm-linux-gnueabi-gcc -o tempProg -mcpu=arm1176jzf-s -mtune=arm1176jzf-s " + "divZero" + ".s");
        pr.waitFor();
        Process pr2 = rt.exec("qemu-arm -L /usr/arm-linux-gnueabi/ tempProg");
        pr2.waitFor();
        OutputStreamWriter osw = new OutputStreamWriter(pr2.getOutputStream());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pr2.getInputStream()));
//        assertTrue(bufferedReader.readLine().contains("runtime_error"));
        assertEquals(pr2.exitValue(), 255);
    }

    @Test
    public void backend_modByZero() throws IOException, InterruptedException {
        String fp = "wacc_examples/valid/runtimeErr/divideByZero/modByZero.wacc";
        emulator(fp);
        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec("arm-linux-gnueabi-gcc -o tempProg -mcpu=arm1176jzf-s -mtune=arm1176jzf-s " + "modByZero" + ".s");
        pr.waitFor();
        Process pr2 = rt.exec("qemu-arm -L /usr/arm-linux-gnueabi/ tempProg");
        pr2.waitFor();
        OutputStreamWriter osw = new OutputStreamWriter(pr2.getOutputStream());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pr2.getInputStream()));
//        assertTrue(bufferedReader.readLine().contains("runtime_error"));
        assertEquals(pr2.exitValue(), 255);
    }




    @Test
    public void backend_exit_1() throws IOException, InterruptedException {
        Result_of_execution result = exec_basic_exit("exit-1");
        BufferedReader myOutput = result.getBufferedReader();
        assertEquals(result.getExit_code(), 255);
    }

    @Test
    public void backend_Basic_2() throws IOException, InterruptedException {
        Result_of_execution result = exec_basic_exit("exitBasic2");
        BufferedReader myOutput = result.getBufferedReader();
        assertEquals(result.getExit_code(), 42);
    }

    private Result_of_execution exec_basic_exit(String filename) throws IOException, InterruptedException {
        String fp = "wacc_examples/valid/basic/exit/" + filename + ".wacc";
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


    private Result_of_execution exec_simple_functions(String filename) throws IOException, InterruptedException {
        String fp = "wacc_examples/valid/function/simple_functions/" + filename + ".wacc";
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

    private Result_of_execution exec_while(String filename) throws IOException, InterruptedException {
        String fp = "wacc_examples/valid/while/" + filename + ".wacc";
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

    private Result_of_execution exec_variable(String filename) throws IOException, InterruptedException {
        String fp = "wacc_examples/valid/variables/" + filename + ".wacc";
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

    private Result_of_execution exec_sequence(String filename) throws IOException, InterruptedException {
        String fp = "wacc_examples/valid/sequence/" + filename + ".wacc";
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

    private Result_of_execution exec_scope(String filename) throws IOException, InterruptedException {
        String fp = "wacc_examples/valid/scope/" + filename + ".wacc";
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
