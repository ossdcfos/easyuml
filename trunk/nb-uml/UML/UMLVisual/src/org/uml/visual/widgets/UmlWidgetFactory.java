/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets;

import org.netbeans.api.visual.action.ActionFactory;
import org.uml.model.ClassComponent;

/**
 *
 * @author hrza
 */
public class UmlWidgetFactory {

    /**
     * Creates new Class Widget
     *
     * @param scene
     * @param component
     * @return
     */
    public static ClassWidget createClassWidget(ClassDiagramScene scene, ClassComponent component) {
        ClassWidget widget = new ClassWidget(scene, component);

        widget.getActions().addAction(ActionFactory.createMoveAction());
        //single-click, the event is not consumed:
        widget.getActions().addAction(scene.createSelectAction());

        //mouse-dragged, the event is consumed while mouse is dragged:
        widget.getActions().addAction(ActionFactory.createMoveAction());

        //mouse-over, the event is consumed while the mouse is over the widget:
        widget.getActions().addAction(scene.createObjectHoverAction());

        return widget;
    }

    /**
     * Creates Array of Class Widgets
     *
     * @param scene
     * @param component
     * @param number
     * @return ClassWidget[] array
     */
    public static ClassWidget[] createClassWidgets(ClassDiagramScene scene, ClassComponent component, int number) {
        ClassWidget[] widget = new ClassWidget[number];
        while (number-- > 0) {
            widget[number - 1] = new ClassWidget(scene, component);
        }
        return widget;
    }
}
