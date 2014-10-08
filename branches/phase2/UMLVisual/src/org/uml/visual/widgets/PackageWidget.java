/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets;

import javax.swing.JOptionPane;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.SeparatorWidget;
import org.netbeans.api.visual.widget.Widget;
import org.uml.model.ClassDiagramComponent;
import org.uml.model.PackageComponent;
import org.uml.visual.widgets.actions.NameEditorAction;
import org.uml.visual.widgets.providers.popups.PackagePopupMenuProvider;

/**
 *
 * @author Uros
 */
public class PackageWidget extends ComponentWidgetBase implements NameableWidget{

    PackageComponent packageComponent;
    
    private static final Border BORDER_4 = BorderFactory.createEmptyBorder(6);
    private WidgetAction nameEditorAction = ActionFactory.createInplaceEditorAction(new NameEditorAction(this));
    
    public PackageWidget(ClassDiagramScene scene,PackageComponent packageComponent) {
        super(scene);
        this.packageComponent = packageComponent;
        
        
        Widget emptyWidget = new Widget(scene);
        emptyWidget.setLayout(LayoutFactory.createAbsoluteLayout());
        emptyWidget.setOpaque(false);
        emptyWidget.setBorder(BorderFactory.createEmptyBorder(4));
        addChild(emptyWidget);
        
        addChild(new SeparatorWidget(scene, SeparatorWidget.Orientation.HORIZONTAL));
       
        Widget packageWidget = new Widget(scene);
        packageWidget.setLayout(LayoutFactory.createVerticalFlowLayout());
        packageWidget.setBorder(BORDER_4);
        
        packageWidget.addChild(nameWidget);
        addChild(packageWidget);
        
        nameWidget.getActions().addAction(nameEditorAction);
        
        
        this.nameWidget.setLabel(packageComponent.getName());
        
        getActions().addAction(ActionFactory.createPopupMenuAction(new PackagePopupMenuProvider(this)));
        
    }
    
    
    @Override
    public ClassDiagramComponent getComponent() {
        return packageComponent;
    }

    @Override
    public LabelWidget getNameLabel() {
        return nameWidget;
    }

    @Override
    public void setName(String newName) {
        
        if (getNameLabel().getLabel().equals(newName)) {
            return;
        }

        String oldName = packageComponent.getName();
        if (!packageComponent.getParentDiagram().nameExists(newName)) {
            this.nameWidget.setLabel(newName);
            packageComponent.setName(newName);
            packageComponent.getParentDiagram().componentNameChanged(packageComponent, oldName);
        }
        else {
            //WidgetAction editor = ActionFactory.createInplaceEditorAction(new LabelTextFieldEditorAction());
            //ActionFactory.getInplaceEditorController(nameEditorAction).openEditor(getNameLabel());
            JOptionPane.showMessageDialog(this.getScene().getView(), "Greska, ime vec postoji.");
        }
        
        //nije uradjeno jos uvek
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getName() {
       return nameWidget.getLabel();
    }

    @Override
    public void setAttributes(String attributes) {
       
    }
    
}
