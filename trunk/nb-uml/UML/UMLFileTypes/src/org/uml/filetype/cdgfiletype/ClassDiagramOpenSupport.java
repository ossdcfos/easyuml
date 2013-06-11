/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.filetype.cdgfiletype;

import java.io.IOException;
import org.openide.cookies.CloseCookie;
import org.openide.cookies.OpenCookie;
import org.openide.cookies.SaveCookie;
import org.openide.loaders.MultiDataObject.Entry;
import org.openide.loaders.OpenSupport;
import org.openide.windows.CloneableTopComponent;
import org.openide.windows.WindowManager;
import org.uml.model.ClassDiagram;

/**
 *
 * @author Jelena
 */
public class ClassDiagramOpenSupport extends OpenSupport implements OpenCookie, CloseCookie, SaveCookie {

    CDGDataObject cdDataObject;
    
    ClassDiagramOpenSupport(CDGDataObject.Entry entry) {
        super(entry);
        cdDataObject = (CDGDataObject) entry.getDataObject();
    }
    
    @Override
    protected CloneableTopComponent createCloneableTopComponent() {
//        dobj = (NeuralNetworkDataObject) entry.getDataObject();
//        VisualTopComponent tc = new VisualTopComponent();
// //       tc.open();
//        return tc;
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void open() {
        
        ClassDiagram cd = cdDataObject.getClassDiagram();

        //ViewManager.getInstance().openNeuralNetworkWindow(cd);
        WindowManager.getDefault().findTopComponent("ExplorerTopComponent").open();
        
    }

    public void save() throws IOException {
        SaveCookieAction saveAction = new SaveCookieAction(this);
        saveAction.save(cdDataObject);
    }
}
