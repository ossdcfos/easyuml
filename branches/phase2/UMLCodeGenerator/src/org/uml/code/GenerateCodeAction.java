package org.uml.code;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.netbeans.api.project.Project;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.uml.model.ClassDiagram;

@ActionID(
    category = "Source",
id = "org.uml.code.GenerateCodeAction")
@ActionReference(path = "Menu/Source", position = 200)
@ActionRegistration(
    displayName = "#CTL_GenerateCodeAction")
@Messages("CTL_GenerateCodeAction=easyUML Generate Code")
public final class GenerateCodeAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        
      ClassDiagram classDiagram =  Utilities.actionsGlobalContext().lookup(ClassDiagram.class);
      Project project = Utilities.actionsGlobalContext().lookup(Project.class);
      
      FileWriter.getInstance().setProject(project);
      ClassDiagramCodeGenerator.getInstance().generateCode();
      
//      FileWriter.writeFiles();
      
      
   //     String code = ClassDiagramCodeGenerator.getInstance().generateCode();
  //      CodeTopComponent ctc = new CodeTopComponent();
 //       ctc.open();
   //     ctc.requestActive();        
  //      ctc.setCode(code);
    }
}
