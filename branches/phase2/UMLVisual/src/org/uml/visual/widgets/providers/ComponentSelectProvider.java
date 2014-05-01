/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets.providers;

import java.awt.Point;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.widget.Widget;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.ComponentWidgetBase;

/**
 *
 * @author stefanpetrovic
 */
public class ComponentSelectProvider implements SelectProvider{

    @Override
    public boolean isAimingAllowed(Widget widget, Point point, boolean bln) {
        return true;
    }

    @Override
    public boolean isSelectionAllowed(Widget widget, Point point, boolean bln) {
        return true;
    }

    @Override
    public void select(Widget widget, Point point, boolean bln) {
        if (widget instanceof ComponentWidgetBase) {
            ComponentWidgetBase comp = (ComponentWidgetBase) widget;
            if (comp.getState().isSelected()) {
                comp.notifyStateChanged(comp.getState(), comp.getState().deriveSelected(false));
            }else {
                ((ClassDiagramScene) comp.getScene()).getContent().add(comp.getComponent());
                comp.notifyStateChanged(comp.getState(), comp.getState().deriveSelected(true));
            }
        }
    }
    
}
