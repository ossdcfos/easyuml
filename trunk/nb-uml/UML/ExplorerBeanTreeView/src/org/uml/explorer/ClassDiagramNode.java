/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.explorer;

import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.Lookups;
import org.uml.model.ClassDiagram;
import org.uml.model.ClassDiagramComponent;

/**
 *
 * @author Jelena
 */
public class ClassDiagramNode  extends AbstractNode {
    
    private ClassDiagram classDiagram;

    public ClassDiagramNode(ClassDiagram classDiagram) {
        this(classDiagram, new InstanceContent());
    }

    private ClassDiagramNode(ClassDiagram classDiagram, InstanceContent content) {
        super(new ClassDiagramChildren(classDiagram), new AbstractLookup(content));
        content.add(this);

        this.classDiagram = classDiagram;
        //this.setDisplayName(classDiagram.getName());
        this.setDisplayName("UML Class Diagram");
    }
    
//    @Override
//    public Image getIcon(int type) {
//        return ImageUtilities.loadImage("org/neuroph/netbeans/files/dset/iconTs.png");
//    }

    @Override
    protected Sheet createSheet() {

        Sheet sheet = Sheet.createDefault();
        Sheet.Set set = Sheet.createPropertiesSet();
        set.setName("Class Diagram Properties");

        try {
 //           Node.Property label = new PropertySupport.Reflection(classDiagram, String.class, "getName", null);
//            Node.Property inputSize = new PropertySupport.Reflection(classDiagramComponent, Integer.class, "getInputSize", null);
//            Node.Property outputSize = new PropertySupport.Reflection(classDiagramComponent, Integer.class, "getOutputSize", null);
//            Node.Property size = new PropertySupport.Reflection(classDiagramComponent, Integer.class, "size", null);

 //           label.setName("Label");
//            inputSize.setName("Input size");
//            outputSize.setName("Output size");
//            size.setName("Number of elements");

 //           set.put(label);
//            set.put(inputSize);
//            set.put(outputSize);
//            set.put(size);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        sheet.put(set);
        return sheet;
    }
}
