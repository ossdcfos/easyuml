/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.code;



import java.util.HashMap;
import java.util.Map;
import org.uml.model.ClassDiagramComponent;
import org.uml.model.Field;

/**
 *
 * @author zoran
 */
public class FieldCodeGenerator implements CodeGenerator{

    HashMap<String, Field> fields= new  HashMap<String, Field>();

    public FieldCodeGenerator(HashMap<String, Field> fields) {
        this.fields = fields;
    }
    
    
    @Override
    public String generateCode() {
        String fieldsString="";
        
        for (Map.Entry<String, Field> en : fields.entrySet() ) {

            Field field = en.getValue();
            fieldsString += field.getSignature();
        }
        return fieldsString;
        
}   

    @Override
    public void setClassDiagramComponent(ClassDiagramComponent component) {
        
    }
            
        
    }
    

