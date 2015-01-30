package org.uml.reveng;

import java.io.File;
import java.io.IOException;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.uml.jung.JUNGEngine;
import org.uml.visual.UMLTopComponent;

/**
 *
 * @author Boris
 */
public abstract class ReverseEngineerActionBase {

    protected void saveDiagramAndOpenTopComponent(FileObject projectFolder, UMLTopComponent umlTopComponent) {
        saveDiagramToProjectRoot(projectFolder, umlTopComponent);
        //Present the generated diagram to user
        umlTopComponent.open();
        umlTopComponent.requestActive();
    }

    protected void arrangeDiagram(UMLTopComponent umlTopComponent) {
        //Make JUNG object
        JUNGEngine je = new JUNGEngine(umlTopComponent.getClassDiagramScene());
        //Apply it's layout to diagram (scene)
        je.applyJUNGLayout();
    }

    /**
     * Saves the generated diagram into the selected project's root (inside a
     * specified folder)
     *
     * @param projectFolder folder of the project
     * @param generatedDiagram diagram to be saved
     * @param tc scene that this diagram belongs to
     * @return folder inside which the generated diagram is saved
     */
    private void saveDiagramToProjectRoot(FileObject projectFolder, UMLTopComponent tc) {
        String diagramFolderName = "UML Reverse Engineer";
        String path = projectFolder.getPath() + File.separator + diagramFolderName;
        File diagramFolder = new File(path);
        diagramFolder.mkdirs();
        String format = ".cdg";
        path += File.separator + tc.getClassDiagramScene().getClassDiagram().getName() + format;
        File diagramFile = new File(path);
        try {
            diagramFile.createNewFile();
            tc.setFileObject(FileUtil.createData(diagramFile));
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        tc.saveTopComponent();
    }
}
