/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets.providers;

import java.awt.Point;
import java.awt.event.*;
import org.netbeans.api.visual.action.*;
import org.netbeans.api.visual.widget.*;
import org.openide.*;
import org.uml.model.*;
import org.uml.visual.dialogs.*;
import org.uml.visual.palette.PaletteItem;
import org.uml.visual.palette.PaletteSupport;
import org.uml.visual.widgets.*;
/**
 *
 * @author "NUGS"
 */
public class ClassConnectProvider implements ConnectProvider {
    DialogDescriptor dd;
    PaletteItem pi = null;
    //private ClassDiagramComponent source=null;
    //private ClassDiagramComponent target=null;
    //ClassDiagramScene scene;
    public ClassConnectProvider() {
    }

    @Override
    public boolean isSourceWidget(Widget sourceWidget) {
        return (sourceWidget != null && (sourceWidget instanceof ClassWidget || sourceWidget instanceof InterfaceWidget));
    }

    @Override
    public ConnectorState isTargetWidget(Widget sourceWidget, Widget targetWidget) {
        pi = PaletteSupport.getPalette().getSelectedItem().lookup(PaletteItem.class);
        //PaletteItem pi = PaletteSupport.pc.getSelectedItem().lookup(PaletteItem.class);
        if(pi == null) return ConnectorState.REJECT;
        
        if(sourceWidget instanceof ClassWidget){
            if(targetWidget instanceof ClassWidget){
                //System.out.println(((ClassWidget)targetWidget).getName());
                if(pi.getTitle().equals("Is") || pi.getTitle().equals("Has") || pi.getTitle().equals("Use")) return ConnectorState.ACCEPT;
                else return ConnectorState.REJECT;
            } else if(targetWidget instanceof InterfaceWidget){
                if(pi.getTitle().equals("Implements") || pi.getTitle().equals("Has") || pi.getTitle().equals("Use")) return ConnectorState.ACCEPT;
                else return ConnectorState.REJECT;
            } else if(targetWidget instanceof EnumWidget){
                if(pi.getTitle().equals("Has") || pi.getTitle().equals("Use")) return ConnectorState.ACCEPT;
                else return ConnectorState.REJECT;
            }
        } else {
            
        }
        return ConnectorState.REJECT;
        //return (targetWidget instanceof ComponentWidgetBase) ? ConnectorState.ACCEPT : ConnectorState.REJECT;
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

        // connect class -> interface
        if (sourceWidget.getClass().getSimpleName().equals("ClassWidget")
                && targetWidget.getClass().getSimpleName().equals("InterfaceWidget")) {
            RelationComponent relation = new ImplementsRelationComponent();
            ComponentWidgetBase source = (ComponentWidgetBase) sourceWidget;
            ComponentWidgetBase target = (ComponentWidgetBase) targetWidget;
            relation.setSource(source.getComponent());
            relation.setTarget(target.getComponent());

            ClassDiagramScene scene = (ClassDiagramScene) sourceWidget.getScene();
            scene.addEdge(relation);
            scene.setEdgeSource(relation, source.getComponent());
            scene.setEdgeTarget(relation, target.getComponent());
            return;
        } // connect interface -> any
        else if (sourceWidget.getClass().getSimpleName().equals("InterfaceWidget")) {
            panel.getRelationComponentsComboBox().addItem(new HasRelationComponent());
            panel.getRelationComponentsComboBox().addItem(new UseRelationComponent());
            createRelation(sourceWidget, targetWidget, panel);
            dd.setValid(true);
        } // connect class -> other than interface
        else if (sourceWidget.getClass().getSimpleName().equals("ClassWidget")) {
            panel.getRelationComponentsComboBox().addItem(new HasRelationComponent());
            panel.getRelationComponentsComboBox().addItem(new IsRelationComponent());
            panel.getRelationComponentsComboBox().addItem(new UseRelationComponent());
            panel.getRelationComponentsComboBox().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // ok button enable
                    if (panel.getRelationComponentsComboBox().getSelectedItem() instanceof ImplementsRelationComponent
                            || panel.getRelationComponentsComboBox().getSelectedItem() instanceof IsRelationComponent) {
                        panel.getNameTextField().setEnabled(false);
                        panel.getCardinalitySourceComboBox().setEnabled(false);
                        panel.getCardinalityTargetComboBox().setEnabled(false);
                        dd.setValid(true);
                    }
                    if (panel.getRelationComponentsComboBox().getSelectedItem() instanceof IsRelationComponent) {
                        panel.getCardinalityTargetComboBox().removeAllItems();
                        //panel.getCardinalityTargetComboBox().addItem(CardinalityEnum.One2One);
                        //panel.getCardinalityTargetComboBox().addItem(CardinalityEnum.Zero2One);
                        panel.getCardinalitySourceComboBox().setEnabled(false);
                        panel.getCardinalityTargetComboBox().setEnabled(false);
                        dd.setValid(true);
                    } else if (panel.getRelationComponentsComboBox().getSelectedItem() instanceof HasRelationComponent) {
                        panel.getNameTextField().setEnabled(true);
                        panel.getCardinalityTargetComboBox().removeAllItems();
                        panel.getCardinalityTargetComboBox().addItem(CardinalityEnum.One2Many);
                        panel.getCardinalityTargetComboBox().addItem(CardinalityEnum.Zero2Many);
                        panel.getCardinalityTargetComboBox().addItem(CardinalityEnum.One2One);
                        panel.getCardinalityTargetComboBox().addItem(CardinalityEnum.Zero2One);
                        panel.getCardinalitySourceComboBox().setEnabled(false);
                        panel.getCardinalityTargetComboBox().setEnabled(true);
                        dd.setValid(true);
                    } else if (panel.getRelationComponentsComboBox().getSelectedItem() instanceof UseRelationComponent) {
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
                        dd.setValid(true);
                    } else {
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
        if (targetWidget == null) {
            return;
        }
        String msg = "Choose relation";
        panel.getRelationComponentsComboBox().setSelectedItem(null);
        dd = new DialogDescriptor(panel, msg);
        dd.setValid(false);

        if (DialogDisplayer.getDefault().notify(dd) == NotifyDescriptor.OK_OPTION) {
            RelationComponent relation = panel.getRelationComponent();
            relation.setName(panel.getNameFieldValue());
            if ((sourceWidget instanceof ComponentWidgetBase) && (targetWidget instanceof ComponentWidgetBase)) {
                ClassDiagramScene scene = (ClassDiagramScene) sourceWidget.getScene();
                ComponentWidgetBase source = (ComponentWidgetBase) sourceWidget;
                ComponentWidgetBase target = (ComponentWidgetBase) targetWidget;
                if (relation instanceof HasRelationComponent) {
                    HasRelationComponent hasRelation = (HasRelationComponent) relation;
                    hasRelation.setCardinalitySource((CardinalityEnum) panel.getCardinalitySourceComboBox().getSelectedItem());
                    CardinalityEnum ce = (CardinalityEnum) panel.getCardinalityTargetComboBox().getSelectedItem();
                    hasRelation.setCardinalityTarget(ce);
                    if (ce.equals(CardinalityEnum.One2Many) || ce.equals(CardinalityEnum.Zero2Many)) {
                        String collectionType = panel.getCollectionType();
                        if (collectionType == null || collectionType.equals("")) {
                            collectionType = "List";
                        }
                        hasRelation.setCollectionType(collectionType);
                    }
                }
                if (relation instanceof UseRelationComponent) {
                    UseRelationComponent useRelation = (UseRelationComponent) relation;
                    useRelation.setCardinalitySource((CardinalityEnum) panel.getCardinalitySourceComboBox().getSelectedItem());
                    useRelation.setCardinalityTarget((CardinalityEnum) panel.getCardinalitySourceComboBox().getSelectedItem());
                }

                relation.setSource(source.getComponent());
                relation.setTarget(target.getComponent());
                scene.addEdge(relation);
                scene.setEdgeSource(relation, source.getComponent());
                scene.setEdgeTarget(relation, target.getComponent());
            }
        }
    }
}
