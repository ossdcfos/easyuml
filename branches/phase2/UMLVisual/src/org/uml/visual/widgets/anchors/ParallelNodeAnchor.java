package org.uml.visual.widgets.anchors;

import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.api.visual.widget.Widget;

import java.awt.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Set;
import static org.netbeans.api.visual.anchor.Anchor.DIRECTION_ANY;
import org.netbeans.api.visual.vmd.VMDColorScheme;
import org.netbeans.api.visual.vmd.VMDFactory;

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
    private final boolean vertical;
    private VMDColorScheme scheme;

    /**
     * Creates a node anchor with vertical direction.
     *
     * @param widget the node widget where the anchor is attached to
     */
    public ParallelNodeAnchor(Widget widget) {
        this(widget, true);
    }

    /**
     * Creates a node anchor.
     *
     * @param widget the node widget where the anchor is attached to
     * @param vertical if true, then anchors are placed vertically; if false,
     * then anchors are placed horizontally
     */
    public ParallelNodeAnchor(Widget widget, boolean vertical) {
        this(widget, vertical, VMDFactory.getOriginalScheme());
    }

    /**
     * Creates a node anchor.
     *
     * @param widget the node widget where the anchor is attached to
     * @param vertical if true, then anchors are placed vertically; if false,
     * then anchors are placed horizontally
     * @param scheme color scheme
     * @since 2.5
     */
    public ParallelNodeAnchor(Widget widget, boolean vertical, VMDColorScheme scheme) {
        super(widget);
        assert widget != null;
        assert scheme != null;
        this.vertical = vertical;
        this.scheme = scheme;
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

    private void recalculate() {
        if (!requiresRecalculation)
            return;

        Widget widget = getRelatedWidget();
        Point relatedLocation = getRelatedSceneLocation();

        Rectangle bounds = widget.convertLocalToScene(widget.getBounds());

        LinkedHashMap<Anchor.Entry, Float> topmap = new LinkedHashMap<>();
        LinkedHashMap<Anchor.Entry, Float> bottommap = new LinkedHashMap<>();

        for (Anchor.Entry entry : getEntries()) {
            Point oppositeLocation = getOppositeSceneLocation(entry);
            if (oppositeLocation == null || relatedLocation == null) {
                results.put(entry, new Anchor.Result(new Point(bounds.x, bounds.y), DIRECTION_ANY));
                continue;
            }

            int dy = oppositeLocation.y - relatedLocation.y;
            int dx = oppositeLocation.x - relatedLocation.x;

            if (vertical) {
                if (dy > 0)
                    bottommap.put(entry, (float) dx / (float) dy);
                else if (dy < 0)
                    topmap.put(entry, (float) -dx / (float) dy);
                else
                    topmap.put(entry, dx < 0 ? Float.MAX_VALUE : Float.MIN_VALUE);
            } else {
                if (dx > 0)
                    bottommap.put(entry, (float) dy / (float) dx);
                else if (dy < 0)
                    topmap.put(entry, (float) -dy / (float) dx);
                else
                    topmap.put(entry, dy < 0 ? Float.MAX_VALUE : Float.MIN_VALUE);
            }
        }

        Anchor.Entry[] topList = toArray(topmap);
        Anchor.Entry[] bottomList = toArray(bottommap);

        // Offsetted by 1, because of composite borders being used
        int borderGap = widget.getBorder().getInsets().top - 1;
        int y = bounds.y + borderGap;
        int x = bounds.x + borderGap;
        int len = topList.length;

        for (int a = 0; a < len; a++) {
            Anchor.Entry entry = topList[a];
            if (vertical)
                x = bounds.x + (a + 1) * bounds.width / (len + 1);
            else
                y = bounds.y + (a + 1) * bounds.height / (len + 1);
            results.put(entry, new Anchor.Result(new Point(x, y), vertical ? Anchor.Direction.TOP : Anchor.Direction.LEFT));
        }

        y = bounds.y + bounds.height - borderGap;
        x = bounds.x + bounds.width - borderGap;
        len = bottomList.length;

        for (int a = 0; a < len; a++) {
            Anchor.Entry entry = bottomList[a];
            if (vertical)
                x = bounds.x + (a + 1) * bounds.width / (len + 1);
            else
                y = bounds.y + (a + 1) * bounds.height / (len + 1);
            results.put(entry, new Anchor.Result(new Point(x, y), vertical ? Anchor.Direction.BOTTOM : Anchor.Direction.RIGHT));
        }

        requiresRecalculation = false;
    }

    private Anchor.Entry[] toArray(final LinkedHashMap<Anchor.Entry, Float> map) {
        Set<Anchor.Entry> keys = map.keySet();
        Anchor.Entry[] entries = keys.toArray(new Anchor.Entry[keys.size()]);
//        Arrays.sort(entries, new Comparator<Anchor.Entry>() {
//            public int compare(Anchor.Entry o1, Anchor.Entry o2) {
//                float f = map.get(o1) - map.get(o2);
//                if (f > 0.0f)
//                    return 1;
//                else if (f < 0.0f)
//                    return -1;
//                else
//                    return 0;
//            }
//        });
        return entries;
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
