package org.uml.visual.widgets;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.TwoStateHoverProvider;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.widget.ImageWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.openide.util.Lookup;
import org.uml.model.ClassDiagramComponent;
import org.uml.visual.widgets.providers.ClassConnectProvider;

/**
 *
 * @author "NUGS"
 */
abstract public class ComponentWidgetBase extends ImageWidget implements NameableWidget, Lookup.Provider {

    private static final Dimension MINDIMENSION = new Dimension(100, 0);
    //atribut name

    protected LabelWidget nameWidget;
    ClassDiagramScene scene;

    public ComponentWidgetBase(ClassDiagramScene scene) {
        super(scene);
        this.scene = scene;
        getActions().addAction(ActionFactory.createExtendedConnectAction(scene.getInterractionLayer(), new ClassConnectProvider()));
        getActions().addAction(ActionFactory.createAlignWithMoveAction(scene.getMainLayer(), scene.getInterractionLayer(), null));
        getActions().addAction(scene.createSelectAction());
        getActions().addAction(ActionFactory.createHoverAction (new ChangeCursor ()));
        setMinimumSize(MINDIMENSION);
        
        nameWidget = new LabelWidget(scene);
        nameWidget.setFont(scene.getDefaultFont().deriveFont(Font.BOLD));
        nameWidget.setAlignment(LabelWidget.Alignment.CENTER);    
        
        //Delete dugme, za sada ne funkcionise kako bi trebalo
//       InputMap inputMap = new InputMap ();
//       inputMap.put (KeyStroke.getKeyStroke (KeyEvent.VK_DELETE, 0, false), "myAction");        
//       ActionMap actionMap = new ActionMap ();
//       actionMap.put ("myAction", new DeleteClassAction (this));     
//       getActions().addAction(ActionFactory.createActionMapAction(inputMap, actionMap));
    }

    @Override
    public void notifyStateChanged(ObjectState previousState, ObjectState state) {
        // u ovu metodu ubaciti reakcija ne hover, focus, selected itd.
        super.notifyStateChanged(previousState, state);
        if (state.isSelected()) {
            //setBorder(BorderFactory.createLineBorder(10));
        } else {
            setBorder(BorderFactory.createLineBorder());
        }
        
        if(previousState.isSelected()) {
          setBorder(BorderFactory.createLineBorder());
        }                          
       
          
        
    }
    
    
    
    // this method should be abstract...
    abstract public ClassDiagramComponent getComponent();

    abstract public LabelWidget getNameLabel();
    
    public ClassDiagramScene getClassDiagramScene() {
        return scene;
    }

    
    
    private class ChangeCursor implements TwoStateHoverProvider {

        @Override
        public void unsetHovering (Widget widget) {
            widget.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }

        @Override
        public void setHovering (Widget widget) {
            widget.setCursor(new Cursor(Cursor.HAND_CURSOR));
       }

    }
    
    
}
