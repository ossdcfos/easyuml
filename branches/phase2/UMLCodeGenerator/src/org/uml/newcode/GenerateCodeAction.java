/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.newcode;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.uml.model.ClassDiagram;

//@ActionID(
//        category = "Source",
//        id = "org.uml.newcode.GenerateCodeAction"
//)
//@ActionRegistration(
//        displayName = "#CTL_GenerateCodeAction"
//)
//@ActionReference(path = "Menu/Source", position = -50)
//@Messages("CTL_GenerateCodeAction=easyUML NEW Generate Code")
public final class GenerateCodeAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent ev) {
        ClassDiagram classDiagram = Utilities.actionsGlobalContext().lookup(ClassDiagram.class);
        ClassDiagramCodeGenerator.generateOrUpdateCode(classDiagram);
    }
}
