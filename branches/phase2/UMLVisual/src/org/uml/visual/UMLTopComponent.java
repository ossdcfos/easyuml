package org.uml.visual;

import com.timboudreau.vl.jung.ObjectSceneAdapter;
import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JScrollPane;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.netbeans.api.settings.ConvertAsProperties;
import org.netbeans.api.visual.model.ObjectSceneEvent;
import org.netbeans.api.visual.model.ObjectSceneEventType;
import org.netbeans.api.visual.widget.EventProcessingType;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.spi.actions.AbstractSavable;
import org.netbeans.spi.palette.PaletteController;
import org.openide.*;
import org.openide.NotifyDescriptor.Confirmation;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.explorer.ExplorerManager;
import org.openide.filesystems.FileObject;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;
import org.uml.explorer.ClassDiagramComponentNode;
import org.uml.explorer.ExplorerTopComponent;
import org.uml.model.ClassDiagram;
import org.uml.model.ComponentBase;
import org.uml.visual.palette.PaletteSupport;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.xmlSerialization.ClassDiagramXmlSerializer;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//org.uml.visual//UML//EN",
        autostore = false)
@TopComponent.Description(
        preferredID = "UMLTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_NEVER)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "org.uml.visual.UMLTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_UMLAction",
        preferredID = "UMLTopComponent")
@Messages({
    "CTL_UMLAction=UML Designer",
    "CTL_UMLTopComponent=UML Class Diagramv Window",
    "HINT_UMLTopComponent=This is a UML Class Diagram window"
})
public final class UMLTopComponent extends TopComponent {

    private ClassDiagram classDiagram;
    private ClassDiagramScene classDiagramScene;
    private JScrollPane classDiagramPanel;
    private PaletteController palette;
    private FileObject fileObject;
    private InstanceContent ic;
    private boolean initialized = false;

    public UMLTopComponent() {
        this(new ClassDiagram(), null);
    }

    public UMLTopComponent(ClassDiagram classDiagram, FileObject fo) {
        initComponents();
        setName(classDiagram.getName()); // samo se ime razlikuje, Bundle.CTL_UMLTopComponent(), da li je bitno?
        setToolTipText(Bundle.HINT_UMLTopComponent());

        this.classDiagram = classDiagram;
        classDiagramScene = new ClassDiagramScene(classDiagram, this);
        classDiagramPanel = new JScrollPane();
        classDiagramPanel.setViewportView(classDiagramScene.createView());

        classDiagramScene.setKeyEventProcessingType(EventProcessingType.FOCUSED_WIDGET_AND_ITS_CHILDREN);

        add(classDiagramPanel, BorderLayout.CENTER);
        //Instead of adding the satellite view to the TopComponent,
        //use the Navigator panel:
//        add(classDiagramScene.createSatelliteView(), BorderLayout.WEST);

        palette = PaletteSupport.getPalette();

        classDiagramScene.validate();

        ic = new InstanceContent();

        Lookup fixedLookup = Lookups.fixed(
                classDiagramScene,
                classDiagramScene.getLookup(),
                classDiagram,
                palette,
                new UMLNavigatorLookupHint()
        );

        AbstractLookup abstrLookup = new AbstractLookup(ic);

        ProxyLookup jointLookup = new ProxyLookup(fixedLookup, abstrLookup);

        associateLookup(jointLookup);

        fileObject = fo;

        classDiagramScene.addObjectSceneListener(new ObjectSceneAdapter() {
            @Override
            public void objectAdded(ObjectSceneEvent event, Object addedObject) {
                modify();
            }

            @Override
            public void objectRemoved(ObjectSceneEvent event, Object removedObject) {
                modify();
            }
        }, ObjectSceneEventType.OBJECT_ADDED, ObjectSceneEventType.OBJECT_REMOVED);

        initialized = true;
        //classDiagramScene.getMainLayer().bringToFront();
        // pomereno iz konstruktora class diagram scene
        //GraphLayout graphLayout = GraphLayoutFactory.createOrthogonalGraphLayout(classDiagramScene, true);
        //graphLayout.layoutGraph(classDiagramScene);
        ExplorerManager em = ExplorerTopComponent.getStaticExplorerManager();
        em.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("selectedNodes")) {
//                    for (Object o : classDiagramScene.getObjects()) {
//                        if (o instanceof ClassDiagramComponent) {
//                            ClassDiagramComponent component = (ClassDiagramComponent) o;
////                            if (classDiagramScene.isNode(component)) {
//                            Widget w = classDiagramScene.findWidget(component);
//                            w.setState(w.getState().deriveWidgetFocused(false));
////                            }
//                        }
//                    }
                    Node[] oldNodes = (Node[]) evt.getOldValue();
                    Node[] nodes = (Node[]) evt.getNewValue();
                    for (Node n : oldNodes) {
                        if (n instanceof ClassDiagramComponentNode) {
                            ClassDiagramComponentNode cdcn = (ClassDiagramComponentNode) n;
                            ComponentBase component = cdcn.getComponent();
                            if (classDiagramScene.isNode(component)) {
                                Widget w = classDiagramScene.findWidget(component);
                                w.setState(w.getState().deriveWidgetFocused(false));
                            }
                        }
                    }
                    for (Node n : nodes) {
                        if (n instanceof ClassDiagramComponentNode) {
                            ClassDiagramComponentNode cdcn = (ClassDiagramComponentNode) n;
                            ComponentBase component = cdcn.getComponent();
                            if (classDiagramScene.isNode(component)) {
                                Widget w = classDiagramScene.findWidget(component);
                                w.setState(w.getState().deriveWidgetFocused(true));
                            }
                        }
                    }
                    classDiagramScene.repaint();
                    classDiagramScene.validate();
                }
            }
        });
    }

    @Override
    public void componentActivated() {
        super.componentActivated();
        classDiagramScene.getView().requestFocusInWindow();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        viewPane = new javax.swing.JScrollPane();

        setLayout(new java.awt.BorderLayout());
        add(viewPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane viewPane;
    // End of variables declaration//GEN-END:variables

    @Override
    public void componentOpened() {
    }

    @Override
    public void componentClosed() {
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

//    Project selectedProject;
//
//    @Override
//    public void resultChanged(LookupEvent le) {
//        Lookup.Result localResult = (Result)le.getSource();
//        Collection<Object> coll = localResult.allInstances();
//        if (!coll.isEmpty()){
//            for (Object selectedItem : coll){
//                if (selectedItem instanceof Project) selectedProject = (Project) selectedItem;
//            }
//        }
//        
//         FileObject folder = selectedProject.getProjectDirectory();
//         String path = folder.getPath();
//    }
    public ClassDiagramScene getScene() {
        return classDiagramScene;
    }

    public void modify() {
        // in other case, when we are doing reverse engineering, modify is called before the lookup is
        // associated with TopComponent, so there is an exception when we associate it later, because it already exists
        if (initialized) {
            if (getLookup().lookup(UMLTopComponentSavable.class) == null && ic != null) {
                ic.add(new UMLTopComponentSavable(this, ic));
            }
        }
    }

    class UMLTopComponentSavable extends AbstractSavable {

        private final UMLTopComponent topComponent;
        private final InstanceContent ic;

        public UMLTopComponentSavable(UMLTopComponent topComponent, InstanceContent instanceContent) {
            this.topComponent = topComponent;
            this.ic = instanceContent;
            register();
        }

        @Override
        protected String findDisplayName() {
            return "My name is " + topComponent.getName(); // get display name somehow
        }

        @Override
        protected void handleSave() throws IOException {
            Confirmation msg = new NotifyDescriptor.Confirmation(
                    "Do you want to save \"" + fileObject.getNameExt() + "\"?",
                    NotifyDescriptor.OK_CANCEL_OPTION,
                    NotifyDescriptor.QUESTION_MESSAGE);
            Object result = DialogDisplayer.getDefault().notify(msg);
            //When user clicks "Yes", indicating they really want to save,
            //we need to disable the Save button and Save menu item,
            //so that it will only be usable when the next change is made
            // save 'obj' somehow
            if (NotifyDescriptor.OK_OPTION.equals(result)) {
                //Implement your save functionality here.
                doSave();

                ic.remove(this);
                unregister();
            }
        }

        @Override
        public boolean equals(Object other) {
            if (other instanceof UMLTopComponentSavable) {
                return ((UMLTopComponentSavable) other).topComponent.equals(topComponent);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return topComponent.hashCode();
        }

        public void doSave() {
            FileOutputStream fileOut = null;
            XMLWriter writer = null;
            try {
                FileObject fo = fileObject;
                String putanja = fo.getPath();
                fileOut = new FileOutputStream(putanja);

                ClassDiagramXmlSerializer serializer = ClassDiagramXmlSerializer.getInstance();
                serializer.setClassDiagram(classDiagram);
                serializer.setClassDiagramScene(classDiagramScene);
                Document document = DocumentHelper.createDocument();
                // document.setXMLEncoding("UTF-8");
                Element root = document.addElement("ClassDiagram");
                serializer.serialize(root);
                OutputFormat format = OutputFormat.createPrettyPrint();
                writer = new XMLWriter(fileOut, format);
                writer.write(document);
            } catch (Exception ex) {
                Exceptions.printStackTrace(ex);
            } finally {
                try {
                    if (fileOut != null) fileOut.close();
                    if (writer != null) writer.close();
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }
    }
}
