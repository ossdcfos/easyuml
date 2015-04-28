package org.uml.visual.widgets.providers;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.datatransfer.Transferable;
import javax.swing.JComponent;
import org.netbeans.api.visual.action.AcceptProvider;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.widget.Widget;
import org.openide.nodes.NodeTransfer;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.uml.model.relations.ImplementsRelation;
import org.uml.model.relations.RelationBase;
import org.uml.visual.dialogs.ConnectRelationPanel;
import org.uml.visual.palette.PaletteItemNode;
import org.uml.visual.widgets.components.ComponentWidgetBase;
import org.uml.visual.widgets.components.EnumWidget;
import org.uml.visual.widgets.components.InterfaceWidget;

/**
 *
 * @author NUGS
 */
public class ComponentWidgetAcceptProvider implements AcceptProvider {

    @Override
    public ConnectorState isAcceptable(Widget widget, Point point, Transferable t) {
        PaletteItemNode pin = (PaletteItemNode) NodeTransfer.node(t, NodeTransfer.DND_COPY_OR_MOVE);
        Class<?> droppedClass = pin.getPaletteItem().getDropClass();
        Image dragImage = ImageUtilities.loadImage(pin.getPaletteItem().getIcon());
        JComponent view = widget.getScene().getView();
        Graphics2D g2 = (Graphics2D) view.getGraphics();

        view.paintImmediately(view.getVisibleRect());
        
        g2.drawImage(dragImage, widget.getLocation().x + point.x, widget.getLocation().y + point.y, view);

        return canAccept(widget, droppedClass) ? ConnectorState.ACCEPT : ConnectorState.REJECT;
    }

    @Override
    public void accept(Widget widget, Point point, Transferable t) {
        try {
            PaletteItemNode pin = (PaletteItemNode) NodeTransfer.node(t, NodeTransfer.DND_COPY_OR_MOVE);
            Class<?> droppedClass = pin.getPaletteItem().getDropClass();
            Class<? extends RelationBase> relationClass = droppedClass.asSubclass(RelationBase.class);
            RelationBase relation = relationClass.newInstance();
            ComponentWidgetBase componentWidget = (ComponentWidgetBase) widget;
            relation.setSource(componentWidget.getComponent());

            if (!(relation instanceof ImplementsRelation && !(widget instanceof InterfaceWidget))) {
                ConnectRelationPanel panel = new ConnectRelationPanel(componentWidget.getClassDiagramScene(), componentWidget, relation);
                panel.openRelationDialog();
            }
            
            widget.getScene().repaint();
        } catch (InstantiationException | IllegalAccessException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private boolean canAccept(Widget widget, Class<?> droppedClass) {
        if (RelationBase.class.isAssignableFrom(droppedClass)) {
            if (widget instanceof EnumWidget) return false;
            else if (widget instanceof InterfaceWidget && !droppedClass.equals(ImplementsRelation.class)) return false;
            else return true;
        } else return false;
    }
}
