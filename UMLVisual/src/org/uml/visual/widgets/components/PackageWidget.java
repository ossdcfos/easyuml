package org.uml.visual.widgets.components;

import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.widget.SeparatorWidget;
import org.uml.model.components.PackageComponent;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.popups.EnumPopupMenuProvider;
import org.uml.visual.widgets.popups.PackagePopupMenuProvider;

/**
 *
 * @author NUGS
 */
public class PackageWidget extends ComponentWidgetBase {

    private final MemberContainerWidget literalsContainer;

    public PackageWidget(ClassDiagramScene scene, PackageComponent packageComponent) {
        super(scene, packageComponent);
        setMinimumSize(MIN_DIMENSION_2ROW);
        
        iconNameContainer.addChild(iconWidget);
        iconNameContainer.addChild(nameLabel);
        headerWidget.addChild(iconNameContainer);
        addChild(headerWidget);

        SeparatorWidget separatorWidget = new SeparatorWidget(scene, SeparatorWidget.Orientation.HORIZONTAL);
        separatorWidget.setForeground(getColorTheme().getDefaultBorderColor());
        separators.add(separatorWidget);
        addChild(separatorWidget);

        literalsContainer = new MemberContainerWidget(scene, "literal");

        this.nameLabel.setLabel(component.getName());

        getActions().addAction(ActionFactory.createPopupMenuAction(new PackagePopupMenuProvider(this)));
        
//        setMaximumSize(new Dimension(Integer.MAX_VALUE, MIN_DIMENSION_2ROW.height + (getLabelSizeForFont(ADD_LABEL_FONT_SIZE) + 2*EMPTY_BORDER_SIZE)));

    }

    @Override
    public final PackageComponent getComponent() {
        return (PackageComponent) component;
    }
    
}
