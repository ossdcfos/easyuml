package org.uml.visual.widgets.components;

import org.uml.visual.widgets.members.ConstructorWidget;
import org.uml.visual.widgets.members.MethodWidget;
import org.uml.visual.widgets.members.FieldWidget;
import java.awt.Font;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.Callable;
import javax.swing.JOptionPane;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.SeparatorWidget;
import org.uml.memberparser.MemberParser;
import org.uml.model.components.ClassComponent;
import org.uml.model.members.Constructor;
import org.uml.model.members.Field;
import org.uml.model.Visibility;
import org.uml.model.members.Method;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.actions.MemberNameEditor;
import org.uml.visual.widgets.popups.ClassPopupMenuProvider;
import org.uml.visual.widgets.providers.ComponentWidgetAcceptProvider;
import org.uml.visual.widgets.providers.CloseInplaceEditorOnClickAdapter;

/**
 *
 * @author NUGS
 */
public class ClassWidget extends ComponentWidgetBase {

    private final MemberContainerWidget fieldsContainer;
    private final MemberContainerWidget methodsContainer;

    public ClassWidget(ClassDiagramScene scene, ClassComponent classComponent) {
        super(scene, classComponent);

        // Header
        if (classComponent.isAbstract()) {
            LabelWidget abstractLabel = new LabelWidget(headerWidget.getScene(), "<<abstract>>");
            abstractLabel.setFont(headerWidget.getScene().getFont().deriveFont(Font.ITALIC));
            headerWidget.addChild(abstractLabel);
            setMinimumSize(MIN_DIMENSION_2ROW);
        } else {
            setMinimumSize(MIN_DIMENSION_1ROW);
        }
        iconNameContainer.addChild(iconWidget);
        iconNameContainer.addChild(nameLabel);
        headerWidget.addChild(iconNameContainer);
        addChild(headerWidget);

        SeparatorWidget separatorWidget = new SeparatorWidget(scene, SeparatorWidget.Orientation.HORIZONTAL);
        separatorWidget.setForeground(getColorTheme().getDefaultBorderColor());
        separators.add(separatorWidget);
        addChild(separatorWidget);

        // Fields
        fieldsContainer = new MemberContainerWidget(scene, "field");
        fieldsContainer.addAddAction(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                addFieldWidget();
                return null;
            }
        });
        addChild(fieldsContainer);

        separatorWidget = new SeparatorWidget(scene, SeparatorWidget.Orientation.HORIZONTAL);
        separatorWidget.setForeground(getColorTheme().getDefaultBorderColor());
        separators.add(separatorWidget);
        addChild(separatorWidget);

        // Methods
        methodsContainer = new MemberContainerWidget(scene, "method");
        methodsContainer.addAddAction(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                addMethodWidget();
                return null;
            }
        });
        addChild(methodsContainer);

        // Actions
        getActions().addAction(ActionFactory.createAcceptAction(new ComponentWidgetAcceptProvider()));
        getActions().addAction(ActionFactory.createPopupMenuAction(new ClassPopupMenuProvider(this)));

//        setMaximumSize(new Dimension(Integer.MAX_VALUE, MIN_DIMENSION_1ROW.height + 2*(getLabelSizeForFont(ADD_LABEL_FONT_SIZE) + 2*EMPTY_BORDER_SIZE)));
        // Fill the widget when loading an existing diagram
        for (Constructor constructor : classComponent.getConstructors()) {
            ConstructorWidget constructorWidget = new ConstructorWidget(scene, constructor);
            addMember(methodsContainer, constructorWidget);
        }

        for (Field field : classComponent.getFields()) {
            FieldWidget fieldWidget = new FieldWidget(scene, field);
            addMember(fieldsContainer, fieldWidget);
        }

        for (Method method : classComponent.getMethods()) {
            MethodWidget methodWidget = new MethodWidget(scene, method);
            addMember(methodsContainer, methodWidget);
        }
    }

    @Override
    public ClassComponent getComponent() {
        return (ClassComponent) component;
    }

    public void addFieldWidget() {
        Field field = new Field("untitledField", "Object", Visibility.PRIVATE);
        getComponent().addField(field);
        FieldWidget fieldWidget = new FieldWidget(getClassDiagramScene(), field);
        addMember(fieldsContainer, fieldWidget);
        getScene().validate();

        // Temp renamer
        WidgetAction nameEditorAction = ActionFactory.createInplaceEditorAction(new MemberNameEditor(fieldWidget));
        ActionFactory.getInplaceEditorController(nameEditorAction).openEditor(fieldWidget.getNameLabel());
        MouseListener mouseListener = new CloseInplaceEditorOnClickAdapter(nameEditorAction);
        getScene().getView().addMouseListener(mouseListener);
    }
    
    public void addField(String fieldSignature) {
        if (getComponent().signatureExists(fieldSignature))
            return;
        Field field = new Field("untitledField", "Object", Visibility.PRIVATE);
        try {
            MemberParser.fillFieldComponents(field, fieldSignature);
        }
        catch(Exception ex) {
            JOptionPane.showMessageDialog(null, "Error while parsing field "+fieldSignature, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        getComponent().addField(field);
        FieldWidget fieldWidget = new FieldWidget(getClassDiagramScene(), field);
        addMember(fieldsContainer, fieldWidget);
        getScene().validate();
    }
    
    public void addFields(List<String> fieldSignatures) {
        for (String field : fieldSignatures) {
            addField(field);
        }        
    }
    
    public void addFields(String fieldSignatures) {
        String[] list = fieldSignatures.split("[\\r\\n]+");
        for (String field : list) {
            addField(field);
        }        
    }            

    public void removeField(FieldWidget field) {
        removeMember(fieldsContainer, field);
    }

    public void addMethodWidgets(LinkedHashSet<Method> methods) {
        for (Method method : methods) {
            getComponent().addMethod(method);
            MethodWidget methodWidget = new MethodWidget(getClassDiagramScene(), method);
            addMember(methodsContainer, methodWidget);
        }
        getScene().validate();
    }
    
    public void addMethodWidget() {
        Method method = new Method("untitledMethod", "void");
        getComponent().addMethod(method);
        MethodWidget methodWidget = new MethodWidget(getClassDiagramScene(), method);
        addMember(methodsContainer, methodWidget);
        getScene().validate();

        // Temp renamer
        WidgetAction nameEditorAction = ActionFactory.createInplaceEditorAction(new MemberNameEditor(methodWidget));
        ActionFactory.getInplaceEditorController(nameEditorAction).openEditor(methodWidget.getNameLabel());
        MouseListener mouseListener = new CloseInplaceEditorOnClickAdapter(nameEditorAction);
        getScene().getView().addMouseListener(mouseListener);
    }
    
    public void addMethod(String methodSignature) {
        if (getComponent().signatureExists(methodSignature))
            return;
        Method method = new Method("untitledMethod", "void");
        try {
            MemberParser.fillMethodComponents(method, methodSignature);
        }
        catch(Exception ex) {
            JOptionPane.showMessageDialog(null, "Error while parsing method "+methodSignature, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        getComponent().addMethod(method);
        MethodWidget methodWidget = new MethodWidget(getClassDiagramScene(), method);
        addMember(methodsContainer, methodWidget);
        getScene().validate();
    }
    
    public void addMethods(List<String> methodSignatures) {
        for (String method : methodSignatures) {
            addMethod(method);
        }        
    }
    
    public void addMethods(String methodSignatures) {
        String[] list = methodSignatures.split("[\\r\\n]+");
        for (String method : list) {
            addMethod(method);
        }        
    }        

    public final void addDefaultConstructorWidget() {
        Constructor constructor = new Constructor(getName());
        String signature = constructor.getSignature();

        if (component.signatureExists(signature)) {
            JOptionPane.showMessageDialog(null, "Constructor \"" + signature + "\" already exists!");
        } else {
            getComponent().addConstructor(constructor);
            ConstructorWidget constructorWidget = new ConstructorWidget(getClassDiagramScene(), constructor);
            addMember(methodsContainer, constructorWidget);
        }
        getScene().validate();
    }

    public void removeMethod(MethodWidget methodWidget) {
        removeMember(methodsContainer, methodWidget);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        super.propertyChange(evt);
        if ("isAbstract".equals(evt.getPropertyName())) {
            boolean isAbstract = (Boolean) evt.getNewValue();
            updateAbstractLabel(isAbstract);
        }
    }

    private void updateAbstractLabel(boolean isAbstract) {
        if (isAbstract) {
            LabelWidget abstractLabel = new LabelWidget(headerWidget.getScene(), "<<abstract>>");
            abstractLabel.setFont(headerWidget.getScene().getFont().deriveFont(Font.ITALIC));
            abstractLabel.setAlignment(LabelWidget.Alignment.CENTER);
            headerWidget.addChild(0, abstractLabel);
            setMinimumSize(MIN_DIMENSION_2ROW);
        } else {
            headerWidget.removeChild(headerWidget.getChildren().get(0));
            setMinimumSize(MIN_DIMENSION_1ROW);
        }
        getScene().validate();
    }
}
