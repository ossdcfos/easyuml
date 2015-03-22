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

    RelationBase relation;

    public RelationLabelTextFieldEditorAction(RelationBase relationComponent) {
        this.relation = relationComponent;
    }

    @Override
    public boolean isEnabled(Widget widget) {
        return true;
    }

    @Override
    public String getText(Widget widget) {
        return relation.getName();
    }

    @Override
    public void setText(Widget widget, String string) {
        relation.setName(string);
        ((LabelWidget) widget).setLabel(string);
    }
}
