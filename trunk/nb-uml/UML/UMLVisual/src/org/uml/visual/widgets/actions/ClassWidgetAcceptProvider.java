/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets.actions;

import java.awt.Point;
import java.awt.datatransfer.Transferable;
import org.netbeans.api.visual.action.AcceptProvider;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.ConnectDecorator;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.widget.Widget;
import org.uml.visual.providers.ClassConnectProvider;
import org.uml.visual.widgets.ClassWidget;

/**
 *
 * @author "NUGS"
 */
// Currently does nothing usefull, needs upgrade
public class ClassWidgetAcceptProvider implements AcceptProvider{

    ClassWidget classWidget;

    public ClassWidgetAcceptProvider(ClassWidget classWidget) {
        this.classWidget = classWidget;
        classWidget.getActions().addAction(ActionFactory.createConnectAction(classWidget.getClassDiagramScene().getInterractionLayer(), new ClassConnectProvider()));
    }
    
    
    @Override
    public ConnectorState isAcceptable(Widget widget, Point point, Transferable t) {
        return ConnectorState.ACCEPT;
    }

    @Override
    public void accept(Widget widget, Point point, Transferable t) {
        //classWidget.getActions().addAction(ActionFactory.createConnectAction(classWidget.getClassDiagramScene().getInterractionLayer(), new ClassConnectProvider()));
        //classWidget.getActions().addAction(ActionFactory.createExtendedConnectAction(classWidget.getClassDiagramScene().getInterractionLayer(), new ClassConnectProvider()));
    }
    
}
