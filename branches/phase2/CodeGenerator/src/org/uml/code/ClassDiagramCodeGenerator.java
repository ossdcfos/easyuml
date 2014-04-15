/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.code;

import java.util.HashMap;
import org.uml.model.ClassComponent;
import org.uml.model.ClassDiagram;
import org.uml.model.ClassDiagramComponent;
import org.uml.model.EnumComponent;
import org.uml.model.InterfaceComponent;

/**
 *
 * @author zoran
 */
public class ClassDiagramCodeGenerator implements CodeGenerator {

    private static ClassDiagramCodeGenerator instance;
    ClassDiagram classDiagram;
    HashMap<Class, CodeGenerator> generators;

    public ClassDiagramCodeGenerator() {
        this.generators = new HashMap<Class, CodeGenerator>();
        generators.put(ClassComponent.class, new ClassCodeGenerator());
        generators.put(InterfaceComponent.class, new InterfaceCodeGenerator());
        generators.put(EnumComponent.class, new EnumCodeGenerator());
    }

    public static ClassDiagramCodeGenerator getInstance() {
        if (instance == null) {
            instance = new ClassDiagramCodeGenerator();
        }

        return instance;
    }

    public void setClassDiagram(ClassDiagram classDiagram) {
        this.classDiagram = classDiagram;
    }

    @Override
    public String generateCode() {
        StringBuilder sb = new StringBuilder();

        for (ClassDiagramComponent comp : classDiagram.getComponents().values()) {
            CodeGenerator codeGen = generators.get(comp.getClass());
            codeGen.setClassDiagramComponent(comp);
            String code = codeGen.generateCode();
            FileWriter.getInstance().writeFiles(code, comp.getName(), comp.getPack());

            sb.append(code);
            sb.append("\n");
        }
//        ClassDiagramXmlSerializer serializer = ClassDiagramXmlSerializer.getInstance();
//        serializer.setClassDiagram(classDiagram);
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
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//        ClassDiagram cd = new ClassDiagram();
//            SAXReader reader = new SAXReader();
//             Document document2;
//        try {
//            document2 = reader.read(new File("out.xml"));
//            System.out.println("Fajl ucitan");
//            
//            Element root2 = document2.getRootElement();
//            ClassDiagramDeserializer cdd = new ClassDiagramDeserializer(cd);
//            cdd.deserialize(root);
//            System.out.println("Deserialized");
//        } catch (DocumentException ex) {
//            Exceptions.printStackTrace(ex);
//        }
//        
//        ClassDiagramXmlSerializer serializer2 = ClassDiagramXmlSerializer.getInstance();
//            serializer2.setClassDiagram(cd);
//            Document document3 = DocumentHelper.createDocument();
//            Element root2 = document3.addElement("ClassDiagram");
//            serializer2.serialize(root2);
//            
//            OutputFormat format = OutputFormat.createPrettyPrint();
//            XMLWriter writer;
//        try { 
//            writer = new XMLWriter(
//                    new FileWriter( "out2.xml"), format
//            );
//            writer.write( document3 );
//            writer.close(); 
//            System.out.println("Ispisano u fajl");
//        } catch (IOException ex) {
//            Exceptions.printStackTrace(ex);
//        }







        return sb.toString();
    }

    @Override
    public void setClassDiagramComponent(ClassDiagramComponent component) {
    }
}
