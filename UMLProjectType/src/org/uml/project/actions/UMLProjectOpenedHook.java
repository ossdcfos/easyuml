package org.uml.project.actions;

import org.netbeans.api.project.Project;
import org.netbeans.api.project.ui.OpenProjects;
import org.netbeans.spi.project.ui.ProjectOpenedHook;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataFolder;
import org.openide.nodes.Node;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.uml.filetype.cdg.ClassDiagramDataObject;
import org.uml.project.UMLProject;

/**
 *
 * @author Boris
 */
public class UMLProjectOpenedHook extends ProjectOpenedHook {

    private final Project project;

    public UMLProjectOpenedHook(Project project) {
        this.project = project;
    }

    @Override
    protected void projectOpened() {
        // Nothing special when opening project
    }

    @Override
    protected void projectClosed() {
        // Get diagram data objects
        FileObject umlProjectFileObject = project.getProjectDirectory();
        DataFolder umlClassDiagramFolder = DataFolder.findFolder(umlProjectFileObject.getFileObject("//Class Diagrams//"));
        Node umlClassDiagramFolderNode = umlClassDiagramFolder.getNodeDelegate();
        Node[] classDiagramNodes = umlClassDiagramFolderNode.getChildren().getNodes();
        // Close UMLTopComponents corresponding to diagram data objects and dispose diagram data objects
        for (Node node : classDiagramNodes) {
            ClassDiagramDataObject cddo = node.getLookup().lookup(ClassDiagramDataObject.class);
            cddo.notifyDispose();
        }
        // Check if there is some open UMLProject remaining
        boolean someUmlProjectOpen = false;
        for (Project openProject : OpenProjects.getDefault().getOpenProjects()) {
            if (openProject instanceof UMLProject) {
                someUmlProjectOpen = true;
                break;
            }
        }
        // If no UMLProject is open, close ExplorerTopComponent
        if (!someUmlProjectOpen) {
            WindowManager.getDefault().invokeWhenUIReady(new Runnable() {
                @Override
                public void run() {
                    TopComponent explorerTopComponent = WindowManager.getDefault().findTopComponent("easyUMLExplorerTopComponent");
                    explorerTopComponent.close();
                }
            });
        }
    }
}
