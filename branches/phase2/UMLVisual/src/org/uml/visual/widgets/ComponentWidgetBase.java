package org.uml.visual.widgets;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.ResizeProvider;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.widget.ImageWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.openide.util.Lookup;
import org.uml.model.ClassDiagramComponent;
import org.uml.visual.widgets.actions.ComponentWidgetKeyboardAction;
import org.uml.visual.widgets.actions.resize.DesignerTools;
import org.uml.visual.widgets.actions.resize.ResizeAction;
import org.uml.visual.widgets.actions.resize.ResizeStrategyProvider;
import org.uml.visual.widgets.actions.resize.WindowStyleResizeProvider;
import org.uml.visual.widgets.actions.resize.border.ResizeBorder;
import org.uml.visual.widgets.providers.ClassConnectProvider;

/**
 *
 * @author "NUGS"
 */
abstract public class ComponentWidgetBase extends ImageWidget implements NameableWidget, Lookup.Provider {

    private static final Dimension MIN_DIMENSION = new Dimension(120, 120);
    private static final Border DEFAULT_BORDER = BorderFactory.createLineBorder();
    private static final Border SELECTED_BORDER = BorderFactory.createResizeBorder(5, Color.black, false);
    //atribut name
    protected LabelWidget nameWidget;

    public ComponentWidgetBase(ClassDiagramScene scene) {
        super(scene);
        setBorder(DEFAULT_BORDER);
        setMinimumSize(MIN_DIMENSION);

        getActions().addAction(ActionFactory.createExtendedConnectAction(scene.getInterractionLayer(), new ClassConnectProvider()));

        getActions().addAction(new ComponentWidgetKeyboardAction());

        getActions().addAction(scene.createObjectHoverAction());
        getActions().addAction(scene.createSelectAction());
        //getActions().addAction(new ResizeAction(new WindowStyleResizeProvider()));
        getActions().addAction(ActionFactory.createResizeAction());
//        getActions().addAction(ActionFactory.createResizeAction(null, ActionFactory.createDefaultResizeProvider())); // /*new MyResizeProvider())*/);     
        getActions().addAction(ActionFactory.createAlignWithMoveAction(scene.getMainLayer(), scene.getInterractionLayer(), null));

        nameWidget = new LabelWidget(scene);
        nameWidget.setFont(scene.getDefaultFont().deriveFont(Font.BOLD));
        nameWidget.setAlignment(LabelWidget.Alignment.CENTER);

//        getActions().addAction(ActionFactory.createSelectAction(new SelectProvider() {
//            @Override
//            public boolean isAimingAllowed(Widget widget, Point localLocation, boolean invertSelection) {
//                return true;
//            }
//            @Override
//            public boolean isSelectionAllowed(Widget widget, Point localLocation, boolean invertSelection) {
//                return true;
//            }
//            @Override
//            public void select(Widget widget, Point localLocation, boolean invertSelection) {
//                scene.setFocusedWidget(widget);
//            }
//        }));
        // Delete dugme radi, ali je uradjeno u ComponentWidgetKeyboardAction
//       InputMap inputMap = new InputMap ();
//       inputMap.put (KeyStroke.getKeyStroke (KeyEvent.VK_DELETE, 0, false), "myAction");        
//       ActionMap actionMap = new ActionMap ();
//       actionMap.put ("myAction", new DeleteClassAction (this));     
//       getActions().addAction(ActionFactory.createActionMapAction(inputMap, actionMap));
    }

    @Override
    public void notifyStateChanged(ObjectState previousState, ObjectState state) {
        // u ovu metodu ubaciti reakcija ne hover, focus, selected itd.
        super.notifyStateChanged(previousState, state);

        if (state.isFocused() && state.isSelected()) {
            setBorder(SELECTED_BORDER);
        } else {
            setBorder(DEFAULT_BORDER);
        }

        if (state.isHovered()) {
            setBackground(new Color(0xFBFBFB));
        } else {
            setBackground(Color.WHITE);
        }

        // RESIZE REACTION
        boolean select = state.isFocused();
        boolean wasSelected = previousState.isFocused();

        if (select && !wasSelected) {
            if (!isResizable()) {
                setBorder(NON_RESIZABLE_BORDER);
                return;
            }
            // Allow subclasses to change the resize strategy and provider.
            //ResizeStrategyProvider stratProv = getResizeStrategyProvider();
            //createActions(DesignerTools.SELECT).addAction(0, new ResizeAction(stratProv));
            //setBorder(BorderFactory.createResizeBorder(RESIZE_SIZE));
            setBorder(new ResizeBorder(RESIZE_SIZE, Color.BLACK, getResizeControlPoints()));
            if (getResizeMode() == RESIZEMODE.PREFERREDBOUNDS) {
                Rectangle bnd = getPreferredBounds();
                bnd.width += 2 * RESIZE_SIZE;
                bnd.height += 2 * RESIZE_SIZE;
                setPreferredBounds(bnd);
                Point loc = getPreferredLocation();
                loc.translate(-RESIZE_SIZE, -RESIZE_SIZE);
                setPreferredLocation(loc);
                setMinimumSize(new Dimension(getResizingMinimumSize().width + RESIZE_SIZE * 2,
                        getResizingMinimumSize().height + RESIZE_SIZE * 2));
            } else if (getResizeMode() == RESIZEMODE.PREFERREDSIZE) {
                lastResMode = RESIZEMODE.PREFERREDSIZE;
                setPreferredSize(new Dimension(getPreferredSize().width + 2 * RESIZE_SIZE,
                        getPreferredSize().height + 2 * RESIZE_SIZE));
            } else if (getResizeMode() == RESIZEMODE.MINIMUMSIZE) {
                if (getMinimumSize() == null) {
                    setMinimumSize(new Dimension(getBounds().width - 2 * RESIZE_SIZE, getBounds().height - 2 * RESIZE_SIZE));
                }
                //setMinimumSize(new Dimension(getMinimumSize().width + 2 * RESIZE_SIZE, getMinimumSize().height + 2 * RESIZE_SIZE));
                Rectangle bnd = getPreferredBounds();
                bnd.width += 2 * RESIZE_SIZE - 2;
                bnd.height += 2 * RESIZE_SIZE - 2;
                setPreferredBounds(bnd);
                Point loc = getPreferredLocation();
                loc.translate(-RESIZE_SIZE + 1, -RESIZE_SIZE + 1);
                setPreferredLocation(loc);
//                setMinimumSize(new Dimension(getResizingMinimumSize().width + RESIZE_SIZE * 2,
//                        getResizingMinimumSize().height + RESIZE_SIZE * 2));
            }
        } else if (!select && wasSelected) {
            if (!resizable) {
                setBorder(DEFAULT_BORDER);
                //setBorder(BorderFactory.createEmptyBorder());
                return;
            }
            //Do not have access to the class to recheck, will consider if was selected is here
            //TBD add some additional possibility to check
            //if(getActions().getActions().get(0) instanceof ResizeAction)
            {
                //createActions(DesignerTools.SELECT).removeAction(0);
                setBorder(DEFAULT_BORDER);
                //setBorder(BorderFactory.createEmptyBorder());
                if (lastResMode == RESIZEMODE.PREFERREDBOUNDS) {
                    Rectangle bnd = getPreferredBounds();
                    bnd.width -= 2 * RESIZE_SIZE;
                    bnd.height -= 2 * RESIZE_SIZE;
                    setPreferredBounds(bnd);
                    Point loc = getPreferredLocation();
                    loc.translate(RESIZE_SIZE, RESIZE_SIZE);
                    setPreferredLocation(loc);
                    setMinimumSize(getResizingMinimumSize());
                } else if (lastResMode == RESIZEMODE.PREFERREDSIZE) {
                    setPreferredSize(new Dimension(getPreferredSize().width - 2 * RESIZE_SIZE,
                            getPreferredSize().height - 2 * RESIZE_SIZE));
                } else if (lastResMode == RESIZEMODE.MINIMUMSIZE) {
                    //setMinimumSize(new Dimension(getMinimumSize().width - 2 * RESIZE_SIZE, getMinimumSize().height - 2 * RESIZE_SIZE));
                    Rectangle bnd = getPreferredBounds();
                    bnd.width -= 2 * RESIZE_SIZE + 2;
                    bnd.height -= 2 * RESIZE_SIZE + 2;
                    setPreferredBounds(bnd);
                    Point loc = getPreferredLocation();
                    loc.translate(RESIZE_SIZE - 1, RESIZE_SIZE - 1);
                    setPreferredLocation(loc);
//                    setMinimumSize(getResizingMinimumSize());
                }
                //
            }
        }

        // RESIZE REACTION
    }

    abstract public ClassDiagramComponent getComponent();

    abstract public LabelWidget getNameLabel();

    // allready has getScene in widget ..
    public ClassDiagramScene getClassDiagramScene() {
        return (ClassDiagramScene) getScene();
    }

    @Override
    // to achieve resize cursors and move cursor
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

    public Graphics2D returnGraphics() {
        return this.getGraphics();
    }

    public void setGraphics(Graphics2D newGraphics) {
        this.setGraphics(newGraphics);
    }

    public boolean isResizable() {
        return resizable;
    }

    public void setResizable(boolean resizable) {
        this.resizable = resizable;
    }

    protected ResizeProvider.ControlPoint[] getResizeControlPoints() {
        //by default all sized are active for resize;
        return new ResizeProvider.ControlPoint[]{
            ResizeProvider.ControlPoint.TOP_LEFT,
            ResizeProvider.ControlPoint.TOP_CENTER,
            ResizeProvider.ControlPoint.TOP_RIGHT,
            ResizeProvider.ControlPoint.CENTER_LEFT,
            ResizeProvider.ControlPoint.BOTTOM_LEFT,
            ResizeProvider.ControlPoint.BOTTOM_CENTER,
            ResizeProvider.ControlPoint.BOTTOM_RIGHT,
            ResizeProvider.ControlPoint.CENTER_RIGHT
        };
    }

    public RESIZEMODE getResizeMode() {
        if (lastResMode == null) {
            recalculateResizeModeBasedOnWidget();
        }
        return lastResMode;
    }

    private void recalculateResizeModeBasedOnWidget() {
        if (isPreferredBoundsSet()) {
            lastResMode = RESIZEMODE.PREFERREDBOUNDS;
        } else if (getPreferredSize() != null) {
            lastResMode = RESIZEMODE.PREFERREDSIZE;
        } else {
            //default is minSize
            lastResMode = RESIZEMODE.MINIMUMSIZE;
        }
    }

    /**
     * set resize mode, if null resize mode will be recalculated based on widget
     * properties
     *
     * @param mode
     */
    public void setResizeMode(RESIZEMODE mode) {
        if (mode == null) {
            recalculateResizeModeBasedOnWidget();
        } else {
            lastResMode = mode;
        }
    }

    public ResizeStrategyProvider getResizeStrategyProvider() {
        return new WindowStyleResizeProvider(getResizeControlPoints());
    }

    private boolean resizable = true;
    public enum RESIZEMODE {
        PREFERREDBOUNDS, PREFERREDSIZE, MINIMUMSIZE
    };
    private static final int RESIZE_SIZE = 5;
    private boolean resizedManually = false;
    private RESIZEMODE lastResMode;
    public  static ResizeBorder NON_RESIZABLE_BORDER =
            new ResizeBorder(5, Color.BLACK,
                             new ResizeProvider.ControlPoint[]{});
    
    
    /**
     * instead of Widget::getMinimumSize this is dynamic value used in resizing
     * more used for window like resizing, when content is relative to left-top corner
     * @return size below which resize is not allowed
     */
    public Dimension getResizingMinimumSize() {
        return new Dimension(10, 10);
    }

    /**
     * instead of Widget::getMinimumSize this is dinamic value used in resize to content action and most im[portant
     * for elements without any content or for multiline elements without inner limitations
     * @return miminum size to set in resize to content action, by default the same as in resizing minimum size
     */
    public Dimension getDefaultMinimumSize() {
        return getResizingMinimumSize();
    }

}
