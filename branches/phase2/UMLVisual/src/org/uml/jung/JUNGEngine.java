/*
 * Copyright (c) 2014, Ilija
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.uml.jung;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.List;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;
import org.uml.model.components.ComponentBase;
import org.uml.model.relations.RelationBase;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.components.ComponentWidgetBase;

/**
 *
 * @author Ilija
 */
public class JUNGEngine {

    private ClassDiagramScene scene;

    public JUNGEngine(ClassDiagramScene scene) {
        this.scene = scene;
    }

    public void applyJUNGLayout() {

        //only CircleLayout is currently supported!
//        Result resultCD = Utilities.actionsGlobalContext().lookupResult(ClassDiagramScene.class);

//        Collection<Object> coll = resultCD.allInstances();
//        ClassDiagramScene classDiagramScene = null;
//        if (!coll.isEmpty()) {
//            for (Object o : coll) {
//                classDiagramScene = (ClassDiagramScene) o;
//            }
//        }
        Graph<ComponentWidgetBase, RelationBase> graph = null;
        if (scene != null) {
            graph = UMLSparseMultigraph.create(scene);
        }

//        DirectedSparseMultigraph g;
//        g = new DirectedSparseMultigraph<MyNode, MyLink>();
//        
//        MyNode n1, n2, n3, n4, n5;
//        n1 = new MyNode(1);
//        n2 = new MyNode(2);
//        n3 = new MyNode(3);
//        n4 = new MyNode(4);
//        n5 = new MyNode(5);
//        
//        g.addEdge(new MyLink(2.0, 48), n1, n2, EdgeType.DIRECTED);
//        g.addEdge(new MyLink(2.0, 48), n2, n3, EdgeType.DIRECTED);
//        g.addEdge(new MyLink(3.0, 192), n3, n5, EdgeType.DIRECTED);
//        g.addEdge(new MyLink(2.0, 48), n5, n4, EdgeType.DIRECTED);
//        g.addEdge(new MyLink(2.0, 48), n4, n2);
//        g.addEdge(new MyLink(2.0, 48), n3, n1);
//        g.addEdge(new MyLink(10.0, 48), n2, n5);
//        
//        System.out.println(g.toString());

        if (graph != null) {
            Layout<ComponentWidgetBase, RelationBase> layout = new CircleLayout<>(graph);
//            Layout<ComponentWidgetBase, RelationComponent> layout = new SpringLayout<>(graph); // ovaj layout isto moze da radi

            layout.setSize(new Dimension(700, 700));
            BasicVisualizationServer<ComponentWidgetBase, RelationBase> vv = new BasicVisualizationServer<>(layout);
//            vv.setPreferredSize(new Dimension(1350, 1350));


            applyLayoutOnExistingWidget(layout);
// for testing
//            JFrame frame = new JFrame("Simple Graph View");
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.getContentPane().add(vv);
//            frame.pack();
//            frame.setVisible(true);
        }
    }

    public ClassDiagramScene getScene() {
        return scene;
    }

    public void setScene(ClassDiagramScene scene) {
        this.scene = scene;
    }

    private void applyLayoutOnExistingWidget(Layout<ComponentWidgetBase, RelationBase> layout) {
        LayerWidget mainLayer = scene.getMainLayer();
        List<Widget> mainLayerChildren = mainLayer.getChildren();
        HashMap<ComponentBase, ComponentWidgetBase> components = new HashMap<>();
        if (mainLayerChildren != null && !mainLayerChildren.isEmpty()) {
            for (Widget widget : mainLayerChildren) {
                if (widget instanceof ComponentWidgetBase) {
                    ComponentWidgetBase cwb = (ComponentWidgetBase) widget;
                    components.put(cwb.getComponent(), cwb);
                    System.out.println("Koordinate pre: x: " + cwb.getPreferredLocation().getX() + " y: " + cwb.getPreferredLocation().getY());

                    Point2D coord = layout.transform(cwb);
//                    Graphics2D graphics = cwb.returnGraphics();
//                    graphics.getDeviceConfiguration().getBounds().setLocation((int) coord.getX(), (int) coord.getY());
                    System.out.println("Koordinate COORD -a: x: " + coord.getX() + " y: " + coord.getY());

//                    Graphics2D graphics = cwb.returnGraphics();

//                    double width = cwb.getPreferredBounds().getWidth();
//                    double height = cwb.getPreferredBounds().getHeight();
//                    cwb.setPreferredBounds(new Rectangle((int) coord.getX(), (int) coord.getY(), (int) width, (int) height));

                    cwb.setPreferredLocation(new Point((int) coord.getX(), (int) coord.getY()));


                    //                    Graphics2D newGraphics = graphics;
                    //                    newGraphics.translate(coord.getX(), coord.getY());
                    //                    cwb.setGraphics(newGraphics);
                    cwb.repaint();
                    System.out.println("Koordinate posle: x: " + cwb.getPreferredLocation().getX() + " y: " + cwb.getPreferredLocation().getY());
//                    cwb.setPos((float) coord.getX(), (float) coord.getY());
//                    cwb.repaint();
                } else {
                    System.out.println("ERROR in mainLayer.getChildren");
                }
            }
        }
        scene.repaint();
    }
}
