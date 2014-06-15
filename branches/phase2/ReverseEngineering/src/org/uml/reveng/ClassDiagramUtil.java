/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.reveng;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;
import org.openide.util.Utilities;
import org.uml.model.ClassDiagram;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.xmlSerialization.ClassDiagramXmlSerializer;

/**
 *
 * @author Milan
 */
public class ClassDiagramUtil {
    
    public void save (String path, ClassDiagram diagramToSave, ClassDiagramScene scene, String fileSeparator){
    FileOutputStream fileOut = null;
        XMLWriter writer = null;
        try {
            fileOut = new FileOutputStream(path);
            ClassDiagramXmlSerializer serializer = ClassDiagramXmlSerializer.getInstance();
            ClassDiagram diagram = diagramToSave;
            serializer.setClassDiagram(diagram);
            serializer.setClassDiagramScene(scene);
            Document document = DocumentHelper.createDocument();
           // document.setXMLEncoding("UTF-8");
            Element root = document.addElement("ClassDiagram");
            serializer.serialize(root);
            OutputFormat format = OutputFormat.createPrettyPrint();
            writer = new XMLWriter(
                fileOut, format 
            );
            writer.write( document );
            System.out.println("Reverse engineered object written to external file");
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
