package org.uml.code;

import java.util.LinkedHashMap;
import org.uml.model.components.ComponentBase;
import org.uml.model.members.Field;

/**
 * Class field's code generating class.
 *
 * @author zoran
 */
public class FieldCodeGenerator implements CodeGenerator {

    LinkedHashMap<String, Field> fields = new LinkedHashMap<>();

    /**
     * Constructor that sets this object's fields HasMap to the given value.
     *
     * @param fields which code needs to be generated
     */
    public FieldCodeGenerator(LinkedHashMap<String, Field> fields) {
        this.fields = fields;
    }

    /**
     * Generates code for previously set HashMap of fields. Utilises Field
     * component's getSignature method to properly generate code.
     *
     * @return code of the generated fields
     * @see Field
     */
    @Override
    public String generateCode() {
        String fieldsString = "";

        for (Field field : fields.values()) {
            fieldsString += field.getFullSignature();
        }
        return fieldsString;

    }

    //this method must be implemented because of the CodeGenerator interface, although it is never used
    @Override
    public void setComponent(ComponentBase component) {

    }

}
