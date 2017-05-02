package obfuscation;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.*;
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


    //Generates Decryptor compilation unit with xor-ed key and iv halfs used in the encryption
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

        //Adds xor key and xor init vector fields
        classType.addField("String[]", "keyHalves = {\"" + keyHalf1 + "\", \"" + keyHalf2 + "\"}", Modifier.PRIVATE);
        classType.addField("String[]", "ivHalves = {\"" + ivHalf1 + "\", \"" + ivHalf2 + "\"}", Modifier.PRIVATE);

        //TODO: create decryption method stub and bodies

        //Decrypt Method
        ClassOrInterfaceType stringRetType = new ClassOrInterfaceType("String");
        EnumSet<Modifier> decryptModifiers = EnumSet.of(Modifier.PUBLIC);
        MethodDeclaration decryptMethod = new MethodDeclaration(decryptModifiers, stringRetType, "decrypt");
        decryptMethod.addAndGetParameter(String.class, "encryptedString");

        BlockStmt decryptMethodBlock = new BlockStmt();

        //Try block
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

        //Catch block
        BlockStmt catchBlock1 = new BlockStmt();
        //What to catch
        ClassOrInterfaceType exceptionType = new ClassOrInterfaceType("Exception");
        Parameter parameter = new Parameter(exceptionType, "e");
        CatchClause catchClause = new CatchClause(parameter, catchBlock1);
        //How to handle the exception
        BlockStmt catchBlock2 = new BlockStmt();
        catchBlock2.addStatement("e.printStackTrace();");
        catchClause.setBody(catchBlock2);

        //Add the catch clause to the try statement
        NodeList catchList = new NodeList();
        catchList.add(catchClause);
        tryStmt.setCatchClauses(catchList);

        //Add try block to the decrypt method block
        decryptMethodBlock.addStatement(tryStmt);
        decryptMethodBlock.addStatement("return null;");

        //Add the decrypt method block to the actual method node
        decryptMethod.setBody(decryptMethodBlock);

        //Add decrypt method to the actual class
        classType.addMember(decryptMethod);

        //getOriginal method
        EnumSet<Modifier> getOriginalModifiers = EnumSet.of(Modifier.PRIVATE);
        MethodDeclaration getOriginalMethod = new MethodDeclaration(getOriginalModifiers, stringRetType, "getOriginal");
        //Set parameters to the method
        getOriginalMethod.addAndGetParameter(int.class, "mode");

        BlockStmt getOrigMethodBlock = new BlockStmt();

        getOrigMethodBlock.addStatement("byte[] half1;");
        getOrigMethodBlock.addStatement("byte[] half2;");

        //If statement node
        IfStmt ifStmt = new IfStmt();
        NameExpr ifCondition = new NameExpr("mode == 0");
        ifStmt.setCondition(ifCondition);

        //If statement block body
        BlockStmt ifBlock = new BlockStmt();
        ifBlock.addStatement("half1 = Base64.decode(ivHalves[0], 0);");
        ifBlock.addStatement("half2 = Base64.decode(ivHalves[1], 0);");

        //Add the block to the if statement
        ifStmt.setThenStmt(ifBlock);

        //Else block and adds the else block to the if statement
        BlockStmt elseBlock = new BlockStmt();
        elseBlock.addStatement("half1 = Base64.decode(keyHalves[0], 0);");
        elseBlock.addStatement("half2 = Base64.decode(keyHalves[1], 0);");
        ifStmt.setElseStmt(elseBlock);

        //Add fi statement block (including else) to the mthod body
        getOrigMethodBlock.addStatement(ifStmt);

        getOrigMethodBlock.addStatement("byte[] key = new byte[half1.length];");

        //For loop block, xor.
        ForStmt forStmt = new ForStmt();

        NodeList<Expression> initialisation = new NodeList<>();
        //loop counter initialisation
        NameExpr initExpr = new NameExpr("int i = 0");
        initialisation.add(initExpr);
        forStmt.setInitialization(initialisation);

        //loop condition initialisation
        NameExpr compareExpr = new NameExpr("i < half1.length");
        forStmt.setCompare(compareExpr);

        //loop incrementer initialisation
        NodeList<Expression> update = new NodeList<>();
        NameExpr updateExpr = new NameExpr("i++");
        update.add(updateExpr);
        forStmt.setUpdate(update);

        //for loop body
        BlockStmt forBlock = new BlockStmt();
        forBlock.addStatement("key[i] = (byte) (half1[i] ^ half2[i]);");
        forStmt.setBody(forBlock);

        getOrigMethodBlock.addStatement(forStmt);

        getOrigMethodBlock.addStatement("return new String(key);");

        getOriginalMethod.setBody(getOrigMethodBlock);

        classType.addMember(getOriginalMethod);

        //return decryptor compilation unit
        return cu;
    }

    //Finds highest level package of existing classes from arraylist of package names
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
