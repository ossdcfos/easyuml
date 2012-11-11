/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets;

import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.uml.model.UmlClassDiagram;

/**
 *
 * @author Uros
 */
public class ClassDiagramWidget extends IconNodeWidget {
    
    private UmlClassDiagram umlClassDiagram;

    public ClassDiagramWidget(UmlClassDiagram umlClassDiagram, Scene scene) {
        super(scene);
        this.umlClassDiagram = umlClassDiagram;
    }

    public UmlClassDiagram getUmlClassDiagram() {
        return umlClassDiagram;
    }
    
    
    
}
