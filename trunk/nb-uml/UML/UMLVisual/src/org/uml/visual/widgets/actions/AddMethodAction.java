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
public class AddMethodAction implements SelectProvider {

        ClassWidget classWidget;
        public AddMethodAction(ClassWidget classWidget) {
            this.classWidget=classWidget;
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
            classWidget.removeMethod(widget.getParentWidget());
            classWidget.createAddMethodAction(classWidget.createMethodWidget("metoda "));
            classWidget.createAddMethodAction(classWidget.createAddMethodActionWidget());
            
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
