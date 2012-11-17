/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets.actions;

import org.netbeans.api.visual.action.TextFieldInplaceEditor;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;
import org.uml.model.UmlClassElement;

/**
 *
 * @author "NUGS"
 */
public class FieldNameEditorAction implements TextFieldInplaceEditor{

    UmlClassElement umlClassElement;
    public FieldNameEditorAction(UmlClassElement umlClassElement) {
        this.umlClassElement=umlClassElement;
    }

        
    @Override
    public boolean isEnabled(Widget widget) {
        return true;
    }

    @Override
    public String getText(Widget widget) {
        return ((LabelWidget) widget).getLabel();
    }

    @Override
    public void setText(Widget widget, String string) {
        ((LabelWidget) widget).setLabel(string);
        //podesavam ime konkretnog polja
    }
    
}