/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets.providers;


import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.netbeans.api.visual.action.ConnectProvider;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.uml.model.CardinalityEnum;
import org.uml.model.HasRelationComponent;
import org.uml.model.ImplementsRelationComponent;
import org.uml.model.IsRelationComponent;
import org.uml.model.RelationComponent;
import org.uml.model.UseRelationComponent;
import org.uml.visual.dialogs.ChooseRelationPanel;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.ComponentWidgetBase;

/**
 *
 * @author "NUGS"
 */
public class ClassConnectProvider implements ConnectProvider{
    
    //private ClassDiagramComponent source=null;
    //private ClassDiagramComponent target=null;
    //ClassDiagramScene scene;

    public ClassConnectProvider() {

    }

    @Override
    public boolean isSourceWidget(Widget sourceWidget) {
        return (((sourceWidget instanceof ComponentWidgetBase)&&(sourceWidget!=null)));
    }

    @Override
    public ConnectorState isTargetWidget(Widget sourceWidget, Widget targetWidget) {        
    return (targetWidget instanceof ComponentWidgetBase) ? ConnectorState.ACCEPT : ConnectorState.REJECT;
    }

    @Override
    public boolean hasCustomTargetWidgetResolver(Scene scene) {
        return false;
    }

    @Override
    public Widget resolveTargetWidget(Scene scene, Point point) {
        return null;
    }

    @Override
    public void createConnection(Widget sourceWidget, Widget targetWidget) {
        
        final ChooseRelationPanel panel = new ChooseRelationPanel();
        panel.getCardinalitySourceComboBox().setEnabled(false);
        panel.getCardinalityTargetComboBox().setEnabled(false);
      
        //connect class to class
        
        if (sourceWidget.getClass().getSimpleName().equals("ClassWidget")&&
            targetWidget.getClass().getSimpleName().equals("InterfaceWidget")){
            RelationComponent relation = new ImplementsRelationComponent();
             ClassDiagramScene scene= (ClassDiagramScene)sourceWidget.getScene();
                     ComponentWidgetBase source= (ComponentWidgetBase) sourceWidget;
                     ComponentWidgetBase target= (ComponentWidgetBase) targetWidget;
                     relation.setSource(source.getComponent());
                     relation.setTarget(target.getComponent());
                     scene.addEdge(relation);
                     scene.setEdgeSource(relation,source.getComponent());
                     scene.setEdgeTarget(relation,target.getComponent());
                     return;
        }
        else if (sourceWidget.getClass().getSimpleName().equals("InterfaceWidget")) {
            panel.getRelationComponents().addItem(new HasRelationComponent());
            panel.getRelationComponents().addItem(new UseRelationComponent());
            createRelation(sourceWidget, targetWidget, panel);   
        }
        else if (sourceWidget.getClass().getSimpleName().equals("ClassWidget")) {
            panel.getRelationComponents().addItem(new HasRelationComponent());
            panel.getRelationComponents().addItem(new IsRelationComponent());
            panel.getRelationComponents().addItem(new UseRelationComponent());
            panel.getRelationComponents().addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (panel.getRelationComponents().getSelectedItem() instanceof ImplementsRelationComponent||
                        panel.getRelationComponents().getSelectedItem() instanceof IsRelationComponent){
                        panel.getNameTextField().setEnabled(false);
                        panel.getCardinalitySourceComboBox().setEnabled(false);
                        panel.getCardinalityTargetComboBox().setEnabled(false);     
                    }
                    if (panel.getRelationComponents().getSelectedItem() instanceof IsRelationComponent) {
                        panel.getCardinalityTargetComboBox().removeAllItems();
                        panel.getCardinalityTargetComboBox().addItem(CardinalityEnum.One2One);
                        panel.getCardinalityTargetComboBox().addItem(CardinalityEnum.Zero2One);
                        panel.getCardinalitySourceComboBox().setEnabled(false);
                        panel.getCardinalityTargetComboBox().setEnabled(true);
                    }
                    else if (panel.getRelationComponents().getSelectedItem() instanceof HasRelationComponent) {
                        panel.getNameTextField().setEnabled(true);
                        panel.getCardinalityTargetComboBox().removeAllItems();
                        panel.getCardinalityTargetComboBox().addItem(CardinalityEnum.One2Many);
                        panel.getCardinalityTargetComboBox().addItem(CardinalityEnum.Zero2Many);                 
                        panel.getCardinalitySourceComboBox().setEnabled(false);
                        panel.getCardinalityTargetComboBox().setEnabled(true); 
                    }
                    else if (panel.getRelationComponents().getSelectedItem() instanceof UseRelationComponent) {
                        panel.getNameTextField().setEnabled(true);
                        panel.getCardinalityTargetComboBox().removeAllItems();
                        panel.getCardinalityTargetComboBox().addItem(CardinalityEnum.One2Many);
                        panel.getCardinalityTargetComboBox().addItem(CardinalityEnum.Zero2Many);
                        panel.getCardinalityTargetComboBox().addItem(CardinalityEnum.One2One);
                        panel.getCardinalityTargetComboBox().addItem(CardinalityEnum.Zero2One);
                        panel.getCardinalitySourceComboBox().addItem(CardinalityEnum.One2Many);
                        panel.getCardinalitySourceComboBox().addItem(CardinalityEnum.Zero2Many);
                        panel.getCardinalitySourceComboBox().addItem(CardinalityEnum.One2One);
                        panel.getCardinalitySourceComboBox().addItem(CardinalityEnum.Zero2One);
                        panel.getCardinalitySourceComboBox().setEnabled(true);
                        panel.getCardinalityTargetComboBox().setEnabled(true); 
                    }
                    else {
                        panel.getCardinalitySourceComboBox().setEnabled(false);
                        panel.getCardinalityTargetComboBox().setEnabled(false);
                        panel.getNameTextField().setEnabled(true);
                    }
                        }
            });  
            createRelation(sourceWidget, targetWidget, panel);
        }
         
        //ChooseRelationDialog dialog = new ChooseRelationDialog(null, sourceWidget, targetWidget, true);
        //dialog.setLocationRelativeTo(WindowManager.getDefault().getMainWindow());
        //dialog.setTitle("Choose relation");
        //dialog.setVisible(true);
    }

    private void createRelation(Widget sourceWidget, Widget targetWidget, ChooseRelationPanel panel) {
        if (targetWidget==null) {
            return;
        }
        String msg = "Choose relation";
        panel.getRelationComponents().setSelectedItem(null);
        DialogDescriptor dd = new DialogDescriptor (panel,msg);
        
        if (DialogDisplayer.getDefault().notify(dd)==NotifyDescriptor.OK_OPTION) {
        RelationComponent relation = panel.getRelationComponent();
        relation.setName(panel.getNameFieldValue());
        if((sourceWidget instanceof ComponentWidgetBase)&&(targetWidget instanceof ComponentWidgetBase)) {
                     ClassDiagramScene scene= (ClassDiagramScene)sourceWidget.getScene();
                     ComponentWidgetBase source= (ComponentWidgetBase) sourceWidget;
                     ComponentWidgetBase target= (ComponentWidgetBase) targetWidget;
            if (relation instanceof HasRelationComponent) {
                HasRelationComponent hasRelation = (HasRelationComponent) relation;
                hasRelation.setCardinalitySource((CardinalityEnum)panel.getCardinalitySourceComboBox().getSelectedItem());
                hasRelation.setCardinalityTarget((CardinalityEnum)panel.getCardinalityTargetComboBox().getSelectedItem());
            }
         
                     relation.setSource(source.getComponent());
                     relation.setTarget(target.getComponent());
                     scene.addEdge(relation);
                     scene.setEdgeSource(relation,source.getComponent());
                     scene.setEdgeTarget(relation,target.getComponent());
        }
        }
    }
    
}
