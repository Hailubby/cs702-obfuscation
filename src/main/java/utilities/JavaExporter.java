package utilities;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Created by User on 20/04/2017.
 */
public class JavaExporter {

    public void exportFile(String fileName, String s) {
        PrintWriter out = null;
        try {
            out = new PrintWriter(fileName);
            out.println(s);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            out.close();
        }
    }
}
