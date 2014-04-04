package org.uml.filetype.cdgfiletype;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import javax.rmi.CORBA.Util;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.NotifyDescriptor.Confirmation;
import org.openide.cookies.SaveCookie;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;
import org.openide.util.Utilities;
import org.uml.model.ClassDiagram;
import org.uml.xmlSerialization.ClassDiagramXmlSerializer;

/**
 *
 * @author Jelena
 */
public class SaveCookieImpl implements SaveCookie {

    private final CDGDataObject context;

    public SaveCookieImpl(CDGDataObject context) {
        this.context = context;
    }

    @Override
    public void save() throws IOException {

        Confirmation msg = new NotifyDescriptor.Confirmation("Do you want to save \""
                + "file", NotifyDescriptor.OK_CANCEL_OPTION,
                NotifyDescriptor.QUESTION_MESSAGE);

        Object result = DialogDisplayer.getDefault().notify(msg);

        //When user clicks "Yes", indicating they really want to save,
        //we need to disable the Save button and Save menu item,
        //so that it will only be usable when the next change is made
        //to the text field:
        if (NotifyDescriptor.YES_OPTION.equals(result)) {
           save(context);
            //Implement your save functionality here.
            
        }

    }
    
    public void save(CDGDataObject cdg){
        FileOutputStream fileOut = null;
        XMLWriter writer = null;
        try {
            FileObject fo = cdg.getPrimaryFile();
            String putanja = fo.getPath();
            fileOut = new FileOutputStream(putanja);
            
            ClassDiagramXmlSerializer serializer = ClassDiagramXmlSerializer.getInstance();
            serializer.setClassDiagram(Utilities.actionsGlobalContext().lookup(ClassDiagram.class));
            Document document = DocumentHelper.createDocument();
           // document.setXMLEncoding("UTF-8");
            Element root = document.addElement("ClassDiagram");
            serializer.serialize(root);
            OutputFormat format = OutputFormat.createPrettyPrint();
            writer = new XMLWriter(
                fileOut, format 
            );
            writer.write( document );

            
            System.out.println("Ispisano u fajl");
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            try {
                fileOut.close();
                writer.close();
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }    
    
    
    
    
}
