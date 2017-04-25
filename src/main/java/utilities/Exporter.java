package utilities;

import com.github.javaparser.ast.CompilationUnit;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by User on 20/04/2017.
 */
public class Exporter {

    public void exportJavaFile(HashMap<String, CompilationUnit> cuMap) {
        PrintWriter out = null;

        Iterator<Map.Entry<String, CompilationUnit>> entries = cuMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, CompilationUnit> currentEntry = entries.next();
            try {
                out = new PrintWriter(currentEntry.getKey());
                out.println(currentEntry.getValue().toString());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                out.close();
            }
        }
    }

    public void exportTxtFile(HashMap<String, String> classNamesMap) {
        PrintWriter out = null;
        try {
            out = new PrintWriter("GeneratedClassNames.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Iterator<Map.Entry<String, String>> entries = classNamesMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, String> currentEntry = entries.next();
            out.println(currentEntry.getKey() + " = " + currentEntry.getValue().toString());
        }

        out.close();
    }
}
