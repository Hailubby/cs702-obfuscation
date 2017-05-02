import com.github.javaparser.ast.CompilationUnit;
import obfuscation.DecryptionCreator;
import obfuscation.PackageVisitor;
import obfuscation.StringEncryptionVisitor;
import utilities.CommandLineParser;
import utilities.Exporter;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Created by User on 18/04/2017.
 */
public class Main {

    public static void main(String[] args) throws FileNotFoundException, URISyntaxException {
        CommandLineParser cmdLineParser = new CommandLineParser();
        StringEncryptionVisitor stringEncryptionVisitor = new StringEncryptionVisitor();
        PackageVisitor pkgVisitor = new PackageVisitor();
        Exporter exporter = new Exporter();

        //Set up Key and initialisation vectors to be used for encryption
        stringEncryptionVisitor.setKeyAndIv();
        stringEncryptionVisitor.setHalves();

        //Gets folder "Original" from the resources folder
        File folder = new File(Main.class.getClass().getResource("/Original").toURI());
        //Parse folder into compilation units
        cmdLineParser.findJavaFiles(folder);

        //Retieve hashmap of compilation units
        HashMap<String,CompilationUnit> cuMap = cmdLineParser.getCuMap();

        //For each compilation unit, visit all the strings in them and encrypt them
        if (cuMap.size() != 0){
            Iterator<Map.Entry<String, CompilationUnit>> entries = cuMap.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, CompilationUnit> currentEntry = entries.next();
                pkgVisitor.visit(currentEntry.getValue(), null);
                stringEncryptionVisitor.visit(currentEntry.getValue(), null);
            }

            //Input key and iv halves and package visitor to the decryption creator to create the Decryptor compilation unit
            DecryptionCreator decryptionCreator = new DecryptionCreator(stringEncryptionVisitor.getKeyHalf1(), stringEncryptionVisitor.getKeyHalf2(), stringEncryptionVisitor.getIvHalf1(), stringEncryptionVisitor.getIvHalf2(), pkgVisitor);
            CompilationUnit decryptionCu = decryptionCreator.createDecryption();

            //Add decryptor compilation unit to hashmap of compilation units
            cuMap.put("Decryptor.java", decryptionCu);
            //export all compilation units to java files
            exporter.exportJavaFile(cuMap);

        }
        else {
            System.out.println("No Java files located in folder to obfuscate");
        }
    }

}
