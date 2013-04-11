package org.uml.model;

import java.util.HashMap;

/**
 *
 * @author zoran
 */
public class ClassDiagramComponent {
            private String name;     // class, interface or enum name              
            private HashMap<String,Member> members; // index of all fields, methods and constructors            
            protected ClassDiagram parentDiagram;            
            private int memberCounter = 0;
            

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
    
    protected void addMember(Member member) {
        if(nameExists(member.getName())){
            member.setName(member.getName() + memberCounter);
        }
        members.put(member.getName(), member);
        memberCounter++;
        System.out.println("Members");
        for (Member member1 : members.values()) {
            System.out.println(member1.getName());
        }
    }
    
    protected void removeMember(String name) {
        members.remove(name);
    }
    
    public ClassDiagram getParentDiagram() {
        return parentDiagram;
    }

    public void setParentDiagram(ClassDiagram parentDiagram) {
        this.parentDiagram = parentDiagram;
    }
    
    public boolean nameExists(String name) {
        return members.containsKey(name);
    }
    
    public void componentNameChanged(Member member, String oldName) {
        members.remove(oldName);
        addMember(member);
    }
}
