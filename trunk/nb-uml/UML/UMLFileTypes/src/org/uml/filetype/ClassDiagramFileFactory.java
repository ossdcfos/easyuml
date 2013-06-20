/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.filetype;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import javax.swing.JOptionPane;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.uml.model.ClassDiagram;

/**
 *
 * @author zoran
 */
public class ClassDiagramFileFactory {
    
    private static ClassDiagramFileFactory instance;

    private ClassDiagramFileFactory() {
        
    }
    
    
    
    
    public static ClassDiagramFileFactory getDefault() {
        
        if (instance == null) {
            instance = new ClassDiagramFileFactory();
        }
        
        return instance;
    }
    
    public static void createClassDiagramFile(ClassDiagram classDiagram) {
        try {
            Lookup genlokup = Utilities.actionsGlobalContext();
            Project proj = genlokup.lookup(Project.class);             
//            currentProject = CurrentProject.getInstance().getCurrentProject();                     
//            String path = currentProject.getProjectDirectory().getPath();
//            path += "/" + NeurophProject.NEURAL_NETWORKS_DIR + "/";
//            this.neuralNet = nnet;
//            networkName = this.neuralNet.getLabel();
//            String fullFilePath = path + networkName + ".nnet";
            ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(fullFilePath));
            try {
                createdFilePath = null;
                stream.writeObject(this.neuralNet);   
                createdFilePath = fullFilePath;
                currentProject.addNotify(); // notify project that new file has been created                         
            } finally {
                stream.close();
            }
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
            JOptionPane.showMessageDialog(null, "Please select project");
        }        
    }
    
}
