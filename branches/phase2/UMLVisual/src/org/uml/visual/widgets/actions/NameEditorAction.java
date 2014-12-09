package org.uml.visual.widgets.actions;

import org.netbeans.api.visual.action.TextFieldInplaceEditor;
import org.netbeans.api.visual.widget.Widget;
import org.uml.visual.widgets.IUMLWidget;

/**
 *
 * @author Jelena
 */
public class NameEditorAction implements TextFieldInplaceEditor {

    private IUMLWidget nameable;

    public NameEditorAction(IUMLWidget umlWidget) {
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
    // widget is nameWidget, not used, because we need to update nameable parent
    public void setText(Widget widget, String string) {
        try {
            nameable.setSignature(string);
        } catch (RuntimeException ex) {
//            String input = JOptionPane.showInputDialog("Name you have entered already exists, please enter another one.", getName(nameable));
            //setText(widget, input);
            //JOptionPane.showMessageDialog(null, "Name you have entered already exists, please enter another one.");
        }
        
        //TODO podesavam ime konkretnog elementa, ali ga zove i field editor i method editor, kako da resim to?
        //classWidget.umlClassElement.setName(string);
    }
    
//    private String getName(INameableWidget widget) {
//        Field f = new Field("", "", Visibility.PRIVATE);
//        Method m = new Method("", "", new HashMap<String, MethodArgument>());
//        WidgetParser wp = new WidgetParser();
//        
//        if (widget instanceof FieldWidget) {
//            wp.fillFieldComponents(f, widget.getName());
//            return f.getName();
//        }
//        if (widget instanceof MethodWidget) {
//            wp.fillMethodComponents(m, widget.getName());
//            return m.getName();
//        }
//        return "";
//    }
}
