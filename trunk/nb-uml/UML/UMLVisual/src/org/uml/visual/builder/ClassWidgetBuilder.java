/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.builder;

import java.awt.Image;
import org.openide.util.Utilities;

/**
 *
 * @author hrza
 */
public interface ClassWidgetBuilder {

    public static final Image MethodDefaultImage = Utilities.loadImage("org/uml/visual/icons/MethodDefault.jpg"); // NOI18N
    public static final Image AtributeDefaultImage = Utilities.loadImage("org/uml/visual/icons/AtributeDefault.jpg"); // NOI18N

    public void buildActions();

    public void buildFields();

    public void buildLabels();

    public void buildBorder();

    public void buildMethods();
}
