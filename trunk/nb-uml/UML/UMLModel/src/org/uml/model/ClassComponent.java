package org.uml.model;

import java.util.HashMap;

/**
 *
 * @author Uros
 */
public class ClassComponent extends ClassDiagramComponent {
    
        private HashMap<String,Constructor> constructors;
        private HashMap<String,Field> fields;
        private HashMap<String,Method> methods;

 
    public ClassComponent() {
        this.setName("UntitledClass");
        fields= new HashMap<String, Field>();
        methods= new HashMap<String, Method>();
        constructors = new HashMap<String, Constructor>();
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

    public HashMap<String, Constructor> getConstructors() {
        return constructors;
    }

    public Field getField (String name) {
        return fields.get(name);
    }
    
    public void addField (Field field) {
        field.setDeclaringClass(this);
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
        method.setDeclaringClass(this);
        methods.put(method.getName(), method);
        addMember(method);
    }
    
    public void removeMethod(String name) {
        methods.remove(name);
    }
    
    public Constructor getConstuctor(String name) {
        return constructors.get(name);
    }    
    
    public void addConstructor(Constructor constructor) {
        constructor.setDeclaringClass(this);
        constructors.put(this.getName(), constructor);
        addMember(constructor);
    }
    
    public void removeConstructor(String name) {
        constructors.remove(name);
    }
}
