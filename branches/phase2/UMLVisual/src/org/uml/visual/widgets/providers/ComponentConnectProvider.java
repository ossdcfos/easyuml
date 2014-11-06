/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets.providers;

import org.uml.model.relations.ImplementsRelationComponent;
import org.uml.model.relations.IsRelationComponent;
import org.uml.model.relations.UseRelationComponent;
import org.uml.model.relations.RelationComponent;
import org.uml.model.relations.CardinalityEnum;
import org.uml.model.relations.HasBaseRelationComponent;
import java.awt.Point;
import org.netbeans.api.visual.action.*;
import org.netbeans.api.visual.widget.*;
import org.openide.*;
import org.openide.util.Exceptions;
import org.uml.visual.dialogs.*;
import org.uml.visual.palette.PaletteItem;
import org.uml.visual.palette.PaletteSupport;
import org.uml.visual.widgets.*;

/**
 *
 * @author "NUGS"
 */
public class ComponentConnectProvider implements ConnectProvider {

    DialogDescriptor dd;
    PaletteItem pi = null;

    public ComponentConnectProvider() {
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
    public boolean isSourceWidget(Widget sourceWidget) {
        pi = PaletteSupport.getPalette().getSelectedItem().lookup(PaletteItem.class);
        return (sourceWidget instanceof ClassWidget
                || (sourceWidget instanceof InterfaceWidget
                && pi != null && (pi.getDropClass() == IsRelationComponent.class || pi.getDropClass() == UseRelationComponent.class)));
    }

    @Override
    public ConnectorState isTargetWidget(Widget sourceWidget, Widget targetWidget) {
        pi = PaletteSupport.getPalette().getSelectedItem().lookup(PaletteItem.class);
        if (pi == null || !RelationComponent.class.isAssignableFrom(pi.getDropClass())
                || !(sourceWidget instanceof ComponentWidgetBase) || !(targetWidget instanceof ComponentWidgetBase)) {
            return ConnectorState.REJECT;
        }

        ComponentWidgetBase source = (ComponentWidgetBase) sourceWidget;
        ComponentWidgetBase target = (ComponentWidgetBase) targetWidget;

        RelationComponent relation;
        try {
            relation = (RelationComponent) pi.getDropClass().newInstance();
            if (relation.canConnect(source.getComponent(), target.getComponent())) {
                return ConnectorState.ACCEPT;
            } else {
                return ConnectorState.REJECT;
            }
        } catch (InstantiationException | IllegalAccessException ex) {
            Exceptions.printStackTrace(ex);
        }

        return ConnectorState.REJECT;
    }

    @Override
    public void createConnection(Widget sourceWidget, Widget targetWidget) {
        ComponentWidgetBase source = (ComponentWidgetBase) sourceWidget;
        ComponentWidgetBase target = (ComponentWidgetBase) targetWidget;
        Class<?> sourceClass = source.getClass();
        Class<?> targetClass = target.getClass();

        try {
            RelationComponent relation = (RelationComponent) pi.getDropClass().newInstance();

            if (relation instanceof IsRelationComponent) {
                if (sourceClass == ClassWidget.class && targetClass == ClassWidget.class
                        || sourceClass == InterfaceWidget.class && targetClass == InterfaceWidget.class) {
                    connect(relation, source, target);
                }
            } else if (relation instanceof HasBaseRelationComponent) {
                ClassHasRelationPanel panel = new ClassHasRelationPanel(source);
                panel.getComboBoxTarget().setSelectedItem(target);
                dd = new DialogDescriptor(panel, "Has relation");
                if (DialogDisplayer.getDefault().notify(dd) == NotifyDescriptor.OK_OPTION) {
                    HasBaseRelationComponent r = (HasBaseRelationComponent) relation;
                    r.setName(panel.getRelationName());
                    r.setCardinalityTarget((CardinalityEnum) panel.getComboBoxCardinalityTarget().getSelectedItem());
                    ComponentWidgetBase selectedTarget = (ComponentWidgetBase) panel.getComboBoxTarget().getSelectedItem();

                    CardinalityEnum ce = (CardinalityEnum) panel.getComboBoxCardinalityTarget().getSelectedItem();

                    if (ce.equals(CardinalityEnum.One2Many) || ce.equals(CardinalityEnum.Zero2Many)) {
                        String collectionType = panel.getCollectionType();
                        if (collectionType == null || collectionType.equals("")) {
                            collectionType = "List";
                        }
                        r.setCollectionType(collectionType);
                    }
                    connect(r, source, selectedTarget);
                }
            } else if (relation instanceof UseRelationComponent) {
                ClassUseRelationPanel panel = new ClassUseRelationPanel(source);
                panel.getComboBoxTarget().setSelectedItem(target);
                dd = new DialogDescriptor(panel, "Use relation");
                if (DialogDisplayer.getDefault().notify(dd) == NotifyDescriptor.OK_OPTION) {
                    UseRelationComponent r = (UseRelationComponent) relation;
                    r.setName(panel.getRelationName());
                    r.setCardinalitySource((CardinalityEnum) panel.getComboBoxCardinalitySource().getSelectedItem());
                    r.setCardinalityTarget((CardinalityEnum) panel.getComboBoxCardinalityTarget().getSelectedItem());
                    ComponentWidgetBase selectedTarget = (ComponentWidgetBase) panel.getComboBoxTarget().getSelectedItem();
                    connect(r, source, selectedTarget);
                }
            } else if (relation instanceof ImplementsRelationComponent) {
                connect(relation, source, target);
            }
        } catch (InstantiationException | IllegalAccessException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void connect(RelationComponent relation, ComponentWidgetBase source, ComponentWidgetBase target) {
        relation.setSource(source.getComponent());
        relation.setTarget(target.getComponent());
        
        source.getClassDiagramScene().addRelationToScene(relation, source.getComponent(), target.getComponent());
    }

    // ako se dodaje preko choose relationa - necemo jer moze drag'n'drop bilo sta, a ne da se vuce bilo sta
//            final ChooseRelationPanel panel = new ChooseRelationPanel();
//            panel.getCardinalitySourceComboBox().setEnabled(false);
//            panel.getCardinalityTargetComboBox().setEnabled(false);
//
//            // connect class -> interface
//            if (sourceWidget.getClass().getSimpleName().equals("ClassWidget")
//                    && targetWidget.getClass().getSimpleName().equals("InterfaceWidget")) {
//                relation.setSource(source.getComponent());
//                relation.setTarget(target.getComponent());
//
//                ClassDiagramScene scene = (ClassDiagramScene) sourceWidget.getScene();
//                scene.addEdge(relation);
//                scene.setEdgeSource(relation, source.getComponent());
//                scene.setEdgeTarget(relation, target.getComponent());
//                return;
//            } // connect interface -> any
//            else if (sourceWidget.getClass().getSimpleName().equals("InterfaceWidget")) {
//                panel.getRelationComponentsComboBox().addItem(new HasRelationComponent());
//                panel.getRelationComponentsComboBox().addItem(new UseRelationComponent());
//                createChooseRelationDialog(sourceWidget, targetWidget, panel);
//                dd.setValid(true);
//            } // connect class -> other than interface
//            else if (sourceWidget.getClass().getSimpleName().equals("ClassWidget")) {
//                panel.getRelationComponentsComboBox().addItem(new HasRelationComponent());
//                panel.getRelationComponentsComboBox().addItem(new IsRelationComponent());
//                panel.getRelationComponentsComboBox().addItem(new UseRelationComponent());
//                panel.getRelationComponentsComboBox().addActionListener(new ActionListener() {
//                    @Override
//                    public void actionPerformed(ActionEvent e) {
//                        // ok button enable
//                        if (panel.getRelationComponentsComboBox().getSelectedItem() instanceof ImplementsRelationComponent
//                                || panel.getRelationComponentsComboBox().getSelectedItem() instanceof IsRelationComponent) {
//                            panel.getNameTextField().setEnabled(false);
//                            panel.getCardinalitySourceComboBox().setEnabled(false);
//                            panel.getCardinalityTargetComboBox().setEnabled(false);
//                            dd.setValid(true);
//                        }
//                        if (panel.getRelationComponentsComboBox().getSelectedItem() instanceof IsRelationComponent) {
//                            panel.getCardinalityTargetComboBox().removeAllItems();
//                            panel.getCardinalitySourceComboBox().setEnabled(false);
//                            panel.getCardinalityTargetComboBox().setEnabled(false);
//                            dd.setValid(true);
//                        } else if (panel.getRelationComponentsComboBox().getSelectedItem() instanceof HasRelationComponent) {
//                            panel.getNameTextField().setEnabled(true);
//                            panel.getCardinalityTargetComboBox().removeAllItems();
//                            panel.getCardinalityTargetComboBox().addItem(CardinalityEnum.One2Many);
//                            panel.getCardinalityTargetComboBox().addItem(CardinalityEnum.Zero2Many);
//                            panel.getCardinalityTargetComboBox().addItem(CardinalityEnum.One2One);
//                            panel.getCardinalityTargetComboBox().addItem(CardinalityEnum.Zero2One);
//                            panel.getCardinalitySourceComboBox().setEnabled(false);
//                            panel.getCardinalityTargetComboBox().setEnabled(true);
//                            dd.setValid(true);
//                        } else if (panel.getRelationComponentsComboBox().getSelectedItem() instanceof UseRelationComponent) {
//                            panel.getNameTextField().setEnabled(true);
//                            panel.getCardinalityTargetComboBox().removeAllItems();
//                            panel.getCardinalityTargetComboBox().addItem(CardinalityEnum.One2Many);
//                            panel.getCardinalityTargetComboBox().addItem(CardinalityEnum.Zero2Many);
//                            panel.getCardinalityTargetComboBox().addItem(CardinalityEnum.One2One);
//                            panel.getCardinalityTargetComboBox().addItem(CardinalityEnum.Zero2One);
//                            panel.getCardinalitySourceComboBox().addItem(CardinalityEnum.One2Many);
//                            panel.getCardinalitySourceComboBox().addItem(CardinalityEnum.Zero2Many);
//                            panel.getCardinalitySourceComboBox().addItem(CardinalityEnum.One2One);
//                            panel.getCardinalitySourceComboBox().addItem(CardinalityEnum.Zero2One);
//                            panel.getCardinalitySourceComboBox().setEnabled(true);
//                            panel.getCardinalityTargetComboBox().setEnabled(true);
//                            dd.setValid(true);
//                        } else {
//                            panel.getCardinalitySourceComboBox().setEnabled(false);
//                            panel.getCardinalityTargetComboBox().setEnabled(false);
//                            panel.getNameTextField().setEnabled(true);
//                        }
//                    }
//                });
//                createChooseRelationDialog(sourceWidget, targetWidget, panel);
//            }
//
//            //ChooseRelationDialog dialog = new ChooseRelationDialog(null, sourceWidget, targetWidget, true);
//            //dialog.setLocationRelativeTo(WindowManager.getDefault().getMainWindow());
//            //dialog.setTitle("Choose relation");
//            //dialog.setVisible(true);
}
