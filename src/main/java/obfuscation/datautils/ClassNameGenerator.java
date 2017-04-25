package obfuscation.datautils;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.HashMap;

/**
 * Created by User on 25/04/2017.
 */
public class ClassNameGenerator extends VoidVisitorAdapter<Void> {
    private HashMap<String, String> classNamesMap = new HashMap<String, String>();
    private char[] letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private char[] symbols = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    @Override
    public void visit(ClassOrInterfaceDeclaration n, Void arg) {
        String newName = stringGen();
        while (classNamesMap.containsValue(newName)) {
            newName = stringGen();
        }

        classNamesMap.put(n.getNameAsString(), newName);
        super.visit(n, arg);
    }


    private String stringGen(){
        char[] c = new char[16];

        for (int i = 0; i < 16; i++){
            if (i == 0) {
                int num = (int)Math.floor(Math.random() * 26);
                c[i] = letters[num];
            } else {
                int num = (int) Math.floor(Math.random() * 62);
                c[i] = symbols[num];
            }
        }

        return new String(c);
    }

    public HashMap<String, String> getClassNamesMap() {
        return this.classNamesMap;
    }
}
