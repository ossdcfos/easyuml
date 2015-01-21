package org.uml.visual;

//import com.thoughtworks.xstream.XStream;
//import com.thoughtworks.xstream.converters.Converter;
//import com.thoughtworks.xstream.converters.MarshallingContext;
//import com.thoughtworks.xstream.converters.UnmarshallingContext;
//import com.thoughtworks.xstream.io.HierarchicalStreamReader;
//import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
//import com.thoughtworks.xstream.io.xml.StaxDriver;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.*;
import javax.swing.JScrollPane;
import org.dom4j.*;
import org.dom4j.io.*;
import org.netbeans.api.settings.ConvertAsProperties;
import org.netbeans.api.visual.widget.EventProcessingType;
import org.netbeans.spi.actions.AbstractSavable;
import org.openide.*;
import org.openide.NotifyDescriptor.Confirmation;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.filesystems.FileObject;
import org.openide.util.*;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.*;
import org.openide.windows.WindowManager;
import org.uml.explorer.ClassDiagramNode;
import org.uml.model.ClassDiagram;
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
    "CTL_UMLTopComponent=UML Class Diagram Window",
    "HINT_UMLTopComponent=This is a UML Class Diagram window"
})
public final class UMLTopComponent extends TopComponent implements ExplorerManager.Provider {
    
    private ClassDiagramScene classDiagramScene;
    private JScrollPane classDiagramPanel;
    private FileObject fileObject;
    private InstanceContent content = new InstanceContent();
    private ExplorerManager explorerManager = new ExplorerManager();

    // should never be called
    public UMLTopComponent() {
        this(new ClassDiagram());
    }
    
    public UMLTopComponent(ClassDiagram classDiagram) {
        initComponents();
        setName(classDiagram.getName()); // samo se ime razlikuje, Bundle.CTL_UMLTopComponent(), da li je bitno?
        setToolTipText(Bundle.HINT_UMLTopComponent());
        
        classDiagramScene = new ClassDiagramScene(classDiagram, this);
        classDiagramPanel = new JScrollPane();
        classDiagramPanel.setViewportView(classDiagramScene.createView());
        classDiagramScene.setMaximumBounds(new Rectangle(0, 0, 2000, 2000));
        classDiagramScene.setMaximumSize(new Dimension(2000, 2000));
        classDiagramScene.validate();
        classDiagramScene.setCheckClipping(true);
        classDiagramScene.setKeyEventProcessingType(EventProcessingType.FOCUSED_WIDGET_AND_ITS_CHILDREN);
        
        add(classDiagramPanel, BorderLayout.CENTER);
        
        classDiagramScene.validate();
        
        explorerManager.setRootContext(new ClassDiagramNode(classDiagram));
        
        Lookup fixedLookup = Lookups.fixed(
                classDiagramScene, // for saving of diagram
                PaletteSupport.getPalette(), // palette
                new UMLNavigatorLookupHint() // navigator
        );
        
        AbstractLookup abstrLookup = new AbstractLookup(content);
        
        ProxyLookup jointLookup = new ProxyLookup(
                fixedLookup,
                ExplorerUtils.createLookup(explorerManager, getActionMap()),
                classDiagramScene.getLookup(), // node selection in explorer
                abstrLookup
        );
        
        associateLookup(jointLookup);

        //classDiagramScene.getMainLayer().bringToFront();
        // pomereno iz konstruktora class diagram scene
        //GraphLayout graphLayout = GraphLayoutFactory.createOrthogonalGraphLayout(classDiagramScene, true);
        //graphLayout.layoutGraph(classDiagramScene);
    }
    
    @Override
    public void componentActivated() {
        super.componentActivated();
        requestActive();
    }
    
    @Override
    protected void componentDeactivated() {
        super.componentDeactivated(); //To change body of generated methods, choose Tools | Templates.
        classDiagramScene.setFocusedObject(null);
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
        WindowManager.getDefault().findTopComponent("ExplorerTopComponent").open();
        WindowManager.getDefault().findTopComponent("properties").open();
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
    
    public ClassDiagramScene getClassDiagramScene() {
        return classDiagramScene;
    }
    
    public void modify() {
        // in other case, when we are doing reverse engineering, modify is called before the lookup is
        // associated with TopComponent, so there is an exception when we associate it later, because it already exists
        if (fileObject != null) {
            if (getLookup().lookup(UMLTopComponentSavable.class) == null && content != null) {
                content.add(new UMLTopComponentSavable(this, content));
            }
        }
    }
    
    public void setFileObject(FileObject fileObject) {
        this.fileObject = fileObject;
    }
    
    @Override
    public ExplorerManager getExplorerManager() {
        return explorerManager;
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
            return "Diagram " + topComponent.getName(); // get display name somehow
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
                serializer.setClassDiagram(classDiagramScene.getClassDiagram());
                serializer.setClassDiagramScene(classDiagramScene);
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
                    if (fileOut != null) {
                        fileOut.close();
                    }
                    if (writer != null) {
                        writer.close();
                    }
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }
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
}
