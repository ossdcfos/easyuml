package org.uml.visual.widgets.anchors;

import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.api.visual.widget.Widget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import org.netbeans.api.visual.widget.ConnectionWidget;

/**
 * This class represents a node anchor used in VMD visualization style. The
 * anchor could be assign by multiple connection widgets. For each usage the
 * anchor resolves a different position. The positions are resolved at the top
 * and the bottom of the widget where the anchor is attached to.
 *
 * @author David Kaspar
 */
public class ParallelNodeAnchor extends Anchor {

    private boolean requiresRecalculation = true;

    private LinkedHashMap<Anchor.Entry, Anchor.Result> results = new LinkedHashMap<>();

    /**
     * Creates a node anchor.
     *
     * @param widget the node widget where the anchor is attached to
     * @since 2.5
     */
    public ParallelNodeAnchor(Widget widget) {
        super(widget);
        assert widget != null;
    }

    /**
     * Notifies when an entry is registered
     *
     * @param entry the registered entry
     */
    @Override
    protected void notifyEntryAdded(Anchor.Entry entry) {
        requiresRecalculation = true;
    }

    /**
     * Notifies when an entry is unregistered
     *
     * @param entry the unregistered entry
     */
    @Override
    protected void notifyEntryRemoved(Anchor.Entry entry) {
        results.remove(entry);
        requiresRecalculation = true;
    }

    /**
     * Notifies when the anchor is going to be revalidated.
     *
     * @since 2.8
     */
    @Override
    protected void notifyRevalidate() {
        requiresRecalculation = true;
    }

//    private void recalculate() {
//        if (!requiresRecalculation)
//            return;
//
//        Widget widget = getRelatedWidget();
//        Point relatedLocation = getRelatedSceneLocation();
//
//        Rectangle bounds = widget.convertLocalToScene(widget.getBounds());
//
//        LinkedHashMap<Anchor.Entry, Float> topmap = new LinkedHashMap<>();
//        LinkedHashMap<Anchor.Entry, Float> bottommap = new LinkedHashMap<>();
//        LinkedHashMap<Anchor.Entry, Float> leftmap = new LinkedHashMap<>();
//        LinkedHashMap<Anchor.Entry, Float> rightmap = new LinkedHashMap<>();
//
//        for (Anchor.Entry entry : getEntries()) {
//            Point oppositeLocation = getOppositeSceneLocation(entry);
//            if (oppositeLocation == null || relatedLocation == null) {
//                results.put(entry, new Anchor.Result(new Point(bounds.x, bounds.y), DIRECTION_ANY));
//                continue;
//            }
//
//            int dy = oppositeLocation.y - relatedLocation.y;
//            int dx = oppositeLocation.x - relatedLocation.x;
//
//            if (vertical) {
//                if (dy > 0)
//                    bottommap.put(entry, (float) dx / (float) dy);
//                else if (dy < 0)
//                    topmap.put(entry, (float) -dx / (float) dy);
//                else
//                    topmap.put(entry, dx < 0 ? Float.MAX_VALUE : Float.MIN_VALUE);
//            } else {
//                if (dx > 0)
//                    bottommap.put(entry, (float) dy / (float) dx);
//                else if (dy < 0)
//                    topmap.put(entry, (float) -dy / (float) dx);
//                else
//                    topmap.put(entry, dy < 0 ? Float.MAX_VALUE : Float.MIN_VALUE);
//            }
//        }
//
//        Anchor.Entry[] topList = toArray(topmap);
//        Anchor.Entry[] bottomList = toArray(bottommap);
//
//        // Offsetted by 1, because of composite borders being used
//        int borderGap = widget.getBorder().getInsets().top - 1;
//        int y = bounds.y + borderGap;
//        int x = bounds.x + borderGap;
//        int len = topList.length;
//
//        for (int a = 0; a < len; a++) {
//            Anchor.Entry entry = topList[a];
//            if (vertical)
//                x = bounds.x + (a + 1) * bounds.width / (len + 1);
//            else
//                y = bounds.y + (a + 1) * bounds.height / (len + 1);
//            results.put(entry, new Anchor.Result(new Point(x, y), vertical ? Anchor.Direction.TOP : Anchor.Direction.LEFT));
//        }
//
//        y = bounds.y + bounds.height - borderGap;
//        x = bounds.x + bounds.width - borderGap;
//        len = bottomList.length;
//
//        for (int a = 0; a < len; a++) {
//            Anchor.Entry entry = bottomList[a];
//            if (vertical)
//                x = bounds.x + (a + 1) * bounds.width / (len + 1);
//            else
//                y = bounds.y + (a + 1) * bounds.height / (len + 1);
//            results.put(entry, new Anchor.Result(new Point(x, y), Anchor.Direction.BOTTOM));
//        }
//
//        requiresRecalculation = false;
//    }
    private void recalculate() {
        if (!requiresRecalculation)
            return;

        LinkedHashMap<Entry, Float> topmap = new LinkedHashMap<>();
        LinkedHashMap<Entry, Float> bottommap = new LinkedHashMap<>();
        LinkedHashMap<Entry, Float> leftmap = new LinkedHashMap<>();
        LinkedHashMap<Entry, Float> rightmap = new LinkedHashMap<>();

        ArrayList<Entry> topList = new ArrayList<>();
        ArrayList<Entry> bottomList = new ArrayList<>();
        ArrayList<Entry> leftList = new ArrayList<>();
        ArrayList<Entry> rightList = new ArrayList<>();

        Widget widget = getRelatedWidget();

//        Lookup lookup = widget.getLookup();
        Rectangle bounds = null;
//        WidgetShape shape = null;
//        if (lookup != null) {
//            shape = lookup.lookup(WidgetShape.class);
//            if (shape != null) {
//                bounds = shape.getBounds();
//            }
//        }

        boolean includeBorders = false;

        if (bounds == null) {
            bounds = widget.getBounds();
            if (!includeBorders) {
                Insets insets = widget.getBorder().getInsets();
                bounds.x += insets.left;
                bounds.y += insets.top;
                bounds.width -= insets.left + insets.right;
                bounds.height -= insets.top + insets.bottom;
            }
            bounds = widget.convertLocalToScene(bounds);
        }

        for (Entry entry : getEntries()) {
            Point relatedLocation = getRelatedSceneLocation();
            Point oppositeLocation = getOppositeSceneLocation(entry);

            if (bounds.isEmpty() || relatedLocation.equals(oppositeLocation)) {
//                return new Anchor.Result(relatedLocation, Anchor.DIRECTION_ANY);
            }
            float dx = oppositeLocation.x - relatedLocation.x;
            float dy = oppositeLocation.y - relatedLocation.y;

            float ddx = Math.abs(dx) / (float) bounds.width;
            float ddy = Math.abs(dy) / (float) bounds.height;

            //Anchor.Direction direction;
            // self link case, always route from right edge to bottom
            if (ddx == 0 && ddy == 0) {
                if (entry.isAttachedToConnectionSource()) {
                    rightmap.put(entry, 0f);
                    rightList.add(entry);
                } else {
                    bottommap.put(entry, 0f);
                    bottomList.add(entry);
                }
            } else if (ddx >= ddy) {
                //direction = dx >= 0.0f ? Direction.RIGHT : Direction.LEFT;
                if (dx > 0.0f) {
                    rightmap.put(entry, dy / dx);
                    rightList.add(entry);
                } else {
                    leftmap.put(entry, -dy / dx);
                    leftList.add(entry);
                }
            } else {
                //direction = dy >= 0.0F ? Direction.BOTTOM : Direction.TOP;
                if (dy >= 0.0F) {
                    bottommap.put(entry, dx / dy);
                    bottomList.add(entry);
                } else {
                    topmap.put(entry, -dx / dy);
                    topList.add(entry);
                }
            }
        }

        int edgeGap = 0;
        int len = rightList.size();
        Entry[] sortedEntries = toArray(rightList, rightmap);

        // Inside the loop I need to now calculate the new slop (based on the 
        // location of the entries new point), and then 
        int x = bounds.x + bounds.width + edgeGap;
        for (int a = 0; a < len; a++) {
            Entry curEntry = sortedEntries[a];
            int y = bounds.y + (a + 1) * bounds.height / (len + 1);

            Point newPt = null;
//            if (shape != null) {
//                newPt = shape.getIntersection(getOppositeSceneLocation(curEntry), new Point(x, y));
//            } else {
            newPt = new Point(x, y);
//            }

            results.put(curEntry, new Result(newPt, Direction.RIGHT));
        }

        len = leftList.size();
        sortedEntries = toArray(leftList, leftmap);

        x = bounds.x - edgeGap;
        for (int a = 0; a < len; a++) {
            Entry curEntry = sortedEntries[a];
            int y = bounds.y + (a + 1) * bounds.height / (len + 1);

            Point newPt = null;
//            if (shape != null) {
//                newPt = shape.getIntersection(getOppositeSceneLocation(curEntry), new Point(x, y));
//            } else {
            newPt = new Point(x, y);
//            }

            results.put(curEntry, new Result(newPt, Direction.LEFT));
        }

        len = topList.size();
        sortedEntries = toArray(topList, topmap);

        int y = bounds.y - edgeGap;
        for (int a = 0; a < len; a++) {
            Entry curEntry = sortedEntries[a];
            x = bounds.x + (a + 1) * bounds.width / (len + 1);

            Point newPt = null;
//            if (shape != null) {
//                newPt = shape.getIntersection(getOppositeSceneLocation(curEntry), new Point(x, y));
//            } else {
            newPt = new Point(x, y);
//            }

            results.put(curEntry, new Result(newPt, Direction.TOP));
        }

        len = bottomList.size();
        sortedEntries = toArray(bottomList, bottommap);

        y = bounds.y + bounds.height + edgeGap;
        for (int a = 0; a < len; a++) {
            Entry curEntry = sortedEntries[a];
            x = bounds.x + (a + 1) * bounds.width / (len + 1);

            Point newPt = null;
//            if (shape != null) {
//                newPt = shape.getIntersection(getOppositeSceneLocation(curEntry), new Point(x, y));
//            } else {
            newPt = new Point(x, y);
//            }

            results.put(curEntry, new Result(newPt, Direction.BOTTOM));
        }

        requiresRecalculation = false;
    }

    @Override
    public Point getOppositeSceneLocation(Entry entry) {
        Point retVal = super.getOppositeSceneLocation(entry);

        // If the connection widget has connection points we need to find the 
        // connection point that is closes to the related widget.
        //
        // There are always two, one for the source and target ends.
        ConnectionWidget connection = entry.getAttachedConnectionWidget();
        if ((connection != null) && (connection.getControlPoints().size() > 2)) {
            List< Point> points = connection.getControlPoints();
            if (entry.isAttachedToConnectionSource() == true) {
                // The source end starts from the start of the collection of points.
                retVal = points.get(1);
            } else {
                // The target end starts from the end of the collection of points.
                retVal = points.get(points.size() - 2);
            }
        }

        return retVal;
    }

//    private Anchor.Entry[] toArray(final LinkedHashMap<Anchor.Entry, Float> map) {
//        Set<Anchor.Entry> keys = map.keySet();
//        Anchor.Entry[] entries = keys.toArray(new Anchor.Entry[keys.size()]);
////        Arrays.sort(entries, new Comparator<Anchor.Entry>() {
////            public int compare(Anchor.Entry o1, Anchor.Entry o2) {
////                float f = map.get(o1) - map.get(o2);
////                if (f > 0.0f)
////                    return 1;
////                else if (f < 0.0f)
////                    return -1;
////                else
////                    return 0;
////            }
////        });
//        return entries;
//    }
    private Entry[] toArray(List<Entry> entries, final LinkedHashMap<Entry, Float> map) {
        Set<Entry> keys = map.keySet();

        Entry[] retVal = new Entry[entries.size()];
        if (entries.size() > 0) {
            entries.toArray(retVal);

            Arrays.sort(retVal, new Comparator<Entry>() {

                public int compare(Entry o1, Entry o2) {
                    float f = map.get(o1) - map.get(o2);
                    if (f > 0.0f) {
                        return 1;
                    } else if (f < 0.0f) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            });
        }
        return retVal;
    }

    /**
     * Computes a result (position and direction) for a specific entry.
     *
     * @param entry the entry
     * @return the calculated result
     */
    @Override
    public Anchor.Result compute(Anchor.Entry entry) {
        recalculate();
        return results.get(entry);
    }

}
