package org.uml.visual.widgets.members;

import java.awt.Color;
import java.awt.Point;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;
import org.uml.model.members.Member;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.INameableWidget;

/**
 *
 * @author Jelena
 */
public abstract class MemberWidgetBase extends LabelWidget implements INameableWidget {

    protected static final Border DEFAULT_BORDER = BorderFactory.createEmptyBorder(1);
    protected static final Border HOVER_BORDER = BorderFactory.createLineBorder(1, Color.GRAY);
    protected static final Border SELECT_BORDER = BorderFactory.createLineBorder(1, Color.ORANGE);

    protected static final Color DEFAULT_COLOR = new Color(0, 0, 0, 1);
    protected static final Color HOVER_COLOR = new Color(0xD4DCFF);
    protected static final Color SELECT_COLOR = Color.ORANGE;//new Color(0, 0, 0, 1);

    public MemberWidgetBase(ClassDiagramScene scene) {
        super(scene);
        setOpaque(true);
        setBackground(DEFAULT_COLOR);
        // To support hovering and selecting (in notifyStateChanged), otherwise a Provider is needed
        getActions().addAction(scene.createWidgetHoverAction());
        //getActions().addAction(scene.createSelectAction());
        getActions().addAction(scene.createSelectAction());
        getActions().addAction(ActionFactory.createSelectAction(new SelectProvider() {
            Widget prev = null;

            @Override
            public boolean isAimingAllowed(Widget widget, Point localLocation, boolean invertSelection) {
                return false;
            }

            @Override
            public boolean isSelectionAllowed(Widget widget, Point localLocation, boolean invertSelection) {
                return true;
            }

            @Override
            public void select(Widget widget, Point localLocation, boolean invertSelection) {
                if (invertSelection) widget.setBackground(DEFAULT_COLOR);
                else widget.setBackground(SELECT_COLOR);
//                if(prev != null && prev instanceof MemberWidgetBase) prev.setBackground(DEFAULT_COLOR);
//                prev = widget;
            }
        }));
    }

    public Member getMember() {
        return null;
    }

    abstract public LabelWidget getNameLabel();

    @Override
    public void notifyStateChanged(ObjectState previousState, ObjectState state) {
        // u ovu metodu ubaciti reakcija ne hover, focus, selected itd.
        //super.notifyStateChanged(previousState, state);

        if (previousState.equals(state)) return;

//        if(state.isHovered()){
//            setBackground(HOVER_COLOR);
//        } else {
//            setBackground(DEFAULT_COLOR);
//        }
//        if (state.isSelected()) {
//            setBackground(SELECT_COLOR);
//        }
//
//        if (state.isFocused()) {
//            setBackground(Color.PINK);
//        }
        boolean selected = state.isFocused();

        if (state.isHovered()) {
            if (!selected) {
                setBorder(HOVER_BORDER);
                setBackground(HOVER_COLOR);
            } else {
                setBorder(SELECT_BORDER);
                setBackground(SELECT_COLOR);
            }
            Widget parent = getParentWidget().getParentWidget();
            parent.setState(parent.getState().deriveWidgetHovered(true));
        } else {
            if (!selected) {
                setBorder(DEFAULT_BORDER);
                setBackground(DEFAULT_COLOR);
            } else {
                setBorder(SELECT_BORDER);
                setBackground(SELECT_COLOR);
            }
        }
    }

}
