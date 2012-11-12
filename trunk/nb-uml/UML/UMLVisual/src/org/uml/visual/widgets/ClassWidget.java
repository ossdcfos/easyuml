/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets;

import java.awt.Color;
import java.awt.Dimension;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.uml.model.UmlClassElement;

/**
 *
 * @author Uros
 */
class ClassWidget extends IconNodeWidget {

    //TODO Zoki da li si razmisljao da napravimo domen neki UmlElement pa da ovi nasledjuju to? 
    
    UmlClassElement classElement;

    public ClassWidget(ClassDiagramScene scene, UmlClassElement umlClassElement) {
        super(scene);
        this.classElement = umlClassElement;
        setPreferredSize(new Dimension(100, 200));
        setBorder(BorderFactory.createRoundedBorder(20, 20, Color.white, Color.black));
    }
}
