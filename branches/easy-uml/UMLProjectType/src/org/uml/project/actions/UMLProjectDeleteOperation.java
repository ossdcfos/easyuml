package org.uml.project.actions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.netbeans.spi.project.DeleteOperationImplementation;
import org.openide.filesystems.FileObject;
import org.uml.project.UMLProject;

/**
 *
 * @author Boris
 */
public class UMLProjectDeleteOperation implements DeleteOperationImplementation {

        private final UMLProject project;

        public UMLProjectDeleteOperation(UMLProject project) {
            this.project = project;
        }

        @Override
        public List<FileObject> getDataFiles() {
            List<FileObject> files = new ArrayList<>();
            FileObject[] projectChildren = project.getProjectDirectory().getChildren();
            for (FileObject fo : projectChildren) {
                files.add(fo);
                //addFile(project.getProjectDirectory(), fo.getNameExt(), files);
            }
            return files;
        }

        private void addFile(FileObject projectDirectory, String nameExt, List<FileObject> files) {
            FileObject fo = projectDirectory.getFileObject(nameExt);
            if (fo != null) {
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