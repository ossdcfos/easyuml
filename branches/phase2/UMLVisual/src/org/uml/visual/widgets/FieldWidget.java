/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets;


import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.LabelWidget;
import org.uml.model.Field;
import org.uml.model.Member;
import org.uml.visual.parser.ParserZaWidget;
import org.uml.visual.widgets.actions.NameEditorAction;
import org.uml.visual.widgets.providers.FieldPopupMenuProvider;

/**
 *
 * @author Jelena
 */
public class FieldWidget  extends MemberWidgetBase{

    Field fieldComponent;
    private WidgetAction nameEditorAction = ActionFactory.createInplaceEditorAction(new NameEditorAction(this));
    LabelWidget visibilityLabel;
    LabelWidget fieldNameWidget;
    
    public FieldWidget(ClassDiagramScene scene, Field field) {
        super(scene);
        this.fieldComponent = field;
        this.setLayout(LayoutFactory.createHorizontalFlowLayout());
        visibilityLabel = new LabelWidget(getScene());
        visibilityLabel.setLabel("+");
        this.addChild(visibilityLabel);

        fieldNameWidget = new LabelWidget(getScene());
        fieldNameWidget.setLabel(field.getName());
        this.addChild(fieldNameWidget);
        fieldNameWidget.getActions().addAction(nameEditorAction);
        fieldNameWidget.getActions().addAction(ActionFactory.createPopupMenuAction(new FieldPopupMenuProvider(this)));

    }
    
    @Override
    public LabelWidget getNameLabel() {
        return fieldNameWidget;
    }

    @Override
    public void setName(String newName) {
        if (getName().equals(newName)) {
            return;
        }
        String oldName = fieldComponent.getName();
        if (!fieldComponent.getDeclaringClass().nameExists(newName)) {
            fieldNameWidget.setLabel(newName);
            fieldComponent.setName(newName);
            fieldComponent.getDeclaringClass().componentNameChanged(fieldComponent, oldName);
        }
        else {
            //poruka
        }
    }

    @Override
    public String getName() {
        return fieldComponent.getName();
    }

    @Override
    public Member getMember() {
        return fieldComponent;
    }

    @Override
    public void setAttributes(String attributes) {
        /*String [] keyWords = attributes.split(" ");
        fieldComponent.setName(keyWords[keyWords.length-1]);
        for (String keyWord : keyWords) {
            if(keyWord.equals("int")) {
                fieldComponent.setType(Integer.TYPE);
            }
            if(keyWord.equals("double")){
                fieldComponent.setType(double.class);
            }
            if(keyWord.equals("boolean")) {
                fieldComponent.setType(boolean.class);
            }
            if(keyWord.equals("String")) {
                fieldComponent.setType(String.class);
            }
            if(keyWord.equals("long")) {
                fieldComponent.setType(long.class);
            }
            
        }
        */
        ParserZaWidget p = new ParserZaWidget(attributes);
        p.preskociWhitespace();
        fieldComponent.setVisibility(p.vratiVisibility());
        p.preskociWhitespace();
        String modifiers = "";
        boolean imaModifier = true;
	while(imaModifier) {
		String mod = p.vratiModifier();
		if(mod.equals("")){
			imaModifier = false;
			modifiers += mod;
			break;
		}
		modifiers += mod + " ";
		p.preskociWhitespace();
	}
        fieldComponent.setModifiers(modifiers);
        p.preskociWhitespace();
        p.vratiArgumentType();//ne znam kako da konvertujem string u Type?
        p.preskociWhitespace();
        fieldComponent.setName(p.vratiName());
        
    }

    
}
