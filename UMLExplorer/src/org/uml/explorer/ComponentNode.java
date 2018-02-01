package org.uml.explorer;

import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.concurrent.Callable;
import javax.swing.Action;
import javax.swing.JOptionPane;
import org.openide.actions.DeleteAction;
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
import org.uml.model.components.ClassComponent;
import org.uml.model.components.ComponentBase;
import org.uml.model.components.EnumComponent;
import org.uml.model.components.InterfaceComponent;
import org.uml.model.Visibility;
import org.uml.model.components.PackageComponent;

/**
 *
 * @author Jelena
 */
public class ComponentNode extends AbstractNode implements PropertyChangeListener {

//    https://platform.netbeans.org/tutorials/nbm-nodesapi2.html#propertysheet
//    http://bits.netbeans.org/dev/javadoc/org-openide-nodes/org/openide/nodes/PropertySupport.html
    private ComponentBase component;
    private static String iconFolderPath = "org/uml/explorer/icons/";
    private PropertyChangeListener listener;

    public ComponentNode(ComponentBase component) {
        this(component, new InstanceContent());
        this.displayFormat = new MessageFormat("{0}");
    }

    private ComponentNode(ComponentBase component, InstanceContent content) {
        // synchronous so that selection of members doesn't miss (if everything was not yet generated)
        super(Children.createLazy(new MyCallable(component)), new AbstractLookup(content));
        content.add(this);

        this.component = component;
        super.setName(component.getName());
        this.component.addPropertyChangeListener(WeakListeners.propertyChange(this, this.component));
    }

    public ComponentBase getComponent() {
        return component;
    }

    @Override
    public Action[] getActions(boolean context) {
        return new Action[]{
            SystemAction.get(DeleteAction.class),
            SystemAction.get(RenameAction.class)
        };
    }

    @Override
    public boolean canDestroy() {
        return true;
    }

    @Override
    public void destroy() throws IOException {
        component.getParentDiagram().removeComponentFromContainer(component);
    }

    @Override
    public boolean canRename() {
        return true;
    }

    // Only sets the name in the model, the event is fired if it is successful,
    // and then the name of the node will be updated correspondignly.
    // Use super.setName(String s) to set the name directly.
    @Override
    public void setName(String s) {
        setComponentName(s);
    }

    @Override
    public Image getIcon(int type) {
        if (component instanceof ClassComponent) {
            return ImageUtilities.loadImage(iconFolderPath + "class.png");
        }
        if (component instanceof InterfaceComponent) {
            return ImageUtilities.loadImage(iconFolderPath + "interface.png");
        }
        if (component instanceof EnumComponent) {
            return ImageUtilities.loadImage(iconFolderPath + "enum.png");
        }
        if (component instanceof PackageComponent) {
            return ImageUtilities.loadImage(iconFolderPath + "package.png");
        }
        return super.getIcon(type);
    }

    @Override
    public Image getOpenedIcon(int type) {
        return getIcon(type);
    }

    @Override
    protected Sheet createSheet() {
        Sheet sheet = Sheet.createDefault();

        Sheet.Set generalProperties = Sheet.createPropertiesSet();
        generalProperties.setName("generalSet");
        generalProperties.setDisplayName("General");

        Sheet.Set modifiersProperties = Sheet.createPropertiesSet();
        modifiersProperties.setName("modifiersSet");
        modifiersProperties.setDisplayName("Modifiers");

        try {
            Property<String> nameProp = new PropertySupport.Reflection<>(this, String.class, "getComponentName", "setComponentName");
            nameProp.setName("Name");
            generalProperties.put(nameProp);

            Property<String> packageProp = new PropertySupport.Reflection<>(this, String.class, "getParentPackage", "setParentPackage");
            packageProp.setName("Package");
            generalProperties.put(packageProp);

            if (component instanceof ClassComponent || component instanceof InterfaceComponent) {

                Property<Visibility> visibilityProp = new PropertySupport.Reflection<>(component, Visibility.class, "getVisibility", "setVisibility");
                visibilityProp.setName("Visibility");
                generalProperties.put(visibilityProp);

                if (component instanceof ClassComponent) {
                    ClassComponent classComponent = (ClassComponent) component;
                    Property<Boolean> isStaticProp = new PropertySupport.Reflection<>(classComponent, boolean.class, "isStatic", "setStatic");
                    isStaticProp.setName("static");
                    modifiersProperties.put(isStaticProp);

                    Property<Boolean> isFinalProp = new PropertySupport.Reflection<>(classComponent, boolean.class, "isFinal", "setFinal");
                    isFinalProp.setName("final");
                    modifiersProperties.put(isFinalProp);

                    Property<Boolean> isAbstractProp = new PropertySupport.Reflection<>(classComponent, boolean.class, "isAbstract", "setAbstract");
                    isAbstractProp.setName("abstract");
                    modifiersProperties.put(isAbstractProp);
                } else if (component instanceof InterfaceComponent) {
                    InterfaceComponent interfaceComponent = (InterfaceComponent) component;
                    Property<Boolean> isStaticProp = new PropertySupport.Reflection<>(interfaceComponent, boolean.class, "isStatic", "setStatic");
                    isStaticProp.setName("static");
                    modifiersProperties.put(isStaticProp);
                }
            }
        } catch (Exception e) {
            Exceptions.printStackTrace(e);
        }

        sheet.put(generalProperties);
        sheet.put(modifiersProperties);
        return sheet;
    }

    public String getComponentName() {
        return component.getName();
    }

    /**
     * Changes the name of the Component.
     *
     * @param newName to be set to ClassDiagramComponent
     */
    public void setComponentName(String newName) {
        if (!getName().equals(newName)) {
            if (component.getParentDiagram().signatureExists(component.deriveSignatureFromNewName(newName))) {
                JOptionPane.showMessageDialog(null, "Name \"" + newName + "\" already exists!");
            } else {
                component.setName(newName);
            }
        }
    }

    public String getParentPackage() {
        return component.getParentPackage();
    }

    /**
     * Changes the name of the Component.
     *
     * @param parentPackage
     */
    public void setParentPackage(String parentPackage) {
        if (!parentPackage.equals(getParentPackage())) {
            if (component.getParentDiagram().signatureExists(component.deriveSignatureFromNewPackage(parentPackage))) {
                JOptionPane.showMessageDialog(null, "Component \"" + component.getName() + "\" already exists in package " + parentPackage + "!");
            } else {
                component.setParentPackage(parentPackage);
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (null != evt.getPropertyName()) {
            switch (evt.getPropertyName()) {
                case "name":
                    super.setName((String) evt.getNewValue());
                    break;
                case "ADD_COMPONENT":
                    // create children only when the first member is added, it is refreshed in ComponentChildFactory when others are added
                    if (component.getMembers().size() == 1) {
                        setChildren(Children.create(new ComponentChildFactory(component), false));
                    }
                    break;
                case "REMOVE_COMPONENT":
                    if (component.getMembers().isEmpty()) {
                        setChildren(Children.LEAF);
                    }
                    break;
            }
        }
        // updating Properties window when for example renaming the node
        firePropertySetsChange(null, this.getPropertySets());
    }

    // To enable creating leaf nodes at start, otherwise we need to pass a factory,
    // which can expand only manually and then check that there are no children nodes
    private static class MyCallable implements Callable<Children> {

        private final ComponentBase key;

        private MyCallable(ComponentBase key) {
            this.key = key;
        }

        @Override
        public Children call() throws Exception {
            if (key.getMembers().isEmpty()) {
                return Children.LEAF;
            } else {
                // synchronous so that selection of members doesn't miss (if everything was not yet generated)
                return Children.create(new ComponentChildFactory(key), false);
            }
        }
    }
}

//        //STARA METODA KOJA JE POTPUNO FUNKCIONALNA I KOJA U PROPERTIES WINDOW POSTAVLJA FIELDS, METHODS, LITERALS...
//        Sheet sheet = super.createSheet();
//        Sheet.Set propertiesSet = Sheet.createPropertiesSet();
//        Sheet.Set fieldsSet = Sheet.createPropertiesSet();
//        Sheet.Set methodsSet = Sheet.createPropertiesSet();
//        Sheet.Set literalsSet = Sheet.createPropertiesSet();
//
//        propertiesSet.setName("propertiesSet");
//        propertiesSet.setDisplayName("Properties");
//        fieldsSet.setName("fields");
//        fieldsSet.setDisplayName("Fields");
//        methodsSet.setName("methods");
//        methodsSet.setDisplayName("Methods");
//        literalsSet.setName("literals");
//        literalsSet.setDisplayName("Literals");
//
//        try {
//            Property nameProp = new PropertySupport.Reflection(classDiagramComponent, String.class, "getName", null);
//            nameProp.setName("Name");
//            propertiesSet.put(nameProp);
//            
//            if (classDiagramComponent instanceof ClassComponent) {
//                ClassComponent component = (ClassComponent) classDiagramComponent;
//
//                HashMap<String, Field> fields = component.getFields();
//                for (Map.Entry<String, Field> entry : fields.entrySet()) {
//                    Field field = entry.getValue();
//                    Property fieldProp = new PropertySupport.Reflection(field, String.class, "getName", null);
//                    String fieldPropName = field.getType();
//                    fieldProp.setName(fieldPropName);
//                    fieldsSet.put(fieldProp);
//                }
//
//
//                HashMap<String, Method> methods = component.getMethods();
//                for (Map.Entry<String, Method> entry : methods.entrySet()) {
//                    Method method = entry.getValue();
//                    Property methodProp = new PropertySupport.Reflection(method, String.class, "getName", null);
//                    String methodPropName = method.getSignatureForLabel();
//                    methodProp.setName(methodPropName);
//                    methodsSet.put(methodProp);
//                }
//            } else if (classDiagramComponent instanceof InterfaceComponent) {
//                InterfaceComponent component = (InterfaceComponent) classDiagramComponent;
//                HashMap<String, Method> methods = component.getMethods();
//                for (Map.Entry<String, Method> entry : methods.entrySet()) {
//                    Method method = entry.getValue();
//                    Property methodProp = new PropertySupport.Reflection(method, String.class, "getName", null);
//                    String methodPropName = method.getSignatureForLabel();
//                    methodProp.setName(methodPropName);
//                    methodsSet.put(methodProp);
//                }
//            } else if (classDiagramComponent instanceof EnumComponent) {
//                EnumComponent component = (EnumComponent) classDiagramComponent;
//
//                HashMap<String, Field> fields = component.getFields();
//                for (Map.Entry<String, Field> entry : fields.entrySet()) {
//                    Field field = entry.getValue();
//                    Property fieldProp = new PropertySupport.Reflection(field, String.class, "getName", null);
//                    String fieldPropName = field.getType();
//                    fieldProp.setName(fieldPropName);
//                    fieldsSet.put(fieldProp);
//                }
//
//                HashMap<String, Method> methods = component.getMethods();
//                for (Map.Entry<String, Method> entry : methods.entrySet()) {
//                    Method method = entry.getValue();
//                    Property methodProp = new PropertySupport.Reflection(method, String.class, "getName", null);
//                    String methodPropName = method.getSignatureForLabel();
//                    methodProp.setName(methodPropName);
//                    methodsSet.put(methodProp);
//                }
//
//                HashMap<String, Literal> literals = component.getLiterals();
//                for (Map.Entry<String, Literal> entry : literals.entrySet()) {
//                    Literal literal = entry.getValue();
//                    Property literalProp = new PropertySupport.Reflection(literal, String.class, "getName", null);
//                    literalProp.setName("Literal");
//                    literalsSet.put(literalProp);
//                }
//
//            }
//
//            sheet.put(propertiesSet);
//            sheet.put(fieldsSet);
//            sheet.put(methodsSet);
//            sheet.put(literalsSet);
//
//
////            
////            Property labelProp = new PropertySupport.Reflection(classDiagramComponent, String.class, "getLabel", null);
////            labelProp.setName("Label");
////            set.put(labelProp);
//
//        } catch (Exception ex) {
//            System.out.println(ex.getMessage());
//        }
//
//
//        return sheet;
