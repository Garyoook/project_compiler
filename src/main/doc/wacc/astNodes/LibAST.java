package doc.wacc.astNodes;

import antlr.BasicParser;

import java.util.ArrayList;
import java.util.List;

import static antlr.BasicParser.*;

public class LibAST extends AST{
    private String importeeName;
    private String libPath;
    public List<FuncAST> functions;

    public LibAST(String libName, List<FuncAST> functions) {
        this.importeeName = libName;
        this.libPath = "wacc_standard_libraries/" + importeeName + ".wacc";
        this.functions = functions;
    }

    public String getImporteeName() {
        return importeeName;
    }

    public String getLibPath() {
        return libPath;
    }

    public List<FuncAST> getFunctions() {
        return functions;
    }

    @Override
    public String toString() {
        return "import " + importeeName + ".wacc\n" +
                "Path: " + libPath + "\n";
    }
}
