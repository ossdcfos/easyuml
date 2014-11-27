/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets.providers.unused;

import java.awt.Point;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.widget.Widget;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.components.ComponentWidgetBase;

/**
 *
 * @author stefanpetrovic
 */
public class ComponentSelectProvider implements SelectProvider {

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
            ComponentWidgetBase selectedComponent = (ComponentWidgetBase) widget;
            ClassDiagramScene scene = (ClassDiagramScene) selectedComponent.getScene();

            if (selectedComponent.getState().isSelected()) {
                selectedComponent.notifyStateChanged(selectedComponent.getState(), selectedComponent.getState().deriveSelected(false));
            } else {
                scene.getContent().add(selectedComponent.getComponent());

                selectedComponent.notifyStateChanged(selectedComponent.getState(), selectedComponent.getState().deriveSelected(true));

                for (Widget w : scene.getMainLayer().getChildren()) {
                    if (!w.equals(selectedComponent)) {
                        ComponentWidgetBase component = (ComponentWidgetBase) w;
                        scene.getContent().remove(component.getComponent());
                        component.notifyStateChanged(component.getState(), component.getState().deriveSelected(false).deriveWidgetAimed(true));
                    }
                }
            }
        }
    }
}
