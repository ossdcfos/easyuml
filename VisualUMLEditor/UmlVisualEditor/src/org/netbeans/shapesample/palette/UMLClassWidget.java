/*
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at http://www.netbeans.org/cddl.html
 * or http://www.netbeans.org/cddl.txt.
 *
 * When distributing Covered Code, include this CDDL Header Notice in each file
 * and include the License file at http://www.netbeans.org/cddl.txt.
 * If applicable, add the following below the CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2007 Sun
 * Microsystems, Inc. All Rights Reserved.
 */
package org.netbeans.shapesample.palette;

import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.*;
import org.openide.util.Utilities;

import java.awt.*;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.action.TextFieldInplaceEditor;
import org.netbeans.api.visual.action.WidgetAction;

/**
 * @author David Kaspar
 */
public class UMLClassWidget extends Widget {

    private static final Image IMAGE_CLASS = Utilities.loadImage("org/netbeans/shapesample/palette/class.gif"); // NOI18N
    private static final Image IMAGE_MEMBER = Utilities.loadImage("org/netbeans/shapesample/palette/variablePublic.gif"); // NOI18N
    private static final Image MethodDefaultImage = Utilities.loadImage("org/netbeans/shapesample/palette/MethodDefault.jpg"); // NOI18N
    private static final Image MethodPublic_Image = Utilities.loadImage("org/netbeans/shapesample/palette/MethodPublic.jpg"); // NOI18N
    private static final Image MethodPrivateImage = Utilities.loadImage("org/netbeans/shapesample/palette/MethodPrivate.jpg"); // NOI18N
    private static final Image MethodProtected_Image = Utilities.loadImage("org/netbeans/shapesample/palette/MethodProtected.jpg"); // NOI18N

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

    final void a() {
    }

    private ImageWidget getNextAtributeAccessModifier(int n) {
        ImageWidget Image = new ImageWidget(getScene());
        switch (n) {
            case 1:
                Image.setImage(AtributeDefaultImage);
                break;
            case 2:
                Image.setImage(AtributePublic_Image);
                break;
            case 3:
                Image.setImage(AtributePrivateImage);
                break;
            case 4:
                Image.setImage(AtributeProtected_Image);
                break;
        }


        return Image;
    }
    private static final Image AtributeDefaultImage = Utilities.loadImage("org/netbeans/shapesample/palette/MethodDefault.jpg"); // NOI18N
    private static final Image AtributePublic_Image = Utilities.loadImage("org/netbeans/shapesample/palette/MethodPublic.jpg"); // NOI18N
    private static final Image AtributePrivateImage = Utilities.loadImage("org/netbeans/shapesample/palette/MethodPrivate.jpg"); // NOI18N
    private static final Image AtributeProtected_Image = Utilities.loadImage("org/netbeans/shapesample/palette/MethodProtected.jpg"); // NOI18N

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
                Image.setImage(MethodPublic_Image);
                break;
            case 3:
                Image.setImage(MethodPrivateImage);
                break;
            case 4:
                Image.setImage(MethodProtected_Image);
                break;
        }


        return Image;
    }

    private class addOperation implements SelectProvider {

        public addOperation() {
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

            addOperation(createOperation("metoda "));

        }
    }

    private class addMember implements SelectProvider {

        public addMember() {
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

            addMember(createMember("atribut"));
        }
    }

    private class deleteOperation implements SelectProvider {

        public deleteOperation() {
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
            removeOperation(widget.getParentWidget());
        }
    }

    private class deleteMember implements SelectProvider {

        public deleteMember() {
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
            removeMember(widget.getParentWidget());

        }
    }
    private LabelWidget className;
    private LabelWidget memberName;
    private LabelWidget operationName;
    private Widget members;
    private Widget operations;
    private static final Border BORDER_4 = BorderFactory.createEmptyBorder(4);
    private WidgetAction editorAction = ActionFactory.createInplaceEditorAction(new LabelTextFieldEditor());
    private WidgetAction deleteMember = ActionFactory.createSelectAction(new deleteMember());
    private WidgetAction deleteOperation = ActionFactory.createSelectAction(new deleteOperation());
    private WidgetAction addMember = ActionFactory.createSelectAction(new addMember());
    private WidgetAction addOperation = ActionFactory.createSelectAction(new addOperation());
    private WidgetAction pickModifier = ActionFactory.createSelectAction(new pickMethodModifier());
    private WidgetAction pickStaticKeywordForAtribute = ActionFactory.createSelectAction(new pickStaticKeywordForAtribute());
    private WidgetAction pickFinalKeywordForAtribute = ActionFactory.createSelectAction(new pickFinalKeywordForAtribute());
    private WidgetAction pickStaticKeywordForMethod = ActionFactory.createSelectAction(new pickStaticKeywordForMethod());
    private WidgetAction pickFinalKeywordForMethod = ActionFactory.createSelectAction(new pickFinalKeywordForMethod());

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

    public UMLClassWidget(Scene scene) {
        super(scene);
        setLayout(LayoutFactory.createVerticalFlowLayout());
        setBorder(BorderFactory.createLineBorder());
        setOpaque(true);
        setCheckClipping(true);

        Widget classWidget = new Widget(scene);
        classWidget.setLayout(LayoutFactory.createHorizontalFlowLayout());
        classWidget.setBorder(BORDER_4);

        ImageWidget classImage = new ImageWidget(scene);
        classImage.setImage(IMAGE_CLASS);
        classWidget.addChild(classImage);

        className = new LabelWidget(scene);
        // org.netbeans.api.visual.widget.Bir
        // className = new TextField(scene);
        // className.setEnabled(true);
        // className = new LabelTextFieldEditor();
        className.setFont(scene.getDefaultFont().deriveFont(Font.BOLD));
        classWidget.addChild(className);
        addChild(classWidget);
        className.getActions().addAction(editorAction);
        // WidgetAction editorAction = ActionFactory.createInplaceEditorAction(new LabelTextFieldEditor());

        addChild(new SeparatorWidget(scene, SeparatorWidget.Orientation.HORIZONTAL));

        members = new Widget(scene);
        members.setLayout(LayoutFactory.createVerticalFlowLayout());
        members.setOpaque(false);
        members.setBorder(BORDER_4);
        memberName = new LabelWidget(scene);
        members.addChild(memberName);
        addChild(members);

        addChild(new SeparatorWidget(scene, SeparatorWidget.Orientation.HORIZONTAL));

        operations = new Widget(scene);
        operations.setLayout(LayoutFactory.createVerticalFlowLayout());
        operations.setOpaque(false);
        operations.setBorder(BORDER_4);
        operationName = new LabelWidget(scene);
        members.addChild(operationName);
        addChild(operations);






        addMember(createAddMember());
        addOperation(createAddOperation());
    }

    public String getClassName() {
        return className.getLabel();
    }

    public void setClassName(String className) {
        this.className.setLabel(className);

    }

    public Widget createAddMember() {
        Scene scene = getScene();
        Widget widget = new Widget(scene);
        widget.setLayout(LayoutFactory.createAbsoluteLayout());
        LabelWidget labelWidget = new LabelWidget(scene);
        labelWidget.setLabel("+ add member");
        labelWidget.setAlignment(LabelWidget.Alignment.CENTER);
        labelWidget.getActions().addAction(addMember);
        widget.addChild(labelWidget);
        return widget;
    }

    protected Widget w() {
        return null;
    }

    Widget w1() {
        return null;
    }

    private Widget w2() {
        return null;
    }

    private Widget createAddOperation() {
        Scene scene = getScene();
        Widget widget = new Widget(scene);
        widget.setLayout(LayoutFactory.createAbsoluteLayout());
        LabelWidget labelWidget = new LabelWidget(scene);
        labelWidget.setLabel("+ add operation");
        labelWidget.setAlignment(LabelWidget.Alignment.CENTER);
        labelWidget.getActions().addAction(addOperation);
        widget.addChild(labelWidget);
        return widget;
    }

    public Widget createMember(String member) {
        Scene scene = getScene();
        Widget widget = new Widget(scene);
        widget.setLayout(LayoutFactory.createHorizontalFlowLayout());

        LabelWidget labelMinus = new LabelWidget(scene);
        labelMinus.setFont(scene.getDefaultFont().deriveFont(Font.BOLD));
        labelMinus.setLabel("-");
        labelMinus.getActions().addAction(deleteMember);
        widget.addChild(labelMinus);

        widget.addChild(createAtributeModifierPicker(scene));

        LabelWidget staticKeyword = new LabelWidget(scene);
        staticKeyword.setLabel(" _");
        staticKeyword.setFont(scene.getDefaultFont().deriveFont(Font.ITALIC));
        widget.addChild(staticKeyword);
        staticKeyword.getActions().addAction(pickStaticKeywordForAtribute);

        LabelWidget finalKeyword = new LabelWidget(scene);
        finalKeyword.setLabel(" _ ");
        finalKeyword.setFont(scene.getDefaultFont().deriveFont(Font.ITALIC));
        widget.addChild(finalKeyword);
        finalKeyword.getActions().addAction(pickFinalKeywordForAtribute);

        LabelWidget labelWidget = new LabelWidget(scene);
        labelWidget.setLabel(member);
        widget.addChild(labelWidget);
        labelWidget.getActions().addAction(editorAction);






        return widget;
    }

    private Widget createAtributeModifierPicker(Scene scene) {
        Widget w = new Widget(scene);
        w.setLayout(LayoutFactory.createVerticalFlowLayout());

        ImageWidget DefaultImage = new ImageWidget(scene);
        DefaultImage.setImage(AtributeDefaultImage);
        DefaultImage.getActions().addAction(pickModifier);
        w.addChild(DefaultImage);



        return w;
    }

    private Widget createMethodModifierPicker(Scene scene) {
        Widget w = new Widget(scene);
        w.setLayout(LayoutFactory.createVerticalFlowLayout());

        ImageWidget DefaultImage = new ImageWidget(scene);
        DefaultImage.setImage(MethodDefaultImage);
        DefaultImage.getActions().addAction(pickModifier);
        w.addChild(DefaultImage);



        return w;
    }

    public Widget createOperation(String operation) {
        Scene scene = getScene();
        Widget widget = new Widget(scene);
        widget.setLayout(LayoutFactory.createHorizontalFlowLayout());

        LabelWidget labelMinus = new LabelWidget(scene);
        labelMinus.setFont(scene.getDefaultFont().deriveFont(Font.BOLD));
        labelMinus.setLabel("-");
        labelMinus.getActions().addAction(deleteOperation);
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
        labelWidget.setLabel(operation);
        widget.addChild(labelWidget);
        labelWidget.getActions().addAction(editorAction);



        return widget;
    }

    public void addMember(Widget memberWidget) {
        members.addChild(memberWidget);
    }

    public void removeMember(Widget memberWidget) {
        members.removeChild(memberWidget);
    }

    private void addOperation(Widget operationWidget) {
        operations.addChild(operationWidget);
    }

    public void removeOperation(Widget operationWidget) {
        operations.removeChild(operationWidget);
    }

    public void setLabel(String text) {
        setClassName(text);
    }

    public Object getLabelWidget() {
        return className;
    }
}
