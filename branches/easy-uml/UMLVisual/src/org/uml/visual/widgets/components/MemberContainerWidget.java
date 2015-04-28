package org.uml.visual.widgets.components;

import java.awt.Font;
import java.util.concurrent.Callable;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.EditProvider;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.Exceptions;
import org.uml.visual.themes.Theme;
import org.uml.visual.widgets.ClassDiagramScene;
import static org.uml.visual.widgets.components.ComponentWidgetBase.ADD_LABEL_FONT_SIZE;
import static org.uml.visual.widgets.components.ComponentWidgetBase.EMPTY_CONTAINER_BORDER;
import org.uml.visual.widgets.members.ConstructorWidget;
import org.uml.visual.widgets.members.MemberWidgetBase;

/**
 *
 * @author Boris Perović
 */
public class MemberContainerWidget extends Widget {

    private final LabelWidget addMemberLabel;
    private final String memberType;
    private final LabelWidget emptyLabel = new LabelWidget(getScene(), "");

    public MemberContainerWidget(ClassDiagramScene scene, String memberType) {
        super(scene);
        this.memberType = memberType;

        setLayout(LayoutFactory.createVerticalFlowLayout());
        setBorder(EMPTY_CONTAINER_BORDER);

        addMemberLabel = new LabelWidget(scene, "") {

            @Override
            protected void notifyStateChanged(ObjectState previousState, ObjectState state) {
                super.notifyStateChanged(previousState, state);

                Widget componentWidget = addMemberLabel.getParentWidget().getParentWidget(); // grandparent is component widget

                if (state.isHovered()) {
                    addMemberLabel.setLabel("Double-click to add " + MemberContainerWidget.this.memberType);
                    addMemberLabel.setBackground(getColorTheme().getAddMemberHoverColor());
                    addMemberLabel.setBorder(getColorTheme().getAddMemberHoverBorder());
                    componentWidget.setState(componentWidget.getState().deriveWidgetHovered(true));
                } else {
                    addMemberLabel.setLabel("");
                    addMemberLabel.setBackground(getColorTheme().getAddMemberDefaultColor());
                    addMemberLabel.setBorder(getColorTheme().getAddMemberDefaultBorder());
                    componentWidget.setState(componentWidget.getState().deriveWidgetHovered(false));
                }
            }
        };
        addMemberLabel.getActions().addAction(scene.createWidgetHoverAction());
        addMemberLabel.setOpaque(true);
        addMemberLabel.setBackground(getColorTheme().getAddMemberDefaultColor());
        addMemberLabel.setBorder(getColorTheme().getAddMemberDefaultBorder());
        addMemberLabel.setAlignment(LabelWidget.Alignment.CENTER);
        addMemberLabel.setFont(getScene().getFont().deriveFont(Font.ITALIC, ADD_LABEL_FONT_SIZE));
        addChild(addMemberLabel);
        
        updateMemberDisplay(scene.isShowMembers());
    }

    public void addAddAction(final Callable<Void> function) {
        addMemberLabel.getActions().addAction(ActionFactory.createEditAction(new EditProvider() {
            @Override
            public void edit(Widget widget) {
                try {
                    function.call();
                } catch (Exception ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }));
    }
    
    // already has getScene in widget, but this is casted, so it's easier
    public ClassDiagramScene getClassDiagramScene() {
        return (ClassDiagramScene) getScene();
    }

    public final Theme getColorTheme() {
        return getClassDiagramScene().getColorTheme();
    }

    public void addMemberWidget(MemberWidgetBase memberWidget) {
        ComponentWidgetBase component = (ComponentWidgetBase) this.getParentWidget();
        if (component instanceof ClassWidget && memberWidget instanceof ConstructorWidget) {
            int index = 0;
            for (Widget methodOrConstructorWidget : this.getChildren()) {
                if (!(methodOrConstructorWidget instanceof ConstructorWidget)) {
                    break;
                } else index++;
            }
            addChild(index, memberWidget);
        } else {
            addChild(getChildren().size() - 1, memberWidget); // add so it's 1 before "add" LabelWidget
        }
    }

    void updateColor() {
        for (Widget widget : getChildren()) {
            if (widget instanceof MemberWidgetBase) {
                MemberWidgetBase memberWidget = (MemberWidgetBase) widget;
                memberWidget.updateColor();
            }
        }
    }

    final void updateIconDisplay(boolean iconDisplayEnabled) {
        for (Widget widget : getChildren()) {
            if (widget instanceof MemberWidgetBase) {
                MemberWidgetBase memberWidget = (MemberWidgetBase) widget;
                memberWidget.updateIconDisplay(iconDisplayEnabled);
            }
        }
    }

    final void updateMemberDisplay(boolean memberDisplayEnabled) {
        if (getChildren().contains(emptyLabel)) {
            removeChild(emptyLabel);
        }
        for (Widget widget : getChildren()) {
            widget.setVisible(memberDisplayEnabled);
        }
        if (!memberDisplayEnabled) addChild(emptyLabel);
    }

    void updateTypeNamesDisplay(boolean simpleTypeNamesDisplayEnabled) {
        for (Widget widget : getChildren()) {
            if (widget instanceof MemberWidgetBase) {
                MemberWidgetBase memberWidget = (MemberWidgetBase) widget;
                memberWidget.updateTypeNamesDisplay(simpleTypeNamesDisplayEnabled);
            }
        }
    }
}
