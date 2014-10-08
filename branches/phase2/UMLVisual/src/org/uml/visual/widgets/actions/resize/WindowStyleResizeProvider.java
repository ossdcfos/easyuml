package org.uml.visual.widgets.actions.resize;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import org.netbeans.api.visual.action.ResizeProvider;
import org.netbeans.api.visual.action.ResizeProvider.ControlPoint;
import org.netbeans.api.visual.widget.Widget;
import org.uml.visual.widgets.ComponentWidgetBase;

/**
 *
 * @author sp153251
 */
public class WindowStyleResizeProvider extends ResizeStrategyProvider {

    private ArrayList<ResizeProvider.ControlPoint> points;
    private Point startLocation;
    private int startingmodifiers;
    private int finishingmodifiers;
    private int resizingmodifiers;

    /*
     * 
     * all side of are active for resizing
     * @param provider
     */
    public WindowStyleResizeProvider() {
        this(new ResizeProvider.ControlPoint[]{
            ResizeProvider.ControlPoint.TOP_LEFT,
            ResizeProvider.ControlPoint.TOP_CENTER,
            ResizeProvider.ControlPoint.TOP_RIGHT,
            ResizeProvider.ControlPoint.CENTER_LEFT,
            ResizeProvider.ControlPoint.BOTTOM_LEFT,
            ResizeProvider.ControlPoint.BOTTOM_CENTER,
            ResizeProvider.ControlPoint.BOTTOM_RIGHT,
            ResizeProvider.ControlPoint.CENTER_RIGHT
        });
    }

    /*
     * 
     * 
     * @param provider
     * @param points active control points
     */
    public WindowStyleResizeProvider(ResizeProvider.ControlPoint[] cPoints) {
        points = new ArrayList<>();
        if (cPoints != null) {
            points.addAll(Arrays.asList(cPoints));
        }
    }

    /**
     * Called after an user suggests a new boundary and before the suggested
     * boundary is stored to a specified widget. This allows to manipulate with
     * a suggested boundary to perform snap-to-grid, locked-axis on any other
     * resizing strategy.
     *
     * @param widget the resized widget
     * @param originalBounds the original bounds of the resizing widget
     * @param suggestedBounds the bounds of the resizing widget suggested by an
     * user (usually by a mouse cursor position)
     * @param controlPoint the control point that is used by an user for
     * resizing
     * @return the new (optionally modified) boundary processed by the strategy
     */
    @Override
    public Rectangle boundsSuggested(Widget widget,
            Rectangle originalBounds,
            Rectangle suggestedBounds,
            ControlPoint controlPoint) {

        if (widget.getState().isSelected() && points.contains(controlPoint)) {
            if (controlPoint == ControlPoint.TOP_LEFT
                    || controlPoint == ControlPoint.TOP_CENTER
                    || controlPoint == ControlPoint.TOP_RIGHT
                    || controlPoint == ControlPoint.CENTER_LEFT
                    || controlPoint == ControlPoint.BOTTOM_LEFT) {
                if (suggestedBounds.width < widget.getMinimumSize().width) {
                    suggestedBounds.x -= widget.getMinimumSize().width - suggestedBounds.width;
                    suggestedBounds.width = widget.getMinimumSize().width;
                }
                if (suggestedBounds.height < widget.getMinimumSize().height) {
                    suggestedBounds.y -= widget.getMinimumSize().height - suggestedBounds.height;
                    suggestedBounds.height = widget.getMinimumSize().height;
                }
                //move widget
                int dx = suggestedBounds.x - originalBounds.x;
                int dy = suggestedBounds.y - originalBounds.y;
                Point sceneOriginal = widget.convertLocalToScene(originalBounds.getLocation());
                Point sceneSuggested = widget.convertLocalToScene(suggestedBounds.getLocation());
                int dxSc = sceneSuggested.x - sceneOriginal.x;
                int dySc = sceneSuggested.y - sceneOriginal.y;
                Point loc = getInitialLocation();
                loc.translate(dxSc, dySc);
                widget.setPreferredLocation(loc);
                suggestedBounds.translate(-dx, -dy);
            } else {
                //resizing only
                if (suggestedBounds.width < widget.getMinimumSize().width) {
                    suggestedBounds.width = widget.getMinimumSize().width;
                }
                if (suggestedBounds.height < widget.getMinimumSize().height) {
                    suggestedBounds.height = widget.getMinimumSize().height;
                }
            }
            return suggestedBounds;
        } else {
            return originalBounds;
        }
    }

    //PROVIDER SECTION
    @Override
    public void resizingStarted(Widget widget) {
        startLocation = widget.getPreferredLocation();
        widget.setPreferredBounds(widget.getBounds());
        if (widget instanceof ComponentWidgetBase) {
            Dimension minResSize = new Dimension(10, 10);
            //Dimension minResSize=((ComponentWidgetBase)widget).getResizingMinimumSize();
            if (minResSize != null) {
                Insets ins = widget.getBorder().getInsets();
                minResSize.width += ins.left + ins.right;
                minResSize.height += ins.top + ins.bottom;
            }
            widget.setMinimumSize(minResSize);
            //AS IN 6.1, default and always used mode is preferred size mode allowing to resize to any size, but now with some reasonable limit
            widget.setPreferredSize(null);//disable to not have side effects
            ((ComponentWidgetBase)widget).setResizeMode(ComponentWidgetBase.RESIZEMODE.PREFERREDBOUNDS);

        } else {
            widget.setMinimumSize(new Dimension(10, 10));
        }
//        if (widget.getScene() instanceof DesignerScene)
//        {
//            DesignerScene scene = (DesignerScene) widget.getScene();
//            ContextPaletteManager manager = scene.getContextPaletteManager();
//            if(manager != null)
//            {
//                manager.cancelPalette();
//            }
//        }

    }

    @Override
    public void resizingFinished(Widget widget) {
        //smth like do not collapse after expanding
        Rectangle preferredBounds = widget.getPreferredBounds();
//        ComponentWidgetBase.RESIZEMODE mode = ComponentWidgetBase.RESIZEMODE.MINIMUMSIZE;
//        if(widget instanceof ComponentWidgetBase)
//        {
//            ComponentWidgetBase nW=(ComponentWidgetBase) widget;
//            if((startingmodifiers&InputEvent.SHIFT_DOWN_MASK)==InputEvent.SHIFT_DOWN_MASK)//if resizing was started with shift it force preferred size mode
//            {
//                nW.setResizeMode(ComponentWidgetBase.RESIZEMODE.PREFERREDBOUNDS);
//            }
//            if((startingmodifiers&InputEvent.CTRL_DOWN_MASK)==InputEvent.CTRL_DOWN_MASK)//if resizing was started with shift it force minimum size mode
//            {
//                nW.setResizeMode(ComponentWidgetBase.RESIZEMODE.MINIMUMSIZE);
//            }
//            //in all cases without shift or ctrl resize moide remains unchanged
//            if(nW.getResizeMode()!=null)
//            {
//                mode=nW.getResizeMode();
//            }
//        }
//        if(mode==ComponentWidgetBase.RESIZEMODE.MINIMUMSIZE)
//        {
//            widget.setMinimumSize(new Dimension(preferredBounds.width,
//                                                preferredBounds.height));
//            widget.setPreferredBounds(null);
//            widget.setPreferredSize(null);
//        }
//        else if(mode==ComponentWidgetBase.RESIZEMODE.PREFERREDSIZE)
//        {
        widget.setPreferredSize(new Dimension(preferredBounds.width,
                preferredBounds.height));
        widget.setPreferredBounds(null);
        //mimimum size should be set to some very small value in resize started
//        }
//        else
//        {
//            //normalize bounds on 0-0 at left-top corner
//            Rectangle rec=widget.getPreferredBounds();
//            Point pnt=widget.getPreferredLocation();
//            pnt.x+=rec.x+widget.getBorder().getInsets().left;
//            pnt.y+=rec.y+widget.getBorder().getInsets().left;
//            rec.x=rec.y=-widget.getBorder().getInsets().left;
//            widget.setPreferredBounds(rec);
//            widget.setPreferredLocation(pnt);
//        }
        startLocation = null;

//        Scene scene = widget.getScene();
//        Lookup lookup = scene.getLookup();
//        ContextPaletteManager manager = lookup.lookup(ContextPaletteManager.class);
//        if(manager != null)
//        {
//            manager.selectionChanged(null);
//        }
//        if (scene instanceof DesignerScene)
//        {
//            TopComponent topComp = ((DesignerScene)scene).getTopComponent();
//            if (topComp instanceof UMLDiagramTopComponent) {
//                ((UMLDiagramTopComponent)topComp).setDiagramDirty(true);
//            }
//        }
    }

    public Point getInitialLocation() {
        return new Point(startLocation);
    }

    @Override
    Rectangle boundsSuggested(Widget widget, Rectangle originalSceneRectangle, Rectangle rectangle, ControlPoint controlPoint, int modifiers) {
        this.resizingmodifiers = modifiers;
        return boundsSuggested(widget, originalSceneRectangle, rectangle, controlPoint);
    }

    @Override
    void resizingFinished(Widget widget, int modifiers) {
        this.finishingmodifiers = modifiers;
        resizingFinished(widget);
    }

    @Override
    void resizingStarted(Widget widget, int modifiers) {
        this.startingmodifiers = modifiers;
        resizingStarted(widget);
    }
}
