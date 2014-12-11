package org.uml.visual.dialogs;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import org.netbeans.api.visual.widget.Widget;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.uml.model.relations.CardinalityEnum;
import org.uml.model.relations.HasBaseRelation;
import org.uml.model.relations.ImplementsRelation;
import org.uml.model.relations.IsRelation;
import org.uml.model.relations.RelationBase;
import org.uml.model.relations.RelationUtilities;
import org.uml.model.relations.UseRelation;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.components.ClassWidget;
import org.uml.visual.widgets.components.ComponentWidgetBase;
import org.uml.visual.widgets.components.InterfaceWidget;

/**
 *
 * @author Boris
 */
public class ConnectRelationPanel extends javax.swing.JPanel {

    private DialogDescriptor dd;
    private Dialog dialog;
    private JButton btnOK = new JButton("OK");
    private JButton btnCancel = new JButton("Cancel");
    private ComponentWidgetBase source;
    private ComponentWidgetBase target;
    private RelationBase relation;
    private ClassDiagramScene classDiagramScene;

//    private ActionListener targetChangeListener = new ActionListener() {
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            target = (ComponentWidgetBase) cbxTarget.getSelectedItem();
//
////            if (source != null && target != null && source == target && relation.getClass() != HasBaseRelation.class) {
////                cbxSource.setSelectedItem(null);
////                source = null;
////            }
//            updateOKButton();
//        }
//    };
//    private ActionListener sourceChangeListener = new ActionListener() {
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            source = (ComponentWidgetBase) cbxSource.getSelectedItem();
////            if (source != null) {
////                cbxTarget.removeActionListener(targetChangeListener);
////                cbxTarget.removeAllItems();
////                fillTargetComboBox(relation);
////                cbxTarget.addActionListener(targetChangeListener);
////            }
////            if (source != null && target != null && source == target && relation.getClass() != HasBaseRelation.class) {
////                cbxTarget.setSelectedItem(null);
////                target = null;
////            }
//            updateOKButton();
//        }
//    };
    private ConnectRelationPanel() {
        initComponents();

        btnOK.addActionListener(new ActionListener() {
            ComponentWidgetBase src = (ComponentWidgetBase) cbxSource.getSelectedItem();
            ComponentWidgetBase trg = (ComponentWidgetBase) cbxTarget.getSelectedItem();

            @Override
            public void actionPerformed(ActionEvent e) {
                if (relation instanceof IsRelation) {
                    connect(relation, src, trg);
                } else if (relation instanceof ImplementsRelation) {
                    connect(relation, src, trg);
                } else if (relation instanceof HasBaseRelation) {
                    HasBaseRelation r = (HasBaseRelation) relation;
                    r.setName(txfName.getText());
                    r.setCardinalityTarget((CardinalityEnum) cbxCardinalityTarget.getSelectedItem());
                    ComponentWidgetBase selectedTarget = (ComponentWidgetBase) cbxTarget.getSelectedItem();

                    CardinalityEnum ce = (CardinalityEnum) cbxCardinalityTarget.getSelectedItem();

                    if (ce.equals(CardinalityEnum.One2Many) || ce.equals(CardinalityEnum.Zero2Many)) {
                        String collectionType = (String) cbxCollectionType.getSelectedItem();
                        if (collectionType == null || collectionType.equals("")) {
                            collectionType = "List";
                        }
                        r.setCollectionType(collectionType);
                    }
                    connect(r, src, selectedTarget);
                } else if (relation instanceof UseRelation) {
                    UseRelation r = (UseRelation) relation;
                    r.setName(txfName.getText());
                    r.setCardinalitySource((CardinalityEnum) cbxCardinalitySource.getSelectedItem());
                    r.setCardinalityTarget((CardinalityEnum) cbxCardinalityTarget.getSelectedItem());
                    ComponentWidgetBase selectedTarget = (ComponentWidgetBase) cbxTarget.getSelectedItem();
                    connect(r, src, selectedTarget);
                }
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
    }

    public ConnectRelationPanel(ClassDiagramScene scene) {
        this();
        this.classDiagramScene = scene;
        fillAllRelations();
    }

    public ConnectRelationPanel(ClassDiagramScene scene, RelationBase relation) {
        this();
        this.classDiagramScene = scene;
        this.relation = relation;

        cbxRelation.addItem(relation);
    }

    public ConnectRelationPanel(ClassDiagramScene scene, ComponentWidgetBase source, RelationBase relation) {
        this();
        this.classDiagramScene = scene;
        this.source = source;
        this.relation = relation;

        cbxRelation.addItem(relation);

        cbxSource.setSelectedItem(source);
        cbxTarget.setSelectedItem(target);
    }

    public ConnectRelationPanel(ClassDiagramScene scene, ComponentWidgetBase source, ComponentWidgetBase target) {
        this();
        this.classDiagramScene = scene;
        this.source = source;
        this.target = target;

        fillRelationsComboBox();

        cbxSource.setSelectedItem(source);
        cbxTarget.setSelectedItem(target);
    }

    public ConnectRelationPanel(ClassDiagramScene scene, ComponentWidgetBase source, ComponentWidgetBase target, RelationBase relation) {
        this();
        this.classDiagramScene = scene;
        this.source = source;
        this.target = target;
        this.relation = relation;

        cbxRelation.addItem(relation);

        cbxSource.setSelectedItem(source);
        cbxTarget.setSelectedItem(target);
    }

    public void openRelationDialog() {
        dd = new DialogDescriptor(this, "Create relation", true, new Object[]{btnOK, btnCancel}, null, DialogDescriptor.DEFAULT_ALIGN, null, null);
        dialog = DialogDisplayer.getDefault().createDialog(dd);
        dialog.setVisible(true);
        adaptLayoutBasedOnRelation(relation);
        updateOKButton();
    }

    private void connect(RelationBase relation, ComponentWidgetBase source, ComponentWidgetBase target) {
        relation.setSource(source.getComponent());
        relation.setTarget(target.getComponent());

        source.getClassDiagramScene().addRelationToScene(relation, source.getComponent(), target.getComponent());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlRelation = new javax.swing.JPanel();
        lblRelationType = new javax.swing.JLabel();
        cbxRelation = new javax.swing.JComboBox<RelationBase>();
        pnlName = new javax.swing.JPanel();
        txfName = new javax.swing.JTextField();
        lblName = new javax.swing.JLabel();
        pnlSource = new javax.swing.JPanel();
        lblSource = new javax.swing.JLabel();
        cbxSource = new javax.swing.JComboBox<Widget>();
        pnlTarget = new javax.swing.JPanel();
        lblTarget = new javax.swing.JLabel();
        cbxTarget = new javax.swing.JComboBox<Widget>();
        pnlCardinalitySource = new javax.swing.JPanel();
        lblCardinalitySource = new javax.swing.JLabel();
        cbxCardinalitySource = new javax.swing.JComboBox<CardinalityEnum>();
        pnlCardinalityTarget = new javax.swing.JPanel();
        lblCardinalityTarget = new javax.swing.JLabel();
        cbxCardinalityTarget = new javax.swing.JComboBox<CardinalityEnum>();
        pnlCollectionType = new javax.swing.JPanel();
        lblCollectionType = new javax.swing.JLabel();
        cbxCollectionType = new javax.swing.JComboBox<String>();

        setMinimumSize(new java.awt.Dimension(100, 42));
        setRequestFocusEnabled(false);
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        org.openide.awt.Mnemonics.setLocalizedText(lblRelationType, org.openide.util.NbBundle.getMessage(ConnectRelationPanel.class, "ConnectRelationPanel.lblRelationType.text")); // NOI18N

        cbxRelation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxRelationActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlRelationLayout = new javax.swing.GroupLayout(pnlRelation);
        pnlRelation.setLayout(pnlRelationLayout);
        pnlRelationLayout.setHorizontalGroup(
            pnlRelationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRelationLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblRelationType)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addComponent(cbxRelation, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnlRelationLayout.setVerticalGroup(
            pnlRelationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRelationLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlRelationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRelationType)
                    .addComponent(cbxRelation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        add(pnlRelation);

        txfName.setText(org.openide.util.NbBundle.getMessage(ConnectRelationPanel.class, "ConnectRelationPanel.txfName.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(lblName, org.openide.util.NbBundle.getMessage(ConnectRelationPanel.class, "ConnectRelationPanel.lblName.text")); // NOI18N

        javax.swing.GroupLayout pnlNameLayout = new javax.swing.GroupLayout(pnlName);
        pnlName.setLayout(pnlNameLayout);
        pnlNameLayout.setHorizontalGroup(
            pnlNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlNameLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 69, Short.MAX_VALUE)
                .addComponent(txfName, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnlNameLayout.setVerticalGroup(
            pnlNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlNameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblName)
                    .addComponent(txfName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        add(pnlName);

        org.openide.awt.Mnemonics.setLocalizedText(lblSource, org.openide.util.NbBundle.getMessage(ConnectRelationPanel.class, "ConnectRelationPanel.lblSource.text")); // NOI18N

        cbxSource.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxSourceActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlSourceLayout = new javax.swing.GroupLayout(pnlSource);
        pnlSource.setLayout(pnlSourceLayout);
        pnlSourceLayout.setHorizontalGroup(
            pnlSourceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSourceLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSource)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 66, Short.MAX_VALUE)
                .addComponent(cbxSource, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnlSourceLayout.setVerticalGroup(
            pnlSourceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSourceLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlSourceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSource)
                    .addComponent(cbxSource, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        lblSource.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(ConnectRelationPanel.class, "ConnectRelationPanel.lblSource.AccessibleContext.accessibleName")); // NOI18N

        add(pnlSource);

        org.openide.awt.Mnemonics.setLocalizedText(lblTarget, org.openide.util.NbBundle.getMessage(ConnectRelationPanel.class, "ConnectRelationPanel.lblTarget.text")); // NOI18N

        cbxTarget.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxTargetActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlTargetLayout = new javax.swing.GroupLayout(pnlTarget);
        pnlTarget.setLayout(pnlTargetLayout);
        pnlTargetLayout.setHorizontalGroup(
            pnlTargetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTargetLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTarget)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 67, Short.MAX_VALUE)
                .addComponent(cbxTarget, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnlTargetLayout.setVerticalGroup(
            pnlTargetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTargetLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlTargetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTarget)
                    .addComponent(cbxTarget, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        add(pnlTarget);

        org.openide.awt.Mnemonics.setLocalizedText(lblCardinalitySource, org.openide.util.NbBundle.getMessage(ConnectRelationPanel.class, "ConnectRelationPanel.lblCardinalitySource.text")); // NOI18N

        javax.swing.GroupLayout pnlCardinalitySourceLayout = new javax.swing.GroupLayout(pnlCardinalitySource);
        pnlCardinalitySource.setLayout(pnlCardinalitySourceLayout);
        pnlCardinalitySourceLayout.setHorizontalGroup(
            pnlCardinalitySourceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCardinalitySourceLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblCardinalitySource)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addComponent(cbxCardinalitySource, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnlCardinalitySourceLayout.setVerticalGroup(
            pnlCardinalitySourceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCardinalitySourceLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCardinalitySourceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCardinalitySource)
                    .addComponent(cbxCardinalitySource, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        add(pnlCardinalitySource);

        org.openide.awt.Mnemonics.setLocalizedText(lblCardinalityTarget, org.openide.util.NbBundle.getMessage(ConnectRelationPanel.class, "ConnectRelationPanel.lblCardinalityTarget.text")); // NOI18N

        javax.swing.GroupLayout pnlCardinalityTargetLayout = new javax.swing.GroupLayout(pnlCardinalityTarget);
        pnlCardinalityTarget.setLayout(pnlCardinalityTargetLayout);
        pnlCardinalityTargetLayout.setHorizontalGroup(
            pnlCardinalityTargetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCardinalityTargetLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblCardinalityTarget)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addComponent(cbxCardinalityTarget, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnlCardinalityTargetLayout.setVerticalGroup(
            pnlCardinalityTargetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCardinalityTargetLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCardinalityTargetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCardinalityTarget)
                    .addComponent(cbxCardinalityTarget, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14))
        );

        add(pnlCardinalityTarget);

        org.openide.awt.Mnemonics.setLocalizedText(lblCollectionType, org.openide.util.NbBundle.getMessage(ConnectRelationPanel.class, "ConnectRelationPanel.lblCollectionType.text")); // NOI18N

        javax.swing.GroupLayout pnlCollectionTypeLayout = new javax.swing.GroupLayout(pnlCollectionType);
        pnlCollectionType.setLayout(pnlCollectionTypeLayout);
        pnlCollectionTypeLayout.setHorizontalGroup(
            pnlCollectionTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCollectionTypeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblCollectionType)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addComponent(cbxCollectionType, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnlCollectionTypeLayout.setVerticalGroup(
            pnlCollectionTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCollectionTypeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCollectionTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCollectionType)
                    .addComponent(cbxCollectionType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        add(pnlCollectionType);
    }// </editor-fold>//GEN-END:initComponents

    private void cbxRelationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxRelationActionPerformed
        relation = (RelationBase) cbxRelation.getSelectedItem();

        txfName.setEnabled(false);
        disableElements();

        cbxSource.setEnabled(true);
        fillSourceComboBox(relation);

        cbxTarget.setEnabled(true);
        fillTargetComboBox(relation);

        if (relation instanceof HasBaseRelation) {
            lblName.setEnabled(true);
            lblName.setText("Field name:");
            txfName.setEnabled(true);
            fillCardinalityComboBox(cbxCardinalityTarget);
            lblCardinalityTarget.setEnabled(true);
            fillCollectionsComboBox();
            lblCollectionType.setEnabled(true);
        } else if (relation instanceof UseRelation) {
            lblName.setEnabled(true);
            lblName.setText("Name:");
            txfName.setEnabled(true);
            fillCardinalityComboBox(cbxCardinalitySource);
            lblCardinalitySource.setEnabled(true);
            fillCardinalityComboBox(cbxCardinalityTarget);
            lblCardinalityTarget.setEnabled(true);
        }
        adaptLayoutBasedOnRelation(relation);
        updateOKButton();
    }//GEN-LAST:event_cbxRelationActionPerformed

    private void cbxSourceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxSourceActionPerformed
        updateOKButton();
    }//GEN-LAST:event_cbxSourceActionPerformed

    private void cbxTargetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxTargetActionPerformed
        updateOKButton();
    }//GEN-LAST:event_cbxTargetActionPerformed

    private void fillSourceComboBox(RelationBase rc) {
        List<Widget> widgets = classDiagramScene.getMainLayer().getChildren();

        if (rc instanceof IsRelation) {
            for (Widget widget : widgets) {
                if (widget instanceof ClassWidget || widget instanceof InterfaceWidget) {
                    cbxSource.addItem(widget);
                }
            }
        } else if (rc instanceof HasBaseRelation) {
            for (Widget widget : widgets) {
                if (widget instanceof ClassWidget) {
                    cbxSource.addItem(widget);
                }
            }
        } else if (rc instanceof UseRelation) {
            for (Widget widget : widgets) {
                if (widget instanceof ClassWidget || widget instanceof InterfaceWidget) {
                    cbxSource.addItem(widget);
                }
            }
        } else if (rc instanceof ImplementsRelation) {
            for (Widget widget : widgets) {
                if (widget instanceof ClassWidget) {
                    cbxSource.addItem(widget);
                }
            }
        }

        cbxSource.setSelectedItem(null);
    }

    private void fillTargetComboBox(RelationBase rc) {
        List<Widget> widgets = classDiagramScene.getMainLayer().getChildren();

        if (cbxSource.getSelectedItem() == null) {
            if (rc instanceof IsRelation) {
                for (Widget widget : widgets) {
                    if (widget instanceof ClassWidget || widget instanceof InterfaceWidget) {
                        cbxTarget.addItem(widget);
                    }
                }
            } else if (rc instanceof HasBaseRelation) {
                for (Widget widget : widgets) {
                    cbxTarget.addItem(widget);
                }
            } else if (rc instanceof UseRelation) {
                for (Widget widget : widgets) {
                    cbxTarget.addItem(widget);
                }
            } else if (rc instanceof ImplementsRelation) {
                for (Widget widget : widgets) {
                    if (widget instanceof InterfaceWidget) {
                        cbxTarget.addItem(widget);
                    }
                }
            }
        } else {
            ComponentWidgetBase componentWidget = (ComponentWidgetBase) cbxSource.getSelectedItem();
            for (Widget widget : widgets) {
                if (rc.canConnect(componentWidget.getComponent(), ((ComponentWidgetBase) widget).getComponent())) {
                    cbxTarget.addItem(widget);
                }
            }
        }

        cbxTarget.setSelectedItem(null);
    }

    private void fillSourceAndTargetComboBoxes(RelationBase rc) {
        List<Widget> widgets = classDiagramScene.getMainLayer().getChildren();

        if (rc instanceof IsRelation) {
            for (Widget widget : widgets) {
                if (widget instanceof ClassWidget) {
                    cbxSource.addItem(widget);
                    //if(!widget.equals(cbxClassesSource.getItemAt(0))) 
                    cbxTarget.addItem(widget);
                }
            }
        } else if (rc instanceof HasBaseRelation) {
            for (Widget widget : widgets) {
                if (widget instanceof ClassWidget) {
                    cbxSource.addItem(widget);
                }
                cbxTarget.addItem(widget);
            }
        } else if (rc instanceof UseRelation) {
            for (Widget widget : widgets) {
                if (widget instanceof ClassWidget || widget instanceof InterfaceWidget) {
                    cbxSource.addItem(widget);
                }
                //if(!widget.equals(cbxClassesSource.getItemAt(0)))
                cbxTarget.addItem(widget);
            }
        } else if (rc instanceof ImplementsRelation) {
            for (Widget widget : widgets) {
                if (widget instanceof ClassWidget) {
                    cbxSource.addItem(widget);
                }
                if (widget instanceof InterfaceWidget) {
                    cbxTarget.addItem(widget);
                }
            }
        }

        cbxSource.setSelectedItem(null);
        cbxTarget.setSelectedItem(null);
    }

    private void fillRelationsComboBox() {
        for (RelationBase rc : RelationUtilities.allRelations()) {
            if (rc.canConnect(source.getComponent(), target.getComponent())) {
                cbxRelation.addItem(rc);
            }
        }
    }

    private void fillAllRelations() {
        for (RelationBase rc : RelationUtilities.allRelations()) {
            cbxRelation.addItem(rc);
        }
    }

    private void fillCardinalityComboBox(JComboBox<CardinalityEnum> comboBox) {
        comboBox.addItem(CardinalityEnum.One2Many);
        comboBox.addItem(CardinalityEnum.Zero2Many);
        comboBox.addItem(CardinalityEnum.One2One);
        comboBox.addItem(CardinalityEnum.Zero2One);
        comboBox.setEnabled(true);
    }

    private void fillCollectionsComboBox() {
        String[] collectionTypes = new String[]{"List", "ArrayList", "LinkedList"};
        cbxCollectionType.removeAllItems();
        cbxCollectionType.setModel(new DefaultComboBoxModel<>(collectionTypes));
        cbxCollectionType.setSelectedIndex(0);
        cbxCollectionType.setEditable(true);
        cbxCollectionType.setEnabled(true);
    }

    private void clearAndDisableComboBox(JComboBox<?> comboBox) {
        comboBox.removeAllItems();
        comboBox.setEnabled(false);
    }

    private void disableElements() {
        lblName.setEnabled(false);

        clearAndDisableComboBox(cbxSource);
        clearAndDisableComboBox(cbxTarget);

        clearAndDisableComboBox(cbxCardinalitySource);
        lblCardinalitySource.setEnabled(false);
        clearAndDisableComboBox(cbxCardinalityTarget);
        lblCardinalityTarget.setEnabled(false);
        clearAndDisableComboBox(cbxCollectionType);
        lblCollectionType.setEnabled(false);
    }

    private void adaptLayoutBasedOnRelation(RelationBase relation) {
        if (relation instanceof IsRelation || relation instanceof ImplementsRelation) {
            pnlName.setVisible(false);
            pnlCardinalitySource.setVisible(false);
            pnlCardinalityTarget.setVisible(false);
            pnlCollectionType.setVisible(false);
            if (dialog != null) {
                dialog.setBounds(dialog.getBounds().x, dialog.getBounds().y, dialog.getBounds().width, 3 * 42 + 100);
            }
        } else if (relation instanceof HasBaseRelation || relation instanceof UseRelation) {
            pnlName.setVisible(true);
            pnlCardinalitySource.setVisible(true);
            pnlCardinalityTarget.setVisible(true);
            pnlCollectionType.setVisible(true);
            if (dialog != null) {
                dialog.setBounds(dialog.getBounds().x, dialog.getBounds().y, dialog.getBounds().width, 7 * 42 + 100);
            }
        }
    }

    private void updateOKButton() {
        btnOK.setEnabled(false);
        ComponentWidgetBase src = (ComponentWidgetBase) cbxSource.getSelectedItem();
        ComponentWidgetBase trg = (ComponentWidgetBase) cbxTarget.getSelectedItem();
        if (src != null && trg != null && relation != null) {
            if (relation.canConnect(src.getComponent(), trg.getComponent())) {
                btnOK.setEnabled(true);
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<CardinalityEnum> cbxCardinalitySource;
    private javax.swing.JComboBox<CardinalityEnum> cbxCardinalityTarget;
    private javax.swing.JComboBox<String> cbxCollectionType;
    private javax.swing.JComboBox<RelationBase> cbxRelation;
    private javax.swing.JComboBox<Widget> cbxSource;
    private javax.swing.JComboBox<Widget> cbxTarget;
    private javax.swing.JLabel lblCardinalitySource;
    private javax.swing.JLabel lblCardinalityTarget;
    private javax.swing.JLabel lblCollectionType;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblRelationType;
    private javax.swing.JLabel lblSource;
    private javax.swing.JLabel lblTarget;
    private javax.swing.JPanel pnlCardinalitySource;
    private javax.swing.JPanel pnlCardinalityTarget;
    private javax.swing.JPanel pnlCollectionType;
    private javax.swing.JPanel pnlName;
    private javax.swing.JPanel pnlRelation;
    private javax.swing.JPanel pnlSource;
    private javax.swing.JPanel pnlTarget;
    private javax.swing.JTextField txfName;
    // End of variables declaration//GEN-END:variables

}
