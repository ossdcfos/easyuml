package org.uml.visual.widgets.providers;

import java.awt.Color;
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
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.ImageUtilities;
import org.uml.model.ClassDiagramComponent;
import org.uml.visual.widgets.providers.MouseAdapterZaView;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.ClassWidget;
import org.uml.visual.widgets.Nameable;
import org.uml.visual.widgets.ComponentWidgetBase;
import org.uml.visual.widgets.actions.LabelTextFieldEditorAction;
import org.uml.visual.widgets.actions.NameEditorAction;

/**
 *
 * @author Uros
 */
public class SceneAcceptProvider implements AcceptProvider {

    private ClassDiagramScene classDiagramScene;

    public SceneAcceptProvider(ClassDiagramScene classDiagramScene) {

        this.classDiagramScene = classDiagramScene;
    }

    @Override
    public ConnectorState isAcceptable(Widget widget, Point point, Transferable t) {
        Image dragImage = getImageFromTransferable(t);
        JComponent view = classDiagramScene.getView();
        Graphics2D g2 = (Graphics2D) view.getGraphics();
        Rectangle visRect = view.getVisibleRect();
        view.paintImmediately(visRect.x, visRect.y, visRect.width, visRect.height);
        g2.drawImage(dragImage,
                AffineTransform.getTranslateInstance(point.getLocation().getX(),
                point.getLocation().getY()),
                null);
        DataFlavor flavor = t.getTransferDataFlavors()[2];
        Class droppedClass = flavor.getRepresentationClass();
        return canAccept(droppedClass) ? ConnectorState.ACCEPT : ConnectorState.REJECT;
    }

    @Override
    public void accept(Widget widget, Point point, Transferable t) {
        Class<? extends ClassDiagramComponent> droppedClass = (Class<? extends ClassDiagramComponent>) t.getTransferDataFlavors()[2].getRepresentationClass(); // Jako ruzno! Osmisliti kako da izvlacimo iz DataFlavor-a bez gadjanja indeksa!
        try {
            Widget w=classDiagramScene.addNode(droppedClass.newInstance());
            w.setPreferredLocation(widget.convertLocalToScene(point));
            
            classDiagramScene.validate();
            
//            WidgetAction editorAction = ActionFactory.createInplaceEditorAction(new LabelTextFieldEditorAction());
//            ActionFactory.getInplaceEditorController(editorAction).openEditor(((ComponentWidgetBase)w).getNameLabel());
            WidgetAction editorAction = ActionFactory.createInplaceEditorAction(new NameEditorAction((Nameable)w));
            ActionFactory.getInplaceEditorController(editorAction).openEditor(((ComponentWidgetBase)w).getNameLabel());
            classDiagramScene.getView().addMouseListener(new MouseAdapterZaView(editorAction));
            
        
        } catch (InstantiationException | IllegalAccessException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

    }

    private Image getImageFromTransferable(Transferable transferable) {
        Object o = null;
        try {
            o = transferable.getTransferData(DataFlavor.imageFlavor);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (UnsupportedFlavorException ex) {
            ex.printStackTrace();
        }
        return o instanceof Image ? (Image) o : ImageUtilities.loadImage("org/netbeans/shapesample/palette/shape1.png");
    }

    public boolean canAccept(Class droppedClass) {

        return droppedClass.equals(ClassDiagramComponent.class)
                || droppedClass.getSuperclass().equals(ClassDiagramComponent.class);
    }
}
