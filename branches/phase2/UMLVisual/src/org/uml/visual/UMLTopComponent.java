package org.uml.visual;

import org.uml.visual.navigator.UMLNavigatorLookupHint;
import java.awt.Rectangle;
import java.io.*;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.netbeans.api.settings.ConvertAsProperties;
import org.netbeans.api.visual.widget.EventProcessingType;
import org.netbeans.spi.actions.AbstractSavable;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.NotifyDescriptor.Confirmation;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.filesystems.FileObject;
import org.openide.loaders.SaveAsCapable;
import org.openide.util.*;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.*;
import org.openide.windows.WindowManager;
import org.uml.explorer.ClassDiagramNode;
import org.uml.explorer.ExplorerTopComponent;
import org.uml.model.ClassDiagram;
import org.uml.visual.palette.PaletteSupport;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.xmlSerialization.ClassDiagramSerializer;

@ConvertAsProperties(
        dtd = "-//org.uml.visual//UML//EN",
        autostore = false)
@TopComponent.Description(
        preferredID = "UMLTopComponent",
        iconBase = "org/uml/filetype/cdg/classDiagramIcon.png",
        persistenceType = TopComponent.PERSISTENCE_NEVER)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "org.uml.visual.UMLTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_UMLAction",
        preferredID = "UMLTopComponent")
@Messages({
    "CTL_UMLAction=easyUML Designer",
    "CTL_UMLTopComponent=easyUML Designer",
    "HINT_UMLTopComponent=easyUML Designer"
})
/**
 * UMLTopComponent class represents the TopComponent which holds ClassDiagramScene in its view.
 * Should be refactored so that various diagram types can be shown by UMLTopComponent
 */
public final class UMLTopComponent extends TopComponent implements ExplorerManager.Provider {

    /**
     * Shown scene.
     */
    private final ClassDiagramScene classDiagramScene;
    /**
     * File to which this UMLTopComponent and scene correspond to.
     */
    private FileObject fileObject;
    /**
     * Content, used for adding and removing of Save functionality based on whether
     * the diagram has been modified or not.
     */
    private final InstanceContent content = new InstanceContent();
    /**
     * ExplorerManager which holds background nodes (one for each of the objects in
     * the scene, including scene). When background nodes are selected, corresponding
     * property sheet is shown.
     */
    private final ExplorerManager explorerManager = new ExplorerManager();

    /**
     * Default constructor. Should never be called, except from Window -> UML Designer.
     */
    public UMLTopComponent() {
        this(new ClassDiagram(), "Unsaved UML Class Diagram");
    }

    /**
     * Constructor which specifies classDiagram and the name of UMLTopComponent.
     * Initializes classDiagramScene and background nodes in explorerManager.
     * Adds objects to lookup to support: Palette, Navigator satellite view, Save As...
     * 
     * @param classDiagram which this UMLTopComponent is showing
     * @param name of the UMLTopComponent
     */
    public UMLTopComponent(ClassDiagram classDiagram, String name) {
        initComponents();
        setName(name);
        setToolTipText(Bundle.HINT_UMLTopComponent());
        setFocusable(true);

        // Initialize classDiagramScene
        classDiagramScene = new ClassDiagramScene(classDiagram, this);
        viewPane.setViewportView(classDiagramScene.createView());
        classDiagramScene.setMaximumBounds(new Rectangle(0, 0, Integer.MAX_VALUE, Integer.MAX_VALUE));
        // TODO check clipping?
//        classDiagramScene.setCheckClipping(true);
        classDiagramScene.setKeyEventProcessingType(EventProcessingType.FOCUSED_WIDGET_AND_ITS_CHILDREN);

        // Initialize explorerManager background nodes
        explorerManager.setRootContext(new ClassDiagramNode(classDiagram, classDiagramScene));

        // Initalize lookup with multiple functionalities
        AbstractLookup abstrLookup = new AbstractLookup(content);
        ProxyLookup jointLookup = new ProxyLookup(
                Lookups.fixed(
                        classDiagramScene,              // for scene properties, read from UMLExplorer. TODO maybe remove
                        PaletteSupport.getPalette(),    // Palette support
                        new UMLNavigatorLookupHint(),   // Navigator support
                        new SaveAs(this)                // SaveAs support (always enabled)
                ),
                abstrLookup,                            // Save support (adding UMLTopComponent.Save to content when modified)
                classDiagramScene.getLookup(),          // Exposes the focused object so that Explorer can update the selection
                ExplorerUtils.createLookup(explorerManager, getActionMap()) // Expose background nodes selection for properties
        );
        associateLookup(jointLookup);

        // Moved from ClassDiagramScene constructor
        //GraphLayout graphLayout = GraphLayoutFactory.createOrthogonalGraphLayout(classDiagramScene, true);
        //graphLayout.layoutGraph(classDiagramScene);
    }

    @Override
    public void componentOpened() {
        WindowManager.getDefault().findTopComponent("easyUMLExplorerTopComponent").open();
        WindowManager.getDefault().findTopComponent("properties").open();
    }

    @Override
    public void componentActivated() {
        super.componentActivated();

        TopComponent tc = WindowManager.getDefault().findTopComponent("easyUMLExplorerTopComponent");
        if (tc != null) {
            ExplorerTopComponent explorerTC = (ExplorerTopComponent) tc;
            explorerTC.initializeRoot(classDiagramScene.getClassDiagram());
            explorerTC.setCurrentObjectScene(classDiagramScene);
        }
    }

    @Override
    protected void componentDeactivated() {
        super.componentDeactivated();
        
        if (!isActivatedLinkedTC()) {
            classDiagramScene.deselectAll();
        }
    }

    @Override
    public boolean canClose() {
        Save saveObj = getLookup().lookup(Save.class);
        if (saveObj != null) {
            Confirmation msg = new NotifyDescriptor.Confirmation(
                    "Do you want to save \"" + this.getName() + "\"?",
                    NotifyDescriptor.YES_NO_OPTION,
                    NotifyDescriptor.QUESTION_MESSAGE);
            Object result = DialogDisplayer.getDefault().notify(msg);
            if (NotifyDescriptor.YES_OPTION.equals(result)) {
                saveTopComponent();

                // disable "Save" option
                content.remove(saveObj);
                saveObj.unregisterPublic();
            } else if (NotifyDescriptor.NO_OPTION.equals(result)) {
                // disable "Save" option
                content.remove(saveObj);
                saveObj.unregisterPublic();
            } else {
                // do not close, as the dialog has been closed
                return false;
            }
        }
        return super.canClose();
    }

    @Override
    protected void componentClosed() {
        TopComponent exTC = WindowManager.getDefault().findTopComponent("easyUMLExplorerTopComponent");
        if (exTC != null) {
            ExplorerTopComponent explorerTC = (ExplorerTopComponent) exTC;
            if (explorerTC.isValidDiagramInRoot(classDiagramScene.getClassDiagram())) {
                explorerTC.emptyTree();
            }
        }
        
        super.componentClosed();
    }

    /**
     * Checks if the activated TopComponent is linked, meaning it persists the current selection.
     * @return true if activated TopComponent is linked, false if not
     */
    public boolean isActivatedLinkedTC() {
        TopComponent activatedTC = WindowManager.getDefault().getRegistry().getActivated();
        TopComponent propertiesTC = WindowManager.getDefault().findTopComponent("properties");
        TopComponent explorerTC = WindowManager.getDefault().findTopComponent("easyUMLExplorerTopComponent");
        TopComponent navigatorTC = WindowManager.getDefault().findTopComponent("navigatorTC");
        if (activatedTC == propertiesTC || activatedTC == explorerTC || activatedTC == navigatorTC || activatedTC == this) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return explorerManager;
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

    /**
     * Notifies the TopComponent that it has been modified somehow. Adds Save
     * object to content, to enable save functionality.
     */
    public void notifyModified() {
        // Enabled only after the TopComponent has been fully initialized.
        if (fileObject != null) {
            Save saveObj = getLookup().lookup(Save.class);
            if (saveObj == null) {
                content.add(new Save(this, content));
            }
        }
    }

    /**
     * Sets the file linked to this UMLTopComponent.
     * @param fileObject which persists this ClassDiagramScene
     */
    public void setFileObject(FileObject fileObject) {
        this.fileObject = fileObject;
    }

    /**
     * Serialises the given UMLTopComponent to .cdg XML file given the path.
     *
     * @param path to save (serialise) to
     */
    private void saveTopComponentToPath(String path) {
        FileOutputStream fileOut = null;
        XMLWriter writer = null;
        try {
            fileOut = new FileOutputStream(path);

            ClassDiagramSerializer serializer = ClassDiagramSerializer.getInstance();
            serializer.setClassDiagramScene(classDiagramScene);

            Document document = DocumentHelper.createDocument();
            // document.setXMLEncoding("UTF-8");
            Element root = document.addElement("ClassDiagram");
            serializer.serialize(root);
            OutputFormat format = OutputFormat.createPrettyPrint();
            writer = new XMLWriter(fileOut, format);
            writer.write(document);
            System.out.println("Diagram file saved to " + path);
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

    /**
     * Saves TopComponent to the path given by the set fileObject.
     */
    public void saveTopComponent() {
        saveTopComponentToPath(fileObject.getPath());
    }

    /**
     * Enables SaveAs functionality.
     */
    private class SaveAs implements SaveAsCapable {

        UMLTopComponent umlTopComponent;

        public SaveAs(UMLTopComponent umlTopComponent) {
            this.umlTopComponent = umlTopComponent;
        }

        @Override
        public void saveAs(FileObject folder, String name) throws IOException {
            String path = folder.getPath() + "/" + name;
            umlTopComponent.saveTopComponentToPath(path);
        }
    }

    /**
     * Enables Save functionality.
     */
    public class Save extends AbstractSavable {

        private final UMLTopComponent umlTopComponent;
        private final InstanceContent ic;

        /**
         * Constructor of save object. Registers Save object to SavableRegistry
         * to enable SaveAll functionality.
         * @param topComponent
         * @param instanceContent 
         */
        public Save(UMLTopComponent topComponent, InstanceContent instanceContent) {
            this.umlTopComponent = topComponent;
            this.ic = instanceContent;
            register();
        }

        /**
         * Display name of the UMLTopComponent and file. It is shown during the prompt
         * when closing the IDE without saving the file.
         * @return 
         */
        @Override
        protected String findDisplayName() {
            return "File " + umlTopComponent.getName(); // get display name somehow
        }

        /**
         * Exposes unregister() method so that this Save object can be removed
         * from SavableRegistry if the file has been saved in some other way.
         * A user can save the file on closing of the UMLTopComponent, if it has
         * not been saved.
         */
        public void unregisterPublic() {
            unregister();
        }

        /**
         * Invoked when the user saves the diagram.
         */
        @Override
        protected void handleSave() {
            umlTopComponent.saveTopComponent();

            // Diagram saved -> disable save functionality
            ic.remove(this);
            // unregisters here automatically, since handleSave() is called from AbstractSavable.save()
        }

        @Override
        public boolean equals(Object other) {
            if (other instanceof Save) {
                return ((Save) other).umlTopComponent.equals(umlTopComponent);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return umlTopComponent.hashCode();
        }
    }
}

//                // Xstream serialization
//                XStream xstream = new XStream(new StaxDriver());
//                xstream.autodetectAnnotations(true);
//                xstream.registerConverter(new Converter() {
//                    
//                    @Override
//                    public void marshal(Object o, HierarchicalStreamWriter writer, MarshallingContext mc) {
//                        Point point = (Point) o;
//                        writer.addAttribute("x", Integer.toString(point.x));
//                        writer.addAttribute("y", Integer.toString(point.y));
//                    }
//                    
//                    @Override
//                    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext uc) {
//                        Point point = new Point();
//                        point.x = Integer.parseInt(reader.getAttribute("x"));
//                        point.y = Integer.parseInt(reader.getAttribute("y"));
//                        return point;
//                    }
//                    
//                    @Override
//                    public boolean canConvert(Class type) {
//                        if (type == Point.class) {
//                            return true;
//                        } else {
//                            return false;
//                        }
//                    }
//                });
////                xstream.setMode(XStream.NO_REFERENCES);
//                System.out.println(xstream.toXML(classDiagramScene.getClassDiagram()));
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
