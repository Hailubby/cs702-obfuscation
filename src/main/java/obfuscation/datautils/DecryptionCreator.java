package obfuscation.datautils;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.stmt.BlockStmt;
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
    private String key;
    private String initVector;

    public DecryptionCreator(String key, String initVector, PackageVisitor pkgVisitor) {
        this.key = key;
        this.initVector = initVector;
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
        FieldDeclaration keyField = classType.addField("String", "key = \"" + key + "\"", Modifier.PRIVATE);
        FieldDeclaration ivField = classType.addField("String", "initVector = \"" + initVector + "\"", Modifier.PRIVATE);

        //TODO: create decryption method stub and bodies
        ClassOrInterfaceType decryptRetType = new ClassOrInterfaceType("String");
        EnumSet<Modifier> modifiers = EnumSet.of(Modifier.PUBLIC);
        MethodDeclaration decryptMethod = new MethodDeclaration(modifiers, decryptRetType, "decrypt");
        decryptMethod.addAndGetParameter(String.class, "encryptedString");

        //TODO surround in try and catch statement
        BlockStmt block = new BlockStmt();
        block.addStatement("IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(\"UTF-8\"));");
        block.addStatement("SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(\"UTF-8\"), \"AES\");");
        block.addStatement("Cipher cipher = Cipher.getInstance(\"AES/CBC/PKCS5PADDING\");");
        block.addStatement("cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, iv);");
        block.addStatement("byte[] decodedString = Base64.decode(encryptedString, Base64.DEFAULT);");
        block.addStatement("byte[] decryptedBytes = cipher.doFinal(decodedString);");
        block.addStatement("String decryptedString = new String(decryptedBytes);");
        block.addStatement("return decryptedString;");
        decryptMethod.setBody(block);

        classType.addMember(decryptMethod);

        System.out.println(cu.toString());
        return cu;
    }

    //Finds highest level package of existing classes
    private String findPkgName(){
        String pkgName = "";
        int minLength = 0;

        for(int i = 0; i < pkgList.size(); i++) {
            if (i == 0) {
                pkgName = pkgList.get(i);
                minLength = pkgName.split(".").length;
            } else {
                if( pkgList.get(i).split(".").length < minLength) {
                    pkgName = pkgList.get(i);
                    minLength = pkgName.split(".").length;
                }
            }
        }

        return pkgName;
    }
}
