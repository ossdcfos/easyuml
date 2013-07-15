package org.uml.code;

import org.uml.model.ClassDiagramComponent;

/**
 *
 * @author zoran
 */
public interface CodeGenerator {
    public String generateCode();
    public void setClassDiagramComponent(ClassDiagramComponent component);
}
