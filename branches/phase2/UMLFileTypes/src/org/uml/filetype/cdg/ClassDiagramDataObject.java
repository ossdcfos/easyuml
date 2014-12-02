package org.uml.filetype.cdg;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileOutputStream;
import java.io.IOException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.netbeans.api.actions.Openable;
import org.netbeans.api.actions.Savable;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.MIMEResolver;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;
import org.uml.classDiagramManager.ClassDiagramManager;
import org.uml.model.ClassDiagram;
import org.uml.visual.UMLTopComponent;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.xmlDeserialization.ClassDiagramDeserializer;
import org.uml.xmlSerialization.ClassDiagramXmlSerializer;

@Messages({
    "LBL_ClassDiagram_LOADER=Files of ClassDiagram"
})
@MIMEResolver.ExtensionRegistration(
        displayName = "#LBL_ClassDiagram_LOADER",
        mimeType = "text/x-cdg",
        extension = {"cdg", "CDG"}
)
@DataObject.Registration(
        mimeType = "text/x-cdg",
        iconBase = "org/uml/filetype/cdg/classdiagramicon.png",
        displayName = "#LBL_ClassDiagram_LOADER",
        position = 300
)
@ActionReferences({
    @ActionReference(
            path = "Loaders/text/x-cdg/Actions",
            id = @ActionID(category = "System", id = "org.openide.actions.OpenAction"),
            position = 100,
            separatorAfter = 200
    ),
    @ActionReference(
            path = "Loaders/text/x-cdg/Actions",
            id = @ActionID(category = "Edit", id = "org.openide.actions.CutAction"),
            position = 300
    ),
    @ActionReference(
            path = "Loaders/text/x-cdg/Actions",
            id = @ActionID(category = "Edit", id = "org.openide.actions.CopyAction"),
            position = 400,
            separatorAfter = 500
    ),
    @ActionReference(
            path = "Loaders/text/x-cdg/Actions",
            id = @ActionID(category = "Edit", id = "org.openide.actions.DeleteAction"),
            position = 600
    ),
    @ActionReference(
            path = "Loaders/text/x-cdg/Actions",
            id = @ActionID(category = "System", id = "org.openide.actions.RenameAction"),
            position = 700,
            separatorAfter = 800
    ),
    @ActionReference(
            path = "Loaders/text/x-cdg/Actions",
            id = @ActionID(category = "System", id = "org.openide.actions.SaveAsTemplateAction"),
            position = 900,
            separatorAfter = 1000
    ),
    @ActionReference(
            path = "Loaders/text/x-cdg/Actions",
            id = @ActionID(category = "System", id = "org.openide.actions.FileSystemAction"),
            position = 1100,
            separatorAfter = 1200
    ),
    @ActionReference(
            path = "Loaders/text/x-cdg/Actions",
            id = @ActionID(category = "System", id = "org.openide.actions.ToolsAction"),
            position = 1300
    ),
    @ActionReference(
            path = "Loaders/text/x-cdg/Actions",
            id = @ActionID(category = "System", id = "org.openide.actions.PropertiesAction"),
            position = 1400
    )
})
public class ClassDiagramDataObject extends MultiDataObject implements Openable, Savable {

    FileObject fileObject;
    ClassDiagram classDiagram;
    UMLTopComponent topComponent;

    public ClassDiagramDataObject(FileObject fo, MultiFileLoader loader) throws DataObjectExistsException, IOException {
        super(fo, loader);
        fileObject = fo;
        this.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("name")) {
                    Object o = evt.getNewValue();
                    classDiagram.setName((String) o);
                    topComponent.setName((String) o);
                }
            }
        });
    }

    @Override
    protected int associateLookup() {
        return 1;
    }

    @Override
    public void open() {
        classDiagram = readFromFile(fileObject);

        if (classDiagram == null) {
            classDiagram = new ClassDiagram();
            classDiagram.setName(fileObject.getName());
        }

        if (topComponent == null || !topComponent.isOpened()) {
            topComponent = new UMLTopComponent(classDiagram, fileObject);
            topComponent.open();
        }
        topComponent.requestActive();

        ClassDiagramScene scene = topComponent.getScene();
        ClassDiagramManager.getInstance().addScene(scene);
    }

    private ClassDiagram readFromFile(FileObject fileObject) {
        ClassDiagram classDiag = new ClassDiagram();
        try {
            SAXReader reader = new SAXReader();
            //System.out.println(fileObject.asLines().get(0));
            classDiag.setName(fileObject.getName());
            if (fileObject.asLines().get(0).startsWith("<?xml")) {
                String path = fileObject.getPath();

                Document document = reader.read(path);
                //System.out.println("Fajl ucitan");

                Element root = document.getRootElement();
                ClassDiagramDeserializer cdd = new ClassDiagramDeserializer(classDiag);
                cdd.deserialize(root);

                System.out.println("Deserialized");
            }
        } catch (IOException | DocumentException ex) {
            System.err.println(ex.getMessage());
            Exceptions.printStackTrace(ex);
        }
        return classDiag;
    }
    
    public static void saveDiagram(FileObject fileObject, UMLTopComponent topComponent) {
        NotifyDescriptor.Confirmation msg = new NotifyDescriptor.Confirmation(
                "Do you want to save \"" + fileObject.getNameExt() + "\"?", 
                NotifyDescriptor.OK_CANCEL_OPTION,
                NotifyDescriptor.QUESTION_MESSAGE);
        
        Object result = DialogDisplayer.getDefault().notify(msg);
        if (result.equals(NotifyDescriptor.OK_OPTION)) {
            FileOutputStream fileOut = null;
            XMLWriter writer = null;
            try {
                FileObject fo = fileObject;
                String putanja = fo.getPath();
                fileOut = new FileOutputStream(putanja);

                ClassDiagramXmlSerializer serializer = ClassDiagramXmlSerializer.getInstance();
                ClassDiagram diagram = topComponent.getLookup().lookup(ClassDiagram.class);
//                ClassDiagramScene cdScene = topComponent.getLookup().lookup(ClassDiagramScene.class);
                
                diagram.setName(fo.getName());
                serializer.setClassDiagram(diagram);
                serializer.setClassDiagramScene(ClassDiagramManager.getInstance().getSceneForDiagram(diagram));
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
                    if(fileOut != null) fileOut.close();
                    if(writer != null) writer.close();
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }
    }

    @Override
    public void save() throws IOException {
        saveDiagram(fileObject, topComponent);
    }
}
