/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.reveng.classProcessing;

import componentOriginating.RelationshipResolver;
import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import org.openide.util.Utilities;
import org.uml.reveng.GeneratedDiagramManager;

/**
 * Finding and creating Implements and Extends relation components is done by
 * this class. It is much simpler than doing it during the compilation process.
 *
 * @author Milan
 */
public class ClassProcessing {

    /**
     * Finds, processes and creates Implements and/or Extends relation components. 
     * 
     * @param allClassesPaths List of absolute paths to generated .class files
     * @param separator default file system separator
     * @param tempClassFolder folder where .class files are stored
     */
    public static void ClassProcessor(List<String> allClassesPaths, String separator, String tempClassFolder) {
        for (String classFilePath : allClassesPaths) {
            String[] separated;
            System.out.println(classFilePath);
            separated = classFilePath.toString().split(separator);
            String classFile = separated[separated.length - 1];
            //String foundFileName = javaFile.split("\\.")[0];
            String foundFileName = classFile.toString().split("\\.")[0];
            System.out.println(foundFileName);
            File file = new File(classFilePath.split(tempClassFolder)[0] + tempClassFolder + separator);
            System.out.println(classFilePath.split(foundFileName)[0]);
            try {
                // Convert File to a URL
                URL url = Utilities.toURI(file).toURL(); // file:/c:/myclasses/
                URL[] urls = new URL[]{url};
                // Create a new class loader with the directory
                ClassLoader cl = new URLClassLoader(urls);
                String classPath = "";
                String regexSeparator = "/";
                if (separator.equals("\\\\")) {
                    regexSeparator = separator;
                }
                String regexString = ".*" + regexSeparator + "src" + regexSeparator + ".*";
                if (classFilePath.matches(regexString)) {
                    classPath = classFilePath.split("src" + separator)[1].split(foundFileName)[0].replace(File.separator, ".");
                } else {
                    classPath = classFilePath.split(tempClassFolder + separator)[1].split(foundFileName)[0].replace(File.separator, ".");
                }
                String nameWithPackage = classPath + foundFileName;
                Class<?> cls = cl.loadClass(nameWithPackage);
                System.out.println("cl.loadClass() = " + cl.loadClass(classPath + foundFileName));
                Field[] fields = cls.getDeclaredFields();
                //Implements - IMPLEMENTS
                Class<?>[] interfaces = cls.getInterfaces();
                for (Class<?> clasI : interfaces) {
                    try {
                        Class<?> cla = Class.forName(clasI.getName());
                    } catch (ClassNotFoundException ex) {
                        RelationshipResolver.putIntoRelHashMap(GeneratedDiagramManager.getDefault().getImplementsRelationships(), nameWithPackage, clasI.getName(), "", "");
                    }
                }
                //Extends - IS
                Class<?> superclass = cls.getSuperclass();
                if (superclass != null) {
                    try {
                        Class<?> cla = Class.forName(superclass.getName());
                    } catch (ClassNotFoundException ex) {
                        RelationshipResolver.putIntoRelHashMap(GeneratedDiagramManager.getDefault().getIsRelationships(), nameWithPackage, superclass.getName(), "", "<<extends>>");
                    }
                }
                System.out.println(cls);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        RelationshipResolver.resolveRelationsIsAndImplements();
    }
}
