import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;

/**
 * Created by User on 18/04/2017.
 */
public class Main {
    public static void main(String[] args) throws FileNotFoundException, URISyntaxException {
        File sourceFile = new File(Main.class.getClass().getResource("/AppJavaSrc/com/jjhhh/dice/CustomDiceActivity.java").toURI());

        //parse the file
        CompilationUnit compilationUnit = JavaParser.parse(sourceFile);

        // prints the resulting compilation unit to default system output
        //System.out.println(compilationUnit.toString());

        // visit and print the methods names
        new StringVisitor().visit(compilationUnit, null);
    }

    /**
     * Simple visitor implementation for visiting MethodDeclaration nodes.
     */
    private static class StringVisitor extends VoidVisitorAdapter<Void> {
        @Override
        public void visit(StringLiteralExpr s, Void arg) {
            /* here you can access the attributes of the method.
             this method will be called for all methods in this
             CompilationUnit, including inner class methods */
            System.out.println(s.getValue());
            if (s.getValue().equals("d")){
                s.setValue("ENCRYPTED D");
                System.out.println(s.getValue());
            }
            super.visit(s, arg);
        }
    }
}
