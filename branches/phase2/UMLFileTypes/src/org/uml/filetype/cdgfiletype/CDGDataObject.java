package org.uml.filetype.cdgfiletype;

import java.io.IOException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.cookies.SaveCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataNode;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;
import org.openide.nodes.Children;
import org.openide.nodes.CookieSet;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.uml.model.ClassDiagram;
import org.uml.xmlDeserialization.ClassDiagramDeserializer;


// @MIMEResolver.Registration(
//   displayName="#CLASS_DIAGRAM_FILE",
//   resource="ClassDiagramResolver.xml"
// )
//@Messages({
//    "LBL_CDG_LOADER=Files of CDG",
//    "CLASS_DIAGRAM_FILE=Class Diagram File"
//})
//@MIMEResolver.ExtensionRegistration(
//    displayName = "#LBL_CDG_LOADER",
//mimeType = "text/x-cdg",
//extension = {"cdg", "CDG"})
//@DataObject.Registration(
//    mimeType = "text/x-cdg",
//iconBase = "org/uml/filetype/cdgfiletype/classdiagramicon.png",
//displayName = "#LBL_CDG_LOADER",
//position = 300)
@ActionReferences({
    @ActionReference(
        path = "Loaders/text/x-cdg/Actions",
    id =
    @ActionID(category = "System", id = "org.openide.actions.OpenAction"),
    position = 100,
    separatorAfter = 200),
    @ActionReference(
        path = "Loaders/text/x-cdg/Actions",
    id =
    @ActionID(category = "Edit", id = "org.openide.actions.CutAction"),
    position = 300),
    @ActionReference(
        path = "Loaders/text/x-cdg/Actions",
    id =
    @ActionID(category = "Edit", id = "org.openide.actions.CopyAction"),
    position = 400,
    separatorAfter = 500),
    @ActionReference(
        path = "Loaders/text/x-cdg/Actions",
    id =
    @ActionID(category = "Edit", id = "org.openide.actions.DeleteAction"),
    position = 600),
    @ActionReference(
        path = "Loaders/text/x-cdg/Actions",
    id =
    @ActionID(category = "System", id = "org.openide.actions.RenameAction"),
    position = 700,
    separatorAfter = 800),
    @ActionReference(
        path = "Loaders/text/x-cdg/Actions",
    id =
    @ActionID(category = "System", id = "org.openide.actions.SaveAsTemplateAction"),
    position = 900,
    separatorAfter = 1000),
    @ActionReference(
        path = "Loaders/text/x-cdg/Actions",
    id =
    @ActionID(category = "System", id = "org.openide.actions.FileSystemAction"),
    position = 1100,
    separatorAfter = 1200),
    @ActionReference(
        path = "Loaders/text/x-cdg/Actions",
    id =
    @ActionID(category = "System", id = "org.openide.actions.ToolsAction"),
    position = 1300),
    @ActionReference(
        path = "Loaders/text/x-cdg/Actions",
    id =
    @ActionID(category = "System", id = "org.openide.actions.PropertiesAction"),
    position = 1400)
})
public class CDGDataObject extends MultiDataObject {

    ClassDiagramOpenSupport openAction;
    ClassDiagram classDiagram;
    FileObject fileObject;
    SaveCookie saveCookie;
    CookieSet cookies;
    
    public CDGDataObject(FileObject fo, MultiFileLoader loader) throws DataObjectExistsException, IOException {
        super(fo, loader);
//        registerEditor("text/x-cdg", true); // verovatno baca ex jer je dole iskomentarisana registracija editora
        fileObject = fo;

        classDiagram = readFromFile(fo);
        
        if (classDiagram == null) {
            classDiagram = new ClassDiagram();
            classDiagram.setName(fo.getName());
        }

        cookies = getCookieSet();
        cookies.assign(ClassDiagram.class, classDiagram); // put it in lookup
        openAction = new ClassDiagramOpenSupport(getPrimaryEntry());

        cookies.add((Node.Cookie) openAction);
        cookies.add(this);
        
    //    saveCookie= new SaveCookieImpl(this); 
    //    cookies.add(saveCookie);         
        
        loadData();
    }

    @Override
    protected Node createNodeDelegate() {
        // can use DataNode here as well
        DataNode node = new DataNode(this, Children.LEAF, getLookup());
        node.setDisplayName(fileObject.getName());

        return node;
    }

    public ClassDiagram getClassDiagram() {
        return classDiagram;
    }
    
    @Override
    protected int associateLookup() {
        return 1;
    }
    
    public void loadData() {
        this.classDiagram = readFromFile(fileObject);
        cookies.assign(ClassDiagram.class, this.classDiagram); // add neural network to lookup
        
        saveCookie= new SaveCookieImpl(this); 
        cookies.add(saveCookie); // this should be added only on change
    }     
    
    private ClassDiagram readFromFile(FileObject fileObject) {
        ClassDiagram classDiagram = new ClassDiagram();
        try {
             SAXReader reader = new SAXReader();
             FileObject file = getPrimaryFile();
             System.out.println(file.asLines().get(0));
             classDiagram.setName(file.getName());
             if(file.asLines().get(0).startsWith("<?xml")) {
                String putanja = file.getPath();
                
                
                Document document = reader.read(putanja);
                System.out.println("Fajl ucitan");
            
                Element root = document.getRootElement();
                ClassDiagramDeserializer cdd = new ClassDiagramDeserializer(classDiagram);
                cdd.deserialize(root);
                
                System.out.println("Deserialized");
             }
        }catch (IOException ex) {
            System.err.println(ex.getMessage());
        } catch (DocumentException ex) {
            Exceptions.printStackTrace(ex);
            
        }
        return classDiagram;
    }
    
    @Override
    public Lookup getLookup() {
        return getCookieSet().getLookup();
    }

//    @MultiViewElement.Registration(
//        displayName = "#LBL_CDG_EDITOR",
//    iconBase = "org/uml/filetype/cdgfiletype/classdiagramicon.gif",
//    mimeType = "text/x-cdg",
//    persistenceType = TopComponent.PERSISTENCE_ONLY_OPENED,
//    preferredID = "CDG",
//    position = 1000)
//    @Messages("LBL_CDG_EDITOR=Source")
//    public static MultiViewEditorElement createEditor(Lookup lkp) {
//        return new MultiViewEditorElement(lkp);
//    }
}
