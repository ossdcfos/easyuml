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
public class PickStaticKeywordForAttributeAction implements SelectProvider {

        private int currentStatic = 1;

        public PickStaticKeywordForAttributeAction() {
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