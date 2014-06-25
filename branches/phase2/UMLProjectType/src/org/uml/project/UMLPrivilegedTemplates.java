package org.uml.project;

import org.netbeans.spi.project.ProjectServiceProvider;
import org.netbeans.spi.project.ui.PrivilegedTemplates;

/**
 *
 * @author Geertjan
 */

@ProjectServiceProvider(
        service = PrivilegedTemplates.class,
        projectType = "org-uml-project")
public class UMLPrivilegedTemplates implements PrivilegedTemplates {

    private static final String[] PRIVILEGED_NAMES = new String[]{
        "Templates/UML/org-uml-filetype-ClassDiagramWizardIterator"};

    @Override
    public String[] getPrivilegedTemplates() {
        return PRIVILEGED_NAMES;
    }
    
}
