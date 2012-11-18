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
public class PickFinalKeywordForAttributeAction implements SelectProvider {

        private int currentFinal = 1;

        public PickFinalKeywordForAttributeAction() {
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
