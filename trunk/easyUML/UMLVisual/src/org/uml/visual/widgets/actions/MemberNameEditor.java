package org.uml.visual.widgets.actions;

import org.netbeans.api.visual.action.TextFieldInplaceEditor;
import org.netbeans.api.visual.widget.Widget;
import org.uml.visual.widgets.ISignedUMLWidget;

/**
 *
 * @author Jelena
 */
public class MemberNameEditor implements TextFieldInplaceEditor {

    private ISignedUMLWidget nameable;

    public MemberNameEditor(ISignedUMLWidget umlWidget) {
        this.nameable = umlWidget;
    }

    @Override
    public boolean isEnabled(Widget widget) {
        return true;
    }

    @Override
    public String getText(Widget widget) {
        return nameable.getSignature();
    }

    @Override
    // widget parameter is nameWidget. Not used, because we need to update nameable parent.
    public void setText(Widget widget, String string) {
        nameable.setSignature(string);
    }
}
