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

    private static FileWriter instance;
    private Project project;

    private FileWriter() {
        project = null;
    }

    public static FileWriter getInstance(){
        if (instance == null){
            instance = new FileWriter();
        }
        return instance;
    }
    public void writeFiles(String code, String name) {
        FileObject folder = getProject().getProjectDirectory();
        String path = folder.getPath();
        System.err.println("Putanja je: " + path);

        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path + "/" + name + ".java")));
            System.err.println(path + "/" + name + ".java");
            writer.write(code);
        } catch (Exception ex) {
            System.err.println("Error while writing files.");
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
                System.err.println("Error while closing BufferedWriter.");
            }
        }
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
