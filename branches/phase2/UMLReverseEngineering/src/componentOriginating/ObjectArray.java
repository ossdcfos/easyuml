package componentOriginating;

/**
 * Utility class that is primarily used for preserving information about
 * relationships.
 * <p>
 * Because different types arrays are not supported by Java, this class is used
 * to carry information about relationship throughout the creation process -
 * name of the relation, more specific, name of the object that caused relation
 * to be created.
 *
 * @author Milan
 */
public class ObjectArray {

    private String field;
    private Object object;

    public ObjectArray() {
    }

    public ObjectArray(String field, Object object) {
        this.field = field;
        this.object = object;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

}
