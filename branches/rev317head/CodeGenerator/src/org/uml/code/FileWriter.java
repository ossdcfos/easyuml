package org.uml.code;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;
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

    public static FileWriter getInstance() {
        if (instance == null) {
            instance = new FileWriter();
        }
        return instance;
    }

    public void writeFiles(String code, String name, String pack) {
        FileObject folder = getProject().getProjectDirectory();
        String path = folder.getPath();

        Writer writer = null;
        File folderPath = new File(path + "/src/");
        createDirectory(folderPath);

        String longestPath = path + "/src/";

        if (pack != null) {
            if (pack.contains(".")) {

                String[] folders = pack.split("\\.");
                for (String s : folders) {

                    String newPath = longestPath + s;
                    File f = new File(newPath);
                    createDirectory(f);
                    longestPath += s + "/";
                }
            } else {
                if (!pack.equals("")) {
                    longestPath += pack;
                    File f = new File(longestPath);
                    createDirectory(f);
                    longestPath += "/";
                }
            }
        }
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(longestPath + name + ".java")));
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

    public void createDirectory(File file) {
        if (!file.exists()) {
            file.mkdir();
        }
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
