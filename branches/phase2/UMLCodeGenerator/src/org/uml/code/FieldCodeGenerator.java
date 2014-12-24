package org.uml.code;

import java.util.LinkedHashSet;
import org.uml.model.components.ComponentBase;
import org.uml.model.members.Field;

/**
 * Class field's code generating class.
 *
 * @author zoran
 */
public class FieldCodeGenerator implements CodeGenerator {

    LinkedHashSet<Field> fields = new LinkedHashSet<>();

    /**
     * Constructor that sets this object's fields HasMap to the given value.
     *
     * @param fields which code needs to be generated
     */
    public FieldCodeGenerator(LinkedHashSet<Field> fields) {
        this.fields = fields;
    }

    /**
     * Generates code for previously set HashSet of fields. Utilises Field
     * component's getSignature method to properly generate code.
     *
     * @return code of the generated fields
     * @see Field
     */
    @Override
    public String generateCode() {
        StringBuilder fieldsString = new StringBuilder();

        for (Field field : fields) {
//            fieldsString.append(field.getFullSignature()).append(";\n");
        }
        return fieldsString.toString();
    }

    //this method must be implemented because of the CodeGenerator interface, although it is never used
    @Override
    public void setComponent(ComponentBase component) {
    }

}
