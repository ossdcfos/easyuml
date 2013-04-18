/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.explorer;

import java.awt.Image;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.uml.model.ClassComponent;
import org.uml.model.ClassDiagramComponent;

/**
 *
 * @author Jelena
 */
public class ClassComponentNode extends AbstractNode {

    private ClassComponent classComponent;

    public ClassComponentNode(ClassComponent component) {
        this(component, new InstanceContent());
    }

    private ClassComponentNode(ClassComponent component, InstanceContent content) {
        super(FilterNode.Children.LEAF, new AbstractLookup(content));
        content.add(this);

        this.classComponent = component;
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
}
