package utilities;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by User on 21/04/2017.
 */
public class CommandLineParser {
    private ArrayList<CompilationUnit>  cuList = new ArrayList<CompilationUnit>();

    public void findJavaFiles(File folder) {
        //traverse folder structure searching for java files
        for(File file : folder.listFiles()) {
            if (file.isDirectory()){
                findJavaFiles(file);
            }
            else if (file.getName().endsWith(".java")) {
                //System.out.println(file.getName());
                //System.out.println(file.getAbsolutePath());
                createCompilationUnits(file.getAbsolutePath());
            }
        }
    }

    private void createCompilationUnits(String filePath) {
        File javaFile = new File("" + filePath);
        CompilationUnit compilationUnit = null;
        try {
            compilationUnit = JavaParser.parse(javaFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        cuList.add(compilationUnit);
    }

    public ArrayList<CompilationUnit> getCuList() {
        return cuList;
    }
}
