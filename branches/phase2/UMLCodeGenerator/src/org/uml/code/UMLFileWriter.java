package org.uml.code;

import de.hunsicker.jalopy.Jalopy;
import java.io.File;
import static java.io.File.separator;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.netbeans.api.project.Project;
import org.openide.util.Exceptions;

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
public class UMLFileWriter {

    private static UMLFileWriter instance;
    private Project project;

    /**
     * A constructor without parameters - only sets project variable to null.
     */
    private UMLFileWriter() {
        project = null;
    }

    /**
     * Used to instantiate and get this singleton's instance. Basic singleton
     * implementation method.
     *
     * @return FileWriter singleton instance
     */
    public static UMLFileWriter getInstance() {
        if (instance == null) {
            instance = new UMLFileWriter();
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
    public void writeFile(String name, String pack, String code) {
        String folderBasePath = "C:" + separator + "tempProj" + separator;
        String fullPath = folderBasePath + "src" + separator;
        if (!pack.equals("")) {
            String packagePath = pack.replace(".", separator);
            fullPath += packagePath + separator;
        }
        createDirectory(new File(fullPath));
        try {
            File sourceFile = new File(fullPath + name + ".java");
            FileUtils.writeStringToFile(sourceFile, code);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
            System.err.println("Error while writing file " + name + ".java");
        }

        File sourceFile = new File(fullPath + name + ".java");

        try {
            Jalopy jlp = new Jalopy();
            jlp.setInput(sourceFile);
            jlp.setOutput(sourceFile);
            jlp.format();
        } catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        }

        //this one was used: https://blogs.oracle.com/geertjan/entry/format_files_while_starting_netbeans1 
//        try {
//            FileObject fileObject = FileUtil.toFileObject(sourceFile);
//            DataObject dobj = DataObject.find(fileObject);
//            EditorCookie ec = dobj.getLookup().lookup(EditorCookie.class);
//
//            final StyledDocument doc = ec.openDocument();
//            final Reformat rf = Reformat.get(doc);
//            rf.lock();
//            try {
//                NbDocument.runAtomicAsUser(doc, new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            rf.reformat(0, doc.getLength());
//                        } catch (BadLocationException ex) {
//                            Exceptions.printStackTrace(ex);
//                        }
//                    }
//                });
//            } finally {
//                rf.unlock();
//            }
//
//            ec.saveDocument();
//        } catch (BadLocationException | IOException ex) {
//            Exceptions.printStackTrace(ex);
//        }
    }

    /**
     * If the file does not exist, it is created.
     *
     * @param file
     */
    public void createDirectory(File file) {
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * Returns project currently worked on by FileWriter instance.
     *
     * @return project that is currently set
     */
    public Project getProject() {
        return project;
    }

    /**
     * Sets project for FileWriter instance to work on.
     *
     * @param project to be used for code generating
     */
    public void setProject(Project project) {
        this.project = project;
    }
}
