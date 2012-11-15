/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets.actions;

import java.awt.Point;
import java.awt.datatransfer.Transferable;
import org.netbeans.api.visual.action.AcceptProvider;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.Exceptions;
import org.uml.model.UmlClassElement;
import org.uml.visual.widgets.ClassDiagramContainerWidget;
import org.uml.visual.widgets.ClassDiagramScene;

/**
 *
 * @author Uros
 */
public class SceneAcceptProvider implements AcceptProvider{
    
    private ClassDiagramScene classDiagramScene;

    public SceneAcceptProvider(ClassDiagramScene classDiagramScene) {
   
        this.classDiagramScene= classDiagramScene;
    }
    

    @Override
    public ConnectorState isAcceptable(Widget widget, Point point, Transferable t) {
        return ConnectorState.ACCEPT;
    }

    @Override
    public void accept(Widget widget, Point point, Transferable t) {
           Class<? extends UmlClassElement> droppedClass = (Class<? extends UmlClassElement>) t.getTransferDataFlavors()[2].getRepresentationClass(); // Jako ruzno! Osmisliti kako da izvlacimo iz DataFlavor-a bez gadjanja indeksa!
                try {
                    classDiagramScene.addNode((UmlClassElement) droppedClass.newInstance());
                    //addChild(new ClassWidget((ClassDiagramScene) getScene(), (UmlClassElement) droppedClass.newInstance()));
                } catch (InstantiationException ex) {
                    Exceptions.printStackTrace(ex);
                } catch (IllegalAccessException ex) {
                    Exceptions.printStackTrace(ex);
                }
    }
    
    
}
