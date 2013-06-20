/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.filetype;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import org.uml.model.ClassDiagram;
import org.uml.visual.UMLTopComponent;

/**
 *
 * @author Jelena
 */
public class ViewManager implements
        Serializable {

    private static final long serialVersionUID = 1L;
    private static ViewManager instance;
    private HashMap<ClassDiagram, UMLTopComponent> openedDiagrams = new HashMap<ClassDiagram, UMLTopComponent>();
    
    public static ViewManager getInstance() {
        if (instance == null) {
            instance = new ViewManager();
        }
        return instance;
    }

    private ViewManager() {
    }

    public void openUMLDiagramWindow(ClassDiagram cd) {
        UMLTopComponent umlTopComponent;

        if (openedDiagrams.containsKey(cd)) {
            umlTopComponent = openedDiagrams.get(cd); // if diagram is allready opened get the window
            umlTopComponent.requestActive();
        } else {
            umlTopComponent = new UMLTopComponent(cd); // otherwise create new window to open diagram in
            umlTopComponent.open();
            openedDiagrams.put(cd, umlTopComponent);
            umlTopComponent.requestActive();
        }
    }

   

    public void onDiagramClose(ClassDiagram cd) {
        openedDiagrams.remove(cd);
    }

//    public void openNeuronPropertiesFrame(Neuron neuron) {
//        NeuronPropertiesFrameTopComponent neuronPropertiesFrame = NeuronPropertiesFrameTopComponent.findInstance();
//        neuronPropertiesFrame.setNeuronForNeuronPropertiesFrame(neuron);
//        neuronPropertiesFrame.open();
//        neuronPropertiesFrame.requestActive();
//    }


    /**
     * Shows message in MessageBoxTopComponent
     *
     * @param message - input message that will be displayed
     */
    public void showMessage(String message) {
    }

    
}
