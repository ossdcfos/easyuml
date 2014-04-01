package org.uml.code;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileObject;

/**
 * Provides writing generated code to .java files into project's directory.
 *
 * @author Ilija
 * @version 1.0
 */
public class FileWriter {
    
    static Project project;

    public static void writeFiles(String code, String name) {
        FileObject folder = project.getProjectDirectory();
        String path = folder.getPath();
        System.err.println("Putanja je: "+path);

        Writer writer = null;
        try{
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path+"/"+name+".java")));
            System.err.println(path+"/"+name+".java");
            writer.write(code);
        } catch (Exception ex) {
            System.err.println("Error while writing files.");
        } finally {
            try {
                writer.close();
            } catch (Exception e){
                System.err.println ("Error while closing BufferedWriter.");
            }
        }
    }
}
