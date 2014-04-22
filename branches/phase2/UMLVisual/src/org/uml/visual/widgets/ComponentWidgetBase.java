package org.uml.visual.widgets;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.ResizeControlPointResolver;
import org.netbeans.api.visual.action.ResizeProvider;
import org.netbeans.api.visual.action.TwoStateHoverProvider;
import org.netbeans.api.visual.border.Border;
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
    private static final Border SELECTED_BORDER = BorderFactory.createResizeBorder(4, Color.black, true);
    private static final Border DEFAULT_BORDER = BorderFactory.createLineBorder(0);
    
    //atribut name

    protected LabelWidget nameWidget;
    ClassDiagramScene scene;

    public ComponentWidgetBase(ClassDiagramScene scene) {
        super(scene);
        this.scene = scene;
        setBorder(DEFAULT_BORDER);
        getActions().addAction(ActionFactory.createExtendedConnectAction(scene.getInterractionLayer(), new ClassConnectProvider()));
        getActions().addAction(ActionFactory.createResizeAction());
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
            //setBorder(SELECTED_BORDER);
            setBorder (SELECTED_BORDER);
            
        } else {
            if (state.isHovered()) {
                
            }else {
                setBorder(DEFAULT_BORDER);
            }
            
        }
        
        if(previousState.isSelected()) {
            setBorder(DEFAULT_BORDER);
        }                          
        
        
    }
    
    
    abstract public ClassDiagramComponent getComponent();

    abstract public LabelWidget getNameLabel();
    
    // allready has getScene in widget ..
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
    
    private static class MyResizeControlPointResolver implements ResizeControlPointResolver {

        public ResizeProvider.ControlPoint resolveControlPoint (Widget widget, Point point) {
            Rectangle bounds = widget.getBounds ();
            Insets insets = widget.getBorder ().getInsets ();
            if (new Rectangle (bounds.x + bounds.width - insets.right, bounds.y + bounds.height - insets.bottom, insets.right, insets.bottom).contains (point))
                return ResizeProvider.ControlPoint.BOTTOM_RIGHT;
            return null;
        }
    }
    
    
}
