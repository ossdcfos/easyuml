package org.uml.visual.widgets.members.unused;

import java.awt.Font;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author Boris
 */
public class AddMemberWidget extends LabelWidget {
    
    private final String memberType;

    public AddMemberWidget(Scene scene, String memberType) {
        super(scene, "");
        this.memberType = memberType;
        
        setAlignment(LabelWidget.Alignment.CENTER);
//        setBorder(EMPTY_BORDER_BOT_4);
        
        getActions().addAction(scene.createWidgetHoverAction());
        setFont(getScene().getDefaultFont().deriveFont(Font.ITALIC, 11));
    }

    @Override
    protected void notifyStateChanged(ObjectState previousState, ObjectState state) {
        super.notifyStateChanged(previousState, state);
        Widget parent = getParentWidget(); // parent is component widget
        if (state.isHovered()) {
            parent.setState(parent.getState().deriveWidgetHovered(true));
            setLabel("Click to add a "+memberType);
        } else {
            parent.setState(parent.getState().deriveWidgetHovered(false));
            setLabel("");
        }
    }
}
