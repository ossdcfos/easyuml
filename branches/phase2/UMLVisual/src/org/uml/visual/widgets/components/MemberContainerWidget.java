package org.uml.visual.widgets.components;

import java.awt.Font;
import java.awt.Point;
import java.util.concurrent.Callable;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.EditProvider;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.Exceptions;
import static org.uml.visual.widgets.components.ComponentWidgetBase.ADD_LABEL_FONT_SIZE;
import static org.uml.visual.widgets.components.ComponentWidgetBase.EMPTY_CONTAINER_BORDER;
import org.uml.visual.widgets.members.MemberWidgetBase;

/**
 *
 * @author Boris
 */
public class MemberContainerWidget extends Widget {

    private final LabelWidget addMemberLabel;
    private final String memberType;

    public MemberContainerWidget(Scene scene, String memberType) {
        super(scene);
        this.memberType = memberType;
        
        setLayout(LayoutFactory.createVerticalFlowLayout());
        setOpaque(false);
        setBorder(EMPTY_CONTAINER_BORDER);
        getActions().addAction(scene.createWidgetHoverAction());
        
        addMemberLabel = new LabelWidget(scene, "");
        addMemberLabel.setAlignment(LabelWidget.Alignment.CENTER);
        addMemberLabel.setFont(getScene().getDefaultFont().deriveFont(Font.ITALIC, ADD_LABEL_FONT_SIZE));
        addChild(addMemberLabel);
    }
    
    public void addAddAction(final Callable<Void> function){
        getActions().addAction(ActionFactory.createSelectAction(new SelectProvider() {

            @Override
            public boolean isAimingAllowed(Widget widget, Point localLocation, boolean invertSelection) {
                return true;
            }

            @Override
            public boolean isSelectionAllowed(Widget widget, Point localLocation, boolean invertSelection) {
                return true;
            }

            @Override
            public void select(Widget widget, Point localLocation, boolean invertSelection) {
                try {
                    function.call();
                } catch (Exception ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }));
    }

    public void addMemberWidget(MemberWidgetBase memberWidget) {
        addChild(getChildren().size()-1, memberWidget); // add so it's 1 before "add" LabelWidget
    }

    @Override
    protected void notifyStateChanged(ObjectState previousState, ObjectState state) {
        super.notifyStateChanged(previousState, state);
        Widget componentWidget = getParentWidget(); // grandparent is component widget
        if (state.isHovered()) {
            componentWidget.setState(componentWidget.getState().deriveWidgetHovered(true));
            addMemberLabel.setLabel("Add a " + memberType);
        } else {
            componentWidget.setState(componentWidget.getState().deriveWidgetHovered(false));
            addMemberLabel.setLabel("");
        }
    }
}
