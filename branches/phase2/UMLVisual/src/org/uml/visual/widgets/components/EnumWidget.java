package org.uml.visual.widgets.components;

import java.awt.event.MouseListener;
import java.util.concurrent.Callable;
import org.uml.visual.widgets.members.LiteralWidget;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.SeparatorWidget;
import org.netbeans.api.visual.widget.Widget;
import org.uml.model.components.EnumComponent;
import org.uml.model.members.Literal;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.actions.MemberNameEditor;
import org.uml.visual.widgets.popups.EnumPopupMenuProvider;
import org.uml.visual.widgets.providers.CloseInplaceEditorOnClickAdapter;

/**
 *
 * @author "NUGS"
 */
public class EnumWidget extends ComponentWidgetBase {

    private final MemberContainerWidget literalsContainer;

    public EnumWidget(ClassDiagramScene scene, EnumComponent enumComponent) {
        super(scene, enumComponent);
        setMinimumSize(MIN_DIMENSION_2ROW);

        Widget headerWidget = new Widget(scene);
        headerWidget.setLayout(LayoutFactory.createVerticalFlowLayout());
        headerWidget.setBorder(EMPTY_CONTAINER_BORDER);
        
        LabelWidget enumerationLabel = new LabelWidget(scene, "<<enumeration>>");
        enumerationLabel.setAlignment(LabelWidget.Alignment.CENTER);
        headerWidget.addChild(enumerationLabel);
        
        headerWidget.addChild(nameLabel);
        addChild(headerWidget);

        SeparatorWidget separatorWidget = new SeparatorWidget(scene, SeparatorWidget.Orientation.HORIZONTAL);
        separatorWidget.setForeground(getColorTheme().getDefaultBorderColor());
        separators.add(separatorWidget);
        addChild(separatorWidget);

        literalsContainer = new MemberContainerWidget(scene, "literal");
        literalsContainer.addAddAction(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                addLiteralWidget();
                return null;
            }
        });
        addChild(literalsContainer);

        this.nameLabel.setLabel(component.getName());

        getActions().addAction(ActionFactory.createPopupMenuAction(new EnumPopupMenuProvider(this)));
        
//        setMaximumSize(new Dimension(Integer.MAX_VALUE, MIN_DIMENSION_2ROW.height + (getLabelSizeForFont(ADD_LABEL_FONT_SIZE) + 2*EMPTY_BORDER_SIZE)));

        for (Literal literal : getComponent().getLiterals()) {
            LiteralWidget literalWidget = new LiteralWidget(scene, literal);
            addMember(literalsContainer, literalWidget);
        }
    }

    @Override
    public final EnumComponent getComponent() {
        return (EnumComponent) component;
    }
    
    public final void addLiteralWidget() {
        Literal literal = new Literal("LITERAL");
        getComponent().addLiteral(literal);
        LiteralWidget literalWidget = new LiteralWidget(getClassDiagramScene(), literal);
        addMember(literalsContainer, literalWidget);
        getScene().validate();

        WidgetAction nameEditorAction = ActionFactory.createInplaceEditorAction(new MemberNameEditor(literalWidget));
        ActionFactory.getInplaceEditorController(nameEditorAction).openEditor(literalWidget.getNameLabel());
        MouseListener mouseListener = new CloseInplaceEditorOnClickAdapter(nameEditorAction);
        getScene().getView().addMouseListener(mouseListener);
    }
}
