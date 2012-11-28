/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets.actions;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.Exceptions;
import org.uml.visual.widgets.ClassWidget;

/**
 *
 * @author "NUGS"
 */
public class AddFieldAction implements SelectProvider {

    ClassWidget classWidget;

    public AddFieldAction(ClassWidget widget) {
        this.classWidget = widget;
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
        classWidget.removeField(widget.getParentWidget());

        Widget w = classWidget.createFieldWidget("atribut ");
        classWidget.createAddFieldAction(w);
        classWidget.createAddFieldAction(classWidget.createAddFieldActionWidget());
        
//        Robot robot = null;
//        try {
//            robot = new Robot();
//        } catch (AWTException ex) {
//            Exceptions.printStackTrace(ex);
//        }
//            
//                robot.mousePress(InputEvent.BUTTON1_MASK);
//                robot.mouseRelease(InputEvent.BUTTON1_MASK);
//                robot.mousePress(InputEvent.BUTTON1_MASK);
//                robot.mouseRelease(InputEvent.BUTTON1_MASK);
//                
            }
        }
