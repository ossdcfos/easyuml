package org.uml.code;

import org.uml.model.ClassDiagramComponent;

/**
 * The definition of key methods interface used for generating the code out of
 * UML diagram.
 *
 * @author zoran
 */
public interface CodeGenerator {

    public String generateCode();

    public void setClassDiagramComponent(ClassDiagramComponent component);
}
