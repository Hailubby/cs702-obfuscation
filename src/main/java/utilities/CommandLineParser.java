package utilities;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by User on 21/04/2017.
 */
public class CommandLineParser {
    private HashMap<String, CompilationUnit> cuMap = new HashMap<String, CompilationUnit>();

    //TODO chanfe param to String path, create/find file using that path
    public void findJavaFiles(File folder) {
        //traverse folder structure searching for java files
        for(File file : folder.listFiles()) {
            if (file.isDirectory()){
                findJavaFiles(file);
            }
            else if (file.getName().endsWith(".java")) {
                createCompilationUnits(file.getName(),file.getAbsolutePath());
            }
        }
    }

    //Takes file name and path as input and uses javaparser to parse them into compilation units which are then stored in a hashmap
    private void createCompilationUnits(String fileName, String filePath) {
        File javaFile = new File("" + filePath);
        CompilationUnit compilationUnit = null;
        try {
            compilationUnit = JavaParser.parse(javaFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        cuMap.put(fileName, compilationUnit);
    }

    public HashMap<String, CompilationUnit> getCuMap() {
        return cuMap;
    }
}
