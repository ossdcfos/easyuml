package org.uml.reveng;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
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
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.uml.model.ClassDiagram;
import org.uml.visual.UMLTopComponent;
import org.openide.filesystems.FileObject;
import org.uml.jung.JUNGEngine;
import org.openide.NotifyDescriptor.Confirmation;
import org.openide.awt.ActionReferences;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
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
        //Get project's root direstory
        FileObject projectFile = context.getProjectDirectory();
        //Get root directory's path
        String projectSourcePath = projectFile.getPath();
        //Get default file system separator
        String separator = getSystemSeparator();
        //Start the compilation of files found inside specified path and default file system separator
        Compilation.initiateCompilation(projectSourcePath, separator);
        //Get the generated CLass diagram from Generated diagram manager singleton object
        ClassDiagram generatedDiagram = GeneratedDiagramManager.getInstance().getClassDiagram();
        //If the flag for no .class files generated is raised, inform the user
        if (GeneratedDiagramManager.getInstance().isZeroClassesGenerated()) {
            Confirmation msg = new NotifyDescriptor.Confirmation("Compilation had not generated classes - diagram not generated",
                    NotifyDescriptor.DEFAULT_OPTION, NotifyDescriptor.ERROR_MESSAGE);
            Object result = DialogDisplayer.getDefault().notify(msg);
        } else {
            //If .class files fave been generated
            //Make top component
            UMLTopComponent tc;
            //Initialise it with generated Class diagram
            tc = new UMLTopComponent(generatedDiagram);
            
            //Save the diagram into project's path
            File savedFile = saveDiagramLocally(projectFile, generatedDiagram, tc, separator);
            try {
                tc.setFileObject(FileUtil.createData(savedFile));
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
            //Present the generated diagram to user
            tc.open();
            //Make JUNG object
            JUNGEngine je = new JUNGEngine(tc.getScene());
            //Apply it's layout to diagram (scene)
            je.applyJUNGLayout();
            //Reset static Generated diagram manager's content
            GeneratedDiagramManager.getInstance().clearContent();
        }
    }

    /**
     * Escapes if necessary and returns default Operating system file separator:
     * / or \
     *
     * @return escaped (regex safe) file separator
     */
    private String getSystemSeparator() {
        if (File.separator.equals("\\")) {
            return "\\" + File.separator;
        } else {
            return File.separator;
        }
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
    private File saveDiagramLocally(FileObject projectFile, ClassDiagram generatedDiagram, UMLTopComponent tc, String separator) {
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
    public void save (String path, ClassDiagram diagramToSave, ClassDiagramScene scene, String fileSeparator){
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
            writer.write( document );
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
