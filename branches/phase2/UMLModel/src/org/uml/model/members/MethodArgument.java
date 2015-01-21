package org.uml.model.members;

//import com.thoughtworks.xstream.annotations.XStreamAlias;
//import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * An input argument (variable) which is used to describe Methods or
 * Constructors.
 * <p>
 * A representation of input arguments used to create methods or
 * constructors e.g. "public methodTest(<b>String argOne</b>)".
 *
 * @author Uros
 * @see Method
 * @see Constructor
 */
//@XStreamAlias("MethodArgument")
public class MethodArgument {

//    @XStreamAsAttribute
    String type;
//    @XStreamAsAttribute
    String name;

    public MethodArgument(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns string representing that MethodArgument.
     *
     * @return string
     */
    @Override
    public String toString() {
        return type + " " + name;
    }
}
