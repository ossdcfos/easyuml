package org.uml.explorer;

import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import javax.swing.Action;
import javax.swing.JOptionPane;
import org.openide.actions.DeleteAction;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.FilterNode;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.WeakListeners;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
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
        super(FilterNode.Children.LEAF, new AbstractLookup(content));
        content.add(this);

        this.member = member;
        setName(member.getName());
        setDisplayName(member.getName());
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
    public void destroy() throws IOException {
        ComponentBase parent = member.getDeclaringComponent();
        parent.removePartFromContainer(member);
        parent.removeMember(member);
    }

    @Override
    public Image getIcon(int type) {
        if (member instanceof Field) {
            return ImageUtilities.loadImage(iconFolderPath + "attributePublicIcon.png");
        }
        if (member instanceof MethodBase) {
            return ImageUtilities.loadImage(iconFolderPath + "methodPublicIcon.png");
        }
        if (member instanceof Constructor) {
            return ImageUtilities.loadImage(iconFolderPath + "constructorPublicIcon.png");
        }
        if (member instanceof Literal) {
            return ImageUtilities.loadImage(iconFolderPath + "literalIcon.png");
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
        if (member.getVisibility() == Visibility.PUBLIC) return InterfaceMethodVisibility.PUBLIC;
        else return InterfaceMethodVisibility.PACKAGE;
    }

    public void setInterfaceMethodVisibility(InterfaceMethodVisibility interfaceMethodVisibility) {
        if (interfaceMethodVisibility == InterfaceMethodVisibility.PUBLIC) member.setVisibility(Visibility.PUBLIC);
        else member.setVisibility(Visibility.PACKAGE);
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
                member.setName(newName);
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
                member.setType(newType);
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (null != evt.getPropertyName()) {
            switch (evt.getPropertyName()) {
                case "name":
                    setName((String) evt.getNewValue());
                    break;
            }
        }
        firePropertySetsChange(null, this.getPropertySets());
    }
}
