/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.model;

import java.awt.Image;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 *
 * @author Uros
 */
public class ClassComponent extends ClassDiagramComponent {
    
        private Image image; // ovo treba stavit iu widget a ne ovde....
        private HashMap<String,Constructor> constructors;
        private HashMap<String,Field> fields;
        private HashMap<String,Method> methods;



    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
       

    public ClassComponent() {
        this.setName("UntitledClass");
        fields= new HashMap<String, Field>();
        methods= new HashMap<String, Method>();
    }

    public ClassComponent(String name) {
        super(name);
    }


    
    

    public HashMap<String, Field> getFields() {
        return fields;
    }

    public HashMap<String, Method> getMethods() {
        return methods;
    }


    
    public Field getField (String name) {
        return fields.get(name);
    }
    
    public void addField (Field field) {
        fields.put(field.getName(), field);
        addMember(field);
    } 
    
    public void removeField (String name) {
        fields.remove(name);
    }
    
     public Method getMethod(String name) {
        return methods.get(name);
    }    
    
    public void addMethod(Method method) {
        methods.put(method.getName(), method);
        addMember(method);
    }
    
    public void removeMethod(String name) {
        methods.remove(name);
    }

    public void changeName(String newName) {
        String oldName = getName();
        setName(newName);
        parentDiagram.componentNameChanged(this, oldName);
    }
                      
}
