package obfuscation;

import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

/**
 * Simple visitor implementation for visiting String Literal Expressions nodes and encrypts them
 */
public class StringEncryptionVisitor  extends VoidVisitorAdapter<Void>{
    private EncryptionHelper encryptionHelper = new EncryptionHelper();
    private String key = "";
    private String keyHalf1 = "";
    private String keyHalf2 = "";
    private String initVector = "";
    private String ivHalf1 = "";
    private String ivHalf2 = "";

    //Visits all strings in the compilation unit and calls encryption method on it
    @Override
    public void visit(StringLiteralExpr s, Void arg) {
        String encryptedString = encryptionHelper.encrypt(key, initVector, s.getValue());
        s.setValue(encryptedString);

        super.visit(s, arg);
    }


    //Set random key and initialisation vector to be used in encryption
    public void setKeyAndIv(){
        this.key = encryptionHelper.generateString(16);
        this.initVector = encryptionHelper.generateString(16);
    }

    //Creates two xor halves each for key and iv
    public void setHalves(){
        String[] keyHalves = encryptionHelper.generateKeyHalves(this.key);
        this.keyHalf1 = keyHalves[0];
        this.keyHalf2 = keyHalves[1];

        String[] ivHalves = encryptionHelper.generateKeyHalves(this.initVector);
        this.ivHalf1 = ivHalves[0];
        this.ivHalf2 = ivHalves[1];
    }

    //getters of key and iv halves
    public String getKeyHalf1() {
        return keyHalf1;
    }

    public String getKeyHalf2() {
        return keyHalf2;
    }

    public String getIvHalf1() {
        return ivHalf1;
    }

    public String getIvHalf2() {
        return ivHalf2;
    }
}

