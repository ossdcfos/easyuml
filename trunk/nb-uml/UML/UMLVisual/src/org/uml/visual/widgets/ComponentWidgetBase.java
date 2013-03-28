package org.uml.visual.widgets;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.KeyStroke;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.uml.model.ClassDiagramComponent;
import org.uml.visual.widgets.providers.ClassConnectProvider;
import org.uml.visual.widgets.actions.DeleteClassAction;

/**
 *
 * @author "NUGS"
 */
abstract public class ComponentWidgetBase extends IconNodeWidget implements Nameable{

    private static final Dimension MINDIMENSION= new Dimension (100,0);
    
    public ComponentWidgetBase(ClassDiagramScene scene) {
        super(scene);
        
        getActions().addAction(ActionFactory.createExtendedConnectAction(scene.getInterractionLayer(), new ClassConnectProvider()));
        getActions().addAction (ActionFactory.createAlignWithMoveAction (scene.getMainLayer(), scene.getInterractionLayer(), null));
        setMinimumSize(MINDIMENSION);
       //Delete dugme, za sada ne funkcionise kako bi trebalo
//       InputMap inputMap = new InputMap ();
//       inputMap.put (KeyStroke.getKeyStroke (KeyEvent.VK_DELETE, 0, false), "myAction");        
//       ActionMap actionMap = new ActionMap ();
//       actionMap.put ("myAction", new DeleteClassAction (this));     
//       getActions().addAction(ActionFactory.createActionMapAction(inputMap, actionMap));
    }
    
    // this method should be abstract...
    public ClassDiagramComponent getComponent() {
        return null;
    }
    
    abstract public LabelWidget getNameLabel();
}
