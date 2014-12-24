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
public class SceneSelectProvider implements SelectProvider {

    private static HashSet<Object> EMPTY_SET = new HashSet<>();

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
        ClassDiagramScene scene = (ClassDiagramScene) widget;
        scene.setFocusedObject(null);
        scene.setSelectedObjects(EMPTY_SET);
        scene.setSceneFocusForExplorer();
                    
        scene.getUmlTopComponent().requestFocusInWindow();
    }
}
