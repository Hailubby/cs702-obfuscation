import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;

/**
 * Created by User on 18/04/2017.
 */
public class Main {
    public static void main(String[] args) throws FileNotFoundException, URISyntaxException {
        //System.out.println("Hello frands");

        File sourceFile = new File(Main.class.getClass().getResource("/CustomDiceActivity.java").toURI());

        //parse the file
        CompilationUnit compilationUnit = JavaParser.parse(sourceFile);

        // prints the resulting compilation unit to default system output
        //System.out.println(compilationUnit.toString());

        // visit and print the methods names
        new MethodVisitor().visit(compilationUnit, null);
    }
    /**
     * Simple visitor implementation for visiting MethodDeclaration nodes.
     */
    private static class MethodVisitor extends VoidVisitorAdapter<Void> {
        @Override
        public void visit(MethodDeclaration n, Void arg) {
            /* here you can access the attributes of the method.
             this method will be called for all methods in this
             CompilationUnit, including inner class methods */
            System.out.println(n.getName());
            super.visit(n, arg);
        }
    }
}
