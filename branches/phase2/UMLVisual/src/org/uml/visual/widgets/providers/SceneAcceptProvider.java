package org.uml.visual.widgets.providers;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
import org.uml.model.ClassDiagram;
import org.uml.model.components.ComponentBase;
import org.uml.model.relations.RelationBase;
import org.uml.visual.dialogs.AddRelationDialog;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.palette.*;

/**
 *
 * @author Uros
 */
public class SceneAcceptProvider implements AcceptProvider {

    private final ClassDiagramScene classDiagramScene;
    private Point lastPoint = new Point(0, 0);

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
        //view.paint(g2);
        Rectangle visRect = view.getVisibleRect();
        view.paintImmediately(visRect.x, visRect.y, visRect.width, visRect.height);

//        view.paintImmediately(point.x, point.y, dragImage.getWidth(view), dragImage.getHeight(view));
//        int absdX = Math.abs(point.x - lastPoint.x);
//        int absdY = Math.abs(point.y - lastPoint.y);
//        
//        view.paintImmediately(point.x-absdX*5, point.y-absdY*5, dragImage.getWidth(view)+absdX*5, dragImage.getHeight(view)+absdY*5);
        g2.drawImage(dragImage, widget.getLocation().x + point.getLocation().x, widget.getLocation().y + point.getLocation().y, view);

//        g2.drawImage(
//                dragImage,
//                AffineTransform.getTranslateInstance(point.getLocation().getX(), point.getLocation().getY()),
//                null);
        lastPoint = point;

        return ConnectorState.ACCEPT;
        //return canAccept(droppedClass) ? ConnectorState.ACCEPT : ConnectorState.REJECT;
    }

    @Override
    public void accept(Widget widget, Point point, Transferable t) {
        Node node = NodeTransfer.node(t, NodeTransfer.DND_COPY_OR_MOVE);
        PaletteItemNode pin = (PaletteItemNode) node;
        Class<?> droppedComponentClass = pin.getPaletteItem().getDropClass();
        try {

            if (ComponentBase.class.isAssignableFrom(droppedComponentClass)) {
                Constructor<?> cons = droppedComponentClass.getConstructor(ClassDiagram.class);
                Object cobj = cons.newInstance(classDiagramScene.getClassDiagram());
                ComponentBase component = (ComponentBase) cobj;

                Widget w = classDiagramScene.addNode(component);
                w.setPreferredLocation(point);
                component.setPosition(w.getLocation());

//            WidgetAction editorAction = ActionFactory.createInplaceEditorAction(new NameEditorAction((NameableWidget) w));
//            ActionFactory.getInplaceEditorController(editorAction).openEditor(((ComponentWidgetBase) w).getNameLabel());
//            classDiagramScene.getView().addMouseListener(new MouseAdapterZaView(editorAction));
            } else if (RelationBase.class.isAssignableFrom(droppedComponentClass)) {
                Object cobj = droppedComponentClass.newInstance();
                RelationBase component = (RelationBase) cobj;
                AddRelationDialog dialog = new AddRelationDialog(null, classDiagramScene, true);
                for (int i = 0; i < dialog.getRelationComponents().getItemCount(); i++) {
                    RelationBase selectedComponent = dialog.getRelationComponents().getItemAt(i);
                    if (component.getClass() == selectedComponent.getClass()) {
                        dialog.getRelationComponents().setSelectedIndex(i);
                        break;
                    }
                }
                dialog.setLocationRelativeTo(WindowManager.getDefault().getMainWindow());
                dialog.setVisible(true);
            } else {
                throw new RuntimeException("Unknown dropped component: " + droppedComponentClass + "!");
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException | RuntimeException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            Exceptions.printStackTrace(ex);
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

    public boolean canAccept(Class<?> droppedClass) {
        return droppedClass.equals(ComponentBase.class)
                || droppedClass.getSuperclass().equals(ComponentBase.class)
                || droppedClass.getSuperclass().equals(RelationBase.class);
    }
}
