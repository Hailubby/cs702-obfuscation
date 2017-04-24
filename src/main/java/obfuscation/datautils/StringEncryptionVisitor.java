package obfuscation.datautils;

import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

/**
 * Simple visitor implementation for visiting String Literal Expressions nodes and encrypts them
 */
public class StringEncryptionVisitor  extends VoidVisitorAdapter<Void>{
    private EncryptionHelper encryptionHelper = new EncryptionHelper();
    private String key = "spiritualisation";
    private String initVector = "victoriousnesses";

    @Override
    public void visit(StringLiteralExpr s, Void arg) {
        String encryptedString = encryptionHelper.encrypt(key, initVector, s.getValue());
        s.setValue(encryptedString);

        super.visit(s, arg);
    }

    public String getKey() {
        return key;
    }

    public String getInitVector() {
        return initVector;
    }
}

