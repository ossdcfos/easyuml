package org.uml.explorer;

import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.text.MessageFormat;
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
import org.uml.memberparser.MemberParser;
import org.uml.model.GetterSetterGeneration;
import org.uml.model.Visibility;
import org.uml.model.components.ComponentBase;
import org.uml.model.components.InterfaceComponent;
import org.uml.model.members.*;

/**
 *
 * @author Jelena
 */
public class MemberNode extends AbstractNode implements PropertyChangeListener {

    private MemberBase member;
    private static String iconFolderPath = "org/uml/explorer/icons/";

    public MemberNode(MemberBase member) {
        this(member, new InstanceContent());
    }

    private MemberNode(MemberBase member, InstanceContent content) {
        super(Children.LEAF, new AbstractLookup(content));
        content.add(this);

        this.member = member;
        this.displayFormat = new MessageFormat("{0}");
        super.setName(member.getName());
        this.member.addPropertyChangeListener(WeakListeners.propertyChange(this, this.member));
    }

    public MemberBase getMember() {
        return member;
    }

    @Override
    public Action[] getActions(boolean context) {
        return new Action[]{
            SystemAction.get(DeleteAction.class)
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

    // Only sets the name in the model, the event is fired if it is successful,
    // and then the name of the node will be updated correspondignly.
    // Use super.setName(String s) to set the name directly.
    @Override
    public void setName(String s) {
        setMemberName(s);
    }

    @Override
    public void destroy() throws IOException {
        ComponentBase parent = member.getDeclaringComponent();
        parent.removeMember(member);
    }

    @Override
    public Image getIcon(int type) {
        String lowercaseVisibility = member.getVisibility().toString().toLowerCase();
        String suffix = lowercaseVisibility.substring(0, 1).toUpperCase() + lowercaseVisibility.substring(1);
        if (member instanceof Field) {
            Field field = (Field) member;
            if (field.isStatic()) {
                suffix = "Static" + suffix;
            }
            return ImageUtilities.loadImage(iconFolderPath + "fields/field" + suffix + ".png");
        } else if (member instanceof Method) {
            Method method = (Method) member;
            if (method.isStatic()) {
                suffix = "Static" + suffix;
            }
            return ImageUtilities.loadImage(iconFolderPath + "methods/method" + suffix + ".png");
        } else if (member instanceof Constructor) {
            return ImageUtilities.loadImage(iconFolderPath + "constructors/constructor" + suffix + ".png");
        } else if (member instanceof Literal) {
            return ImageUtilities.loadImage(iconFolderPath + "other/literal.png");
        }
        return super.getIcon(type);
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

        Sheet.Set generationProperties = Sheet.createPropertiesSet();
        generationProperties.setName("generationSet");
        generationProperties.setDisplayName("Code generation");
                
        try {
            if (member instanceof Constructor) {
                Property<Visibility> visibilityProp = new PropertySupport.Reflection<>(member, Visibility.class, "getVisibility", "setVisibility");
                visibilityProp.setName("Visibility");
                generalProperties.put(visibilityProp);
            } else if (member instanceof Field || member instanceof Method || member instanceof Literal) {
                Property<String> nameProp = new PropertySupport.Reflection<>(this, String.class, "getMemberName", "setMemberName");
                nameProp.setName("Name");
                generalProperties.put(nameProp);

                if (member instanceof Field) {
                    Field field = (Field) member;

                    Property<String> typeProp = new PropertySupport.Reflection<>(this, String.class, "getMemberType", "setMemberType");
                    typeProp.setName("Type");
                    generalProperties.put(typeProp);

                    Property<Visibility> visibilityProp = new PropertySupport.Reflection<>(member, Visibility.class, "getVisibility", "setVisibility");
                    visibilityProp.setName("Visibility");
                    generalProperties.put(visibilityProp);

                    Property<Boolean> isStaticProp = new PropertySupport.Reflection<>(field, boolean.class, "isStatic", "setStatic");
                    isStaticProp.setName("static");
                    modifiersProperties.put(isStaticProp);

                    Property<Boolean> isFinalProp = new PropertySupport.Reflection<>(field, boolean.class, "isFinal", "setFinal");
                    isFinalProp.setName("final");
                    modifiersProperties.put(isFinalProp);

                    Property<Boolean> isTransientProp = new PropertySupport.Reflection<>(field, boolean.class, "isTransient", "setTransient");
                    isTransientProp.setName("transient");
                    modifiersProperties.put(isTransientProp);

                    Property<Boolean> isVolatileProp = new PropertySupport.Reflection<>(field, boolean.class, "isVolatile", "setVolatile");
                    isVolatileProp.setName("volatile");
                    modifiersProperties.put(isVolatileProp);
                    
                    if (!field.isStatic()) {
                        Property<GetterSetterGeneration> getterGenerationProp = new PropertySupport.Reflection<>(field, GetterSetterGeneration.class, "getGetterGeneration", "setGetterGeneration");
                        getterGenerationProp.setName("Getters");
                        generationProperties.put(getterGenerationProp);

                        Property<GetterSetterGeneration> setterGenerationProp = new PropertySupport.Reflection<>(field, GetterSetterGeneration.class, "getSetterGeneration", "setSetterGeneration");
                        setterGenerationProp.setName("Setters");
                        generationProperties.put(setterGenerationProp);
                    }
                                        
                    
                } else if (member instanceof Method) {
                    Method method = (Method) member;

                    Property<String> typeProp = new PropertySupport.Reflection<>(this, String.class, "getMemberType", "setMemberType");
                    typeProp.setName("Return type");
                    generalProperties.put(typeProp);

                    if (method.getDeclaringComponent() instanceof InterfaceComponent) {
                        Property<InterfaceMethodVisibility> visibilityProp = new PropertySupport.Reflection<>(this, InterfaceMethodVisibility.class, "getInterfaceMethodVisibility", "setInterfaceMethodVisibility");
                        visibilityProp.setName("Visibility");
                        generalProperties.put(visibilityProp);
                    } else {
                        Property<Visibility> visibilityProp = new PropertySupport.Reflection<>(member, Visibility.class, "getVisibility", "setVisibility");
                        visibilityProp.setName("Visibility");
                        generalProperties.put(visibilityProp);
                    }

                    Property<Boolean> isStaticProp = new PropertySupport.Reflection<>(method, boolean.class, "isStatic", "setStatic");
                    isStaticProp.setName("static");
                    modifiersProperties.put(isStaticProp);

                    if (!(method.getDeclaringComponent() instanceof InterfaceComponent)) {
                        Property<Boolean> isFinalProp = new PropertySupport.Reflection<>(method, boolean.class, "isFinal", "setFinal");
                        isFinalProp.setName("final");
                        modifiersProperties.put(isFinalProp);

                        Property<Boolean> isAbstractProp = new PropertySupport.Reflection<>(method, boolean.class, "isAbstract", "setAbstract");
                        isAbstractProp.setName("abstract");
                        modifiersProperties.put(isAbstractProp);

                        Property<Boolean> isSynchronizedProp = new PropertySupport.Reflection<>(method, boolean.class, "isSynchronized", "setSynchronized");
                        isSynchronizedProp.setName("synchronized");
                        modifiersProperties.put(isSynchronizedProp);
                    }
                }
            }
        } catch (Exception e) {
            Exceptions.printStackTrace(e);
        }

        sheet.put(generalProperties);
        sheet.put(modifiersProperties);
        sheet.put(generationProperties);
        return sheet;
    }

    // TODO maybe move this somewhere more appropriate
    public enum InterfaceMethodVisibility {

        PUBLIC {
                    @Override
                    public String toString() {
                        return "public";
                    }

                },
        PACKAGE {
                    @Override
                    public String toString() {
                        return "package";
                    }
                };
    }

    public InterfaceMethodVisibility getInterfaceMethodVisibility() {
        if (member.getVisibility() == Visibility.PUBLIC) {
            return InterfaceMethodVisibility.PUBLIC;
        } else {
            return InterfaceMethodVisibility.PACKAGE;
        }
    }

    public void setInterfaceMethodVisibility(InterfaceMethodVisibility interfaceMethodVisibility) {
        if (interfaceMethodVisibility == InterfaceMethodVisibility.PUBLIC) {
            member.setVisibility(Visibility.PUBLIC);
        } else {
            member.setVisibility(Visibility.PACKAGE);
        }
    }

    public String getMemberName() {
        return member.getName();
    }

    /**
     * Changes the name of the Component.
     *
     * @param newName to be set to ClassDiagramComponent
     */
    public void setMemberName(String newName) {
        if (!member.getName().equals(newName)) {
            String newSignature = member.deriveSignatureFromName(newName);
            if (member.getDeclaringComponent().signatureExists(newSignature)) {
                JOptionPane.showMessageDialog(null, "Member \"" + newSignature + "\" already exists!");
            } else {
                try {
                    if (member instanceof Field) {
                        MemberParser.fillFieldComponents((Field) member, newSignature);
                    } else if (member instanceof Method) {
                        MemberParser.fillMethodComponents((Method) member, newSignature);
                    } else if (member instanceof Constructor) {
                        MemberParser.fillConstructorComponents((Constructor) member, newSignature);
                    } else if (member instanceof Literal) {
                        member.setName(newName);
                    }
                } catch (IllegalArgumentException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "Illegal format error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    public String getMemberType() {
        return member.getType();
    }

    public void setMemberType(String newType) {
        if (!member.getName().equals(newType)) {
            String newSignature = member.deriveSignatureFromType(newType);
            if (member.getDeclaringComponent().signatureExists(newSignature)) {
                JOptionPane.showMessageDialog(null, "Member \"" + newSignature + "\" already exists!");
            } else {
                try {
                    if (member instanceof Field) {
                        MemberParser.fillFieldComponents((Field) member, newSignature);
                    } else if (member instanceof Method) {
                        MemberParser.fillMethodComponents((Method) member, newSignature);
                    } else if (member instanceof Constructor) {
                        MemberParser.fillConstructorComponents((Constructor) member, newSignature);
                    } else if (member instanceof Literal) {
                        member.setType(newType);
                    }
                } catch (IllegalArgumentException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "Illegal format error", JOptionPane.ERROR_MESSAGE);
                }
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
                case "visibility":
                    fireIconChange();
                    break;
                case "isStatic":
                    fireIconChange();
                    break;
            }
        }
        firePropertySetsChange(null, this.getPropertySets());
    }
}
