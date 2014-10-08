package org.uml.visual.widgets.providers;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.Transferable;
import java.awt.geom.AffineTransform;
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
import org.uml.model.CardinalityEnum;
import org.uml.model.HasRelationComponent;
import org.uml.model.ImplementsRelationComponent;
import org.uml.model.RelationComponent;
import org.uml.model.UseRelationComponent;
import org.uml.visual.dialogs.ClassHasRelationPanel;
import org.uml.visual.dialogs.ImplementsRelationPanel;
import org.uml.visual.dialogs.IsRelationPanel;
import org.uml.visual.dialogs.ClassUseRelationPanel;
import org.uml.visual.palette.PaletteItemNode;
import org.uml.visual.widgets.ClassWidget;
import org.uml.visual.widgets.ComponentWidgetBase;

/**
 *
 * @author "NUGS"
 */
// Currently does nothing usefull, needs upgrade
public class ClassWidgetAcceptProvider implements AcceptProvider {

    Class<?> drClass;

    @Override
    public ConnectorState isAcceptable(Widget widget, Point point, Transferable t) {
        //DataFlavor flavor = t.getTransferDataFlavors()[2];
        //Class<?> droppedClass = flavor.getRepresentationClass();
        Node node = NodeTransfer.node(t, NodeTransfer.DND_COPY_OR_MOVE);
        PaletteItemNode pin = (PaletteItemNode)node;
        
        //Image dragImage = getImageFromTransferable(t);
        Image dragImage = ImageUtilities.loadImage(pin.getPaletteItem().getIcon());
        JComponent view = widget.getScene().getView();
        Graphics2D g2 = (Graphics2D) view.getGraphics();
//        Rectangle visRect = view.getVisibleRect();
//        view.paintImmediately(visRect.x, visRect.y, visRect.width, visRect.height);
        g2.drawImage(dragImage, widget.getLocation().x + point.getLocation().x, widget.getLocation().y + point.getLocation().y, view);
//        g2.drawImage()
//                dragImage,
//                AffineTransform.getTranslateInstance(point.getLocation().getX(), point.getLocation().getY()),
//                null);
        
        
        //Node node = NodeTransfer.node(t, NodeTransfer.DND_COPY_OR_MOVE);
        //PaletteItemNode pin = (PaletteItemNode)node;
        //view.repaint();
        widget.getScene().validate();
        Class<?> droppedClass = pin.getPaletteItem().getDropClass();
        drClass = droppedClass;
        return canAccept(droppedClass) ? ConnectorState.ACCEPT : ConnectorState.REJECT;
    }

    @Override
    public void accept(Widget widget, Point point, Transferable t) {
        try {
            DialogDescriptor dd = null;
            String msg = "Choose target";
            Class<? extends RelationComponent> relationClass = (Class<? extends RelationComponent>) drClass;
            RelationComponent relation = relationClass.newInstance();
            ClassWidget w = (ClassWidget) widget;
            relation.setSource(w.getComponent());
            if (drClass.getSimpleName().equals("IsRelationComponent")) {
                IsRelationPanel panel = new IsRelationPanel((ClassWidget) widget);
                dd = new DialogDescriptor(panel, msg);
                if (DialogDisplayer.getDefault().notify(dd) == NotifyDescriptor.OK_OPTION) {
                    ComponentWidgetBase compWidget = (ComponentWidgetBase) panel.getComboBoxTarget().getSelectedItem();
                    createConnection(w, compWidget, relation);
                }
            } else if (drClass.getSimpleName().equals("HasRelationComponent")) {
                ClassHasRelationPanel panel = new ClassHasRelationPanel(w);
                dd = new DialogDescriptor(panel, msg);
                if (DialogDisplayer.getDefault().notify(dd) == NotifyDescriptor.OK_OPTION) {
                    HasRelationComponent r = (HasRelationComponent) relation;
                    r.setName(panel.getRelationName());
                    r.setCardinalityTarget((CardinalityEnum) panel.getComboBoxCardinalityTarget().getSelectedItem());
                    ComponentWidgetBase compWidget = (ComponentWidgetBase) panel.getComboBoxTarget().getSelectedItem();
                    createConnection(w, compWidget, relation);
                }
            } else if (drClass.getSimpleName().equals("UseRelationComponent")) {
                ClassUseRelationPanel panel = new ClassUseRelationPanel(w);
                dd = new DialogDescriptor(panel, msg);
                if (DialogDisplayer.getDefault().notify(dd) == NotifyDescriptor.OK_OPTION) {
                    UseRelationComponent r = (UseRelationComponent) relation;
                    r.setName(panel.getRelationName());
                    r.setCardinalitySource((CardinalityEnum) panel.getComboBoxCardinalitySource().getSelectedItem());
                    r.setCardinalityTarget((CardinalityEnum) panel.getComboBoxCardinalityTarget().getSelectedItem());
                    ComponentWidgetBase compWidget = (ComponentWidgetBase) panel.getComboBoxTarget().getSelectedItem();
                    createConnection(w, compWidget, relation);
                }
            } else if (drClass.getSimpleName().equals("ImplementsRelationComponent")) {
                ImplementsRelationPanel panel = new ImplementsRelationPanel(w);
                dd = new DialogDescriptor(panel, msg);
                if (DialogDisplayer.getDefault().notify(dd) == NotifyDescriptor.OK_OPTION) {
                    ImplementsRelationComponent r = (ImplementsRelationComponent) relation;
                    ComponentWidgetBase compWidget = (ComponentWidgetBase) panel.getComboBoxTarget().getSelectedItem();
                    createConnection(w, compWidget, relation);
                }
            }

        } catch (InstantiationException | IllegalAccessException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private boolean canAccept(Class<?> droppedClass) {

        return droppedClass.equals(RelationComponent.class)
                || droppedClass.getSuperclass().equals(RelationComponent.class);

    }

    private void createConnection(ClassWidget sourceW, ComponentWidgetBase targetW, RelationComponent relation) {
        if (targetW == null) {
            return;
        }
        relation.setTarget(targetW.getComponent());
        sourceW.getClassDiagramScene().addEdge(relation);
        sourceW.getClassDiagramScene().setEdgeSource(relation, relation.getSource());
        sourceW.getClassDiagramScene().setEdgeTarget(relation, relation.getTarget());
    }
}
