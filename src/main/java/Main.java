import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import obfuscation.datautils.EncryptionHelper;
import obfuscation.datautils.StringEncryptionVisitor;
import org.apache.commons.codec.binary.Base64;
import utilities.CommandLineParser;
import utilities.JavaExporter;

import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by User on 18/04/2017.
 */
public class Main {
    private static JavaExporter javaExporter = new JavaExporter();

    public static void main(String[] args) throws FileNotFoundException, URISyntaxException {
        CommandLineParser cmdLineParser = new CommandLineParser();
//        File sourceFile = new File(Main.class.getClass().getResource("/AppJavaSrc/com/jjhhh/dice/CustomDiceActivity.java").toURI());
//        String fileName = "CustomDiceActivity.java";
//
//        //parse the file
//        CompilationUnit compilationUnit = JavaParser.parse(sourceFile);
//
//        // prints the resulting compilation unit to default system output
//        //System.out.println(compilationUnit.toString());
//
//        // visit and print the methods names
//        new StringEncryptionVisitor().visit(compilationUnit, null);
//        System.out.println(compilationUnit.toString());
//
//        javaExporter.exportFile(fileName, compilationUnit.toString());

//
//        String string = "hello everyone";
//        byte[] byteString = string.getBytes();
//        byte[] encodedBytes = Base64.encodeBase64(byteString);
//
//        String encodedString = new String(encodedBytes);
//        System.out.println("Encoded dtring is: " + Arrays.toString(encodedBytes));
//        System.out.println("Encoded dtring is: " + encodedString);
//        EncryptionHelper encryptionHelper = new EncryptionHelper();
//        String key = "HailunHenryJohns";
//        String iv = "1234567890qwerty";
//        String encryptedString = encryptionHelper.encrypt(key, iv, string);
//
//        try {
//            encryptionHelper.decrypt(key, iv, encryptedString);
//        } catch (NoSuchPaddingException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }

        File folder = new File("C:\\Users\\User\\Desktop\\temp");
        cmdLineParser.findJavaFiles(folder);

        ArrayList<CompilationUnit> cuList = cmdLineParser.getCuList();

        if (cuList.size() != 0){
            for (CompilationUnit cu : cuList) {
                System.out.println(cu.toString());
            }
        }
        else {
            System.out.println("No Java files located in folder to obfuscate");
        }
    }

}
