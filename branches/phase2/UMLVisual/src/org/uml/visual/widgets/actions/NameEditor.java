package org.uml.visual.widgets.actions;

import org.netbeans.api.visual.action.TextFieldInplaceEditor;
import org.netbeans.api.visual.widget.Widget;
import org.uml.visual.widgets.IUMLWidget;
import org.uml.visual.widgets.components.ComponentWidgetBase;

/**
 *
 * @author Jelena
 */
public class NameEditor implements TextFieldInplaceEditor {

    private IUMLWidget nameable;

    public NameEditor(IUMLWidget umlWidget) {
        this.nameable = umlWidget;
    }

    @Override
    public boolean isEnabled(Widget widget) {
        return true;
    }

    @Override
    public String getText(Widget widget) {
        // TODO rethink this
        if (nameable instanceof ComponentWidgetBase)
            return ((ComponentWidgetBase) nameable).getName();
        else return nameable.getSignature();
    }

    @Override
    // widget is nameWidget, not used, because we need to update nameable parent
    public void setText(Widget widget, String string) {
        try {
            if (nameable instanceof ComponentWidgetBase)
                ((ComponentWidgetBase) nameable).setName(string);
            else nameable.setSignature(string);
        } catch (RuntimeException ex) {
        }
    }
}
