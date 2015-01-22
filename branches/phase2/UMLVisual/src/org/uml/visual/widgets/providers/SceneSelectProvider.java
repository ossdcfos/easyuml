package org.uml.visual.widgets.providers;

import java.awt.Point;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.widget.Widget;
import org.uml.visual.widgets.ClassDiagramScene;

/**
 *
 * @author stefanpetrovic
 */
public class SceneSelectProvider implements SelectProvider {

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
        scene.selectScene();
    }
}
