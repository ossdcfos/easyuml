package org.uml.visual.widgets.components.unused;

import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.SeparatorWidget;
import org.netbeans.api.visual.widget.Widget;
import org.uml.model.components.PackageComponent;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.ISignedUMLWidget;
import org.uml.visual.widgets.components.ComponentWidgetBase;
import org.uml.visual.widgets.popups.PackagePopupMenuProvider;

/**
 *
 * @author Uros
 */
// TODO use or delete
public class PackageWidget extends ComponentWidgetBase implements ISignedUMLWidget {

    public PackageWidget(ClassDiagramScene scene, PackageComponent packageComponent) {
        super(scene, packageComponent);

        Widget emptyWidget = new Widget(scene);
        emptyWidget.setLayout(LayoutFactory.createAbsoluteLayout());
        emptyWidget.setBorder(BorderFactory.createEmptyBorder(4));
        emptyWidget.setOpaque(false);
        addChild(emptyWidget);

        addChild(new SeparatorWidget(scene, SeparatorWidget.Orientation.HORIZONTAL));

        Widget packageWidget = new Widget(scene);
        packageWidget.setLayout(LayoutFactory.createVerticalFlowLayout());
//        packageWidget.setBorder(EMPTY_BORDER_6);

        nameLabel.setLabel(component.getName());
        packageWidget.addChild(nameLabel);
        addChild(packageWidget);

        getActions().addAction(ActionFactory.createPopupMenuAction(new PackagePopupMenuProvider(this)));
    }

    @Override
    public PackageComponent getComponent() {
        return (PackageComponent) component;
    }
    @Override
    public void setSignature(String signature) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getSignature() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
