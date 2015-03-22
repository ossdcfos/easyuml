package org.uml.model;

/**
 * A class implements the INameable interface to indicate it has a name.
 * Example: <br>1) Each ContainerBase has a name.
 *          <br>2) Each ComponentBase has a name, used in ContainerBase to rename if the component with the same signature exists.
 *          <br>3) Each MemberBase has a name, used in ComponentBase to rename if the member with the same signature exists.
 * 
 * @author Boris Perović Perović
 */
public interface INameable {

    public void setName(String name);

    public String getName();
}
