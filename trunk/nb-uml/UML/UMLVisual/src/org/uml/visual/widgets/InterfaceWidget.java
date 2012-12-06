/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets;

import javax.swing.BorderFactory;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.general.IconNodeWidget;

/**
 *
 * @author hrza
 */
public class InterfaceWidget extends IconNodeWidget {
    
    public InterfaceWidget(Scene scene) {
        super(scene);
        setBorder(BorderFactory.createEtchedBorder());
    }
}
