package org.uml.project;

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

/**
 *
 * @author Boris
 */
@NodeFactory.Registration(projectType = "org-uml-project", position = 10)
public class UMLProjectNodeFactory implements NodeFactory {

    @Override
    public NodeList<?> createNodes(Project project) {
        UMLProject p = project.getLookup().lookup(UMLProject.class);
        assert p != null;
        return new UMLProjectNodeList(p);
    }

    private class UMLProjectNodeList implements NodeList<Node> {

        UMLProject project;

        public UMLProjectNodeList(UMLProject project) {
            this.project = project;
        }

        @Override
        public List<Node> keys() {
            List<Node> result = new ArrayList<>();

            FileObject umlDiagramProjectFileObject = project.getProjectDirectory();
            DataFolder umlDiagramProjectFolder = DataFolder.findFolder(umlDiagramProjectFileObject.getFileObject("//Class Diagrams//"));
            Node umlDiagNode = umlDiagramProjectFolder.getNodeDelegate();
            result.add(umlDiagNode);

//            if (umlDiagramProjectFileObject != null) {
//                for (FileObject diagramFolder : umlDiagramProjectFileObject.getChildren()) {
//                    if (diagramFolder.isFolder() && diagramFolder.getName().contains("Diagrams")) {
//                        result.add(DataFolder.findFolder(diagramFolder).getNodeDelegate());
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
