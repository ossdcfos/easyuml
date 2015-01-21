package org.uml.visual.widgets.components;

import java.awt.Color;
import java.awt.Font;
import java.util.concurrent.Callable;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.EditProvider;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.border.BorderFactory;
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

    /* Visual */
    protected static final Border DEFAULT_BORDER = BorderFactory.createEmptyBorder(1);
    protected static final Border HOVER_BORDER = BorderFactory.createLineBorder(1, Color.GRAY);
//    protected static final Border SELECT_BORDER = BorderFactory.createLineBorder(1, new Color(0x0000A1)); //0x0096FF33

    protected static final Color DEFAULT_COLOR = new Color(0, 0, 0, 1);
    protected static final Color HOVER_COLOR = new Color(0xD4DCFF);
//    protected static final Color SELECT_COLOR = new Color(0x00, 0x00, 0xA1, 0x4D);

//    protected static final Color DEFAULT_FONT_COLOR = Color.BLACK;
//    protected static final Color SELECT_FONT_COLOR = new Color(0xFFFFFF);
    /* End Visual */
    private final LabelWidget addMemberLabel;
    private final String memberType;

    public MemberContainerWidget(Scene scene, String memberType) {
        super(scene);
        this.memberType = memberType;

        setLayout(LayoutFactory.createVerticalFlowLayout());
        setOpaque(false);
        setBorder(EMPTY_CONTAINER_BORDER);

        addMemberLabel = new LabelWidget(scene, "") {

            @Override
            protected void notifyStateChanged(ObjectState previousState, ObjectState state) {
                super.notifyStateChanged(previousState, state);
                
                Widget componentWidget = addMemberLabel.getParentWidget().getParentWidget(); // grandparent is component widget
                
                if (state.isHovered()) {
                    addMemberLabel.setLabel("Dclick to add " + MemberContainerWidget.this.memberType);
//                    addMemberLabel.setBackground(HOVER_COLOR);
//                    addMemberLabel.setBorder(HOVER_BORDER);
                    componentWidget.setState(componentWidget.getState().deriveWidgetHovered(true));
                } else {
                    addMemberLabel.setLabel("");
//                    addMemberLabel.setBackground(DEFAULT_COLOR);
//                    addMemberLabel.setBorder(DEFAULT_BORDER);
                    componentWidget.setState(componentWidget.getState().deriveWidgetHovered(false));
                }
            }
        };
        addMemberLabel.getActions().addAction(scene.createWidgetHoverAction());
        
//        addMemberLabel.setOpaque(true);
//        addMemberLabel.setBackground(DEFAULT_COLOR);
        addMemberLabel.setBorder(DEFAULT_BORDER);
        addMemberLabel.setAlignment(LabelWidget.Alignment.CENTER);
        addMemberLabel.setFont(getScene().getDefaultFont().deriveFont(Font.ITALIC, ADD_LABEL_FONT_SIZE));
        addChild(addMemberLabel);
    }

    public void addAddAction(final Callable<Void> function) {
        getActions().addAction(ActionFactory.createEditAction(new EditProvider() {
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

    public void addMemberWidget(MemberWidgetBase memberWidget) {
        addChild(getChildren().size() - 1, memberWidget); // add so it's 1 before "add" LabelWidget
    }
}
