package org.uml.visual.widgets.providers;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.Transferable;
import javax.swing.JComponent;
import org.netbeans.api.visual.action.AcceptProvider;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.widget.Widget;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.nodes.Node;
import org.openide.nodes.NodeTransfer;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.uml.model.relations.CardinalityEnum;
import org.uml.model.relations.HasBaseRelationComponent;
import org.uml.model.relations.ImplementsRelationComponent;
import org.uml.model.relations.IsRelationComponent;
import org.uml.model.relations.RelationComponent;
import org.uml.model.relations.UseRelationComponent;
import org.uml.visual.dialogs.ClassHasRelationPanel;
import org.uml.visual.dialogs.ImplementsRelationPanel;
import org.uml.visual.dialogs.IsRelationPanel;
import org.uml.visual.dialogs.ClassUseRelationPanel;
import org.uml.visual.palette.PaletteItemNode;
import org.uml.visual.widgets.ClassWidget;
import org.uml.visual.widgets.ComponentWidgetBase;
import org.uml.visual.widgets.InterfaceWidget;

/**
 *
 * @author "NUGS"
 */
// Currently does nothing usefull, needs upgrade
public class ComponentWidgetAcceptProvider implements AcceptProvider {

    boolean init = false;
    Node node;
    PaletteItemNode pin;
    Class<?> droppedClass;
    Image dragImage;
    JComponent view;
    Graphics2D g2;
    Point wLoc;
    Rectangle wArea;

    @Override
    public ConnectorState isAcceptable(Widget widget, Point point, Transferable t) {
        if (!init) {
            node = NodeTransfer.node(t, NodeTransfer.DND_COPY_OR_MOVE);
            pin = (PaletteItemNode) node;
            droppedClass = pin.getPaletteItem().getDropClass();
            dragImage = ImageUtilities.loadImage(pin.getPaletteItem().getIcon());
            view = widget.getScene().getView();
            g2 = (Graphics2D) view.getGraphics();
            wLoc = widget.getLocation();
            wArea = widget.getClientArea();
            init = true;
        }

        view.paintImmediately(wLoc.x, wLoc.y, wArea.width + dragImage.getWidth(view), wArea.height + dragImage.getHeight(view));

        g2.drawImage(dragImage, widget.getLocation().x + point.getLocation().x, widget.getLocation().y + point.getLocation().y, view);

        return canAccept(widget, droppedClass) ? ConnectorState.ACCEPT : ConnectorState.REJECT;
    }

    @Override
    public void accept(Widget widget, Point point, Transferable t) {
        try {
            DialogDescriptor dd = null;
            String msg = "Choose target";
            Class<? extends RelationComponent> relationClass = droppedClass.asSubclass(RelationComponent.class);
            //Class<? extends RelationComponent> relationClass = (Class<? extends RelationComponent>) drClass;
            RelationComponent relation = relationClass.newInstance();
            ComponentWidgetBase w = (ComponentWidgetBase) widget;
            relation.setSource(w.getComponent());
            if (relation instanceof IsRelationComponent) {
                IsRelationPanel panel = new IsRelationPanel((ComponentWidgetBase) widget);
                dd = new DialogDescriptor(panel, msg);
                if (DialogDisplayer.getDefault().notify(dd) == NotifyDescriptor.OK_OPTION) {
                    ComponentWidgetBase compWidget = (ComponentWidgetBase) panel.getComboBoxTarget().getSelectedItem();
                    createConnection(relation, w, compWidget);
                }
            } else if (relation instanceof HasBaseRelationComponent) {
                ClassHasRelationPanel panel = new ClassHasRelationPanel(w);
                dd = new DialogDescriptor(panel, msg);
                if (DialogDisplayer.getDefault().notify(dd) == NotifyDescriptor.OK_OPTION) {
                    HasBaseRelationComponent r = (HasBaseRelationComponent) relation;
                    r.setName(panel.getRelationName());
                    r.setCardinalityTarget((CardinalityEnum) panel.getComboBoxCardinalityTarget().getSelectedItem());
                    ComponentWidgetBase compWidget = (ComponentWidgetBase) panel.getComboBoxTarget().getSelectedItem();
                    createConnection(relation, w, compWidget);
                }
            } else if (relation instanceof UseRelationComponent) {
                ClassUseRelationPanel panel = new ClassUseRelationPanel(w);
                dd = new DialogDescriptor(panel, msg);
                if (DialogDisplayer.getDefault().notify(dd) == NotifyDescriptor.OK_OPTION) {
                    UseRelationComponent r = (UseRelationComponent) relation;
                    r.setName(panel.getRelationName());
                    r.setCardinalitySource((CardinalityEnum) panel.getComboBoxCardinalitySource().getSelectedItem());
                    r.setCardinalityTarget((CardinalityEnum) panel.getComboBoxCardinalityTarget().getSelectedItem());
                    ComponentWidgetBase compWidget = (ComponentWidgetBase) panel.getComboBoxTarget().getSelectedItem();
                    createConnection(relation, w, compWidget);
                }
            } else if (relation instanceof ImplementsRelationComponent && !(widget instanceof InterfaceWidget)) {
                ImplementsRelationPanel panel = new ImplementsRelationPanel((ClassWidget) w);
                dd = new DialogDescriptor(panel, msg);
                if (DialogDisplayer.getDefault().notify(dd) == NotifyDescriptor.OK_OPTION) {
                    ImplementsRelationComponent r = (ImplementsRelationComponent) relation;
                    ComponentWidgetBase compWidget = (ComponentWidgetBase) panel.getComboBoxTarget().getSelectedItem();
                    createConnection(relation, w, compWidget);
                }
            }

            widget.getScene().validate();
            view.paintImmediately(wLoc.x, wLoc.y, wArea.width + dragImage.getWidth(view), wArea.height + dragImage.getHeight(view));
            init = false;
        } catch (InstantiationException | IllegalAccessException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private boolean canAccept(Widget widget, Class<?> droppedClass) {
        if (RelationComponent.class.isAssignableFrom(droppedClass)) {
            if (!(widget instanceof InterfaceWidget)) return true;
            else return !droppedClass.equals(ImplementsRelationComponent.class);
        }
        return false;
    }

    private void createConnection(RelationComponent relation, ComponentWidgetBase source, ComponentWidgetBase target) {
        if (target == null) {
            return;
        }
        relation.setTarget(target.getComponent());
        
        source.getClassDiagramScene().addRelationToScene(relation, source.getComponent(), target.getComponent());
    }
}
