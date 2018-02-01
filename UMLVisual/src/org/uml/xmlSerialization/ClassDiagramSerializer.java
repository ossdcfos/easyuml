package org.uml.xmlSerialization;

import java.awt.Rectangle;
import org.uml.xmlSerialization.components.ComponentSerializer;
import org.uml.xmlSerialization.relations.HasRelationSerializer;
import org.uml.xmlSerialization.relations.IsRelationSerializer;
import org.uml.xmlSerialization.relations.ImplementsRelationSerializer;
import org.uml.xmlSerialization.relations.UseRelationSerializer;
import org.uml.xmlSerialization.relations.RelationSerializer;
import org.uml.xmlSerialization.components.EnumSerializer;
import org.uml.xmlSerialization.components.ClassSerializer;
import org.uml.xmlSerialization.components.InterfaceSerializer;
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
import org.uml.model.components.PackageComponent;
import org.uml.model.relations.AggregationRelation;
import org.uml.model.relations.CompositionRelation;
import org.uml.model.relations.HasRelation;
import org.uml.model.relations.IsRelation;
import org.uml.model.relations.RelationBase;
import org.uml.model.relations.UseRelation;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.components.ComponentWidgetBase;
import org.uml.xmlSerialization.components.PackageSerializer;
//import org.uml.xmltesting.serialization.ClassDiagramXmlSerializerDeserializer;

/**
 *
 * @author Stefan
 */
public class ClassDiagramSerializer implements XmlSerializer {

    private static ClassDiagramSerializer instance;
    private ClassDiagramScene classDiagramScene;
    private final HashMap<Class<?>, ComponentSerializer> componentSerializers;
    private final HashMap<Class<?>, RelationSerializer> relationSerializers;

    private ClassDiagramSerializer() {
        componentSerializers = new HashMap<>();
        relationSerializers = new HashMap<>();
        componentSerializers.put(ClassComponent.class, new ClassSerializer());
        componentSerializers.put(InterfaceComponent.class, new InterfaceSerializer());
        componentSerializers.put(EnumComponent.class, new EnumSerializer());
        componentSerializers.put(PackageComponent.class, new PackageSerializer());
        relationSerializers.put(IsRelation.class, new IsRelationSerializer());
        relationSerializers.put(ImplementsRelation.class, new ImplementsRelationSerializer());
        relationSerializers.put(HasRelation.class, new HasRelationSerializer());
        relationSerializers.put(AggregationRelation.class, new HasRelationSerializer());
        relationSerializers.put(CompositionRelation.class, new HasRelationSerializer());
        relationSerializers.put(UseRelation.class, new UseRelationSerializer());
    }

    public static ClassDiagramSerializer getInstance() {
        if (instance == null) {
            instance = new ClassDiagramSerializer();
        }
        return instance;
    }

    public void setClassDiagramScene(ClassDiagramScene scene) {
        classDiagramScene = scene;
    }

    /**
     * Serializes classDiagram object to xml tree.
     *
     * @param node in which to put classDiagram object.
     */
    @Override
    public void serialize(Element node) {
//        System.out.println(new ClassDiagramXmlSerializerDeserializer(classDiagramScene).serialize(classDiagram));
        ClassDiagram classDiagram = classDiagramScene.getClassDiagram();
        if (classDiagram.getName() != null) {
            node.addAttribute("name", classDiagram.getName());
        }
//        node.addAttribute("showIcons", Boolean.toString(classDiagramScene.isShowIcons()));
//        node.addAttribute("showMembers", Boolean.toString(classDiagramScene.isShowMembers()));
//        node.addAttribute("showSimpleTypeNames", Boolean.toString(classDiagramScene.isShowSimpleTypes()));
        Element classDiagramComponents = node.addElement("ClassDiagramComponents");
        for (Widget layer : classDiagramScene.getChildren()) {
            for (Widget w : layer.getChildren()) {
                if (!(w instanceof ComponentWidgetBase))
                    continue;
                ComponentWidgetBase widgetBase = (ComponentWidgetBase)w;
                ComponentBase component = widgetBase.getComponent();
                Element componentNode = classDiagramComponents.addElement(component.getClass().getSimpleName().replace("Component", ""));
                ComponentSerializer serializer = componentSerializers.get(component.getClass());
                serializer.setClassDiagramComponent(component);
                serializer.serialize(componentNode);
                componentNode.addAttribute("xPosition", String.valueOf(w.getPreferredLocation().getX()));
                componentNode.addAttribute("yPosition", String.valueOf(w.getPreferredLocation().getY()));
                Rectangle bounds = w.getBounds();
                if (bounds != null) {
                    componentNode.addAttribute("width", String.valueOf(bounds.getSize().width));
                    componentNode.addAttribute("height", String.valueOf(bounds.getSize().height));
                }
            }
        }

        Element classDiagramRelations = node.addElement("ClassDiagramRelations");
        for (RelationBase relation : classDiagram.getRelations()) {
            String name;
            // TODO fix this
            if (HasBaseRelation.class.isAssignableFrom(relation.getClass())) name = "HasRelation";
            else name = relation.getClass().getSimpleName();
            Element relationNode = classDiagramRelations.addElement(name);

            RelationSerializer serializer = relationSerializers.get(relation.getClass());
            serializer.setClassDiagramRelation(relation);
            serializer.serialize(relationNode);
        }
    }
}
