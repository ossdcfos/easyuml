/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.xmlSerialization;

import java.util.HashMap;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;
import org.uml.model.ClassComponent;
import org.uml.model.ClassDiagram;
import org.uml.model.ClassDiagramComponent;
import org.uml.model.EnumComponent;
import org.uml.model.HasRelationComponent;
import org.uml.model.ImplementsRelationComponent;
import org.uml.model.InterfaceComponent;
import org.uml.model.IsRelationComponent;
import org.uml.model.RelationComponent;
import org.uml.model.UseRelationComponent;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.ClassWidget;

/**
 *
 * @author Stefan
 */
public class ClassDiagramXmlSerializer implements XmlSerializer{
    
    private static ClassDiagramXmlSerializer instance;
    private ClassDiagram classDiagram;
    private ClassDiagramScene classDiagramScene;
    private HashMap<Class, ClassDiagramComponentSerializer> componentSerializers;
    private HashMap<Class, RelationSerializer> relationSerializers;
    
    
    private ClassDiagramXmlSerializer() {
        componentSerializers = new HashMap<>();
        relationSerializers = new HashMap<>();
        componentSerializers.put(ClassComponent.class, new ClassSerializer());
        componentSerializers.put(InterfaceComponent.class, new InterfaceSerializer());
        componentSerializers.put(EnumComponent.class, new EnumSerializer());
        relationSerializers.put(UseRelationComponent.class, new UseRelationSerializer());
        relationSerializers.put(IsRelationComponent.class, new IsRelationSerializer());
        relationSerializers.put(HasRelationComponent.class, new HasRelationSerializer());
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

    @Override
    public void serialize(Element node) {
//        LayerWidget mainLayer = classDiagramScene.getMainLayer();
//        List<Widget> widgets = mainLayer.getChildren();
//        if (classDiagramScene.getUmlClassDiagram().getName() != null) node.addAttribute("name", classDiagramScene.getUmlClassDiagram().getName());
//        Element classDiagramComponents = node.addElement("ClassDiagramComponents");
//        for (int i = 0; i < widgets.size(); i++) {
//            if (widgets.get(i) instanceof ClassWidget) {
//                ClassWidget widget = (ClassWidget) widgets.get(i);
//                Element componentNode = classDiagramComponents.addElement("Class");
//                componentNode.addAttribute("xCoord", widget.getLocation().getX() + "");
//                componentNode.addAttribute("yCoord", widget.getLocation().getY() + "");
//                ClassDiagramComponentSerializer serializer = componentSerializers.get(widget.getComponent().getClass());
//                serializer.addClassDiagramComponent(widget.getComponent());
//                serializer.serialize(componentNode);
//            }
//        }
        
        if (classDiagram.getName() != null)
        {
            node.addAttribute("name", classDiagram.getName());
        }
        Element classDiagramComponents = node.addElement("ClassDiagramComponents");
        for (ClassDiagramComponent component : classDiagram.getComponents().values()) {
            Element componentNode = classDiagramComponents.addElement("Class");
            componentNode.addAttribute("xPosition", String.valueOf(component.getPosition().getX()));
            System.out.println(component.getPosition().getX() + " pozicija x");
            componentNode.addAttribute("yPosition", String.valueOf(component.getPosition().getY()));
            ClassDiagramComponentSerializer serializer = componentSerializers.get(component.getClass());
            serializer.addClassDiagramComponent(component);
            serializer.serialize(componentNode);
        }
        
    }


    
}
