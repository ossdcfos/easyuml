/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.explorer;

import java.awt.Image;
import java.util.HashMap;
import java.util.Map;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.uml.model.ClassComponent;
import org.uml.model.ClassDiagramComponent;
import org.uml.model.EnumComponent;
import org.uml.model.Field;
import org.uml.model.InterfaceComponent;
import org.uml.model.Literal;
import org.uml.model.Method;

/**
 *
 * @author Jelena
 */
public class ClassDiagramComponentNode extends AbstractNode {

//    https://platform.netbeans.org/tutorials/nbm-nodesapi2.html#propertysheet
//    http://bits.netbeans.org/dev/javadoc/org-openide-nodes/org/openide/nodes/PropertySupport.html
    private ClassDiagramComponent classDiagramComponent;

    public ClassDiagramComponentNode(ClassDiagramComponent component) {
        this(component, new InstanceContent());
    }

    private ClassDiagramComponentNode(ClassDiagramComponent component, InstanceContent content) {
        super(new ClassDiagramComponentChildren(component), new AbstractLookup(content));
        content.add(this);

        this.classDiagramComponent = component;
        this.setDisplayName(component.getName());
    }

//    @Override
//    public Image getIcon(int type) {
//        return ImageUtilities.loadImage("org/neuroph/netbeans/files/dset/iconTs.png");
//    }
    @Override
    protected Sheet createSheet() {

        Sheet sheet = Sheet.createDefault();
        Sheet.Set set = Sheet.createPropertiesSet();
        set.setName("Class Component Properties");

        try {

            Property nameProp = new PropertySupport.Reflection(classDiagramComponent, String.class, "getName", null);
            nameProp.setName("Name");
            set.put(nameProp);

            if (classDiagramComponent instanceof ClassComponent) {
                int index = 1;
                ClassComponent component = (ClassComponent) classDiagramComponent;
                HashMap<String, Field> fields = component.getFields();
                for (Map.Entry<String, Field> entry : fields.entrySet()) {
                    Field field = entry.getValue();
                    Property fieldProp = new PropertySupport.Reflection(field, String.class, "getName", null);
                    String fieldPropName = "Field " + index;
                    fieldProp.setName(fieldPropName);
                    set.put(fieldProp);
                    index++;
                }

                int index2 = 1;
                HashMap<String, Method> methods = component.getMethods();
                for (Map.Entry<String, Method> entry : methods.entrySet()) {
                    Method method = entry.getValue();
                    Property methodProp = new PropertySupport.Reflection(method, String.class, "getName", null);
                    String methodPropName = "Method " + index2;
                    methodProp.setName(methodPropName);
                    set.put(methodProp);
                    index2++;
                }
            } else if (classDiagramComponent instanceof InterfaceComponent) {
                InterfaceComponent component = (InterfaceComponent) classDiagramComponent;
                int index2 = 1;
                HashMap<String, Method> methods = component.getMethods();
                for (Map.Entry<String, Method> entry : methods.entrySet()) {
                    Method method = entry.getValue();
                    Property methodProp = new PropertySupport.Reflection(method, String.class, "getName", null);
                    String methodPropName = "Method " + index2;
                    methodProp.setName(methodPropName);
                    set.put(methodProp);
                    index2++;
                    System.out.println("?????");
                    System.out.println(method.getName());
                }
            } else if (classDiagramComponent instanceof EnumComponent) {
                EnumComponent component = (EnumComponent) classDiagramComponent;

                int index = 1;
                HashMap<String, Field> fields = component.getFields();
                for (Map.Entry<String, Field> entry : fields.entrySet()) {
                    Field field = entry.getValue();
                    Property fieldProp = new PropertySupport.Reflection(field, String.class, "getName", null);
                    String fieldPropName = "Field " + index;
                    fieldProp.setName(fieldPropName);
                    set.put(fieldProp);
                    index++;
                    System.out.println("?????");
                    System.out.println(field.getName());
                }

                int index2 = 1;
                HashMap<String, Method> methods = component.getMethods();
                for (Map.Entry<String, Method> entry : methods.entrySet()) {
                    Method method = entry.getValue();
                    Property methodProp = new PropertySupport.Reflection(method, String.class, "getName", null);
                    String methodPropName = "Method " + index2;
                    methodProp.setName(methodPropName);
                    set.put(methodProp);
                    index2++;
                    System.out.println("?????");
                    System.out.println(method.getName());
                }

                int index3 = 1;
                HashMap<String, Literal> literals = component.getLiterals();
                for (Map.Entry<String, Literal> entry : literals.entrySet()) {
                    Literal literal = entry.getValue();
                    Property literalProp = new PropertySupport.Reflection(literal, String.class, "getName", null);
                    String methodPropName = "Literal " + index3;
                    literalProp.setName(methodPropName);
                    set.put(literalProp);
                    index3++;
                    System.out.println("?????");
                    System.out.println(literal.getName());
                }

            }




//            
//            Property labelProp = new PropertySupport.Reflection(classDiagramComponent, String.class, "getLabel", null);
//            labelProp.setName("Label");
//            set.put(labelProp);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        sheet.put(set);
        return sheet;
    }

    @Override
    public Image getIcon(int type) {
        if (classDiagramComponent instanceof ClassComponent) {
            return ImageUtilities.loadImage("org/uml/explorer/klasaIcon.png");
        }
        if (classDiagramComponent instanceof InterfaceComponent) {
            return ImageUtilities.loadImage("org/uml/explorer/interfejsIcon.png");
        }
        if (classDiagramComponent instanceof EnumComponent) {
            return ImageUtilities.loadImage("org/uml/explorer/enumIcon.png");
        }
        return null;
    }

    @Override
    public Image getOpenedIcon(int type) {
        if (classDiagramComponent instanceof ClassComponent) {
            return ImageUtilities.loadImage("org/uml/explorer/klasaIcon.png");
        }
        if (classDiagramComponent instanceof InterfaceComponent) {
            return ImageUtilities.loadImage("org/uml/explorer/interfejsIcon.png");
        }
        if (classDiagramComponent instanceof EnumComponent) {
            return ImageUtilities.loadImage("org/uml/explorer/enumIcon.png");
        }
        return super.getIcon(type);
    }

    public ClassDiagramComponent getClassDiagramComponent() {
        return classDiagramComponent;
    }
}
//staro
//        try {
//            Node.Property label = new PropertySupport.Reflection(classDiagramComponent, String.class, "getName", null);
//            Node.Property inputSize = new PropertySupport.Reflection(classDiagramComponent, Integer.class, "getInputSize", null);
//            Node.Property outputSize = new PropertySupport.Reflection(classDiagramComponent, Integer.class, "getOutputSize", null);
//            Node.Property size = new PropertySupport.Reflection(classDiagramComponent, Integer.class, "size", null);
//label.setName("Label");
//            inputSize.setName("Input size");
//            outputSize.setName("Output size");
//            size.setName("Number of elements");
//set.put(label);
//            set.put(inputSize);
//            set.put(outputSize);
//            set.put(size);
//        } catch (Exception ex) {
//            System.out.println(ex.getMessage());
//        }