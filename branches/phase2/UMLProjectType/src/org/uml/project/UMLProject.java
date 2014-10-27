/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.project;

import java.awt.Image;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.netbeans.api.annotations.common.StaticResource;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.spi.project.ActionProvider;
import org.netbeans.spi.project.CopyOperationImplementation;
import org.netbeans.spi.project.DeleteOperationImplementation;
import org.netbeans.spi.project.MoveOrRenameOperationImplementation;
import org.netbeans.spi.project.ProjectState;
import org.netbeans.spi.project.support.LookupProviderSupport;
import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.netbeans.spi.project.ui.support.CommonProjectActions;
import org.netbeans.spi.project.ui.support.DefaultProjectOperations;
import org.netbeans.spi.project.ui.support.NodeFactorySupport;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.FilterNode;
import org.openide.nodes.FilterNode.Children;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author Jelena
 */
public class UMLProject implements Project {

    private final FileObject projectDir;
    private final ProjectState state;
    private Lookup lkp;

    public static final String CLASS_DIAGRAMS_DIR = "ClassDiagrams";

    UMLProject(FileObject dir, ProjectState state) {
        this.projectDir = dir;
        this.state = state;

        createProjectFolders();
    }

    @Override
    public FileObject getProjectDirectory() {
        return projectDir;
    }

    @Override
    public Lookup getLookup() {
        if (lkp == null) {
            lkp = Lookups.fixed(new Object[]{
                this,
                new Info(),
                new UMLProjectLogicalView(this), //new UMLCustomizerProvider(this)
                new UMLProjectActionProvider(),
                new UMLProjectCopyOperation(),
                new UMLProjectMoveOrRenameOperation(),
                new UMLProjectDeleteOperation(this)
            });
        }
        return LookupProviderSupport.createCompositeLookup(
                lkp,
                "Projects/org-uml-project/Lookup"
        );
    }

    public final void createProjectFolders() {
        FileObject umlDiagramsFolder = null;

        if (projectDir.getFileObject(CLASS_DIAGRAMS_DIR) == null) {
            try {
                umlDiagramsFolder = projectDir.createFolder(CLASS_DIAGRAMS_DIR);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        } else {
            umlDiagramsFolder = projectDir.getFileObject(CLASS_DIAGRAMS_DIR);
        }
    }

    public void addNotify() {
        refresh();
    }

    private void refresh() {
        for (FileObject projectFolder : getProjectDirectory().getChildren()) {
            projectFolder.refresh();
        }
    }

    private final class Info implements ProjectInformation {

        @StaticResource()
        public static final String UML_PROJECT_ICON = "org/uml/project/UMLProject.png";

        @Override
        public Icon getIcon() {
            return new ImageIcon(ImageUtilities.loadImage(UML_PROJECT_ICON));
        }

        @Override
        public String getName() {
            return getProjectDirectory().getName();
        }

        @Override
        public String getDisplayName() {
            return getName();
        }

        @Override
        public void addPropertyChangeListener(PropertyChangeListener pcl) {
            //do nothing, won't change
        }

        @Override
        public void removePropertyChangeListener(PropertyChangeListener pcl) {
            //do nothing, won't change
        }

        @Override
        public Project getProject() {
            return UMLProject.this;
        }
    }

    class UMLProjectLogicalView implements LogicalViewProvider {

        @StaticResource()
        public static final String UML_PROJECT_ICON = "org/uml/project/UMLProject.png";

        private final UMLProject project;

        public UMLProjectLogicalView(UMLProject project) {
            this.project = project;
        }

        @Override
        public Node createLogicalView() {
            try {
                //Obtain the project directory's node:
                FileObject projectDirectory = project.getProjectDirectory();
                DataFolder projectFolder = DataFolder.findFolder(projectDirectory);
                Node nodeOfProjectFolder = projectFolder.getNodeDelegate();
                //Decorate the project directory's node:
                return new ProjectNode(nodeOfProjectFolder, project);
            } catch (DataObjectNotFoundException donfe) {
                Exceptions.printStackTrace(donfe);
                //Fallback-the directory couldn't be created -
                //read-only filesystem or something evil happened
                return new AbstractNode(Children.LEAF);
            }
        }

        @Override
        public Node findPath(Node root, Object target) {
            // TODO: implement this!!!
            // throw new UnsupportedOperationException("Not supported yet.");
            return Node.EMPTY;
        }

        private final class ProjectNode extends FilterNode {

            final UMLProject project;

            public ProjectNode(Node node, UMLProject project)
                    throws DataObjectNotFoundException {
                super(node, NodeFactorySupport.createCompositeChildren(project, "Projects/org-uml-project/Nodes"),
                        // new FilterNode.Children(node),
                        new ProxyLookup(new Lookup[]{Lookups.singleton(project), node.getLookup()}));
                this.project = project;
            }

            @Override
            public Action[] getActions(boolean arg0) {
                return new Action[]{
                    CommonProjectActions.newFileAction(),
                    null,
                    CommonProjectActions.copyProjectAction(),
                    CommonProjectActions.moveProjectAction(),
                    null,
                    CommonProjectActions.renameProjectAction(),
                    CommonProjectActions.deleteProjectAction(),
                    null,
                    //CommonProjectActions.customizeProjectAction(),
                    CommonProjectActions.closeProjectAction()
                };
            }

            @Override
            public Image getIcon(int type) {
                return ImageUtilities.loadImage(UML_PROJECT_ICON);
            }

            @Override
            public Image getOpenedIcon(int type) {
                return getIcon(type);
            }

            @Override
            public String getDisplayName() {
                return project.getProjectDirectory().getName();
            }
        }
    }

    class UMLProjectActionProvider implements ActionProvider {

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
                DefaultProjectOperations.performDefaultRenameOperation(UMLProject.this, "");
            }
            if (command.equalsIgnoreCase(ActionProvider.COMMAND_MOVE)) {
                DefaultProjectOperations.performDefaultMoveOperation(UMLProject.this);
            }
            if (command.equalsIgnoreCase(ActionProvider.COMMAND_COPY)) {
                DefaultProjectOperations.performDefaultCopyOperation(UMLProject.this);
            }
            if (command.equalsIgnoreCase(ActionProvider.COMMAND_DELETE)) {
                DefaultProjectOperations.performDefaultDeleteOperation(UMLProject.this);
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

    private final class UMLProjectMoveOrRenameOperation implements MoveOrRenameOperationImplementation {

        @Override
        public List<FileObject> getMetadataFiles() {
            return new ArrayList<>();
        }

        @Override
        public List<FileObject> getDataFiles() {
            return new ArrayList<>();
        }

        @Override
        public void notifyRenaming() throws IOException {
        }

        @Override
        public void notifyRenamed(String nueName) throws IOException {
        }

        @Override
        public void notifyMoving() throws IOException {
        }

        @Override
        public void notifyMoved(Project original, File originalPath, String nueName) throws IOException {
        }
    }

    private final class UMLProjectCopyOperation implements CopyOperationImplementation {

        @Override
        public List<FileObject> getMetadataFiles() {
            return new ArrayList<>();
        }

        @Override
        public List<FileObject> getDataFiles() {
            return new ArrayList<>();
        }

        @Override
        public void notifyCopying() throws IOException {
        }

        @Override
        public void notifyCopied(Project prjct, File file, String string) throws IOException {
        }
    }

    private final class UMLProjectDeleteOperation implements DeleteOperationImplementation {
        
        private final UMLProject project;
        
        private UMLProjectDeleteOperation(UMLProject project){
            this.project = project;
        }

        @Override
        public List<FileObject> getDataFiles() {
            List<FileObject> files = new ArrayList<>();
            FileObject[] projectChildren = project.getProjectDirectory().getChildren();
            for(FileObject fo : projectChildren){
                files.add(fo);
                //addFile(project.getProjectDirectory(), fo.getNameExt(), files);
            }
            return files;
        }

        private void addFile(FileObject projectDirectory, String nameExt, List<FileObject> files) {
            FileObject fo = projectDirectory.getFileObject(nameExt);
            if(fo != null){
                files.add(fo);
            }
        }

        @Override
        public List<FileObject> getMetadataFiles() {
            return new ArrayList<>();
        }

        @Override
        public void notifyDeleting() throws IOException {
        }

        @Override
        public void notifyDeleted() throws IOException {
        }
    }

}
