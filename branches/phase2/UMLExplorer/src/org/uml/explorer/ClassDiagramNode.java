package org.uml.explorer;

import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.ImageUtilities;
import org.openide.util.WeakListeners;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.uml.model.ClassDiagram;

/**
 *
 * @author Jelena
 */
public class ClassDiagramNode extends AbstractNode implements PropertyChangeListener {

    private ClassDiagram classDiagram;
    private static String iconFolderPath = "org/uml/explorer/icons/";

    public ClassDiagramNode(ClassDiagram classDiagram) {
        this(classDiagram, new InstanceContent());
    }

    private ClassDiagramNode(ClassDiagram classDiagram, InstanceContent content) {
        // not callable, because it is always expanded
        super(Children.create(new ClassDiagramChildrenFactory(classDiagram), false), new AbstractLookup(content));
        content.add(this);

        this.classDiagram = classDiagram;
        this.setDisplayName(classDiagram.getName());
        this.classDiagram.addPropertyChangeListener(WeakListeners.propertyChange(this, this.classDiagram));
    }

    public ClassDiagram getClassDiagram() {
        return classDiagram;
    }

    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage(iconFolderPath + "classDiagramIcon.png");
    }

    @Override
    public Image getOpenedIcon(int type) {
        return ImageUtilities.loadImage(iconFolderPath + "classDiagramIcon.png");
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (null != evt.getPropertyName()) {
            switch (evt.getPropertyName()) {
                case "name":
                    setName((String) evt.getNewValue());
                    break;
                case "REMOVE":
                    if (classDiagram.getComponents().isEmpty()) {
                        setChildren(Children.LEAF);
                    }
                    break;
            }
        }
    }
}
