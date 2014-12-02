package org.uml.project.panels;

import javax.swing.JComponent;
import javax.swing.JPanel;
import org.netbeans.spi.project.ui.support.ProjectCustomizer;
import org.netbeans.spi.project.ui.support.ProjectCustomizer.Category;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

/**
 *
 * @author Jelena
 */
public class GeneralUMLProperties 
    implements ProjectCustomizer.CompositeCategoryProvider {

    private static final String GENERAL = "General";

    @ProjectCustomizer.CompositeCategoryProvider.Registration(
            projectType = "org-uml-project", position = 10)
    public static GeneralUMLProperties createGeneral() {
        return new GeneralUMLProperties();
    }

    @NbBundle.Messages("LBL_Config_General=General")
    @Override
    public Category createCategory(Lookup lkp) {
        return ProjectCustomizer.Category.create(
                GENERAL,
                Bundle.LBL_Config_General(),
                null);
    }

    @Override
    public JComponent createComponent(Category category, Lookup lkp) {
        return new JPanel();
    }

}
