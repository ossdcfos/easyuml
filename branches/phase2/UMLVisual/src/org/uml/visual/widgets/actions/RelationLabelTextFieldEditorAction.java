package org.uml.visual.widgets.actions;

import org.netbeans.api.visual.action.TextFieldInplaceEditor;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;
import org.uml.model.relations.RelationBase;

/**
 *
 * @author Uros
 */
public class RelationLabelTextFieldEditorAction implements TextFieldInplaceEditor {

    RelationBase relationComponent;

    public RelationLabelTextFieldEditorAction(RelationBase relationComponent) {
        this.relationComponent = relationComponent;
    }

    @Override
    public boolean isEnabled(Widget widget) {
        return true;
    }

    @Override
    public String getText(Widget widget) {
        return relationComponent.getName();
    }

    @Override
    public void setText(Widget widget, String string) {
        relationComponent.setName(string);
        ((LabelWidget) widget).setLabel(string);
    }

}
