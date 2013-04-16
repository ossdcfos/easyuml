/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.dialogs;

import java.util.List;
import javax.swing.JComboBox;
import org.netbeans.api.visual.widget.Widget;
import org.uml.visual.widgets.ClassWidget;
import org.uml.visual.widgets.InterfaceWidget;

/**
 *
 * @author Uros
 */
public class ClassImplementsRelationPanel extends javax.swing.JPanel {

    /**
     * Creates new form ClassImplementsRelationPanel
     */
    ClassWidget widget;
    
    public ClassImplementsRelationPanel(ClassWidget widget) {
        initComponents();
        this.widget=widget;
        fillCombos(widget);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        comboBoxTarget = new javax.swing.JComboBox();

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(ClassImplementsRelationPanel.class, "ClassImplementsRelationPanel.jLabel1.text")); // NOI18N

        comboBoxTarget.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxTargetActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(comboBoxTarget, 0, 141, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(comboBoxTarget, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void comboBoxTargetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxTargetActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboBoxTargetActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox comboBoxTarget;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables

    private void fillCombos(ClassWidget widget) {
        List<Widget> widgets =  widget.getClassDiagramScene().getMainLayer().getChildren();
        for (Widget w : widgets) {
            if (w instanceof InterfaceWidget) {
                comboBoxTarget.addItem(w);
            }
        }
    }

    public JComboBox getComboBoxTarget() {
        return comboBoxTarget;
    }
    
}
