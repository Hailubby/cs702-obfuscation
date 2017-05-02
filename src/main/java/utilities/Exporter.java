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

    //Prints each compilation unit to files with their respective java file names
    public void exportJavaFile(HashMap<String, CompilationUnit> cuMap) {
        PrintWriter out = null;

        Iterator<Map.Entry<String, CompilationUnit>> entries = cuMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, CompilationUnit> currentEntry = entries.next();
            try {
                //File name = key of hashmap
                out = new PrintWriter(currentEntry.getKey());
                //Prints corresponding compilation unit to the file
                out.println(currentEntry.getValue().toString());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                out.close();
            }
        }
    }

}
