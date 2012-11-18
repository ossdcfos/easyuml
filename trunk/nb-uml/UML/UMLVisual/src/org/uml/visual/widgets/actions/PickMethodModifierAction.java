/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets.actions;

import java.awt.Image;
import java.awt.Point;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.widget.ImageWidget;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.Utilities;
import org.uml.visual.widgets.ClassWidget;

/**
 *
 * @author "NUGS"
 */
public class PickMethodModifierAction implements SelectProvider {

        private static final Image MethodDefaultImage = Utilities.loadImage("org/uml/visual/icons/MethodDefault.jpg"); // NOI18N
        private static final Image MethodPublicImage = Utilities.loadImage("org/uml/visual/icons/MethodPublic.jpg"); // NOI18N
        private static final Image MethodPrivateImage = Utilities.loadImage("org/uml/visual/icons/MethodPrivate.jpg"); // NOI18N
        private static final Image MethodProtectedImage = Utilities.loadImage("org/uml/visual/icons/MethodProtected.jpg"); // NOI18N
    
        private int currentImage = 1;
        ClassWidget classWidget;
        public PickMethodModifierAction(ClassWidget classWidget) {
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

            if (currentImage == 4) {
                currentImage = 1;
            } else {
                currentImage++;
            }
            widget.addChild(getNextMethodAccessModifier(currentImage));

        }
        
           public ImageWidget getNextMethodAccessModifier(int n) {
           ImageWidget Image = new ImageWidget(classWidget.getScene());
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
    }
