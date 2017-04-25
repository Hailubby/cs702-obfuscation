package obfuscation.datautils;

import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

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

    public void setKeyAndIv(){
        this.key = encryptionHelper.generateString(16);
        this.initVector = encryptionHelper.generateString(16);
        System.out.println("Generated key: " + this.key);
        System.out.println("Generated iv: " + this.initVector);
    }

    public String getKey() {
        return key;
    }

    public String getInitVector() {
        return initVector;
    }
}

