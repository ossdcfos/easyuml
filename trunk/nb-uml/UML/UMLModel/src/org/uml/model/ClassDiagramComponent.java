/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.model;

import java.lang.reflect.Member;
import java.util.HashMap;

/**
 *
 * @author zoran
 */
public class ClassDiagramComponent {
            private String name;     // class, interface or enum name              
            private HashMap<String,Member> members;            
            

    public ClassDiagramComponent() {
        members = new HashMap<String, Member>(); 
    }

            
            
    public ClassDiagramComponent(String name) {
        this.name = name;
    }
            
            
            
            
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }            
            
}
