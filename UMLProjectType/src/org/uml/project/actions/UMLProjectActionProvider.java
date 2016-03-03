package org.uml.project.actions;

import org.netbeans.spi.project.ActionProvider;
import org.netbeans.spi.project.ui.support.DefaultProjectOperations;
import org.openide.util.Lookup;
import org.uml.project.UMLProject;

/**
 *
 * @author Boris
 */
public class UMLProjectActionProvider implements ActionProvider {

    UMLProject project;

    public UMLProjectActionProvider(UMLProject project) {
        this.project = project;
    }
    
    @Override
    public String[] getSupportedActions() {
        return new String[]{
            ActionProvider.COMMAND_RENAME,
            ActionProvider.COMMAND_MOVE,
            ActionProvider.COMMAND_COPY,
            ActionProvider.COMMAND_DELETE
        };
    }

    @Override
    public void invokeAction(String command, Lookup context) throws IllegalArgumentException {
        if (command.equalsIgnoreCase(ActionProvider.COMMAND_RENAME)) {
            DefaultProjectOperations.performDefaultRenameOperation(project, "");
        }
        if (command.equalsIgnoreCase(ActionProvider.COMMAND_MOVE)) {
            DefaultProjectOperations.performDefaultMoveOperation(project);
        }
        if (command.equalsIgnoreCase(ActionProvider.COMMAND_COPY)) {
            DefaultProjectOperations.performDefaultCopyOperation(project);
        }
        if (command.equalsIgnoreCase(ActionProvider.COMMAND_DELETE)) {
            DefaultProjectOperations.performDefaultDeleteOperation(project);
        }
    }

    @Override
    public boolean isActionEnabled(String command, Lookup context) throws IllegalArgumentException {
        if ((command.equals(ActionProvider.COMMAND_RENAME))) {
            return true;
        } else if ((command.equals(ActionProvider.COMMAND_MOVE))) {
            return true;
        } else if ((command.equals(ActionProvider.COMMAND_COPY))) {
            return true;
        } else if ((command.equals(ActionProvider.COMMAND_DELETE))) {
            return true;
        }
        return false;
    }
}
