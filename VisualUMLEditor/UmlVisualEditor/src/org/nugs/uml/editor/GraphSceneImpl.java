/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nugs.uml.editor;

import com.sun.org.apache.bcel.internal.generic.Select;
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
import org.netbeans.api.visual.action.AcceptProvider;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.action.TextFieldInplaceEditor;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.layout.Layout;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.netbeans.shapesample.palette.UMLClassWidget;
import org.openide.util.ImageUtilities;

/**
 *
 * @author zoran
 */
public class GraphSceneImpl extends GraphScene<MyNode, String> {

    private LayerWidget mainLayer;
    private WidgetAction editorAction = ActionFactory.createInplaceEditorAction(new LabelTextFieldEditor());

    public GraphSceneImpl() {
        mainLayer = new LayerWidget(this);
        addChild(mainLayer);

//        LabelWidget label = new LabelWidget(this, "Ja sam Label");
//        mainLayer.addChild(label);


        getActions().addAction(ActionFactory.createAcceptAction(new AcceptProvider() {

            @Override
            public ConnectorState isAcceptable(Widget widget, Point point, Transferable transferable) {
                Image dragImage = getImageFromTransferable(transferable);
                JComponent view = getView();
                Graphics2D g2 = (Graphics2D) view.getGraphics();
                Rectangle visRect = view.getVisibleRect();
                view.paintImmediately(visRect.x, visRect.y, visRect.width, visRect.height);
                g2.drawImage(dragImage,
                        AffineTransform.getTranslateInstance(point.getLocation().getX(),
                        point.getLocation().getY()),
                        null);
                return ConnectorState.ACCEPT;
            }

            @Override
            public void accept(Widget widget, Point point, Transferable transferable) {

                UMLClassWidget classO = new UMLClassWidget(GraphSceneImpl.this);
//                classO.setClassName ("Class 1");
//               classO.setPreferredLocation(widget.convertLocalToScene(point));               
//                  GraphSceneImpl.this.addChild(classO);

                //Image image = getImageFromTransferable(transferable);
                Widget w = GraphSceneImpl.this.addNode(new MyNode(classO));

                w.setPreferredLocation(widget.convertLocalToScene(point));

            }
        }));

        getActions().addAction(ActionFactory.createZoomAction());
        getActions().addAction(ActionFactory.createPanAction());

    }

    private class LabelTextFieldEditor implements TextFieldInplaceEditor {

        @Override
        public boolean isEnabled(Widget widget) {
            return true;
        }

        @Override
        public String getText(Widget widget) {
            return ((LabelWidget) widget).getLabel();
        }

        @Override
        public void setText(Widget widget, String text) {
            ((LabelWidget) widget).setLabel(text);
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

    @Override
    protected Widget attachNodeWidget(MyNode node) {
        //IconNodeWidget widget = new IconNodeWidget(this);
//        widget.setImage(node.getImage());
//        widget.setLabel(Long.toString(node.hashCode()));
        UMLClassWidget widget = new UMLClassWidget(GraphSceneImpl.this);
        widget.setClassName(" Class 1");


        //widget.addField(widget.createFieldWidget("member1"));
        //widget.addField(widget.createFieldWidget("member2"));
        //widget.addField(widget.createFieldWidget("member3"));


        //widget.addOperation(widget.createMethodWidget("operation2"));
        //widget.addOperation(widget.createMethodWidget("operation3"));

        //double-click, the event is consumed while double-clicking only:
        //widget.getLabelWidget().getActions().addAction(editorAction);
//        LabelWidget className = (LabelWidget) widget.getLabelWidget();
//        className.getActions().addAction(editorAction);
        
        //single-click, the event is not consumed:
        widget.getActions().addAction(createSelectAction());

        //mouse-dragged, the event is consumed while mouse is dragged:
        widget.getActions().addAction(ActionFactory.createMoveAction());

        //mouse-over, the event is consumed while the mouse is over the widget:
        widget.getActions().addAction(createObjectHoverAction());

        mainLayer.addChild(widget);
        return widget;
    }

    @Override
    protected Widget attachEdgeWidget(String e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void attachEdgeSourceAnchor(String e, MyNode n, MyNode n1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void attachEdgeTargetAnchor(String e, MyNode n, MyNode n1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
