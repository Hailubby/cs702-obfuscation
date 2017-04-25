import com.github.javaparser.ast.CompilationUnit;
import obfuscation.datautils.DecryptionCreator;
import obfuscation.datautils.PackageVisitor;
import obfuscation.datautils.StringEncryptionVisitor;
import utilities.CommandLineParser;
import utilities.JavaExporter;
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
        JavaExporter javaExporter = new JavaExporter();
        PackageVisitor pkgVisitor = new PackageVisitor();

        stringEncryptionVisitor.setKeyAndIv();

        File folder = new File(Main.class.getClass().getResource("/Original").toURI());
        cmdLineParser.findJavaFiles(folder);

        HashMap<String,CompilationUnit> cuMap = cmdLineParser.getCuMap();

        if (cuMap.size() != 0){
            Iterator<Map.Entry<String, CompilationUnit>> entries = cuMap.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, CompilationUnit> currentEntry = entries.next();
                pkgVisitor.visit(currentEntry.getValue(), null);
                stringEncryptionVisitor.visit(currentEntry.getValue(), null);
            }

            DecryptionCreator decryptionCreator = new DecryptionCreator(stringEncryptionVisitor.getKey(), stringEncryptionVisitor.getInitVector(), pkgVisitor);
            CompilationUnit decryptionCu = decryptionCreator.createDecryption();
            cuMap.put("Decryptor.java", decryptionCu);
            javaExporter.exportFile(cuMap);
        }
        else {
            System.out.println("No Java files located in folder to obfuscate");
        }
    }

}
