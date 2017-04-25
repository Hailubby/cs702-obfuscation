package obfuscation.datautils;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.sun.javafx.fxml.expression.VariableExpression;

import java.util.ArrayList;
import java.util.EnumSet;

/**
 * Created by User on 24/04/2017.
 */
public class DecryptionCreator {
    private ArrayList<String> pkgList;
    private String keyHalf1;
    private String keyHalf2;
    private String ivHalf1;
    private String ivHalf2;

    public DecryptionCreator(String keyHalf1, String keyHalf2, String ivHalf1, String ivHalf2, PackageVisitor pkgVisitor) {
        this.keyHalf1 = keyHalf1;
        this.keyHalf2 = keyHalf2;
        this.ivHalf1 = ivHalf1;
        this.ivHalf2 = ivHalf2;
        this.pkgList = pkgVisitor.getPkgNames();
    }


    public CompilationUnit createDecryption() {
        CompilationUnit cu = new CompilationUnit();
        String pkgName = findPkgName();

        //Set package of class
        cu.setPackageDeclaration(pkgName);

        //Imports needed by class
        cu.addImport("android.util.Base64;");
        cu.addImport("javax.crypto.Cipher;");
        cu.addImport("javax.crypto.spec.IvParameterSpec");
        cu.addImport("javax.crypto.spec.SecretKeySpec");

        //Sets class name
        ClassOrInterfaceDeclaration classType = cu.addClass("Decryptor");

        //Adds key and init vector fields
        FieldDeclaration keyHalf1Field = classType.addField("String", "keyHalf1 = \"" + keyHalf1 + "\"", Modifier.PRIVATE);
        FieldDeclaration keyHalf2Field = classType.addField("String", "keyHalf2 = \"" + keyHalf2 + "\"", Modifier.PRIVATE);
        FieldDeclaration ivHalf1Field = classType.addField("String", "ivHalf1 = \"" + ivHalf1 + "\"", Modifier.PRIVATE);
        FieldDeclaration ivHalf2Field = classType.addField("String", "ivHalf2 = \"" + ivHalf2 + "\"", Modifier.PRIVATE);

        //TODO: create decryption method stub and bodies
        ClassOrInterfaceType decryptRetType = new ClassOrInterfaceType("String");
        EnumSet<Modifier> modifiers = EnumSet.of(Modifier.PUBLIC);
        MethodDeclaration decryptMethod = new MethodDeclaration(modifiers, decryptRetType, "decrypt");
        decryptMethod.addAndGetParameter(String.class, "encryptedString");

        //TODO surround in try and catch statement
        BlockStmt methodBlock = new BlockStmt();

        TryStmt tryStmt = new TryStmt();
        BlockStmt tryBlock = new BlockStmt();
        tryBlock.addStatement("IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(\"UTF-8\"));");
        tryBlock.addStatement("SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(\"UTF-8\"), \"AES\");");
        tryBlock.addStatement("Cipher cipher = Cipher.getInstance(\"AES/CBC/PKCS5PADDING\");");
        tryBlock.addStatement("cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, iv);");
        tryBlock.addStatement("byte[] decodedString = Base64.decode(encryptedString, Base64.DEFAULT);");
        tryBlock.addStatement("byte[] decryptedBytes = cipher.doFinal(decodedString);");
        tryBlock.addStatement("String decryptedString = new String(decryptedBytes);");
        tryBlock.addStatement("return decryptedString;");

        tryStmt.setTryBlock(tryBlock);

        BlockStmt catchBlock1 = new BlockStmt();

        ClassOrInterfaceType exceptionType = new ClassOrInterfaceType("Exception");
        Parameter parameter = new Parameter(exceptionType, "e");
        CatchClause catchClause = new CatchClause(parameter, catchBlock1);
        BlockStmt catchBlock2 = new BlockStmt();
        catchBlock2.addStatement("e.printStackTrace();");
        catchClause.setBody(catchBlock2);

        NodeList catchList = new NodeList();
        catchList.add(catchClause);
        tryStmt.setCatchClauses(catchList);

        //TODO return null statement - don't forget to add

        methodBlock.addStatement(tryStmt);

        decryptMethod.setBody(methodBlock);

        classType.addMember(decryptMethod);

        return cu;
    }

    //Finds highest level package of existing classes
    private String findPkgName(){
        String pkgName = "";
        int minLength = 0;

        for(int i = 0; i < pkgList.size(); i++) {
            if (i == 0) {
                pkgName = pkgList.get(i);
                minLength = pkgName.split("\\.").length;
            } else {
                if( pkgList.get(i).split("\\.").length < minLength) {
                    pkgName = pkgList.get(i);
                    minLength = pkgName.split("\\.").length;
                }
            }
        }

        return pkgName;
    }
}
