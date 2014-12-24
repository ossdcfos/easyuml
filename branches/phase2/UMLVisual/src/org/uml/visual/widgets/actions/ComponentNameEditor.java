package org.uml.visual.widgets.actions;

import org.netbeans.api.visual.action.TextFieldInplaceEditor;
import org.netbeans.api.visual.widget.Widget;
import org.uml.visual.widgets.components.ComponentWidgetBase;

/**
 *
 * @author Jelena
 */
public class ComponentNameEditor implements TextFieldInplaceEditor {

    private ComponentWidgetBase widget;

    public ComponentNameEditor(ComponentWidgetBase widget) {
        this.widget = widget;
    }

    @Override
    public boolean isEnabled(Widget widget) {
        return true;
    }

    @Override
    public String getText(Widget widget) {
        return this.widget.getName();
    }

    @Override
    public void setText(Widget widget, String string) {
        this.widget.setName(string);
    }
}
