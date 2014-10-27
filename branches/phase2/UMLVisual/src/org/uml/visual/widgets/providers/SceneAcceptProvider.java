package org.uml.visual.widgets.providers;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import org.netbeans.api.visual.action.AcceptProvider;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.widget.Widget;
import org.openide.nodes.Node;
import org.openide.nodes.NodeTransfer;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.windows.WindowManager;
import org.uml.model.ClassDiagramComponent;
import org.uml.model.RelationComponent;
import org.uml.visual.dialogs.AddRelationDialog;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.palette.*;

/**
 *
 * @author Uros
 */
public class SceneAcceptProvider implements AcceptProvider {

    private final ClassDiagramScene classDiagramScene;

    public SceneAcceptProvider(ClassDiagramScene classDiagramScene) {
        this.classDiagramScene = classDiagramScene;
    }

    @Override
    public ConnectorState isAcceptable(Widget widget, Point point, Transferable t) {
        // iscrtavanje tokom drag-a
        Node node = NodeTransfer.node(t, NodeTransfer.DND_COPY_OR_MOVE);
        PaletteItemNode pin = (PaletteItemNode) node;
        //Image dragImage = getImageFromTransferable(t);
        Image dragImage = ImageUtilities.loadImage(pin.getPaletteItem().getIcon());
        JComponent view = classDiagramScene.getView();
        Graphics2D g2 = (Graphics2D) view.getGraphics();
        view.paint(g2);
//        Rectangle visRect = view.getVisibleRect();
//        view.paintImmediately(visRect.x, visRect.y, visRect.width, visRect.height);
        
        //view.paintImmediately(point.x, point.y, dragImage.getWidth(view), dragImage.getHeight(view));
        g2.drawImage(dragImage, widget.getLocation().x + point.getLocation().x, widget.getLocation().y + point.getLocation().y, view);
//        g2.drawImage(
//                dragImage,
//                AffineTransform.getTranslateInstance(point.getLocation().getX(), point.getLocation().getY()),
//                null);

        //DataFlavor flavor = t.getTransferDataFlavors()[2];
        //Class droppedClass = flavor.getRepresentationClass();
        return ConnectorState.ACCEPT;
        //return canAccept(droppedClass) ? ConnectorState.ACCEPT : ConnectorState.REJECT;
    }

    @Override
    public void accept(Widget widget, Point point, Transferable t) { 
//     Jako ruzno! Osmisliti kako da izvlacimo iz DataFlavor-a bez gadjanja indeksa!
//     if (t.getTransferDataFlavors()[2].getRepresentationClass().getSimpleName().contains("RelationComponent")) {
//     Class<? extends RelationComponent> droppedClass = (Class<? extends RelationComponent>) t.getTransferDataFlavors()[2].getRepresentationClass();
//     Class<? extends ClassDiagramComponent> droppedClass = (Class<? extends ClassDiagramComponent>) t.getTransferDataFlavors()[2].getRepresentationClass();

        Node node = NodeTransfer.node(t, NodeTransfer.DND_COPY_OR_MOVE);
        PaletteItemNode pin = (PaletteItemNode) node;
        Class<?> droppedComponentClass = pin.getPaletteItem().getDropClass();
        Object cobj = null;
        try {
            cobj = droppedComponentClass.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            Exceptions.printStackTrace(ex);
        }

        if (droppedComponentClass.getSuperclass().equals(ClassDiagramComponent.class)) {
            ClassDiagramComponent component = (ClassDiagramComponent) cobj;
            
            Widget w = classDiagramScene.addNode(component);
            w.setPreferredLocation(point);
            component.setPosition(w.getLocation());

//            WidgetAction editorAction = ActionFactory.createInplaceEditorAction(new NameEditorAction((NameableWidget) w));
//            ActionFactory.getInplaceEditorController(editorAction).openEditor(((ComponentWidgetBase) w).getNameLabel());
//            classDiagramScene.getView().addMouseListener(new MouseAdapterZaView(editorAction));
        } else if (droppedComponentClass.getSuperclass().equals(RelationComponent.class)) {
            RelationComponent component = (RelationComponent) cobj;
            AddRelationDialog dialog = new AddRelationDialog(null, classDiagramScene, true);
            for (int i = 0; i < dialog.getRelationComponents().getItemCount(); i++) {
                if (component.getClass().getSimpleName().equals(dialog.getRelationComponents().getItemAt(i).getClass().getSimpleName())) {
                    dialog.getRelationComponents().setSelectedIndex(i);
                    break;
                }
            }
            dialog.setLocationRelativeTo(WindowManager.getDefault().getMainWindow());
            dialog.setVisible(true);
        } else {
            throw new RuntimeException("Unknown dropped component!");
        }

        classDiagramScene.validate();
    }

    private Image getImageFromTransferable(Transferable transferable) {
        Object o = null;
        try {
            o = transferable.getTransferData(DataFlavor.imageFlavor);
        } catch (IOException | UnsupportedFlavorException ex) {
            ex.printStackTrace(System.out);
        }
        return o instanceof Image ? (Image) o : ImageUtilities.loadImage("org/netbeans/shapesample/palette/shape1.png");
    }

    public boolean canAccept(Class droppedClass) {
        return droppedClass.equals(ClassDiagramComponent.class)
                || droppedClass.getSuperclass().equals(ClassDiagramComponent.class)
                || droppedClass.getSuperclass().equals(RelationComponent.class);
    }
}
