/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets.actions;

import org.netbeans.api.visual.action.TextFieldInplaceEditor;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;
import org.uml.model.ClassComponent;
import org.uml.visual.widgets.ClassWidget;

/**
 *
 * @author "NUGS"
 */
public class LabelTextFieldEditorAction implements TextFieldInplaceEditor{

//    ClassWidget classWidget;
//    public LabelTextFieldEditorAction(ClassWidget classWidget) {
//        this.classWidget=classWidget;
//    }
    
    
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
        //TODO podesavam ime konkretnog elementa, ali ga zove i field editor i method editor, kako da resim to?
        //classWidget.umlClassElement.setName(string);
    }
    
}
