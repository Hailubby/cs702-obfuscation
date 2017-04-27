package obfuscation;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

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
        FieldDeclaration keyHalvesField = classType.addField("String[]", "keyHalves = {\"" + keyHalf1 + "\", \"" + keyHalf2 + "\"}", Modifier.PRIVATE);
        FieldDeclaration ivHalvesField = classType.addField("String[]", "ivHalves = {\"" + ivHalf1 + "\", \"" + ivHalf2 + "\"}", Modifier.PRIVATE);

        //TODO: create decryption method stub and bodies

        //Decrypt Method
        ClassOrInterfaceType stringRetType = new ClassOrInterfaceType("String");
        EnumSet<Modifier> decryptModifiers = EnumSet.of(Modifier.PUBLIC);
        MethodDeclaration decryptMethod = new MethodDeclaration(decryptModifiers, stringRetType, "decrypt");
        decryptMethod.addAndGetParameter(String.class, "encryptedString");

        BlockStmt decryptMethodBlock = new BlockStmt();

        TryStmt tryStmt = new TryStmt();
        BlockStmt tryBlock = new BlockStmt();
        tryBlock.addStatement("IvParameterSpec iv = new IvParameterSpec(getOriginal(0).getBytes(\"UTF-8\"));");
        tryBlock.addStatement("SecretKeySpec secretKeySpec = new SecretKeySpec(getOriginal(1).getBytes(\"UTF-8\"), \"AES\");");
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

        decryptMethodBlock.addStatement(tryStmt);
        decryptMethodBlock.addStatement("return null;");

        decryptMethod.setBody(decryptMethodBlock);

        classType.addMember(decryptMethod);

        //getOriginal method
        EnumSet<Modifier> getOriginalModifiers = EnumSet.of(Modifier.PRIVATE);
        MethodDeclaration getOriginalMethod = new MethodDeclaration(getOriginalModifiers, stringRetType, "getOriginal");
        getOriginalMethod.addAndGetParameter(int.class, "mode");

        BlockStmt getOrigMethodBlock = new BlockStmt();

        getOrigMethodBlock.addStatement("byte[] half1;");
        getOrigMethodBlock.addStatement("byte[] half2;");

        IfStmt ifStmt = new IfStmt();
        NameExpr ifCondition = new NameExpr("mode == 0");
        ifStmt.setCondition(ifCondition);

        BlockStmt ifBlock = new BlockStmt();
        ifBlock.addStatement("half1 = Base64.decode(ivHalves[0], 0);");
        ifBlock.addStatement("half2 = Base64.decode(ivHalves[1], 0);");

        ifStmt.setThenStmt(ifBlock);

        BlockStmt elseBlock = new BlockStmt();
        elseBlock.addStatement("half1 = Base64.decode(keyHalves[0], 0);");
        elseBlock.addStatement("half2 = Base64.decode(keyHalves[1], 0);");
        ifStmt.setElseStmt(elseBlock);

        getOrigMethodBlock.addStatement(ifStmt);

        //TODO add for loop of code
        getOriginalMethod.setBody(getOrigMethodBlock);

        classType.addMember(getOriginalMethod);

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
