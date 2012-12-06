package org.uml.visual.widgets.actions;

import java.awt.Point;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.widget.Widget;
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

        Widget w = classWidget.createFieldWidget("field");
        classWidget.createFieldAction(w);
        classWidget.createFieldAction(classWidget.createAddFieldActionWidget());
        
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
