package org.uml.explorer;

import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;
import javax.swing.Action;
import org.netbeans.api.visual.model.ObjectScene;
import org.openide.actions.RenameAction;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.WeakListeners;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.uml.model.ClassDiagram;
import org.uml.model.GetterSetterGeneration;

/**
 *
 * @author Jelena
 */
public class ClassDiagramNode extends AbstractNode implements PropertyChangeListener {

    private ClassDiagram classDiagram;
    private static String iconFolderPath = "org/uml/explorer/icons/";
//    private ObjectScene objectScene;

    public ClassDiagramNode(ClassDiagram classDiagram, ObjectScene objectScene) {
        this(classDiagram, new InstanceContent());
//        this.objectScene = objectScene;
        this.displayFormat = new MessageFormat("{0}");
    }

    private ClassDiagramNode(ClassDiagram classDiagram, InstanceContent content) {
        // not callable, because it is always expanded
        super(Children.create(new ClassDiagramChildFactory(classDiagram), false), new AbstractLookup(content));
        content.add(this);

        this.classDiagram = classDiagram;
        super.setName(classDiagram.getName());
        this.classDiagram.addPropertyChangeListener(WeakListeners.propertyChange(this, this.classDiagram));
    }

    public ClassDiagram getClassDiagram() {
        return classDiagram;
    }

    @Override
    public Action[] getActions(boolean context) {
        return new Action[]{
            SystemAction.get(RenameAction.class)
        };
    }

    @Override
    public boolean canRename() {
        return true;
    }

    @Override
    public void setName(String s) {
        classDiagram.setName(s);
    }

    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage(iconFolderPath + "classDiagram.png");
    }

    @Override
    public Image getOpenedIcon(int type) {
        return getIcon(type);
    }

    /**
     *
     * TODO These properties are related to ClassDiagramScene NOT ClassDiagram,
     * as ClassDiagram does not have visual properties such as icons or members showing.
     * This is why objectScene is read from the lookup (we do not have access to ClassDiagramScene.class,
     * because of cyclic dependency UMLExplorer-UMLVisual).
     * This kind of breaks Model-View relationship, as the View (UMLExplorer) does not interact only with the Model (UMLModel).
     * It could be made uniform if UMLExplorer showed properties of ClassDiagramScene and its children,
     * but it would require a major overhaul of the module and M-V relationship would be gone completely.
     * DISCUSS ABOUT OTHER SOLUTIONS.
     */
    @Override
    protected Sheet createSheet() {
        Sheet sheet = Sheet.createDefault();

        Sheet.Set generalProperties = Sheet.createPropertiesSet();
        generalProperties.setName("generalSet");
        generalProperties.setDisplayName("General");

        Sheet.Set generationProperties = Sheet.createPropertiesSet();
        generationProperties.setName("generationSet");
        generationProperties.setDisplayName("Code generation");
                
        try {
            Property<String> nameProp = new PropertySupport.Reflection<>(classDiagram, String.class, "getName", "setName");
            nameProp.setName("Name");
            generalProperties.put(nameProp);
            
            Property<GetterSetterGeneration> getterGenerationProp = new PropertySupport.Reflection<>(classDiagram, GetterSetterGeneration.class, "getGetterGeneration", "setGetterGeneration");
            getterGenerationProp.setName("Getters");
            generationProperties.put(getterGenerationProp);

            Property<GetterSetterGeneration> setterGenerationProp = new PropertySupport.Reflection<>(classDiagram, GetterSetterGeneration.class, "getSetterGeneration", "setSetterGeneration");
            setterGenerationProp.setName("Setters");
            generationProperties.put(setterGenerationProp);
       
        } catch (Exception e) {
            Exceptions.printStackTrace(e);
        }

        sheet.put(generalProperties);
        sheet.put(generationProperties);
//        Sheet.Set visualProperties = Sheet.createPropertiesSet();
//        visualProperties.setName("visualSet");
//        visualProperties.setDisplayName("Visual");
//        try {
//            Property<Boolean> isIconDisplayEnabledProp = new PropertySupport.Reflection<>(objectScene, boolean.class, "isShowIcons", "setShowIcons");
//            isIconDisplayEnabledProp.setName("Show icons");
//            visualProperties.put(isIconDisplayEnabledProp);
//
//            Property<Boolean> isMemberDisplayEnabledProp = new PropertySupport.Reflection<>(objectScene, boolean.class, "isShowMembers", "setShowMembers");
//            isMemberDisplayEnabledProp.setName("Show members");
//            visualProperties.put(isMemberDisplayEnabledProp);
//            
//            Property<Boolean> issimpleTypeNamesDisplayEnabled = new PropertySupport.Reflection<>(objectScene, boolean.class, "isShowSimpleTypes", "setShowSimpleTypes");
//            issimpleTypeNamesDisplayEnabled.setName("Simple type names");
//            visualProperties.put(issimpleTypeNamesDisplayEnabled);
//        } catch (Exception e) {
//            Exceptions.printStackTrace(e);
//        }
//        sheet.put(visualProperties);
        return sheet;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (null != evt.getPropertyName()) {
            switch (evt.getPropertyName()) {
                case "name":
                    super.setName((String) evt.getNewValue());
                    break;
                case "REMOVE_COMPONENT":
                    if (classDiagram.getComponents().isEmpty()) {
                        setChildren(Children.LEAF);
                    }
                    break;
            }
            // updating Properties window when for example renaming the node
            firePropertySetsChange(null, this.getPropertySets());
        }
    }
}
