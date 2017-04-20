package obfuscation.datautils;

import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

/**
 * Created by User on 20/04/2017.
 */
public class StringEncryptionVisitor {
    /**
     * Simple visitor implementation for visiting String Literal Expressions nodes.
     */
    private static class StringVisitor extends VoidVisitorAdapter<Void> {
        @Override
        public void visit(StringLiteralExpr s, Void arg) {

            //encryptionMethod(s.getValue());
            super.visit(s, arg);
        }
    }
}
