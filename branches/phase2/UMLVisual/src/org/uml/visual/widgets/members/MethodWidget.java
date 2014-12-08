package org.uml.visual.widgets.members;

import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.LabelWidget;
import org.uml.model.members.MemberBase;
import org.uml.model.members.MethodBase;
import org.uml.visual.parser.WidgetParser;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.providers.popups.MemberBasePopupProvider;

/**
 *
 * @author Jelena
 */
public class MethodWidget extends MemberWidgetBase {

    MethodBase methodComponent;
    LabelWidget visibilityLabel;
    LabelWidget nameLabel;
    WidgetParser wp;

    public MethodWidget(ClassDiagramScene scene, MethodBase method) {
        super(scene, method);
        this.methodComponent = method;
        this.setLayout(LayoutFactory.createHorizontalFlowLayout());
        visibilityLabel = new LabelWidget(getScene());
        visibilityLabel.setLabel("+");
        this.addChild(visibilityLabel);

        nameLabel = new LabelWidget(getScene());
        nameLabel.setLabel(methodComponent.getSignatureForLabel());
        this.addChild(nameLabel);
        nameLabel.getActions().addAction(nameEditorAction);
        
        getActions().addAction(ActionFactory.createPopupMenuAction(new MemberBasePopupProvider(this)));
        wp = new WidgetParser();
        refreshLabel();
    }

    @Override
    public LabelWidget getNameLabel() {
        return nameLabel;
    }

    @Override
    public void setName(String newName) {
        if (getName().equals(newName)) {
            return;
        }
        String oldName = methodComponent.getName();
        if (!methodComponent.getDeclaringClass().nameExists(newName)) {
            nameLabel.setLabel(newName);
            methodComponent.setName(newName);
            methodComponent.getDeclaringClass().notifyMemberNameChanged(methodComponent, oldName);
        } else {
            //poruka
        }
    }

    @Override
    public String getName() {
        return methodComponent.getName();
    }

    @Override
    public MemberBase getMember() {
        return methodComponent;
    }

    public LabelWidget getMethodNameWidget() {
        return nameLabel;
    }

    @Override
    public void setAttributes(String attributes) {
        String oldName = methodComponent.getName();
        wp.fillMethodComponents(methodComponent, attributes);
        String newName = methodComponent.getName();
        if (newName.equals(oldName)) {
            
        } else {
            if (!methodComponent.getDeclaringClass().nameExists(newName)) {
                methodComponent.getDeclaringClass().notifyMemberNameChanged(methodComponent, oldName);
            } else {
                methodComponent.setName(oldName);
                throw new RuntimeException("Error: name already exists.");
            }
        }
        nameLabel.setLabel(methodComponent.getSignatureForLabel());
        refreshLabel();

    }

    public void refreshLabel() {
        switch (methodComponent.getVisibility()) {
            case PUBLIC:
                visibilityLabel.setLabel("+");
                break;
            case PRIVATE:
                visibilityLabel.setLabel("-");
                break;
            case PROTECTED:
                visibilityLabel.setLabel("#");
                break;
            case PACKAGE:
                visibilityLabel.setLabel("~");
                break;
        }
    }

    @Override
    protected void setSelected(boolean isSelected) {
        if(isSelected){
            visibilityLabel.setForeground(SELECT_FONT_COLOR);
            nameLabel.setForeground(SELECT_FONT_COLOR);
        } else {
            visibilityLabel.setForeground(DEFAULT_FONT_COLOR);
            nameLabel.setForeground(DEFAULT_FONT_COLOR);
        }
    }
    
}
