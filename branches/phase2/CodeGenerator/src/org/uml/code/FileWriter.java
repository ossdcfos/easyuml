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
