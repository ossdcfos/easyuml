/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets.actions;

import java.util.HashMap;
import javax.swing.JOptionPane;
import org.netbeans.api.visual.action.TextFieldInplaceEditor;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;
import org.uml.model.Field;
import org.uml.model.Member;
import org.uml.model.Method;
import org.uml.model.MethodArgument;
import org.uml.model.Visibility;
import org.uml.visual.parser.WidgetParser;
import org.uml.visual.widgets.FieldWidget;
import org.uml.visual.widgets.MethodWidget;
import org.uml.visual.widgets.NameableWidget;

/**
 *
 * @author Jelena
 */
public class NameEditorAction implements TextFieldInplaceEditor {

    NameableWidget nameable;

    public NameEditorAction(NameableWidget umlWidget) {
        this.nameable = umlWidget;
    }
    private String oldName;

    @Override
    public boolean isEnabled(Widget widget) {
        return true;
    }

    @Override
    public String getText(Widget widget) {
        oldName = ((LabelWidget) widget).getLabel();
        return oldName;
    }

    @Override
    public void setText(Widget widget, String string) {

        try {
            nameable.setName(string);
            nameable.setAttributes(string);
        } catch (RuntimeException ex) {
            String input = JOptionPane.showInputDialog("Name you have entered already exists, please enter another one.", getName(nameable));
            setText(widget, input);
            //JOptionPane.showMessageDialog(null, "Name you have entered already exists, please enter another one.");
        }



        //TODO podesavam ime konkretnog elementa, ali ga zove i field editor i method editor, kako da resim to?
        //classWidget.umlClassElement.setName(string);
    }
    
    private String getName(NameableWidget widget) {
        Field f = new Field("", "", Visibility.PRIVATE);
        Method m = new Method("", "", new HashMap<String, MethodArgument>());
        WidgetParser wp = new WidgetParser();
        
        if (widget instanceof FieldWidget) {
            wp.fillFieldComponents(f, widget.getName());
            return f.getName();
        }
        if (widget instanceof MethodWidget) {
            wp.fillMethodComponents(m, widget.getName());
            return m.getName();
        }
        return "";
    }
}
