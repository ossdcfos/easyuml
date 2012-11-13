/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets;

import java.awt.Color;
import java.awt.Dimension;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.uml.model.UmlClassElement;

/**
 *
 * @author NUGS
 */
class ClassWidget extends IconNodeWidget {

    //TODO Zoki da li si razmisljao da napravimo domen neki UmlElement pa da ovi nasledjuju to? 
    UmlClassElement classElement;

    public ClassWidget(ClassDiagramScene scene, UmlClassElement umlClassElement) {
        super(scene);
        this.classElement = umlClassElement;
        setPreferredSize(new Dimension(100, 100));
        setBorder(BorderFactory.createResizeBorder(8, Color.BLACK, false));
        getActions().addAction(ActionFactory.createResizeAction());
        getActions().addAction(ActionFactory.createMoveAction(ActionFactory.createSnapToGridMoveStrategy(16, 16), null));

    }
}
