package org.uml.reveng;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.uml.filetype.cdg.ClassDiagramDataObject;
import org.uml.jung.JUNGEngine;
import org.uml.model.ClassDiagram;
import org.uml.project.UMLProject;
import org.uml.visual.UMLTopComponent;
import org.uml.visual.widgets.ClassDiagramScene;

/**
 *
 * @author Boris Perović
 */
public class ReverseEngineerUtils {

    public static void saveDiagramAndOpenTopComponent(FileObject projectFolder, ClassDiagram classDiagram) {
        String defaultPath = projectFolder.getPath().replace("/", File.separator) + File.separator + UMLProject.CLASS_DIAGRAMS_FOLDER;
        String fileName = "";
        String fileExt = ".cdg";
        UMLTopComponent umlTopComponent;

        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH-mm-ss");
        Date date = new Date();
        fileName = classDiagram.getName() + " " + dateFormat.format(date) + fileExt;

        umlTopComponent = new UMLTopComponent(classDiagram, fileName);
        FileObject fileObject = saveDiagramToFolder(defaultPath, umlTopComponent);
        // Do not open the umlTopComponent, but rather open the ClassDiagramDataObject,
        // so that new UMLTopComponent is tied to the file and the file correctly registers
        // events for renaming purposes
        try {
            DataObject dataObject = DataObject.find(fileObject);
            Node node = dataObject.getNodeDelegate();
            ClassDiagramDataObject cddo = node.getLookup().lookup(ClassDiagramDataObject.class);
            cddo.open();
        } catch (DataObjectNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        }

        // Previous implementation where the user could save reverse engineered diagram to an arbitrary location. Can remove.
//        int result = JOptionPane.showConfirmDialog(null, "Save to default location (" + defaultPath + ")?", "Save location", JOptionPane.YES_NO_CANCEL_OPTION);
//        switch (result) {
//            case JOptionPane.YES_OPTION:
//                DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH-mm-ss-SSS");
//                Date date = new Date();
//                fileName = classDiagram.getName() + " " + dateFormat.format(date) + fileExt;
//
//                umlTopComponent = new UMLTopComponent(classDiagram, fileName);
//                saveDiagramToFolder(defaultPath, umlTopComponent);
//                // If saved, present the generated diagram to user
//                umlTopComponent.open();
//                umlTopComponent.requestActive();
//                return;
//            case JOptionPane.NO_OPTION:
//                JFileChooser chooser = new JFileChooser() {
//                    @Override
//                    public void approveSelection() {
//                        File f = getSelectedFile();
//                        if (!f.getName().toLowerCase().endsWith(".cdg")) {
//                            f = new File(f.getParentFile(), f.getName() + ".cdg");
//                        }
//                        if (f.exists() && getDialogType() == SAVE_DIALOG) {
//                            int result = JOptionPane.showConfirmDialog(this, "The file exists, overwrite?", "Existing file", JOptionPane.YES_NO_OPTION);
//                            switch (result) {
//                                case JOptionPane.YES_OPTION:
//                                    super.approveSelection();
//                                    return;
//                                case JOptionPane.NO_OPTION:
//                                    return;
//                                case JOptionPane.CLOSED_OPTION:
//                                    return;
//                            }
//                        }
//                        super.approveSelection();
//                    }
//                };
//                chooser.setCurrentDirectory(FileUtil.toFile(projectFolder));
//                chooser.setFileFilter(new FileNameExtensionFilter("easyUML Class Diagram (.cdg)", "cdg"));
//                if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
//                    File f = chooser.getSelectedFile();
//                    if (!f.getName().toLowerCase().endsWith(".cdg")) {
//                        f = new File(f.getParentFile(), f.getName() + ".cdg");
//                    }
//                    umlTopComponent = new UMLTopComponent(classDiagram, fileName);
//                    saveDiagramToFolder(f.getParent(), umlTopComponent);
//                    // If saved, present the generated diagram to user
//                    umlTopComponent.open();
//                    umlTopComponent.requestActive();
//                }
//                return;
//            case JOptionPane.CANCEL_OPTION:
//                return;
//            case JOptionPane.CLOSED_OPTION:
//                return;
//        }
    }

    public static void arrangeDiagram(ClassDiagramScene classDiagramScene) {
        //Make JUNG object
        JUNGEngine je = new JUNGEngine(classDiagramScene);
        //Apply it's layout to diagram (scene)
        je.applyJUNGLayout();
    }

    /**
     * Saves the generated diagram onto the selected path (inside a
     * specified folder)
     *
     * @param folderPath path to save diagram to
     * @param tc UMLTopComponent that this diagram belongs to
     */
    private static FileObject saveDiagramToFolder(String folderPath, UMLTopComponent tc) {
        File projectFolder = new File(folderPath);
        projectFolder.mkdirs();
        String filePath = folderPath + File.separator + tc.getName();
        File diagramFile = new File(filePath);
        FileObject fileObject = null;
        try {
            diagramFile.createNewFile();
            fileObject = FileUtil.createData(diagramFile);
            tc.setFileObject(fileObject);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        tc.saveTopComponent();
        return fileObject;
    }
}
