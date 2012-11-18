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
public class PickAttributeModifierAction implements SelectProvider {

        private static final Image AtributeDefaultImage = Utilities.loadImage("org/uml/visual/icons/AtributeDefault.jpg"); // NOI18N
        private static final Image AtributePublicImage = Utilities.loadImage("org/uml/visual/icons/AtributePublic.jpg"); // NOI18N
        private static final Image AtributePrivateImage = Utilities.loadImage("org/uml/visual/icons/AtributePrivate.jpg"); // NOI18N
        private static final Image AtributeProtectedImage = Utilities.loadImage("org/uml/visual/icons/AtributeProtected.jpg"); // NOI18N
    
        private int currentImage = 1;
        ClassWidget classWidget;
        public PickAttributeModifierAction(ClassWidget classWidget) {
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
            widget.addChild(getNextAtributeAccessModifier(currentImage));

        }
        
            public ImageWidget getNextAtributeAccessModifier(int n) {
        ImageWidget Image = new ImageWidget(classWidget.getScene());
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
        
    }