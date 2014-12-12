package org.uml.reveng;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import static java.io.File.separator;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.netbeans.api.project.Project;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.uml.model.ClassDiagram;
import org.uml.visual.UMLTopComponent;
import org.openide.filesystems.FileObject;
import org.openide.awt.ActionReferences;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.uml.jung.JUNGEngine;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.xmlSerialization.ClassDiagramXmlSerializer;

@ActionID(
        category = "Source",
        id = "org.uml.reveng.ReverseEngineerAction")
@ActionRegistration(
        displayName = "#CTL_ReverseEngineerAction")
@ActionReferences({
    @ActionReference(path = "Menu/Source", position = 50),
    @ActionReference(path = "Projects/org-netbeans-modules-java-j2seproject/Actions", position = 1050)
})
@Messages("CTL_ReverseEngineerAction=easyUML Reverse Engineer")
/**
 * Class that responds to Reverse Engineer command from the main window
 *
 * @author Milan Djoric
 */
public final class ReverseEngineerAction implements ActionListener {

    private final Project context;

    /**
     * Default constructor
     *
     * @param context in which the command is issued
     */
    public ReverseEngineerAction(Project context) {
        this.context = context;
    }

    /**
     * Main method for starting the Reverse engineering process.
     * <p>
     * It finds the source folder and starts compilation upon that path and also
     * defines a file system separator based on Operating system. When The
     * compilation finishes, it checks if any .class fines have been generated
     * and if there are any, proceeds to making proceeds to saving the diagram
     * into a specified folder in the root of the project as a .cdg file.
     *
     * @param ev action event that occurred (button pressed into main window)
     */
    @Override
    public void actionPerformed(ActionEvent ev) {
        ClassDiagram classDiagram = ReverseEngineer.createClassDiagram(context);
        UMLTopComponent umlTopComponent = new UMLTopComponent(classDiagram);
        
        //Save the diagram into project's path
        File savedFile = saveDiagramLocally(context.getProjectDirectory(), classDiagram, umlTopComponent);
        try {
            umlTopComponent.setFileObject(FileUtil.createData(savedFile));
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        //Present the generated diagram to user
        umlTopComponent.open();
        //Make JUNG object
        JUNGEngine je = new JUNGEngine(umlTopComponent.getScene());
        //Apply it's layout to diagram (scene)
        je.applyJUNGLayout();
    }

    /**
     * Saves the generated diagram into the selected project's root (inside a
     * specified folder)
     *
     * @param projectFile folder of the project
     * @param generatedDiagram diagram to be saved
     * @param tc scene that this diagram belongs to
     * @param separator file system default separator
     * @return folder inside which the generated diagram is saved
     */
    private File saveDiagramLocally(FileObject projectFile, ClassDiagram generatedDiagram, UMLTopComponent tc) {
        String diagramFolderName = "UML Reverse Engineer";
        DateFormat dateFormat = new SimpleDateFormat("HH-mm-ss yyyy.MM.dd");
        Date date = new Date();
        System.out.println(dateFormat.format(date));
        String path = projectFile.getPath() + File.separator + diagramFolderName;
        File fileForSaving = new File(path);
        fileForSaving.mkdirs();
        String format = ".cdg";
        path += File.separator + "Reverse Engineered diagram XML " + dateFormat.format(date) + format;
        save(path, generatedDiagram, tc.getScene(), separator);
        return fileForSaving;
    }

    /**
     * Seriialises given diagram to .cdg XML file.
     *
     * @param path to save (serialise) to
     * @param diagramToSave (serialise) to .cdg file
     * @param scene where given diagram belongs to
     * @param fileSeparator default file system separator
     */
    public void save(String path, ClassDiagram diagramToSave, ClassDiagramScene scene, String fileSeparator) {
        FileOutputStream fileOut = null;
        XMLWriter writer = null;
        try {
            fileOut = new FileOutputStream(path);
            ClassDiagramXmlSerializer serializer = ClassDiagramXmlSerializer.getInstance();
            ClassDiagram diagram = diagramToSave;
            serializer.setClassDiagram(diagram);
            serializer.setClassDiagramScene(scene);
            Document document = DocumentHelper.createDocument();
            //document.setXMLEncoding("UTF-8");
            Element root = document.addElement("ClassDiagram");
            serializer.serialize(root);
            OutputFormat format = OutputFormat.createPrettyPrint();
            writer = new XMLWriter(
                    fileOut, format
            );
            writer.write(document);
            System.out.println("Reverse engineered object written to external file");
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            try {
                fileOut.close();
                writer.close();
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }
}
