package obfuscation.datautils;

import com.github.javaparser.ast.CompilationUnit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by User on 24/04/2017.
 */
public class DecryptionCreator {
    private ArrayList<String> pkgList;
    private String key;
    private String initVector;

    public DecryptionCreator(String key, String initVector, PackageVisitor pkgVisitor) {
        this.key = key;
        this.initVector = initVector;
        this.pkgList = pkgVisitor.getPkgNames();
    }


    public CompilationUnit createDecryption() {
        CompilationUnit cu = new CompilationUnit();
        String pkgName = findPkgName();

        // TODO: set package name, initialise fileds with key and IV
        cu.setPackageDeclaration(pkgName);
        //TODO: create decryption method stub and bodies

        System.out.println(cu.toString());
        return cu;
    }

    //Finds highest level package of existing classes
    private String findPkgName(){
        String pkgName = "";
        int minLength = 0;

        for(int i = 0; i < pkgList.size(); i++) {
            if (i == 0) {
                pkgName = pkgList.get(i);
                minLength = pkgName.split(".").length;
            } else {
                if( pkgList.get(i).split(".").length < minLength) {
                    pkgName = pkgList.get(i);
                    minLength = pkgName.split(".").length;
                }
            }
        }

        return pkgName;
    }
}
