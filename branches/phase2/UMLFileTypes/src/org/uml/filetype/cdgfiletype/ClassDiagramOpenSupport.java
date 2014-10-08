package org.uml.filetype.cdgfiletype;

import org.openide.cookies.CloseCookie;
import org.openide.cookies.OpenCookie;
import org.openide.loaders.OpenSupport;
import org.openide.windows.CloneableTopComponent;
import org.openide.windows.TopComponent;
import org.uml.classDiagramManager.ClassDiagramManager;
import org.uml.model.ClassDiagram;
import org.uml.visual.UMLTopComponent;
import org.uml.visual.widgets.ClassDiagramScene;

/**
 *
 * @author Jelena
 */
public class ClassDiagramOpenSupport extends OpenSupport implements OpenCookie, CloseCookie {

    CDGDataObject classDiagramDataObject;
    TopComponent topComponent;
    
    ClassDiagramOpenSupport(CDGDataObject.Entry entry) {
        super(entry);
        classDiagramDataObject = (CDGDataObject) entry.getDataObject();
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
        if  (topComponent !=null) {
            if (topComponent.isOpened()) {
                topComponent.requestActive();
            } else {
                topComponent.open();
                topComponent.requestActive();
             }
        } else {
               ClassDiagram cdiag = classDiagramDataObject.getLookup().lookup(ClassDiagram.class);

               topComponent = new UMLTopComponent(cdiag);
               topComponent.open();
               topComponent.requestActive();
        }
        ClassDiagramScene scene = ((UMLTopComponent)topComponent).getScene();
        ClassDiagramManager.getDefault().addScene(scene);
        
        // dependency na Visual Library API
//        scene.getView().requestFocusInWindow();
//        Widget focused = scene.getFocusedWidget();
    }
}
