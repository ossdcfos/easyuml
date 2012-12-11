package org.uml.visual.widgets.actions;

import java.awt.Point;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author Jelena
 */
public class EditFieldNameAction implements SelectProvider{

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
        
        WidgetAction editorAction = ActionFactory.createInplaceEditorAction(new LabelTextFieldEditorAction());
        ActionFactory.getInplaceEditorController (editorAction).openEditor(widget);
        
        
        
    }
}
