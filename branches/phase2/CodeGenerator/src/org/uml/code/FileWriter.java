package org.uml.code;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import org.netbeans.api.project.Project;
import org.netbeans.modules.editor.indent.api.Reformat;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.text.NbDocument;

/**
 * Provides writing generated code to .java files into project's directory.
 *
 * @author Ilija
 * @version 1.0
 * @see
 * https://blogs.oracle.com/geertjan/entry/format_files_while_starting_netbeans
 * @see
 * http://bits.netbeans.org/dev/javadoc/org-netbeans-modules-editor-indent/overview-summary.html
 */
public class FileWriter {

    private static FileWriter instance;
    private Project project;

    /**
     * A constructor without parameters - only sets project variable to null.
     */
    private FileWriter() {
        project = null;
    }

    /**
     * Used to instantiate and get this singleton's instance. Basic singleton
     * implementation method.
     *
     * @return FileWriter singleton instance
     */
    public static FileWriter getInstance() {
        if (instance == null) {
            instance = new FileWriter();
        }
        return instance;
    }

    /**
     * Creates folder (package) structure and writes .java code into it.
     *
     * @param code to be written
     * @param name of .java file
     * @param pack - package where it should be written
     */
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

//this one was used: https://blogs.oracle.com/geertjan/entry/format_files_while_starting_netbeans1 
        File oneFileToBeFormatted = new File(longestPath + name + ".java");
        try {
            FileObject fo = FileUtil.toFileObject(oneFileToBeFormatted);
            DataObject dobj = DataObject.find(fo);
            EditorCookie ec = dobj.getCookie(EditorCookie.class);
            final StyledDocument doc = ec.openDocument();
            final Reformat rf = Reformat.get(doc);
            rf.lock();
            try {
                NbDocument.runAtomicAsUser(doc, new Runnable() {
                    @Override
                    public void run() {
                        try {
                            rf.reformat(0, doc.getLength());
                        } catch (BadLocationException ex) {
                        }
                    }
                });
            } finally {
                rf.unlock();
            }
            ec.saveDocument();
        } catch (BadLocationException ex) {
        } catch (IOException ex) {
        }

    }

    /**
     * If the file does not exist, it is created.
     */
    public void createDirectory(File file) {
        if (!file.exists()) {
            file.mkdir();
        }
    }
    
    /**
     * Returns project currently worked on by FileWriter instance.
     * @return project that is currently set
     */
    public Project getProject() {
        return project;
    }

    /**
     * Sets project for FileWriter instance to work on.
     * @param project to be used for code generating
     */
    public void setProject(Project project) {
        this.project = project;
    }
}
