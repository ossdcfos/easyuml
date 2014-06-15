/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package componentOriginating;

/**
 *
 * @author Milan
 */
public class ObjectArray {

    private String field;
    private Object object;
    
    public ObjectArray(){
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
