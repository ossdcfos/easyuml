package org.uml.visual.widgets.members;

import java.awt.Color;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;
import org.uml.model.members.Member;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.ComponentWidgetBase;
import org.uml.visual.widgets.INameableWidget;

/**
 *
 * @author Jelena
 */
public abstract class MemberWidgetBase extends LabelWidget /*IconNodeWidget*/ implements INameableWidget {

    protected static final Border DEFAULT_BORDER = BorderFactory.createEmptyBorder(1);
    protected static final Border HOVER_BORDER = BorderFactory.createLineBorder(1, Color.GRAY);
    protected static final Border SELECT_BORDER = BorderFactory.createLineBorder(1, Color.BLUE);

    public MemberWidgetBase(ClassDiagramScene scene) {
        super(scene);
        setOpaque(true);
        setBackground(new Color(0, 0, 0, 1));
        // To support hovering (in notifyStateChanged), otherwise a Provider is needed
        getActions().addAction(scene.createWidgetHoverAction());
    }

    public Member getMember() {
        return null;
    }

    abstract public LabelWidget getNameLabel();

    @Override
    public void notifyStateChanged(ObjectState previousState, ObjectState state) {
        // u ovu metodu ubaciti reakcija ne hover, focus, selected itd.
        super.notifyStateChanged(previousState, state);

        if (state.isHovered()) {
            setBorder(HOVER_BORDER);
            setBackground(new Color(0xD4DCFF));
            Widget parent = getParentWidget().getParentWidget();
            parent.setBackground(new Color(0xF0F0F0));
            if (!parent.getState().isFocused()) {
                parent.setBorder(ComponentWidgetBase.HOVER_BORDER);
                parent.setBackground(new Color(0xF0F0F0));
            } else {
                parent.setBorder(ComponentWidgetBase.RESIZE_BORDER);
                parent.setBackground(new Color(0xF0F0F0));
            }
        } else {
            setBorder(DEFAULT_BORDER);
            setBackground(new Color(0, 0, 0, 1));
        }
    }

}
