/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets.providers;

import java.awt.Color;
import java.awt.Point;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author "NUGS"
 */
public class ClassSelectProvider implements SelectProvider{

    @Override
    public boolean isAimingAllowed(Widget widget, Point point, boolean bln) {
        //return true;
        return false;
    }

    @Override
    public boolean isSelectionAllowed(Widget widget, Point point, boolean bln) {
        return true;
    }

    @Override
    public void select(Widget widget, Point point, boolean bln) {
        widget.getScene().setFocusedWidget(widget);
        widget.setBackground(Color.WHITE);
    }
    
}
