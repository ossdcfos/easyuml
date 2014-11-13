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
    protected static final Border SELECT_BORDER = BorderFactory.createLineBorder(1, new Color(0x0000A1)); //0x0096FF33

    protected static final Color DEFAULT_COLOR = new Color(0, 0, 0, 1);
    protected static final Color HOVER_COLOR = new Color(0xD4DCFF);
    protected static final Color SELECT_COLOR = new Color(0x00, 0x00, 0xA1, 0x4D);

    protected static final Color DEFAULT_FONT_COLOR = Color.BLACK;
    protected static final Color SELECT_FONT_COLOR = new Color(0xFFFFFF);

    public MemberWidgetBase(ClassDiagramScene scene, Member member) {
        super(scene);
        scene.addObject(member, this);
        setOpaque(true);
        setBackground(DEFAULT_COLOR);
        setBorder(DEFAULT_BORDER);
        // To support hovering and selecting (in notifyStateChanged), otherwise a Provider is needed
        getActions().addAction(scene.createWidgetHoverAction());
        getActions().addAction(scene.createSelectAction());
    }

    public Member getMember() {
        return null;
    }

    public ClassDiagramScene getClassDiagramScene() {
        return (ClassDiagramScene) getScene();
    }

    abstract public LabelWidget getNameLabel();

    @Override
    public void notifyStateChanged(ObjectState previousState, ObjectState state) {
        // u ovu metodu ubaciti reakcija ne hover, focus, selected itd.
        super.notifyStateChanged(previousState, state);

        // in case it has not yet been initialized (adding to the scene calls notifyStateChanged, before the full object has been initialized)
        if(getParentWidget() == null) return;

        boolean selected = state.isFocused();
        Widget parent = getParentWidget().getParentWidget();
        parent.setState(parent.getState().deriveWidgetHovered(false));

        if (selected) {
            setSelected(true);
            setBorder(SELECT_BORDER);
            setBackground(SELECT_COLOR);
            parent.setState(parent.getState().deriveSelected(true));
        } else {
            setBorder(DEFAULT_BORDER);
            setBackground(DEFAULT_COLOR);
            setSelected(false);
        }

        if (state.isHovered()) {
            if (!selected) {
                setBorder(HOVER_BORDER);
                setBackground(HOVER_COLOR);
            }
            parent.setState(parent.getState().deriveWidgetHovered(true));
        }
    }

    protected abstract void setSelected(boolean isSelected);

}
