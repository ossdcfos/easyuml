/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.reveng;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.netbeans.api.project.Project;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.filesystems.FileObject;
import org.uml.filetype.cdgfiletype.CDGDataObject;
import org.uml.model.ClassDiagram;
import org.uml.visual.UMLTopComponent;
import org.openide.filesystems.FileObject;
import org.uml.jung.JUNGEngine;
import org.openide.NotifyDescriptor.Confirmation;

@ActionID(
        category = "Source",
        id = "org.uml.reveng.ReverseEngineer")
@ActionRegistration(
        displayName = "#CTL_ReverseEngineer")
@ActionReference(path = "Menu/Source", position = 50)
@Messages("CTL_ReverseEngineer=Reverse engineer")
public final class ReverseEngineer implements ActionListener {

    private final Project context;

    public ReverseEngineer(Project context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        FileObject projectFile = context.getProjectDirectory();
        String projectSoutcePath = projectFile.getPath();
        String separator = getSystemSeparator();
        Compilation.initiateCompilation(projectSoutcePath, separator);
        ClassDiagram generatedDiagram = GeneratedDiagramManager.getDefault().getClassDiagram();
        if (GeneratedDiagramManager.getDefault().isZeroClassesGenerated()) {
            Confirmation msg = new NotifyDescriptor.Confirmation("Compilation had not generated classes - diagram not generated",
                    NotifyDescriptor.DEFAULT_OPTION, NotifyDescriptor.ERROR_MESSAGE);
        Object result = DialogDisplayer.getDefault().notify(msg);
        } else {
            UMLTopComponent tc;
            tc = new UMLTopComponent(generatedDiagram);
            File savedFile = saveDiagramLocally(projectFile, generatedDiagram, tc, separator);
            tc.open();
            JUNGEngine je = new JUNGEngine(tc.getScene());
            je.applyJUNGLayout();
            GeneratedDiagramManager.getDefault().clearContent();
        }
    }

    private String getSystemSeparator() {
        if (File.separator.equals("\\")) {
            return "\\" + File.separator;
        } else {
            return File.separator;
        }
    }

    private File saveDiagramLocally(FileObject projectFile, ClassDiagram generatedDiagram, UMLTopComponent tc, String separator) {
        String diagramFolderName = "UML Reverse Engineer";
        DateFormat dateFormat = new SimpleDateFormat("HH-mm-ss yyyy.MM.dd");
        Date date = new Date();
        System.out.println(dateFormat.format(date));
        String path = projectFile.getPath() + File.separator + diagramFolderName;
        File fileForSaving = new File(path);
        fileForSaving.mkdirs();
        ClassDiagramUtil cdu = new ClassDiagramUtil();
        String format = ".cdg";
        path += File.separator + "Reverse Engineered diagram XML " + dateFormat.format(date) + format;
        cdu.save(path, generatedDiagram, tc.getScene(), separator);
        return fileForSaving;
    }
}
