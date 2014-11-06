package org.uml.visual.widgets;

import org.uml.visual.widgets.members.MemberWidgetBase;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JOptionPane;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;
import org.openide.explorer.ExplorerManager;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.uml.explorer.ClassDiagramComponentNode;
import org.uml.explorer.ExplorerTopComponent;
import org.uml.model.ClassDiagramComponent;
import org.uml.visual.widgets.actions.ComponentWidgetKeyboardAction;
import org.uml.visual.widgets.actions.NameEditorAction;
import org.uml.visual.widgets.providers.ComponentConnectProvider;

/**
 *
 * @author "NUGS"
 */
// doesn't have to be ImageWidget
abstract public class ComponentWidgetBase extends Widget implements INameableWidget, Lookup.Provider {

    protected ClassDiagramComponent component;

    // attribute name
    protected LabelWidget nameWidget;
    private static final Dimension MIN_DIMENSION = new Dimension(120, 120);
    protected static final Dimension CONTAINER_MIN_DIMENSION = new Dimension(110, 50);

    private static final Border DEFAULT_BORDER = BorderFactory.createLineBorder();
    private static final Border HOVER_BORDER = BorderFactory.createLineBorder(1, Color.BLACK);

    private static final int RESIZE_SIZE = 5;
    private static final Border RESIZE_BORDER = BorderFactory.createResizeBorder(RESIZE_SIZE, Color.black, false);
//    private static final Border RESIZE_BORDER = new ResizeBorder(RESIZE_SIZE, Color.BLACK, getResizeControlPoints());
//    public static ResizeBorder NON_RESIZABLE_BORDER = new ResizeBorder(5, Color.BLACK, new ResizeProvider.ControlPoint[]{});
    
    protected static final Border EMPTY_BORDER_6 = BorderFactory.createEmptyBorder(6);

    protected WidgetAction nameEditorAction = ActionFactory.createInplaceEditorAction(new NameEditorAction(this));
    
    public ComponentWidgetBase(ClassDiagramScene scene) {
        super(scene);

        // Layout
        setBorder(DEFAULT_BORDER);
        setMinimumSize(MIN_DIMENSION);
//        setChildConstraint(this, 1); // getImageWidget()
        setLayout(LayoutFactory.createVerticalFlowLayout());
//        setBorder(BorderFactory.createLineBorder());
        setOpaque(true);
        setCheckClipping(true);

        // Actions
        getActions().addAction(ActionFactory.createExtendedConnectAction(scene.getInterractionLayer(), new ComponentConnectProvider()));

        getActions().addAction(new ComponentWidgetKeyboardAction());

        getActions().addAction(scene.createObjectHoverAction());
        getActions().addAction(scene.createSelectAction());
//        getActions().addAction(new ResizeAction(new WindowStyleResizeProvider()));
        getActions().addAction(ActionFactory.createResizeAction());
//        getActions().addAction(ActionFactory.createResizeAction(null, ActionFactory.createDefaultResizeProvider())); // /*new MyResizeProvider())*/);     
        getActions().addAction(ActionFactory.createAlignWithMoveAction(scene.getMainLayer(), scene.getInterractionLayer(), null));

        nameWidget = new LabelWidget(scene);
        nameWidget.setFont(scene.getDefaultFont().deriveFont(Font.BOLD));
        nameWidget.setAlignment(LabelWidget.Alignment.CENTER);
        nameWidget.getActions().addAction(nameEditorAction);

//        addDependency(new Dependency() {
//            @Override
//            public void revalidateDependency() {
//                System.out.println("PROMENA!");
//            }
//        });
    }

    @Override
    public void notifyStateChanged(ObjectState previousState, ObjectState state) {
        // u ovu metodu ubaciti reakcija ne hover, focus, selected itd.
        super.notifyStateChanged(previousState, state);

        // resize reaction
        boolean select = state.isFocused();
        boolean wasSelected = previousState.isFocused();

        if (state.isHovered()) {
            // ne radi hover debljina jos
            if (!select) setBorder(HOVER_BORDER);
            setBackground(new Color(0xFBFBFB));
        } else {
            if (!select) setBorder(DEFAULT_BORDER);
            setBackground(Color.WHITE);
        }

        if (select && !wasSelected) {
            //ResizeStrategyProvider stratProv = getResizeStrategyProvider();
            //createActions(DesignerTools.SELECT).addAction(0, new ResizeAction(stratProv));
            //setBorder(BorderFactory.createResizeBorder(RESIZE_SIZE));
            //setBorder(new ResizeBorder(RESIZE_SIZE, Color.BLACK, getResizeControlPoints()));
            
            // select u okviru Explorer Managera
//            ExplorerManager em = ExplorerTopComponent.getStaticExplorerManager();
//            Children children = em.getRootContext().getChildren();
//            ArrayList<Node> nodes = new ArrayList<>();
//            for(Node n : children.getNodes()){
//                if(n instanceof ClassDiagramComponentNode){
//                    ClassDiagramComponentNode cdcn = (ClassDiagramComponentNode)n;
//                    if(cdcn.getComponent() == this.getComponent()){
//                        nodes.add(n);
//                    }
//                }
//            }
//            try {
//                em.setSelectedNodes(nodes.toArray(new Node[nodes.size()]));
//            } catch (PropertyVetoException ex) {
//                Exceptions.printStackTrace(ex);
//            }
            
            
            setBorder(RESIZE_BORDER);
            Rectangle bnd = getPreferredBounds();
            bnd.width += 2 * RESIZE_SIZE;
            bnd.height += 2 * RESIZE_SIZE;
            setPreferredBounds(bnd);

            Point loc = getLocation();
            loc.translate(-RESIZE_SIZE, -RESIZE_SIZE + 1);
            setPreferredLocation(loc);
        } else if (!select && wasSelected) {
            //if(getActions().getActions().get(0) instanceof ResizeAction)
            //createActions(DesignerTools.SELECT).removeAction(0);
            //setBorder(BorderFactory.createEmptyBorder());

            setBorder(DEFAULT_BORDER);
            Rectangle bnd = getPreferredBounds();
            bnd.width -= 2 * RESIZE_SIZE;
            bnd.height -= 2 * RESIZE_SIZE;
            setPreferredBounds(bnd);

            Point loc = getLocation();
            loc.translate(RESIZE_SIZE, RESIZE_SIZE - 1);
            setPreferredLocation(loc);
        }

        // RESIZE REACTION
    }

    abstract public ClassDiagramComponent getComponent();

    public LabelWidget getNameLabel() {
        return nameWidget;
    }
    
    @Override
    public final String getName() {
        return nameWidget.getLabel();
    }

    @Override
    public void setName(String newName) {
        if (getNameLabel().getLabel().equals(newName)) {
            return;
        }

        String oldName = component.getName();
        if (!component.getParentDiagram().nameExists(newName)) {
            this.nameWidget.setLabel(newName);
            component.setName(newName);
            component.getParentDiagram().componentNameChanged(component, oldName);
        } else {
            //WidgetAction editor = ActionFactory.createInplaceEditorAction(new LabelTextFieldEditorAction());
            //ActionFactory.getInplaceEditorController(nameEditorAction).openEditor(getNameLabel());
            JOptionPane.showMessageDialog(this.getScene().getView(), "Greska, ime vec postoji.");
        }
    }

    @Override
    public String toString() {
        return nameWidget.getLabel();
    }

    // already has getScene in widget, but this is casted, so it's easier
    public ClassDiagramScene getClassDiagramScene() {
        return (ClassDiagramScene) getScene();
    }
    
    protected void addMember(Widget container, MemberWidgetBase member){
        container.addChild(member);
        changedNotify();
    }
    
    protected void removeMember(Widget container, MemberWidgetBase member){
        container.removeChild(member);
        changedNotify();
    }
    
    private void changedNotify(){
        getClassDiagramScene().getUmlTopComponent().modify();
    }

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

//    protected static ResizeProvider.ControlPoint[] getResizeControlPoints() {
//        //by default all sized are active for resize;
//        return new ResizeProvider.ControlPoint[]{
//            ResizeProvider.ControlPoint.TOP_LEFT,
//            ResizeProvider.ControlPoint.TOP_CENTER,
//            ResizeProvider.ControlPoint.TOP_RIGHT,
//            ResizeProvider.ControlPoint.CENTER_LEFT,
//            ResizeProvider.ControlPoint.BOTTOM_LEFT,
//            ResizeProvider.ControlPoint.BOTTOM_CENTER,
//            ResizeProvider.ControlPoint.BOTTOM_RIGHT,
//            ResizeProvider.ControlPoint.CENTER_RIGHT
//        };
//    }
}
