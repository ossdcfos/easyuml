/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.action.TextFieldInplaceEditor;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.ImageWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.SeparatorWidget;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.openide.util.Exceptions;
import org.openide.util.Utilities;
import org.uml.model.Field;
import org.uml.model.UmlClassElement;
import org.uml.model.Visibility;
import org.uml.visual.widgets.actions.AddFieldAction;
import org.uml.visual.widgets.actions.LabelTextFieldEditorAction;

/**
 *
 * @author NUGS
 */
public class ClassWidget extends IconNodeWidget {

    //TODO Zoki da li si razmisljao da napravimo domen neki UmlElement pa da ovi nasledjuju to? 
    UmlClassElement classElement;
    
    private static final Image MethodDefaultImage = Utilities.loadImage("org/uml/visual/icons/MethodDefault.jpg"); // NOI18N
    private static final Image MethodPublicImage = Utilities.loadImage("org/uml/visual/icons/MethodPublic.jpg"); // NOI18N
    private static final Image MethodPrivateImage = Utilities.loadImage("org/uml/visual/icons/MethodPrivate.jpg"); // NOI18N
    private static final Image MethodProtectedImage = Utilities.loadImage("org/uml/visual/icons/MethodProtected.jpg"); // NOI18N
    private static final Image AtributeDefaultImage = Utilities.loadImage("org/uml/visual/icons/AtributeDefault.jpg"); // NOI18N
    private static final Image AtributePublicImage = Utilities.loadImage("org/uml/visual/icons/AtributePublic.jpg"); // NOI18N
    private static final Image AtributePrivateImage = Utilities.loadImage("org/uml/visual/icons/AtributePrivate.jpg"); // NOI18N
    private static final Image AtributeProtectedImage = Utilities.loadImage("org/uml/visual/icons/AtributeProtected.jpg"); // NOI18N
    
    private LabelWidget classNameWidget;
    private Widget fieldsWidget;
    private Widget methodsWidget;
    
    private WidgetAction addFieldAction = ActionFactory.createSelectAction(new AddFieldAction(this));
    private WidgetAction addMethodAction = ActionFactory.createSelectAction(new AddMethodAction());
    private WidgetAction deleteFieldAction = ActionFactory.createSelectAction(new DeleteFieldAction());
    private WidgetAction deleteMethodAction = ActionFactory.createSelectAction(new DeleteMethodAction());
    private WidgetAction editorAction = ActionFactory.createInplaceEditorAction(new LabelTextFieldEditor());

    private WidgetAction pickMethodModifier = ActionFactory.createSelectAction(new pickMethodModifier());
    private WidgetAction pickAtributeModifier = ActionFactory.createSelectAction(new pickAtributeModifier());
    private WidgetAction pickStaticKeywordForAtribute = ActionFactory.createSelectAction(new pickStaticKeywordForAtribute());
    private WidgetAction pickFinalKeywordForAtribute = ActionFactory.createSelectAction(new pickFinalKeywordForAtribute());
    private WidgetAction pickStaticKeywordForMethod = ActionFactory.createSelectAction(new pickStaticKeywordForMethod());
    private WidgetAction pickFinalKeywordForMethod = ActionFactory.createSelectAction(new pickFinalKeywordForMethod());
    
    private static final Border BORDER_4 = BorderFactory.createEmptyBorder(6);

    public ClassWidget(ClassDiagramScene scene, UmlClassElement umlClassElement) {
        super(scene);
        this.classElement = umlClassElement;
        
        setLayout(LayoutFactory.createVerticalFlowLayout());
        setBorder(BorderFactory.createLineBorder());
        setOpaque(true);
        setCheckClipping(true);
        
        Widget classWidget = new Widget(scene); // mora ovako zbog layouta ne moze this 
        classWidget.setLayout(LayoutFactory.createHorizontalFlowLayout());
        classWidget.setBorder(BORDER_4);
        
        //getActions().addAction(ActionFactory.createResizeAction());
        //getActions().addAction(ActionFactory.createMoveAction(ActionFactory.createSnapToGridMoveStrategy(16, 16), null));
        
        ImageWidget classImage= new ImageWidget(scene);
        classImage.setImage(classElement.getImage());
        
        classNameWidget = new LabelWidget(scene);
        classNameWidget.setFont(scene.getDefaultFont().deriveFont(Font.BOLD));
        classWidget.addChild(classNameWidget);
        addChild(classWidget);
        classNameWidget.getActions().addAction(ActionFactory.createInplaceEditorAction(new LabelTextFieldEditorAction(umlClassElement)));
        addChild(new SeparatorWidget(scene, SeparatorWidget.Orientation.HORIZONTAL));
        
           fieldsWidget = new Widget(scene);
        fieldsWidget.setLayout(LayoutFactory.createVerticalFlowLayout());
        fieldsWidget.setOpaque(false);
        fieldsWidget.setBorder(BORDER_4);
        LabelWidget memberName = new LabelWidget(scene);
        fieldsWidget.addChild(memberName);
        addChild(fieldsWidget);

        addChild(new SeparatorWidget(scene, SeparatorWidget.Orientation.HORIZONTAL));

        methodsWidget = new Widget(scene);
        methodsWidget.setLayout(LayoutFactory.createVerticalFlowLayout());
        methodsWidget.setOpaque(false);
        methodsWidget.setBorder(BORDER_4);
        LabelWidget operationName = new LabelWidget(scene);
        fieldsWidget.addChild(operationName);
        addChild(methodsWidget);
        
        createAddFieldAction(createAddFieldActionWidget());
        createAddMethodAction(createAddMethodActionWidget());
    }
     public String getClassName() {
        return classNameWidget.getLabel();
    }

    public void setClassName(String className) {
        this.classNameWidget.setLabel(className);

    }

    public Widget createAddFieldActionWidget() {
        Scene scene = getScene();
        Widget widget = new Widget(scene);
        widget.setLayout(LayoutFactory.createAbsoluteLayout());
        LabelWidget labelWidget = new LabelWidget(scene);
        labelWidget.setLabel("+ add field");
        labelWidget.setForeground(Color.GRAY);
        labelWidget.setAlignment(LabelWidget.Alignment.CENTER);
        labelWidget.getActions().addAction(addFieldAction);       
        widget.addChild(labelWidget);
        return widget;
    }


    private Widget createAddMethodActionWidget() {
        Scene scene = getScene();
        Widget widget = new Widget(scene);
        widget.setLayout(LayoutFactory.createAbsoluteLayout());
        LabelWidget labelWidget = new LabelWidget(scene);
        labelWidget.setLabel("+ add method");
        labelWidget.setForeground(Color.GRAY);
        labelWidget.setAlignment(LabelWidget.Alignment.CENTER);
        labelWidget.getActions().addAction(addMethodAction);
        widget.addChild(labelWidget);
        return widget;
    }

    public Widget createFieldWidget(String fieldName) {
        Scene scene = getScene();
       // FieldWidget fieldWidget = new FieldWidget(fieldName, scene);

        Widget fieldWidget = new Widget(scene);
        fieldWidget.setLayout(LayoutFactory.createHorizontalFlowLayout());
        
        
        LabelWidget labelMinus = new LabelWidget(scene);
        labelMinus.setFont(scene.getDefaultFont().deriveFont(Font.BOLD));
        labelMinus.setLabel("-");
        labelMinus.getActions().addAction(deleteFieldAction);
        fieldWidget.addChild(labelMinus);

        fieldWidget.addChild(createAtributeModifierPicker(scene));

        LabelWidget staticKeyword = new LabelWidget(scene);
        staticKeyword.setLabel(" _");
        staticKeyword.setFont(scene.getDefaultFont().deriveFont(Font.ITALIC));
        fieldWidget.addChild(staticKeyword);
        staticKeyword.getActions().addAction(pickStaticKeywordForAtribute);

        LabelWidget finalKeyword = new LabelWidget(scene);
        finalKeyword.setLabel(" _ ");
        finalKeyword.setFont(scene.getDefaultFont().deriveFont(Font.ITALIC));
        fieldWidget.addChild(finalKeyword);
        finalKeyword.getActions().addAction(pickFinalKeywordForAtribute);

        LabelWidget labelWidget = new LabelWidget(scene);
        labelWidget.setLabel(fieldName);
        labelWidget.getActions().addAction(editorAction);
        //dodato polje u classElement

        fieldWidget.addChild(labelWidget);
        
        return fieldWidget;
    }

    private Widget createAtributeModifierPicker(Scene scene) {
        Widget w = new Widget(scene);
        w.setLayout(LayoutFactory.createVerticalFlowLayout());

        ImageWidget DefaultImage = new ImageWidget(scene);
        DefaultImage.setImage(AtributeDefaultImage);
        DefaultImage.getActions().addAction(pickAtributeModifier);
        w.addChild(DefaultImage);
        return w;
    }

    private Widget createMethodModifierPicker(Scene scene) {
        Widget w = new Widget(scene);
        w.setLayout(LayoutFactory.createVerticalFlowLayout());
        ImageWidget DefaultImage = new ImageWidget(scene);
        DefaultImage.setImage(MethodDefaultImage);
        DefaultImage.getActions().addAction(pickMethodModifier);
        w.addChild(DefaultImage);
        return w;
    }

    public Widget createMethodWidget(String methodName) {
        Scene scene = getScene();
        Widget widget = new Widget(scene);
        widget.setLayout(LayoutFactory.createHorizontalFlowLayout());

        LabelWidget labelMinus = new LabelWidget(scene);
        labelMinus.setFont(scene.getDefaultFont().deriveFont(Font.BOLD));
        labelMinus.setLabel("-");
        labelMinus.getActions().addAction(deleteMethodAction);
        widget.addChild(labelMinus);

        widget.addChild(createMethodModifierPicker(scene));

        LabelWidget staticKeyword = new LabelWidget(scene);
        staticKeyword.setLabel(" _");
        staticKeyword.setFont(scene.getDefaultFont().deriveFont(Font.ITALIC));
        widget.addChild(staticKeyword);
        staticKeyword.getActions().addAction(pickStaticKeywordForMethod);

        LabelWidget finalKeyword = new LabelWidget(scene);
        finalKeyword.setLabel(" _ ");
        finalKeyword.setFont(scene.getDefaultFont().deriveFont(Font.ITALIC));
        widget.addChild(finalKeyword);
        finalKeyword.getActions().addAction(pickFinalKeywordForMethod);

        LabelWidget labelWidget = new LabelWidget(scene);
        labelWidget.setLabel(methodName);
        widget.addChild(labelWidget);
        labelWidget.getActions().addAction(editorAction);


        return widget;
    }

    public void createAddFieldAction(Widget memberWidget) {
        fieldsWidget.addChild(memberWidget);
    }

    public void removeField(Widget memberWidget) {
        fieldsWidget.removeChild(memberWidget);
    }

    private void createAddMethodAction(Widget operationWidget) {
        methodsWidget.addChild(operationWidget);
    }

    public void removeMethod(Widget operationWidget) {
        methodsWidget.removeChild(operationWidget);
    }

   
    
    private class pickFinalKeywordForMethod implements SelectProvider {

        private int currentFinal = 1;

        public pickFinalKeywordForMethod() {
        }

        @Override
        public boolean isAimingAllowed(Widget widget, Point point, boolean bln) {
            return true;
        }

        @Override
        public boolean isSelectionAllowed(Widget widget, Point point, boolean bln) {
            return true;
        }

        @Override
        public void select(Widget widget, Point point, boolean bln) {
            if (currentFinal == 1 //&& 
                    //!((LabelWidget)widget.getParentWidget().getChildren().get(2)).getLabel().equals(" abstract")
                    ) {
                System.out.println(((LabelWidget) widget.getParentWidget().getChildren().get(2)).getLabel());
                //System.out.println( ((LabelWidget)widget.getParentWidget().getChildren().get(2)).getLabel().equals(" static"));
                ((LabelWidget) widget).setLabel(" final ");

                currentFinal--;
            } else {
                ((LabelWidget) widget).setLabel(" _ ");

                currentFinal++;
            }
        }
    }

    private class pickStaticKeywordForMethod implements SelectProvider {

        private int currentStatic = 1;

        public pickStaticKeywordForMethod() {
        }

        @Override
        public boolean isAimingAllowed(Widget widget, Point point, boolean bln) {
            return true;
        }

        @Override
        public boolean isSelectionAllowed(Widget widget, Point point, boolean bln) {
            return true;
        }

        @Override
        public void select(Widget widget, Point point, boolean bln) {
            switch (currentStatic) {
                case 0:
                    ((LabelWidget) widget).setLabel(" static");
                    currentStatic++;
                    break;
                case 1:
                    ((LabelWidget) widget).setLabel(" abstract");
                    currentStatic++;
                    break;
                case 2:
                    ((LabelWidget) widget).setLabel(" _");
                    currentStatic = 0;
                    break;
            }
        }
    }

    private class pickFinalKeywordForAtribute implements SelectProvider {

        private int currentFinal = 1;

        public pickFinalKeywordForAtribute() {
        }

        @Override
        public boolean isAimingAllowed(Widget widget, Point point, boolean bln) {
            return true;
        }

        @Override
        public boolean isSelectionAllowed(Widget widget, Point point, boolean bln) {
            return true;
        }

        @Override
        public void select(Widget widget, Point point, boolean bln) {
            if (currentFinal == 0) {
                ((LabelWidget) widget).setLabel(" _ ");
                currentFinal++;
            } else {
                ((LabelWidget) widget).setLabel(" final ");
                currentFinal--;
            }
        }
    }

    private class pickStaticKeywordForAtribute implements SelectProvider {

        private int currentStatic = 1;

        public pickStaticKeywordForAtribute() {
        }

        @Override
        public boolean isAimingAllowed(Widget widget, Point point, boolean bln) {
            return true;
        }

        @Override
        public boolean isSelectionAllowed(Widget widget, Point point, boolean bln) {
            return true;
        }

        @Override
        public void select(Widget widget, Point point, boolean bln) {
            if (currentStatic == 0) {
                ((LabelWidget) widget).setLabel(" _");
                currentStatic++;
            } else {
                ((LabelWidget) widget).setLabel(" static");
                currentStatic--;
            }
        }
    }

    private class pickAtributeModifier implements SelectProvider {

        private int currentImage = 1;

        public pickAtributeModifier() {
        }

        @Override
        public boolean isAimingAllowed(Widget widget, Point point, boolean bln) {
            return true;
        }

        @Override
        public boolean isSelectionAllowed(Widget widget, Point point, boolean bln) {
            return true;
        }

        @Override
        public void select(Widget widget, Point point, boolean bln) {

            if (currentImage == 4) {
                currentImage = 1;
            } else {
                currentImage++;
            }
            widget.addChild(getNextAtributeAccessModifier(currentImage));

        }
    }



    private ImageWidget getNextAtributeAccessModifier(int n) {
        ImageWidget Image = new ImageWidget(getScene());
        switch (n) {
            case 1:
                Image.setImage(AtributeDefaultImage);
                break;
            case 2:
                Image.setImage(AtributePublicImage);
                break;
            case 3:
                Image.setImage(AtributePrivateImage);
                break;
            case 4:
                Image.setImage(AtributeProtectedImage);
                break;
        }


        return Image;
    }
   
    private class pickMethodModifier implements SelectProvider {

        private int currentImage = 1;

        public pickMethodModifier() {
        }

        @Override
        public boolean isAimingAllowed(Widget widget, Point point, boolean bln) {
            return true;
        }

        @Override
        public boolean isSelectionAllowed(Widget widget, Point point, boolean bln) {
            return true;
        }

        @Override
        public void select(Widget widget, Point point, boolean bln) {

            if (currentImage == 4) {
                currentImage = 1;
            } else {
                currentImage++;
            }
            widget.addChild(getNextMethodAccessModifier(currentImage));

        }
    }

    private ImageWidget getNextMethodAccessModifier(int n) {
        ImageWidget Image = new ImageWidget(getScene());
        switch (n) {

            
            case 1:
                Image.setImage(MethodDefaultImage);
                break;
            case 2:
                Image.setImage(MethodPublicImage);
                break;
            case 3:
                Image.setImage(MethodPrivateImage);
                break;
            case 4:
                Image.setImage(MethodProtectedImage);
                break;
        }


        return Image;
    }

    private class AddMethodAction implements SelectProvider {

        public AddMethodAction() {
        }

        @Override
        public boolean isAimingAllowed(Widget widget, Point point, boolean bln) {
            return true;
        }

        @Override
        public boolean isSelectionAllowed(Widget widget, Point point, boolean bln) {
            return true;
        }

        @Override
        public void select(Widget widget, Point point, boolean bln) {
            removeMethod(widget.getParentWidget());
            createAddMethodAction(createMethodWidget("metoda "));
            createAddMethodAction(createAddMethodActionWidget());
            
             Robot robot=null;
            try {
                robot = new Robot();
            } catch (AWTException ex) {
                Exceptions.printStackTrace(ex);
            }
           
          robot.mousePress(InputEvent.BUTTON1_MASK);
          robot.mouseRelease( InputEvent.BUTTON1_MASK);
          robot.mousePress(InputEvent.BUTTON1_MASK);
          robot.mouseRelease( InputEvent.BUTTON1_MASK);
            
            
            
            
        }
    }

    private class DeleteMethodAction implements SelectProvider {

        public DeleteMethodAction() {
        }

        @Override
        public boolean isAimingAllowed(Widget widget, Point point, boolean bln) {
            return true;
        }

        @Override
        public boolean isSelectionAllowed(Widget widget, Point point, boolean bln) {
            return true;
        }

        @Override
        public void select(Widget widget, Point point, boolean bln) {
            removeMethod(widget.getParentWidget());
        }
    }

    private class DeleteFieldAction implements SelectProvider {

        public DeleteFieldAction() {
        }

        @Override
        public boolean isAimingAllowed(Widget widget, Point point, boolean bln) {
            //throw new UnsupportedOperationException("Not supported yet.");
            return true;
        }

        @Override
        public boolean isSelectionAllowed(Widget widget, Point point, boolean bln) {
            //throw new UnsupportedOperationException("Not supported yet.");
            return true;
        }

        @Override
        public void select(Widget widget, Point point, boolean bln) {
            removeField(widget.getParentWidget());

        }
    }
    
    private class LabelTextFieldEditor implements TextFieldInplaceEditor {

        @Override
        public boolean isEnabled(Widget widget) {
            return true;
        }

        @Override
        public String getText(Widget widget) {
            return ((LabelWidget) widget).getLabel();
        }

        @Override
        public void setText(Widget widget, String text) {
            ((LabelWidget) widget).setLabel(text);
        }
    }    
    
}
