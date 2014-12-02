package org.uml.project;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.spi.project.ui.CustomizerProvider;
import org.netbeans.spi.project.ui.support.ProjectCustomizer;
import org.openide.awt.StatusDisplayer;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Jelena
 */
public class UMLCustomizerProvider implements CustomizerProvider {

    public final UMLProject project;

    public static final String CUSTOMIZER_FOLDER_PATH =
            "Projects/org-uml-project/Customizer";

    public UMLCustomizerProvider(UMLProject project) {
        this.project = project;
    }

    @Override
    public void showCustomizer() {
        Dialog dialog = ProjectCustomizer.createCustomizerDialog(
                //Path to layer folder:
                CUSTOMIZER_FOLDER_PATH,
                //Lookup, which must contain, at least, the Project:
                Lookups.fixed(project),
                //Preselected category:
                "",
                //OK button listener:
                new OKOptionListener(),
                //HelpCtx for Help button of dialog:
                null);
        dialog.setTitle(ProjectUtils.getInformation(project).getDisplayName());
        dialog.setVisible(true);
    }

    private class OKOptionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            StatusDisplayer.getDefault().setStatusText("OK button clicked for "
                    + project.getProjectDirectory().getName() + " customizer!");
        }
    }
    
}
