package org.uml.model;

/**
 * A class implements the IHasSignature interface to indicate it has
 * a unique signature (within its surrounding scope), by which it can be identified.
 * Example: 1) ComponentBase has a unique signature within a ClassDiagram
 *          2) MemberBase has a unique signature within a ComponentBase
 * 
 * @author Boris Perović
 */
public interface IHasSignature {

    public String getSignature();
    public String getUMLSignature();
}
