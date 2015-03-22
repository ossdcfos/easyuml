package org.uml.project;

import org.uml.project.actions.UMLProjectOpenedHook;
import java.awt.Image;
import java.beans.PropertyChangeListener;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.netbeans.api.annotations.common.StaticResource;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.spi.project.ProjectState;
import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.netbeans.spi.project.ui.support.CommonProjectActions;
import org.netbeans.spi.project.ui.support.NodeFactorySupport;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.*;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;
import org.uml.project.actions.UMLProjectActionProvider;
import org.uml.project.actions.UMLProjectCopyOperation;
import org.uml.project.actions.UMLProjectDeleteOperation;
import org.uml.project.actions.UMLProjectMoveOrRenameOperation;

/**
 *
 * @author Boris Perović
 */
public class UMLProject implements Project {

    private final FileObject projectDir;
    private final ProjectState state;
    private Lookup lkp;

    @StaticResource()
    public static final String UMLPROJECT_ICON = "org/uml/project/UMLProject.png";
    
    public static final String CLASS_DIAGRAMS_FOLDER = "//Class Diagrams//";

    UMLProject(FileObject dir, ProjectState state) {
        this.projectDir = dir;
        this.state = state;
    }

    @Override
    public FileObject getProjectDirectory() {
        return projectDir;
    }

    @Override
    public Lookup getLookup() {
        if (lkp == null) {
            lkp = Lookups.fixed(new Object[]{ // register your features here
                this,
                new Info(),
                new UMLProjectLogicalView(this),
                new UMLProjectActionProvider(this),
                new UMLProjectOpenedHook(this),
                new UMLProjectDeleteOperation(this),
                new UMLProjectCopyOperation(),
                new UMLProjectMoveOrRenameOperation()
            });
        }
        return lkp;
    }

    public void refresh() {
        for (FileObject projectFolder : getProjectDirectory().getChildren()) {
            projectFolder.refresh();
        }
    }

    private final class Info implements ProjectInformation {

        @Override
        public Icon getIcon() {
            return new ImageIcon(ImageUtilities.loadImage(UMLPROJECT_ICON));
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
                Node projectFolderNode = projectFolder.getNodeDelegate();
                //Decorate the project directory's node:
                return new ProjectNode(projectFolderNode, project);
            } catch (DataObjectNotFoundException donfe) {
                Exceptions.printStackTrace(donfe);
                //Fallback-the directory couldn't be created -
                //read-only filesystem or something evil happened
                return new AbstractNode(Children.LEAF);
            }
        }

        private final class ProjectNode extends FilterNode {

            final UMLProject project;

            public ProjectNode(Node node, UMLProject project) throws DataObjectNotFoundException {
                super(node,
                        NodeFactorySupport.createCompositeChildren(project, "Projects/org-uml-project/Nodes"),
                        new ProxyLookup(
                                new Lookup[]{
                                    Lookups.singleton(project),
                                    node.getLookup()
                                }));

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
                    CommonProjectActions.closeProjectAction()
                };
            }

            @Override
            public Image getIcon(int type) {
                return ImageUtilities.loadImage(UMLPROJECT_ICON);
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

        @Override
        public Node findPath(Node root, Object target) {
            //leave unimplemented for now
            return null;
        }
    }
}
