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

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;
import org.uml.model.ClassDiagramComponent;
import org.uml.model.RelationComponent;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.ComponentWidgetBase;
import org.uml.visual.widgets.RelationWidgetBase;

/**
 *
 * We had to use SparseMultiGraph since it is that suupports multiple relations
 * @author Ilija The Graph Master
 */
public class UMLSparseMultigraph extends SparseMultigraph<ComponentWidgetBase, RelationComponent> {

    /**
     * Creates and return s JUNG SparseMultigraph for given ClassDiagramScene
     * @param classDiagramScene
     * @return 
     */
    public static Graph create(ClassDiagramScene classDiagramScene) {

        Graph<ComponentWidgetBase, RelationComponent> graph = new edu.uci.ics.jung.graph.SparseMultigraph<>();

        LayerWidget mainLayer = classDiagramScene.getMainLayer();
        List<Widget> mainLayerChildren = mainLayer.getChildren();
        HashMap<ClassDiagramComponent, ComponentWidgetBase> components = new HashMap<>();
        if (mainLayerChildren != null && !mainLayerChildren.isEmpty()) {
            for (Widget widget : mainLayerChildren) {
                if (widget instanceof ComponentWidgetBase) {
                    ComponentWidgetBase cwb = (ComponentWidgetBase) widget;
                    graph.addVertex(cwb);
                    components.put(cwb.getComponent(), cwb);
                } else {
                    System.out.println("ERROR in mainLayer.getChildren");
                }
            }
        }

        //now we need to make edges

        HashMap<String, RelationComponent> relations = classDiagramScene.getUmlClassDiagram().getRelations();

        for (Map.Entry<ClassDiagramComponent, ComponentWidgetBase> component : components.entrySet()) {
            ClassDiagramComponent classDiagramComponent = component.getKey();
            ComponentWidgetBase componentWidgetBase = component.getValue();

            for (Map.Entry<String, RelationComponent> relation : relations.entrySet()) {
                RelationComponent relationComponent = relation.getValue();
                if (relationComponent.getSource().equals(classDiagramComponent)) {

                    ComponentWidgetBase target = null;

                    for (Map.Entry<ClassDiagramComponent, ComponentWidgetBase> componentInside : components.entrySet()) {
                        ClassDiagramComponent classDiagramComponentInside = componentInside.getKey();
                        ComponentWidgetBase componentWidgetBaseInside = componentInside.getValue();
                        if (relationComponent.getTarget().equals(classDiagramComponentInside)) {
                            target = componentWidgetBaseInside;
                        }
                    }



                    graph.addEdge(relationComponent, componentWidgetBase, target);
                } else if (relationComponent.getTarget().equals(classDiagramComponent)) {

                    ComponentWidgetBase source = null;

                    for (Map.Entry<ClassDiagramComponent, ComponentWidgetBase> componentInside : components.entrySet()) {
                        ClassDiagramComponent classDiagramComponentInside = componentInside.getKey();
                        ComponentWidgetBase componentWidgetBaseInside = componentInside.getValue();
                        if (relationComponent.getSource().equals(classDiagramComponentInside)) {
                            source = componentWidgetBaseInside;
                        }
                    }
                    graph.addEdge(relationComponent, source, componentWidgetBase);
                }


            }
        }








// FOLOWING CODE CANNOT BE USED BECAUSE CONNECTIONLAYER IS ALWAYS EMPTY        
//        LayerWidget connectionLayer = classDiagramScene.getConnectionLayer();
//        List<Widget> connectionLayerChildren = connectionLayer.getChildren();
//        if (connectionLayerChildren != null && !connectionLayerChildren.isEmpty()) {
//            for (Widget widget : connectionLayerChildren) {
//                if (widget instanceof RelationWidgetBase) {
//                    RelationWidgetBase rwb = (RelationWidgetBase) widget;
//
//                    ComponentWidgetBase source = null;
//                    ComponentWidgetBase target = null;
//
//                    for (Map.Entry<ClassDiagramComponent, ComponentWidgetBase> entry : components.entrySet()) {
//                        ClassDiagramComponent classDiagramComponent = entry.getKey();
//                        ComponentWidgetBase componentWidgetBase = entry.getValue();
//                        if (rwb.getRelationComponent().getSource() == classDiagramComponent) {
//                            source = componentWidgetBase;
//                        }
//                        if (rwb.getRelationComponent().getTarget() == classDiagramComponent) {
//                            target = componentWidgetBase;
//                        }
//                    }
//
//                    if (source != null && target != null) {
//
//                        graph.addEdge(rwb, source, target);
//                    }
//                }
//            }
//        }

        System.out.println("The graph g = " + graph.toString());

        return graph;
    }
}
