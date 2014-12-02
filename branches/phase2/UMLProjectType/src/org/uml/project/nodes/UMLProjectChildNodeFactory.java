package org.uml.project.nodes;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeListener;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ui.support.NodeFactory;
import org.netbeans.spi.project.ui.support.NodeList;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataFolder;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.uml.project.UMLProject;

/**
 *
 * @author Jelena
 */
@NodeFactory.Registration(projectType = "org-uml-project", position = 10)
public class UMLProjectChildNodeFactory implements NodeFactory {

    @Override
    public NodeList<?> createNodes(Project project) {
        UMLProject p = project.getLookup().lookup(UMLProject.class);
        assert p != null;
        return new UMLProjectChildNodeList(p);
    }

    private class UMLProjectChildNodeList implements NodeList<Node> {

        UMLProject project;

        public UMLProjectChildNodeList(UMLProject project) {
            this.project = project;
        }

        @Override
        public List<Node> keys() {
            FileObject classDiagFileObject = 
                project.getProjectDirectory().getFileObject(UMLProject.CLASS_DIAGRAMS_DIR);
            List<Node> result = new ArrayList<>();
            
            DataFolder classDiagFolder = DataFolder.findFolder(classDiagFileObject);
            Node classDiagNode = classDiagFolder.getNodeDelegate();            
            
            result.add(classDiagNode);
//            if (classDiagFolder != null) {
//                for (FileObject textsFolderFile : classDiagFolder.getChildren()) {
//                    try {
//                        result.add(DataObject.find(textsFolderFile).getNodeDelegate());
//                    } catch (DataObjectNotFoundException ex) {
//                        Exceptions.printStackTrace(ex);
//                    }
//                }
//            }
            return result;
        }

        @Override
        public Node node(Node node) {
            return new FilterNode(node);
        }

        @Override
        public void addNotify() {
        }

        @Override
        public void removeNotify() {
        }

        @Override
        public void addChangeListener(ChangeListener cl) {
        }

        @Override
        public void removeChangeListener(ChangeListener cl) {
        }
    }
    
}
