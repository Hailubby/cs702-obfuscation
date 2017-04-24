package obfuscation.datautils;

import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;

/**
 * Created by User on 24/04/2017.
 */
public class PackageVisitor  extends VoidVisitorAdapter<Void> {
    ArrayList<String> pkgNames = new ArrayList<String>();

    @Override
    public void visit(PackageDeclaration n, Void arg) {
        //System.out.println(n.getName());
        pkgNames.add(n.getNameAsString());
        super.visit(n, arg);
    }

    public ArrayList<String> getPkgNames() {
        return pkgNames;
    }
}
