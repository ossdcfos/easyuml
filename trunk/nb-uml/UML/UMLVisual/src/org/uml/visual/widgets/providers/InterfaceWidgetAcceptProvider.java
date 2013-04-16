/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets.providers;

import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import org.netbeans.api.visual.action.AcceptProvider;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.widget.Widget;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.Exceptions;
import org.uml.model.CardinalityEnum;
import org.uml.model.HasRelationComponent;
import org.uml.model.RelationComponent;
import org.uml.model.UseRelationComponent;
import org.uml.visual.dialogs.ClassHasRelationPanel;
import org.uml.visual.dialogs.ClassUseRelationPanel;
import org.uml.visual.widgets.ClassWidget;
import org.uml.visual.widgets.ComponentWidgetBase;
import org.uml.visual.widgets.InterfaceWidget;

/**
 *
 * @author Uros
 */
public class InterfaceWidgetAcceptProvider implements AcceptProvider{

   Class drClass;
    
    @Override
    public ConnectorState isAcceptable(Widget widget, Point point, Transferable t) {
        DataFlavor flavor = t.getTransferDataFlavors()[2];
        Class droppedClass = flavor.getRepresentationClass();
        drClass = droppedClass;
        return canAccept(droppedClass) ? ConnectorState.ACCEPT : ConnectorState.REJECT;
    }

    @Override
    public void accept(Widget widget, Point point, Transferable t) {
        try {
            DialogDescriptor dd= null;
            String msg = "Choose target";
            Class<? extends RelationComponent> relationClass =(Class<? extends RelationComponent>) drClass;
            RelationComponent relation = relationClass.newInstance();
            InterfaceWidget w= (InterfaceWidget) widget;
            relation.setSource(w.getComponent());
            
            if (drClass.getSimpleName().equals("HasRelationComponent")) {
                ClassHasRelationPanel panel = new ClassHasRelationPanel(w);
                dd = new DialogDescriptor (panel,msg);
                if (DialogDisplayer.getDefault().notify(dd)==NotifyDescriptor.OK_OPTION) {
                HasRelationComponent r = (HasRelationComponent) relation;
                r.setName(panel.getRelationName());
                r.setCardinalityTarget((CardinalityEnum)panel.getComboBoxCardinalityTarget().getSelectedItem());
                ComponentWidgetBase compWidget = (ComponentWidgetBase) panel.getComboBoxTarget().getSelectedItem();    
                createConnection(w,compWidget,relation);
                }
                }
            else if (drClass.getSimpleName().equals("UseRelationComponent")) {
                ClassUseRelationPanel panel = new ClassUseRelationPanel(w);
                dd = new DialogDescriptor (panel,msg);
                if (DialogDisplayer.getDefault().notify(dd)==NotifyDescriptor.OK_OPTION) {
                      UseRelationComponent r = (UseRelationComponent) relation;
                      r.setName(panel.getName());
                      r.setCardinalitySource((CardinalityEnum)panel.getComboBoxCardinalitySource().getSelectedItem());
                      r.setCardinalityTarget((CardinalityEnum)panel.getComboBoxCardinalityTarget().getSelectedItem());
                ComponentWidgetBase compWidget = (ComponentWidgetBase) panel.getComboBoxTarget().getSelectedItem();    
                      createConnection(w,compWidget,relation);
                }
                }
            
        } catch (InstantiationException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IllegalAccessException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    
        
    private boolean canAccept(Class droppedClass) {
    
        return droppedClass.equals(UseRelationComponent.class)||
                droppedClass.equals(HasRelationComponent.class);
    
    }
    
    private void createConnection(ComponentWidgetBase sourceW,ComponentWidgetBase targetW, RelationComponent relation) {
                 if (targetW==null) {
                        return;
                    }
                relation.setTarget(targetW.getComponent());      
                sourceW.getClassDiagramScene().addEdge(relation);
                sourceW.getClassDiagramScene().setEdgeSource(relation,relation.getSource());
                sourceW.getClassDiagramScene().setEdgeTarget(relation,relation.getTarget());
    }
    
}
