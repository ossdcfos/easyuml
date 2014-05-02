/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.explorer;

import java.awt.Image;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Sheet;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.uml.model.ClassComponent;
import org.uml.model.ClassDiagramComponent;
import org.uml.model.EnumComponent;
import org.uml.model.InterfaceComponent;

/**
 *
 * @author Jelena
 */
public class ClassDiagramComponentNode extends AbstractNode {

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
