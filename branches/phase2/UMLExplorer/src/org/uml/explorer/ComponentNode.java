package org.uml.explorer;

import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import javax.swing.Action;
import javax.swing.JOptionPane;
import org.openide.actions.DeleteAction;
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

/**
 *
 * @author Jelena
 */
public class ComponentNode extends AbstractNode implements PropertyChangeListener {

//    https://platform.netbeans.org/tutorials/nbm-nodesapi2.html#propertysheet
//    http://bits.netbeans.org/dev/javadoc/org-openide-nodes/org/openide/nodes/PropertySupport.html
    private ComponentBase component;
    private static String iconFolderPath = "org/uml/explorer/icons/";
    
    public ComponentNode(ComponentBase component) {
        this(component, new InstanceContent());
    }

    private ComponentNode(ComponentBase component, InstanceContent content) {
        // synchronous so that selection of members doesn't miss (if everything was not yet generated)
        super(Children.create(new ComponentChildrenFactory(component), false), new AbstractLookup(content));
        // disable expand if there are no children
//        if(getChildren().getNodesCount() == 0) setChildren(Children.LEAF);
        content.add(this);

        this.component = component;
        setName(component.getName());
        setDisplayName(component.getName());
        this.component.addPropertyChangeListener(WeakListeners.propertyChange(this, this.component));
    }

    @Override
    public Action[] getActions(boolean context) {
        return new Action[]{SystemAction.get(DeleteAction.class)
//            , SystemAction.get(RenameAction.class)
        };
    }

    @Override
    public boolean canDestroy() {
        return true;
    }

    @Override
    public boolean canRename() {
        return true;
    }

    @Override
    public void destroy() throws IOException {
        component.getParentDiagram().removeComponent(component.getName());
        fireNodeDestroyed();
    }

    @Override
    public Image getIcon(int type) {
        if (component instanceof ClassComponent) {
            return ImageUtilities.loadImage(iconFolderPath + "classIcon.png");
        }
        if (component instanceof InterfaceComponent) {
            return ImageUtilities.loadImage(iconFolderPath + "interfaceIcon.png");
        }
        if (component instanceof EnumComponent) {
            return ImageUtilities.loadImage(iconFolderPath + "enumIcon.png");
        }
        return super.getIcon(type);
    }

    @Override
    public Image getOpenedIcon(int type) {
        return getIcon(type);
    }

    public ComponentBase getComponent() {
        return component;
    }

    @Override
    protected Sheet createSheet() {
        Sheet sheet = Sheet.createDefault();
        Sheet.Set propertiesSet = Sheet.createPropertiesSet();
        propertiesSet.setName("propertiesSet");
        propertiesSet.setDisplayName("Properties");

        try {
            Property<String> nameProp = new PropertySupport.Reflection(this, String.class, "getName", "changeName");
            nameProp.setName("Name");
            propertiesSet.put(nameProp);

            if (component instanceof ClassComponent) {
                ClassComponent classComponent = (ClassComponent)component;
                
                Property<String> visibilityProp = new PropertySupport.Reflection(classComponent, Visibility.class, "getVisibility", "setVisibility");
                visibilityProp.setName("Visibility");
                propertiesSet.put(visibilityProp);
                
                Property<Boolean> isAbstractProp = new PropertySupport.Reflection<>(classComponent, boolean.class, "isAbstract", "setAbstract");
                isAbstractProp.setName("abstract");
                propertiesSet.put(isAbstractProp);
            }

//            PackageComponent pack = component.getParentPackage();
//            Property<String> packageProp = new PropertySupport.Reflection<>(pack, String.class, "getName", null);
//            packageProp.setName("Package");
//            propertiesSet.put(packageProp);
        } catch (Exception e) {
            Exceptions.printStackTrace(e);
        }

        sheet.put(propertiesSet);
        return sheet;
    }

    /**
     * Changes the name of the Component.
     *
     * @param newName to be set to ClassDiagramComponent
     */
    public void changeName(String newName) {
        if(getName().equals(newName)){
        } else if (component.getParentDiagram().signatureExists(newName)) {
            JOptionPane.showMessageDialog(null, "Name \""+newName+"\" already exists!");
//            //WidgetAction editor = ActionFactory.createInplaceEditorAction(new LabelTextFieldEditorAction());
//            //ActionFactory.getInplaceEditorController(nameEditorAction).openEditor(getNameLabel());
        } else {
            component.changeName(newName);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if("name".equals(evt.getPropertyName())){
            String newName = (String)evt.getNewValue();
            setName(newName);
            this.fireDisplayNameChange(null, newName);
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
