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
import org.uml.model.ImplementsRelationComponent;
import org.uml.model.RelationComponent;
import org.uml.model.UseRelationComponent;
import org.uml.visual.dialogs.ClassHasRelationPanel;
import org.uml.visual.dialogs.ClassImplementsRelationPanel;
import org.uml.visual.dialogs.ClassIsRelationPanel;
import org.uml.visual.dialogs.ClassUseRelationPanel;
import org.uml.visual.widgets.ClassWidget;
import org.uml.visual.widgets.ComponentWidgetBase;

/**
 *
 * @author "NUGS"
 */
// Currently does nothing usefull, needs upgrade
public class ClassWidgetAcceptProvider implements AcceptProvider{

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
            DialogDescriptor dd=null;
            String msg = "Choose target";
            Class<? extends RelationComponent> relationClass =(Class<? extends RelationComponent>) drClass;
            RelationComponent relation = relationClass.newInstance();
            ClassWidget w= (ClassWidget) widget;
            relation.setSource(w.getComponent());
            if (drClass.getSimpleName().equals("IsRelationComponent")) {
                ClassIsRelationPanel panel = new ClassIsRelationPanel((ClassWidget) widget);
                dd = new DialogDescriptor (panel,msg);
                if (DialogDisplayer.getDefault().notify(dd)==NotifyDescriptor.OK_OPTION) {
                ComponentWidgetBase compWidget = (ComponentWidgetBase) panel.getComboBoxTarget().getSelectedItem();
                createConnection(w,compWidget,relation);
            }
            }
            else if (drClass.getSimpleName().equals("HasRelationComponent")) {
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
                      r.setName(panel.getRelationName());
                      r.setCardinalitySource((CardinalityEnum)panel.getComboBoxCardinalitySource().getSelectedItem());
                      r.setCardinalityTarget((CardinalityEnum)panel.getComboBoxCardinalityTarget().getSelectedItem());
                ComponentWidgetBase compWidget = (ComponentWidgetBase) panel.getComboBoxTarget().getSelectedItem();    
                          createConnection(w,compWidget,relation);
                }
                }
            else if (drClass.getSimpleName().equals("ImplementsRelationComponent")) {
                ClassImplementsRelationPanel panel = new ClassImplementsRelationPanel(w);
                dd = new DialogDescriptor (panel,msg);
                if (DialogDisplayer.getDefault().notify(dd)==NotifyDescriptor.OK_OPTION) {
                      ImplementsRelationComponent r = (ImplementsRelationComponent) relation;
                ComponentWidgetBase compWidget = (ComponentWidgetBase) panel.getComboBoxTarget().getSelectedItem();
                          createConnection(w,compWidget,relation);
                }
                }

        } catch (InstantiationException | IllegalAccessException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    
    private boolean canAccept(Class droppedClass) {
    
        return droppedClass.equals(RelationComponent.class)||
                droppedClass.getSuperclass().equals(RelationComponent.class);
    
    }
    
    private void createConnection(ClassWidget sourceW,ComponentWidgetBase targetW, RelationComponent relation) {
                 if (targetW==null) {
                        return;
                    }
                relation.setTarget(targetW.getComponent());      
                sourceW.getClassDiagramScene().addEdge(relation);
                sourceW.getClassDiagramScene().setEdgeSource(relation,relation.getSource());
                sourceW.getClassDiagramScene().setEdgeTarget(relation,relation.getTarget());
    }
}
