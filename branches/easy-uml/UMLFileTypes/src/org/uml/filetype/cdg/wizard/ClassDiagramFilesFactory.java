package org.uml.filetype.cdg.wizard;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import javax.swing.JOptionPane;
import org.netbeans.api.project.Project;
import org.openide.util.Exceptions;
import org.uml.model.ClassDiagram;

/**
 *
 * @author Jelena
 */
public class ClassDiagramFilesFactory {

    Project currentProject;
    ClassDiagram classDiagram;
    String classDiagramName; // we should use/have some network ID instead name, since name may not be unigue
    // we need it to select that node in project tree upon network creation
    String createdFilePath;
    
    static final String FOLDER_NAME = "Class Diagrams";

    private static ClassDiagramFilesFactory instance;

    private ClassDiagramFilesFactory() {
    }

    public static ClassDiagramFilesFactory getDefault() {
        if (instance == null) {
            instance = new ClassDiagramFilesFactory();
        }
        return instance;
    }

    public void createClassDiagramFile(ClassDiagram cd) {
        try {
            this.classDiagram = cd;
            this.classDiagramName = cd.getName();
//            Lookup genlokup = Utilities.actionsGlobalContext();
//            currentProject = genlokup.lookup(UMLProject.class);
            //currentProject = CurrentProject.getInstance().getCurrentProject();                     
            String folderPath = currentProject.getProjectDirectory().getPath() + "/" + FOLDER_NAME + "/";
            File folder = new File(folderPath);
            folder.mkdirs();
            String filePath = folderPath + classDiagramName + ".cdg";
            File file = new File(filePath);
            ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(file));
            try {
                createdFilePath = null;
                stream.writeObject(this.classDiagram);
                createdFilePath = filePath;
                currentProject.getProjectDirectory().refresh();
            } finally {
                stream.close();
            }
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
            JOptionPane.showMessageDialog(null, "ClassDiagramProjectFilesFactory exception! " + ex.getMessage());
        }
    }

    public String getCreatedFilePath() {
        return createdFilePath;
    }

    void setProject(Project project) {
        currentProject = project;
    }
}
