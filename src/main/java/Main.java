import com.github.javaparser.ast.CompilationUnit;
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


        File folder = new File("C:\\Users\\User\\Desktop\\temp");
        cmdLineParser.findJavaFiles(folder);

        HashMap<String,CompilationUnit> cuMap = cmdLineParser.getCuMap();

        if (cuMap.size() != 0){
            Iterator<Map.Entry<String, CompilationUnit>> entries = cuMap.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, CompilationUnit> currentEntry = entries.next();
                stringEncryptionVisitor.visit(currentEntry.getValue(), null);
                System.out.println(currentEntry.getValue().toString());
            }

            javaExporter.exportFile(cuMap);
        }
        else {
            System.out.println("No Java files located in folder to obfuscate");
        }
    }

}
