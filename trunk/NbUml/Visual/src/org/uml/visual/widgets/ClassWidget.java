/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets;

import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.uml.model.UmlClassElement;

/**
 *
 * @author Uros
 */
class ClassWidget extends IconNodeWidget{

    UmlClassElement classElement;
    
    public ClassWidget(ClassDiagramScene scene,UmlClassElement umlClassElement) {
        super(scene); 
        this.classElement=umlClassElement;
    } 
 
    
}
