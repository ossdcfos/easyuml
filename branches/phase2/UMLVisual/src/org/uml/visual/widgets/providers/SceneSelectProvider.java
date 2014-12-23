package org.uml.visual.widgets.providers;

import java.awt.Point;
import java.util.HashSet;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.widget.Widget;
import org.uml.visual.widgets.ClassDiagramScene;

/**
 *
 * @author stefanpetrovic
 */
public class SceneSelectProvider implements SelectProvider{

    private ClassDiagramScene scene;
    private static HashSet<Object> EMPTY_SET = new HashSet<>();

    public SceneSelectProvider(ClassDiagramScene scene) {
        this.scene = scene;
    }
    
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
        if (widget instanceof ClassDiagramScene) {
//            for (Widget w : scene.getMainLayer().getChildren()) {
//                ComponentWidgetBase comp = (ComponentWidgetBase) w;
//                scene.getContent().remove(comp.getComponent());
//                //comp.notifyStateChanged(comp.getState(), comp.getState().createNormal());
//            }
            
            //scene.getView().requestFocusInWindow();
            scene.setFocusedObject(null);
            scene.setSelectedObjects(EMPTY_SET);
            
            //scene.validate();
        }        
    }
}
