/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets;

import org.netbeans.api.visual.action.ActionFactory;
import org.uml.model.ClassComponent;
import org.uml.visual.providers.ClassConnectProvider;
import org.uml.visual.providers.ClassPopupMenuProvider;
import org.uml.visual.widgets.actions.ClassWidgetAcceptProvider;

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

        
        //Create connections, using EXTENDED Connect Action because it resolves conflict with moveAction
        widget.getActions().addAction(ActionFactory.createExtendedConnectAction(scene.getInterractionLayer(), new ClassConnectProvider()));
        
        widget.getActions().addAction(ActionFactory.createMoveAction());
        //single-click, the event is not consumed:
        widget.getActions().addAction(scene.createSelectAction());

        //mouse-dragged, the event is consumed while mouse is dragged:
        //widget.getActions().addAction(ActionFactory.createMoveAction());

        //mouse-over, the event is consumed while the mouse is over the widget:
        //widget.getActions().addAction(scene.createObjectHoverAction()); 
        
        // Add Menu Provider
        widget.getActions().addAction(ActionFactory.createPopupMenuAction(new ClassPopupMenuProvider(scene)));
        
        //Add connections, denies move action
        widget.getActions().addAction(ActionFactory.createConnectAction(scene.getInterractionLayer(), new ClassConnectProvider()));
        
        //Accept dropping widgets
        widget.getActions().addAction(ActionFactory.createAcceptAction(new ClassWidgetAcceptProvider(widget)));
        
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
