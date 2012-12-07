package org.uml.visual.widgets;

import java.awt.event.KeyEvent;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.KeyStroke;
import org.netbeans.api.visual.action.ActionFactory;
import org.uml.model.ClassComponent;
import org.uml.model.InterfaceComponent;
import org.uml.visual.providers.ClassConnectProvider;
import org.uml.visual.providers.ClassPopupMenuProvider;
import org.uml.visual.widgets.actions.ClassWidgetAcceptProvider;
import org.uml.visual.widgets.actions.DeleteClassAction;

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


        //This need to be here, widget notifyStateChanged listens to this
        //TODO Make Select and Hover Action outside scene, without blue marker
        //    widget.getActions().addAction(scene.createSelectAction());
        //Create resize action, needs to be activated BEFORE Move Action
        widget.getActions().addAction(ActionFactory.createResizeAction());
        //single-click, the event is not consumed:
        //mouse-dragged, the event is consumed while mouse is dragged:
        //widget.getActions().addAction(ActionFactory.createMoveAction());
        widget.getActions ().addAction (ActionFactory.createAlignWithMoveAction (widget.getClassDiagramScene().getMainLayer(), widget.getClassDiagramScene().getInterractionLayer(), null));
        //mouse-over, the event is consumed while the mouse is over the widget:
        //  widget.getActions().addAction(scene.createWidgetHoverAction());

        // Add Menu Provider
        widget.getActions().addAction(ActionFactory.createPopupMenuAction(new ClassPopupMenuProvider(widget)));

        //Accept dropping widgets
        widget.getActions().addAction(ActionFactory.createAcceptAction(new ClassWidgetAcceptProvider(widget)));
        
        //widget.getActions ().addAction (ActionFactory.createAlignWithMoveAction (widget.getClassDiagramScene().getMainLayer(), widget.getClassDiagramScene().getInterractionLayer(), null));

        InputMap inputMap = new InputMap ();
        inputMap.put (KeyStroke.getKeyStroke (KeyEvent.VK_DELETE, 0, false), "myAction");
        
       ActionMap actionMap = new ActionMap ();
       actionMap.put ("myAction", new DeleteClassAction (widget));
       
       widget.getActions().addAction(ActionFactory.createActionMapAction(inputMap, actionMap));
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

    /**
     * Creates new instance of InterfaceWidget
     *
     * @param scene
     * @param c
     * @return
     */
    public static InterfaceWidget createInterfaceWidget(ClassDiagramScene scene, InterfaceComponent c) {
        
        InterfaceWidget widget= new InterfaceWidget(scene, c);
        
        widget.getActions().addAction(ActionFactory.createExtendedConnectAction(scene.getInterractionLayer(), new ClassConnectProvider()));
        
        widget.getActions().addAction(ActionFactory.createMoveAction());
        
        InputMap inputMap = new InputMap ();
        inputMap.put (KeyStroke.getKeyStroke (KeyEvent.VK_DELETE, 0, false), "myAction"); 
        
       ActionMap actionMap = new ActionMap ();
       actionMap.put ("myAction", new DeleteClassAction (widget));
       
       widget.getActions().addAction(ActionFactory.createActionMapAction(inputMap, actionMap));
        
        return widget;
    }
}
