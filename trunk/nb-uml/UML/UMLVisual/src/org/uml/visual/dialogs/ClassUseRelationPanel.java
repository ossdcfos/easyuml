/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.dialogs;

import java.util.List;
import javax.swing.JComboBox;
import org.netbeans.api.visual.widget.Widget;
import org.uml.model.CardinalityEnum;
import org.uml.visual.widgets.ClassWidget;
import org.uml.visual.widgets.EnumWidget;

/**
 *
 * @author Uros
 */
public class ClassUseRelationPanel extends javax.swing.JPanel {

    /**
     * Creates new form ClassUseRelationPanel
     */
    ClassWidget classWidget;
    
    public ClassUseRelationPanel(ClassWidget widget) {
        initComponents();
        this.classWidget=widget;
        fillCombos();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        textFieldName = new javax.swing.JTextField();
        labelCardinalitySource = new javax.swing.JLabel();
        comboBoxCardinalitySource = new javax.swing.JComboBox();
        labelCardinalityTarget = new javax.swing.JLabel();
        comboBoxCardinalityTarget = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        comboBoxTarget = new javax.swing.JComboBox();

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(ClassUseRelationPanel.class, "ClassUseRelationPanel.jLabel2.text")); // NOI18N

        textFieldName.setText(org.openide.util.NbBundle.getMessage(ClassUseRelationPanel.class, "ClassUseRelationPanel.textFieldName.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(labelCardinalitySource, org.openide.util.NbBundle.getMessage(ClassUseRelationPanel.class, "ClassUseRelationPanel.labelCardinalitySource.text")); // NOI18N

        comboBoxCardinalitySource.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxCardinalitySourceActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(labelCardinalityTarget, org.openide.util.NbBundle.getMessage(ClassUseRelationPanel.class, "ClassUseRelationPanel.labelCardinalityTarget.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(ClassUseRelationPanel.class, "ClassUseRelationPanel.jLabel1.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labelCardinalityTarget, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboBoxCardinalityTarget, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labelCardinalitySource, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboBoxCardinalitySource, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(textFieldName, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(comboBoxTarget, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(textFieldName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(comboBoxTarget, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelCardinalitySource)
                    .addComponent(comboBoxCardinalitySource, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelCardinalityTarget)
                    .addComponent(comboBoxCardinalityTarget, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void comboBoxCardinalitySourceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxCardinalitySourceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboBoxCardinalitySourceActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox comboBoxCardinalitySource;
    private javax.swing.JComboBox comboBoxCardinalityTarget;
    private javax.swing.JComboBox comboBoxTarget;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel labelCardinalitySource;
    private javax.swing.JLabel labelCardinalityTarget;
    private javax.swing.JTextField textFieldName;
    // End of variables declaration//GEN-END:variables

    private void fillCombos() {
               
        List<Widget> widgets =  classWidget.getClassDiagramScene().getMainLayer().getChildren();
        for (Widget w : widgets) {
            if ((w instanceof ClassWidget||w instanceof EnumWidget)&&!w.equals(classWidget)) {
                comboBoxTarget.addItem(w);
            }
        }
        
        comboBoxCardinalitySource.addItem(CardinalityEnum.Zero2One);
        comboBoxCardinalitySource.addItem(CardinalityEnum.One2One);
        comboBoxCardinalitySource.addItem(CardinalityEnum.One2Many);
        comboBoxCardinalitySource.addItem(CardinalityEnum.Zero2Many);
        
        comboBoxCardinalityTarget.addItem(CardinalityEnum.Zero2One);
        comboBoxCardinalityTarget.addItem(CardinalityEnum.One2One);
        comboBoxCardinalityTarget.addItem(CardinalityEnum.One2Many);
        comboBoxCardinalityTarget.addItem(CardinalityEnum.Zero2Many);
    }
    
    public JComboBox getComboBoxCardinalitySource () {
        return comboBoxCardinalitySource;
    }
    
    public JComboBox getComboBoxCardinalityTarget () {
        return comboBoxCardinalityTarget;
    }

    public JComboBox getComboBoxTarget() {
        return comboBoxTarget;
    }
    public String getName () {
        return textFieldName.getText();
    }
}