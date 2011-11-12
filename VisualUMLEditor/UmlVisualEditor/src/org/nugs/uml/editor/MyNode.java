/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nugs.uml.editor;

import java.awt.Image;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author User
 */
public class MyNode {
    
    private Image image;
    private Widget w;
    
    public MyNode(Image image) {
        this.image = image;
    }
    public MyNode(Widget w){
        this.w=w;
    }
    
    public Image getImage() {
        return image;
    }
}
