package org.uml.visual.widgets.components;

import org.uml.visual.widgets.members.MemberWidgetBase;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import javax.swing.JOptionPane;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.MoveProvider;
import org.netbeans.api.visual.action.MoveStrategy;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.SeparatorWidget;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.WeakListeners;
import org.uml.model.components.ComponentBase;
import org.uml.visual.colorthemes.ColorTheme;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.actions.ComponentNameEditor;
import org.uml.visual.widgets.actions.ComponentWidgetKeyboardAction;
import org.uml.visual.widgets.providers.ComponentConnectProvider;

/**
 *
 * @author "NUGS"
 */
abstract public class ComponentWidgetBase extends Widget implements PropertyChangeListener {

    protected ComponentBase component;
    protected LabelWidget nameLabel;

    protected LinkedList<SeparatorWidget> separators = new LinkedList<>();

    public static final int EMPTY_BORDER_SIZE = 5;
    public static final int SELECT_BORDER_SIZE = 5;
    protected static final int OUTER_BORDER_SIZE = 2 * EMPTY_BORDER_SIZE + 2 * (SELECT_BORDER_SIZE + 1);

    protected static final int DEFAULT_LABEL_FONT_SIZE = 12;
    protected static final int ADD_LABEL_FONT_SIZE = 11;

    public static int getLabelSizeForFont(int fontSize) {
        return fontSize + 5;
    }

    protected static final int MIN_WIDTH = 170;
    protected static final Dimension MIN_DIMENSION_1ROW = new Dimension(MIN_WIDTH, OUTER_BORDER_SIZE + getLabelSizeForFont(DEFAULT_LABEL_FONT_SIZE));
    protected static final Dimension MIN_DIMENSION_2ROW = new Dimension(MIN_WIDTH, OUTER_BORDER_SIZE + 2 * getLabelSizeForFont(DEFAULT_LABEL_FONT_SIZE));

    // Borders are +1, because dashed line from resize falls into the widget (thickness of resize border is RESIZE_SIZE+1)
//    public static Border DEFAULT_BORDER = new TranslucentCompositeBorder(BorderFactory.createEmptyBorder(SELECT_BORDER_SIZE), BorderFactory.createLineBorder());
//    public static Border HOVER_BORDER = new TranslucentCompositeBorder(BorderFactory.createEmptyBorder(SELECT_BORDER_SIZE), BorderFactory.createLineBorder(1, getColorTheme().getHoverBorderColor()));
//    public static Border SELECT_BORDER = new TranslucentCompositeBorder(BorderFactory.createResizeBorder(SELECT_BORDER_SIZE, getColorTheme().getSelectBorderColor(), false), BorderFactory.createEmptyBorder(1));
    public static final Border EMPTY_CONTAINER_BORDER = BorderFactory.createEmptyBorder(EMPTY_BORDER_SIZE);

    public ComponentWidgetBase(final ClassDiagramScene scene, ComponentBase component) {
        super(scene);
        this.component = component;
        this.component.addPropertyChangeListener(WeakListeners.propertyChange(this, this.component));
        this.component.setParentDiagram(scene.getClassDiagram());

        // Layout
        setBorder(getColorTheme().getDefaultBorder());
        setLayout(LayoutFactory.createVerticalFlowLayout());
        setOpaque(true);
        setCheckClipping(true);
        setBackground(getColorTheme().getDefaultColor());

        nameLabel = new LabelWidget(scene);
        nameLabel.setLabel(component.getName());
        nameLabel.setFont(scene.getDefaultFont().deriveFont(Font.BOLD));
        nameLabel.setAlignment(LabelWidget.Alignment.CENTER);
        nameLabel.getActions().addAction(ActionFactory.createInplaceEditorAction(new ComponentNameEditor(this)));

        // **** Actions ****
        // Connect action - CTRL + click
        getActions().addAction(ActionFactory.createExtendedConnectAction(scene.getInterractionLayer(), new ComponentConnectProvider()));
        // Keybord actions over components
        getActions().addAction(new ComponentWidgetKeyboardAction());
        // Hover, select and resize
        getActions().addAction(scene.createSelectAction());
        getActions().addAction(ActionFactory.createAlignWithResizeAction(scene.getMainLayer(), scene.getInterractionLayer(), null, false));
        getActions().addAction(ActionFactory.createAlignWithMoveAction(scene.getMainLayer(), scene.getInterractionLayer(), null, false));
//        MoveModifyProvider mmp = new MoveModifyProvider();
//        getActions().addAction(ActionFactory.createMoveAction(null, mmp));
//        getActions().addAction(scene.createWidgetHoverAction());

//        // TODO: Change detection - check how this works
//        // Too slow, should find another solution
//        addDependency(new Dependency() {
//            @Override
//            public void revalidateDependency() {
//                Point widgetBounds = getPreferredLocation();
//                int x = widgetBounds.x;
//                int y = widgetBounds.y;
//                        
//                Rectangle bounds = scene.getBounds();
//                int leftBound = bounds.x;
//                int topBound = bounds.y;
//                int rightBound = bounds.x + bounds.width;
//                int bottomBound = bounds.y + bounds.height;
//                
//                if(x <= leftBound) x = leftBound;
//                if(y <= topBound) y = topBound;
//                if(x >= rightBound - 10) x = rightBound - 10;
//                if(y >= bottomBound - 10) y = bottomBound - 10;
//                
//                if(x != widgetBounds.x || y != widgetBounds.y) setPreferredLocation(new Point(x, y));
//            }
//        });
    }

    class MoveModifyProvider implements MoveProvider {

        @Override
        public void movementStarted(Widget widget) {
        }

        @Override
        public void movementFinished(Widget widget) {
            getClassDiagramScene().getUmlTopComponent().notifyModified();
        }

        @Override
        public Point getOriginalLocation(Widget widget) {
            return null;
        }

        @Override
        public void setNewLocation(Widget widget, Point location) {
        }
    }

    //    @Override
//    public Lookup getLookup() {
//        return Lookups.fixed(this, this.getScene());
//    }
    @Override
    public void notifyStateChanged(ObjectState previousState, ObjectState state) {
        // Reaction to hover, focus and selection goes here
        super.notifyStateChanged(previousState, state);

        boolean focused = state.isFocused();
        boolean hovered = state.isHovered();

        if (focused) {
            setBorder(getColorTheme().getSelectBorder());
            if (hovered) setBackground(getColorTheme().getHoverSelectColor());
            else setBackground(getColorTheme().getSelectColor());
            for (SeparatorWidget separator : separators) {
                separator.setForeground(getColorTheme().getSelectBorderColor());
            }
        } else if (hovered) {
            setBorder(getColorTheme().getHoverBorder());
            setBackground(getColorTheme().getHoverColor());
            for (SeparatorWidget separator : separators) {
                separator.setForeground(getColorTheme().getHoverBorderColor());
            }
        } else {
            setBorder(getColorTheme().getDefaultBorder());
            setBackground(getColorTheme().getDefaultColor());
            for (SeparatorWidget separator : separators) {
                separator.setForeground(getColorTheme().getDefaultBorderColor());
            }
        }
    }

    // used for InplaceEditorAction
    public LabelWidget getNameLabel() {
        return nameLabel;
    }

    public final String getName() {
        return component.getName();
    }

    public void setName(String newName) {
        if (!getName().equals(newName)) {
            if (component.getParentDiagram().signatureExists(component.getParentPackage() + "." + newName)) {
                JOptionPane.showMessageDialog(getScene().getView(), "Name \"" + component.getParentPackage() + "." + newName + "\" already exists!");
            } else {
                component.setName(newName);
            }
        }
    }

    public static final ColorTheme getColorTheme() {
        return ClassDiagramScene.colorTheme;
    }

    public void updateColor() {
        notifyStateChanged(getState(), getState());
        for (Widget widget : getChildren()) {
            if (widget instanceof MemberContainerWidget) {
                MemberContainerWidget containerWidget = (MemberContainerWidget) widget;
                containerWidget.updateColor();
            }
        }
    }

    // already has getScene in widget, but this is casted, so it's easier
    public ClassDiagramScene getClassDiagramScene() {
        return (ClassDiagramScene) getScene();
    }

    protected void addMember(MemberContainerWidget container, MemberWidgetBase member) {
        container.addMemberWidget(member);
//        setMaximumSize(new Dimension(getMaximumSize().width, getMaximumSize().height + getLabelSizeForFont(DEFAULT_LABEL_FONT_SIZE) + 2 * member.getBorder().getInsets().bottom));
        notifyTopComponentModified();
    }

    protected void removeMember(MemberContainerWidget container, MemberWidgetBase member) {
        container.removeChild(member);
//        setMaximumSize(new Dimension(getMaximumSize().width, getMaximumSize().height - getLabelSizeForFont(DEFAULT_LABEL_FONT_SIZE) - 2 * member.getBorder().getInsets().bottom));
        notifyTopComponentModified();
    }

    protected void notifyTopComponentModified() {
        getClassDiagramScene().getUmlTopComponent().notifyModified();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("name".equals(evt.getPropertyName())) {
            String newName = (String) evt.getNewValue();
            nameLabel.setLabel(newName);
        }
        notifyTopComponentModified();
        getScene().validate();
    }

    abstract public ComponentBase getComponent();

    @Override
    // To achieve resize cursors and move cursor
    protected Cursor getCursorAt(Point location) {
        if (getState().isFocused()) {
            Border border = getBorder();
            Rectangle bounds = getBounds();
            Insets insets = border.getInsets();
            int thickness = insets.bottom;

            Rectangle topLeft = new Rectangle(bounds.x, bounds.y, thickness, thickness);
            Rectangle topRight = new Rectangle(bounds.x + bounds.width - thickness, bounds.y, thickness, thickness);
            Rectangle bottomLeft = new Rectangle(bounds.x, bounds.y + bounds.height - thickness, thickness, thickness);
            Rectangle bottomRight = new Rectangle(bounds.x + bounds.width - thickness, bounds.y + bounds.height - thickness, thickness, thickness);

            Point center = new Point(bounds.x + bounds.width / 2, bounds.y + bounds.height / 2);

            Rectangle topCenter = new Rectangle(center.x - thickness / 2, bounds.y, thickness, thickness);
            Rectangle bottomCenter = new Rectangle(center.x - thickness / 2, bounds.y + bounds.height - thickness, thickness, thickness);
            Rectangle leftCenter = new Rectangle(bounds.x, center.y - thickness / 2, thickness, thickness);
            Rectangle rightCenter = new Rectangle(bounds.x + bounds.width - thickness, center.y - thickness / 2, thickness, thickness);

            Rectangle[] rects = new Rectangle[]{
                topLeft,
                topRight,
                bottomLeft,
                bottomRight,
                topCenter,
                bottomCenter,
                leftCenter,
                rightCenter
            };

            Cursor[] cursors = new Cursor[]{
                Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR),
                Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR),
                Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR),
                Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR),
                Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR),
                Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR),
                Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR),
                Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR)
            };

            for (int i = 0; i < rects.length; i++) {
                if (rects[i].contains(location)) {
                    return cursors[i];
                }
            }
            return Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
        } else {
            return Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
        }
    }

    // Used for representation in combo boxes in ConnectRelationPanel dialog
    @Override
    public String toString() {
        return nameLabel.getLabel();
    }
}
