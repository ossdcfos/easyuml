package org.uml.visual.widgets;

import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.SeparatorWidget;
import org.netbeans.api.visual.widget.Widget;
import org.uml.model.PackageComponent;
import org.uml.visual.widgets.providers.popups.PackagePopupMenuProvider;

/**
 *
 * @author Uros
 */
public class PackageWidget extends ComponentWidgetBase implements INameableWidget {

    public PackageWidget(ClassDiagramScene scene, PackageComponent packageComponent) {
        super(scene);
        this.component = packageComponent;

        Widget emptyWidget = new Widget(scene);
        emptyWidget.setLayout(LayoutFactory.createAbsoluteLayout());
        emptyWidget.setBorder(BorderFactory.createEmptyBorder(4));
        emptyWidget.setOpaque(false);
        addChild(emptyWidget);

        addChild(new SeparatorWidget(scene, SeparatorWidget.Orientation.HORIZONTAL));

        Widget packageWidget = new Widget(scene);
        packageWidget.setLayout(LayoutFactory.createVerticalFlowLayout());
//        packageWidget.setBorder(EMPTY_BORDER_6);

        nameWidget.setLabel(component.getName());
        packageWidget.addChild(nameWidget);
        addChild(packageWidget);

        getActions().addAction(ActionFactory.createPopupMenuAction(new PackagePopupMenuProvider(this)));
    }

    @Override
    public PackageComponent getComponent() {
        return (PackageComponent) component;
    }

    @Override
    public void setAttributes(String attributes) {
    }

}
