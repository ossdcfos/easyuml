package org.uml.filetype.cdg.wizard;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import javax.swing.JOptionPane;
import org.openide.util.Exceptions;
import org.uml.model.ClassDiagram;
import org.uml.project.UMLProject;

/**
 *
 * @author Jelena
 */
public class ClassDiagramProjectFilesFactory {
    UMLProject currentProject;
    ClassDiagram classDiagram;
    String classDiagramName; // we should use/have some network ID instead name, since name may not be unigue
    // we need it to select that node in project tree upon network creation
    String createdFilePath;

    private static ClassDiagramProjectFilesFactory instance;
    
    private ClassDiagramProjectFilesFactory() {
    }
    
    public static ClassDiagramProjectFilesFactory getDefault() {
        if (instance == null) {
            instance = new ClassDiagramProjectFilesFactory();
        }
        return instance;
    }    
    
    public void createClassDiagramFile(ClassDiagram cd) {
        try {
//            Lookup genlokup = Utilities.actionsGlobalContext();
//            currentProject = genlokup.lookup(UMLProject.class);
            //currentProject = CurrentProject.getInstance().getCurrentProject();                     
            String path = currentProject.getProjectDirectory().getPath();
            path += "/" + UMLProject.CLASS_DIAGRAMS_DIR + "/";
            this.classDiagram = cd;
            classDiagramName = this.classDiagram.getName();
            String fullFilePath = path + classDiagramName + ".cdg";
            ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(fullFilePath));
            try {
                createdFilePath = null;
                stream.writeObject(this.classDiagram);   
                createdFilePath = fullFilePath;
                currentProject.addNotify();                        
            } finally {
                stream.close();
            }
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
            JOptionPane.showMessageDialog(null, "ClassDiagramProjectFilesFactory exception! "+ex.getMessage());
        }
    }
    
    public String getCreatedFilePath() {
        return createdFilePath;
    }    

    void setProject(UMLProject project) {
        currentProject = project;
    }
}
