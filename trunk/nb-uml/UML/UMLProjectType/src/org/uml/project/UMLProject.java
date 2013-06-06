/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.project;

import org.uml.project.UMLCustomizerProvider;
import java.awt.Image;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import org.netbeans.api.annotations.common.StaticResource;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.spi.project.ProjectState;
import org.netbeans.spi.project.ui.CustomizerProvider;
import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.netbeans.spi.project.ui.support.CommonProjectActions;
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

    public static final String Class_Diagrams_DIR = "ClassDiagrams";

    UMLProject(FileObject dir, ProjectState state) {
        this.projectDir = dir;
        this.state = state;
        createProjectFolders();
    }

    @Override
    public FileObject getProjectDirectory() {
        return projectDir;
    }
    
    public final void createProjectFolders() {        
        FileObject umlDiagramsFolder = null;

        if (projectDir.getFileObject(Class_Diagrams_DIR) == null) {
            try {
                umlDiagramsFolder = projectDir.createFolder(Class_Diagrams_DIR);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        } else {
            umlDiagramsFolder = projectDir.getFileObject(Class_Diagrams_DIR);
        }
    }

    @Override
    public Lookup getLookup() {
        if (lkp == null) {
            lkp = Lookups.fixed(new Object[]{
                        this,
                        new Info(),
                        new UMLProjectLogicalView(this),
                        new UMLCustomizerProvider(this)
                    });
        }
        return lkp;
    }

    private final class Info implements ProjectInformation {

        @StaticResource()
        public static final String CUSTOMER_ICON = "org/uml/projecttype/icon.png";

        @Override
        public Icon getIcon() {
            return new ImageIcon(ImageUtilities.loadImage(CUSTOMER_ICON));
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
        public static final String CLASS_DIAGRAM_ICON = "org/uml/projecttype/icon.png";
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
                super(node,
                        NodeFactorySupport.createCompositeChildren(
                        project,
                        "Projects/org-uml-project/Nodes"),
                        // new FilterNode.Children(node),
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
                            CommonProjectActions.copyProjectAction(),
                            CommonProjectActions.deleteProjectAction(),
                            CommonProjectActions.customizeProjectAction(),
                            CommonProjectActions.closeProjectAction()
                        };
            }

            @Override
            public Image getIcon(int type) {
                return ImageUtilities.loadImage(CLASS_DIAGRAM_ICON);
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
}
