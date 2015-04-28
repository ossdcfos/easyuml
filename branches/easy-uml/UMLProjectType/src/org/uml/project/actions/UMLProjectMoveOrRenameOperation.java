/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.project.actions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.MoveOrRenameOperationImplementation;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Boris
 */
public final class UMLProjectMoveOrRenameOperation implements MoveOrRenameOperationImplementation {

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
