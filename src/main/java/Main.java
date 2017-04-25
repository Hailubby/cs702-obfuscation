import com.github.javaparser.ast.CompilationUnit;
import obfuscation.datautils.*;
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
        Exporter exporter = new Exporter();
        PackageVisitor pkgVisitor = new PackageVisitor();
        ClassNameGenerator classNameGenerator = new ClassNameGenerator();

        stringEncryptionVisitor.setKeyAndIv();
        stringEncryptionVisitor.setHalves();

        File folder = new File(Main.class.getClass().getResource("/Original").toURI());
        cmdLineParser.findJavaFiles(folder);

        HashMap<String,CompilationUnit> cuMap = cmdLineParser.getCuMap();


        if (cuMap.size() != 0){
            Iterator<Map.Entry<String, CompilationUnit>> entries = cuMap.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, CompilationUnit> currentEntry = entries.next();
                pkgVisitor.visit(currentEntry.getValue(), null);
                stringEncryptionVisitor.visit(currentEntry.getValue(), null);
                classNameGenerator.visit(currentEntry.getValue(), null);
            }

            ClassRefactor classRefactor = new ClassRefactor(classNameGenerator.getClassNamesMap());

            entries = cuMap.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, CompilationUnit> currentEntry = entries.next();
                classRefactor.visit(currentEntry.getValue(), null);
            }

            HashMap<String, String> classNamesMap = classNameGenerator.getClassNamesMap();

            DecryptionCreator decryptionCreator = new DecryptionCreator(stringEncryptionVisitor.getKeyHalf1(), stringEncryptionVisitor.getKeyHalf2(), stringEncryptionVisitor.getIvHalf1(), stringEncryptionVisitor.getIvHalf2(), pkgVisitor);
            CompilationUnit decryptionCu = decryptionCreator.createDecryption();

            //System.out.println(decryptionCu.toString());
            cuMap.put("Decryptor.java", decryptionCu);
            exporter.exportJavaFile(cuMap, classNameGenerator.getClassNamesMap());
            exporter.exportTxtFile(classNameGenerator.getClassNamesMap());
        }
        else {
            System.out.println("No Java files located in folder to obfuscate");
        }
    }

}
