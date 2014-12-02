package org.uml.xmlSerialization;

import java.util.HashMap;
import org.dom4j.Element;
import org.netbeans.api.visual.widget.Widget;
import org.uml.model.components.ClassComponent;
import org.uml.model.ClassDiagram;
import org.uml.model.components.ComponentBase;
import org.uml.model.components.EnumComponent;
import org.uml.model.relations.HasBaseRelation;
import org.uml.model.relations.ImplementsRelation;
import org.uml.model.components.InterfaceComponent;
import org.uml.model.relations.AggregationRelation;
import org.uml.model.relations.CompositionRelation;
import org.uml.model.relations.IsRelation;
import org.uml.model.relations.RelationBase;
import org.uml.model.relations.UseRelation;
import org.uml.visual.widgets.ClassDiagramScene;
//import org.uml.xmltesting.serialization.ClassDiagramXmlSerializerDeserializer;

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
        relationSerializers.put(UseRelation.class, new UseRelationSerializer());
        relationSerializers.put(IsRelation.class, new IsRelationSerializer());
        relationSerializers.put(AggregationRelation.class, new HasRelationSerializer());
        relationSerializers.put(CompositionRelation.class, new HasRelationSerializer());
        relationSerializers.put(ImplementsRelation.class, new ImplementsRelationSerializer());
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
//        System.out.println(new ClassDiagramXmlSerializerDeserializer(classDiagramScene).serialize(classDiagram));
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
//                componentNode.addAttribute("width", String.valueOf(w.getPreferredBounds().width));
//                componentNode.addAttribute("height", String.valueOf(w.getPreferredBounds().height));
//                componentNode.addAttribute("xOff", String.valueOf(w.getPreferredBounds().x));
//                componentNode.addAttribute("yOff", String.valueOf(w.getPreferredBounds().y));
                
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
        for (RelationBase component : classDiagram.getRelations()) {
            if (component instanceof UseRelation) {
                Element componentNode = classDiagramRelations.addElement("UseRelation");
                
                RelationSerializer serializer = relationSerializers.get(component.getClass());
                serializer.addRelationComponent(component);
                serializer.serialize(componentNode);
            }
            if (component instanceof HasBaseRelation) {
                Element componentNode = classDiagramRelations.addElement("HasRelation");
                
                RelationSerializer serializer = relationSerializers.get(component.getClass());
                serializer.addRelationComponent(component);
                serializer.serialize(componentNode);
            }
            if (component instanceof IsRelation) {
                Element componentNode = classDiagramRelations.addElement("IsRelation");
                
                RelationSerializer serializer = relationSerializers.get(component.getClass());
                serializer.addRelationComponent(component);
                serializer.serialize(componentNode);
            }
            if (component instanceof ImplementsRelation) {
                Element componentNode = classDiagramRelations.addElement("ImplementsRelation");
                
                RelationSerializer serializer = relationSerializers.get(component.getClass());
                serializer.addRelationComponent(component);
                serializer.serialize(componentNode);
            }
        }
    }
}
