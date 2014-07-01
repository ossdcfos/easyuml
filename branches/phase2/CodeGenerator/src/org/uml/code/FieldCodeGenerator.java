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
 * Class field's code generating class.
 *
 * @author zoran
 */
public class FieldCodeGenerator implements CodeGenerator {

    HashMap<String, Field> fields = new HashMap<String, Field>();

    /**
     * Constructor that sets this object's fields HasMap to the given value.
     *
     * @param fields which code needs to be generated
     */
    public FieldCodeGenerator(HashMap<String, Field> fields) {
        this.fields = fields;
    }

    /**
     * Generates code for previously set HasMap of fields. Utilises Field
     * component's getSignature method to properly generate code.
     *
     * @return code of the generated fields
     * @see Field
     */
    @Override
    public String generateCode() {
        String fieldsString = "";

        for (Map.Entry<String, Field> en : fields.entrySet()) {

            Field field = en.getValue();
            fieldsString += field.getSignature();
        }
        return fieldsString;

    }

    //this method must be implemented because of the CodeGenerator interface, although it is never used
    @Override
    public void setClassDiagramComponent(ClassDiagramComponent component) {

    }

}
