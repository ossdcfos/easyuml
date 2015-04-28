package org.uml.visual.widgets.providers;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.WidgetAction;

/**
 *
 * @author Jelena
 */
public class CloseInplaceEditorOnClickAdapter extends MouseAdapter {

    WidgetAction editorAction;

    public CloseInplaceEditorOnClickAdapter(WidgetAction a) {
        editorAction = a;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        ActionFactory.getInplaceEditorController(editorAction).closeEditor(true);
    }
}
