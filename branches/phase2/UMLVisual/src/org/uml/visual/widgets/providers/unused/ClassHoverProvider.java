/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets.providers.unused;


import java.awt.Color;
import org.netbeans.api.visual.action.TwoStateHoverProvider;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author "NUGS"
 */
public class ClassHoverProvider implements TwoStateHoverProvider{

    @Override
    public void unsetHovering(Widget widget) {
        widget.setBackground(Color.WHITE);
    }

    @Override
    public void setHovering(Widget widget) {
        widget.setBackground(Color.GRAY);
    }


}
