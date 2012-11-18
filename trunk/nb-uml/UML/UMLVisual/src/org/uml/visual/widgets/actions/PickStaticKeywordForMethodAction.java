/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets.actions;

import java.awt.Point;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author "NUGS"
 */
public class PickStaticKeywordForMethodAction implements SelectProvider {

        private int currentStatic = 1;

        public PickStaticKeywordForMethodAction() {
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