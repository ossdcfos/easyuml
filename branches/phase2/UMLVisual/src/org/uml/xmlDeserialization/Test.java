/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.xmlDeserialization;

import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.uml.model.ClassComponent;
import org.uml.model.ClassDiagram;
import org.uml.model.members.Field;
import org.uml.model.members.Method;
import org.uml.model.members.Visibility;
import org.uml.xmlSerialization.ClassDiagramXmlSerializer;
import org.uml.xmlSerialization.ClassDiagramXmlSerializer;

/**
 *
 * @author Stefan
 */
public class Test {
    public static void main(String[] args) {
//        ClassDiagram cd = new ClassDiagram();
//        cd.setName("Moj dijagram");
//        ClassComponent cc = new ClassComponent();
//        cc.setName("Moja klasa");
//        cc.setIsAbstract(false);
//        cc.setParentDiagram(cd);
//        cc.setVisibility(Visibility.PUBLIC);
//        Field f = new Field("polje1", "String", Visibility.PRIVATE);
//        f.setIsStatic(true);
//        cc.addField(f);
//        cd.addComponent(cc);
//        ClassDiagramXmlSerializer serializer = ClassDiagramXmlSerializer.getInstance();
//        serializer.setClassDiagram(cd);
//        Document document = DocumentHelper.createDocument();
//        Element root = document.addElement("ClassDiagram");
//        serializer.serialize(root);
        
        
//        
//        try {
//            
//            OutputFormat format = OutputFormat.createPrettyPrint();
//            XMLWriter writer = new XMLWriter(
//                new FileWriter( "out.xml"), format 
//            );
//            writer.write( document );
//            
//            writer.close(); 
//            System.out.println("Ispisano u fajl");
            /////////////////////////////////
            try {
                ClassDiagram cd = new ClassDiagram();
                SAXReader reader = new SAXReader();
             Document document = reader.read(new File("out.xml"));
            System.out.println("Fajl ucitan");
            
            Element root = document.getRootElement();
            ClassDiagramDeserializer cdd = new ClassDiagramDeserializer(cd);
            cdd.deserialize(root);
            
            System.out.println("Deserialized");
            }catch (Exception ex) {
                ex.printStackTrace();
            }
            
//            
//            ClassDiagramXmlSerializer serializer2 = ClassDiagramXmlSerializer.getInstance();
//            serializer2.setClassDiagram(cd);
//            Document document2 = DocumentHelper.createDocument();
//            Element root2 = document2.addElement("ClassDiagram");
//            serializer2.serialize(root2);
//            
//            OutputFormat format = OutputFormat.createPrettyPrint();
//            XMLWriter writer = new XMLWriter(
//                new FileWriter( "out2.xml"), format 
//            );
//            writer.write( document2 );
//            
//            writer.close(); 
//            System.out.println("Ispisano u fajl");
//        }catch (Exception e) {
//                e.printStackTrace();
//        }
    }
}
