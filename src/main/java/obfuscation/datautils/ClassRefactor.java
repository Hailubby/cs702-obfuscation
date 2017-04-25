package obfuscation.datautils;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.expr.ClassExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.ThisExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by User on 26/04/2017.
 */
public class ClassRefactor extends VoidVisitorAdapter<Void> {
    HashMap<String, String> namesMap;

    public ClassRefactor(HashMap<String, String> namesMap) {
        this.namesMap = namesMap;
    }

    @Override
    public void visit(ClassOrInterfaceType n, Void arg) {

        Iterator<Map.Entry<String, String>> entries = namesMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, String> currentEntry = entries.next();

            if (currentEntry.getKey().equals(n.getNameAsString())) {
                n.setName(currentEntry.getValue());
            }

        }

        super.visit(n,arg);
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration n, Void arg) {
        Iterator<Map.Entry<String, String>> entries = namesMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, String> currentEntry = entries.next();

            if (currentEntry.getKey().equals(n.getNameAsString())) {
                n.setName(currentEntry.getValue());
            }

        }

        super.visit(n,arg);
    }

    @Override
    public void visit(ConstructorDeclaration n, Void arg) {
        Iterator<Map.Entry<String, String>> entries = namesMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, String> currentEntry = entries.next();

            if (currentEntry.getKey().equals(n.getNameAsString())) {
                n.setName(currentEntry.getValue());
            }

        }

        super.visit(n,arg);
    }

    @Override
    public void visit(ThisExpr n, Void arg) {
        String str = n.getClassExpr().toString();
        String result = "";
        if (str.contains("[") && str.contains("]")) {
            result = str.substring(str.indexOf("[") + 1, str.indexOf("]"));
        }


        Iterator<Map.Entry<String, String>> entries = namesMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, String> currentEntry = entries.next();

            if (!result.equals("") && currentEntry.getKey().equals(result)) {
                n.setClassExpr(new NameExpr(currentEntry.getValue()));
            }

        }

        super.visit(n,arg);
    }
}
