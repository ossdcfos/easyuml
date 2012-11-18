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
 public class PickFinalKeywordForMethodAction implements SelectProvider {

        private int currentFinal = 1;

        public PickFinalKeywordForMethodAction() {
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
