/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.xmlSerialization;

import java.util.HashMap;
import org.dom4j.Element;
import org.netbeans.api.visual.widget.Widget;
import org.uml.model.ClassComponent;
import org.uml.model.ClassDiagram;
import org.uml.model.ComponentBase;
import org.uml.model.EnumComponent;
import org.uml.model.relations.HasBaseRelationComponent;
import org.uml.model.relations.ImplementsRelationComponent;
import org.uml.model.InterfaceComponent;
import org.uml.model.relations.IsRelationComponent;
import org.uml.model.relations.RelationComponent;
import org.uml.model.relations.UseRelationComponent;
import org.uml.visual.widgets.ClassDiagramScene;

/**
 *
 * @author Stefan
 */
public class ClassDiagramXmlSerializer implements XmlSerializer {

    private static ClassDiagramXmlSerializer instance;
    private ClassDiagram classDiagram;
    private ClassDiagramScene classDiagramScene;
    private HashMap<Class<?>, ClassDiagramComponentSerializer> componentSerializers;
    private HashMap<Class<?>, RelationSerializer> relationSerializers;

    private ClassDiagramXmlSerializer() {
        componentSerializers = new HashMap<>();
        relationSerializers = new HashMap<>();
        componentSerializers.put(ClassComponent.class, new ClassSerializer());
        componentSerializers.put(InterfaceComponent.class, new InterfaceSerializer());
        componentSerializers.put(EnumComponent.class, new EnumSerializer());
        relationSerializers.put(UseRelationComponent.class, new UseRelationSerializer());
        relationSerializers.put(IsRelationComponent.class, new IsRelationSerializer());
        relationSerializers.put(HasBaseRelationComponent.class, new HasRelationSerializer());
        relationSerializers.put(ImplementsRelationComponent.class, new ImplementsRelationSerializer());
    }

    public static ClassDiagramXmlSerializer getInstance() {
        if (instance == null) {
            instance = new ClassDiagramXmlSerializer();
        }
        return instance;
    }

    public void setClassDiagramScene(ClassDiagramScene scene) {
        classDiagramScene = scene;
    }

    public void setClassDiagram(ClassDiagram diagram) {
        this.classDiagram = diagram;
    }
    
    /**
     * Serializes classDiagram object to xml tree.
     * @param node in which to put classDiagram object.
     */
    @Override
    public void serialize(Element node) {
        if (classDiagram.getName() != null) {
            node.addAttribute("name", classDiagram.getName());
        }
        Element classDiagramComponents = node.addElement("ClassDiagramComponents");
        for (ComponentBase component : classDiagram.getComponents().values()) {
            if (component instanceof ClassComponent) {
                Element componentNode = classDiagramComponents.addElement("Class");
                Widget w = classDiagramScene.findWidget(component);
                componentNode.addAttribute("xPosition", String.valueOf(w.getPreferredLocation().getX()));
                componentNode.addAttribute("yPosition", String.valueOf(w.getPreferredLocation().getY()));
                ClassDiagramComponentSerializer serializer = componentSerializers.get(component.getClass());
                serializer.addClassDiagramComponent(component);
                serializer.serialize(componentNode);

            }
            if (component instanceof InterfaceComponent) {
                Element componentNode = classDiagramComponents.addElement("Interface");
                Widget w = classDiagramScene.findWidget(component);
                componentNode.addAttribute("xPosition", String.valueOf(w.getPreferredLocation().getX()));
                componentNode.addAttribute("yPosition", String.valueOf(w.getPreferredLocation().getY()));                
                ClassDiagramComponentSerializer serializer = componentSerializers.get(component.getClass());
                serializer.addClassDiagramComponent(component);
                serializer.serialize(componentNode);
            }
            if (component instanceof EnumComponent) {
                Element componentNode = classDiagramComponents.addElement("Enum");
                Widget w = classDiagramScene.findWidget(component);
                componentNode.addAttribute("xPosition", String.valueOf(w.getPreferredLocation().getX()));
                componentNode.addAttribute("yPosition", String.valueOf(w.getPreferredLocation().getY()));
                ClassDiagramComponentSerializer serializer = componentSerializers.get(component.getClass());
                serializer.addClassDiagramComponent(component);
                serializer.serialize(componentNode);
            }
        }
        Element classDiagramRelations = node.addElement("ClassDiagramRelations");
        for (RelationComponent component : classDiagram.getRelations().values()) {
            if (component instanceof UseRelationComponent) {
                Element componentNode = classDiagramRelations.addElement("UseRelation");
                
                RelationSerializer serializer = relationSerializers.get(component.getClass());
                serializer.addRelationComponent(component);
                serializer.serialize(componentNode);
            }
            if (component instanceof HasBaseRelationComponent) {
                Element componentNode = classDiagramRelations.addElement("HasRelation");
                
                RelationSerializer serializer = relationSerializers.get(component.getClass());
                serializer.addRelationComponent(component);
                serializer.serialize(componentNode);
            }
            if (component instanceof IsRelationComponent) {
                Element componentNode = classDiagramRelations.addElement("IsRelation");
                
                RelationSerializer serializer = relationSerializers.get(component.getClass());
                serializer.addRelationComponent(component);
                serializer.serialize(componentNode);
            }
            if (component instanceof ImplementsRelationComponent) {
                Element componentNode = classDiagramRelations.addElement("ImplementsRelation");
                
                RelationSerializer serializer = relationSerializers.get(component.getClass());
                serializer.addRelationComponent(component);
                serializer.serialize(componentNode);
            }
        }
    }
}
