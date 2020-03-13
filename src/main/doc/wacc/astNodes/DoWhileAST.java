package doc.wacc.astNodes;

import doc.wacc.utils.ErrorMessage;
import doc.wacc.utils.SymbolTable;

import static doc.wacc.utils.CompilerVisitor.currentCharPos;
import static doc.wacc.utils.CompilerVisitor.currentLine;

public class DoWhileAST extends AST{
    private AST expr;
    private AST stat;
    private SymbolTable symbolTable;


    public DoWhileAST(AST stat, AST expr, SymbolTable symbolTable) {
        this.stat = stat;
        this.expr = expr;
        this.symbolTable = symbolTable;

        if (!is_bool(expr)) {
            ErrorMessage.addSemanticError("wrong type in while condition" +
                    " at line:" + currentLine + ":" + currentCharPos);
        }
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public AST getExpr() {
        return expr;
    }

    public AST getStat() {
        return stat;
    }

    @Override
    public String toString() {
        return "do {\n\t" + stat + "} " +
                "\nwhile (" + expr + ")\n";
    }
}
