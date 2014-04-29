/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.explorer;

import java.util.ArrayList;
import java.util.Iterator;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.uml.model.ClassDiagram;
import org.uml.model.ClassDiagramComponent;
import org.uml.model.Member;

/**
 *
 * @author Jelena
 */
public class ClassDiagramComponentChildren extends Children.Keys<Object> {
    
    
    private ClassDiagramComponent classDiagramComponent;
    
    public ClassDiagramComponentChildren(ClassDiagramComponent classDiagramComponent) {
        this.classDiagramComponent = classDiagramComponent;
    }
    
    @Override
    protected Node[] createNodes(Object object) {
        if(object instanceof Member){                
            Member member = (Member) object;           
            MemberNode memberNode = new MemberNode(member); 
            memberNode.setName(member.getName()); 
            return new Node[] { memberNode }; 
        }else { 
            return new Node[] {  };
        }
    }
    
    @Override
    protected void addNotify() {
        super.addNotify();
        ArrayList<Object> keys = new ArrayList<Object>();
        if(classDiagramComponent.getMembers()!=null){
            Iterator it = classDiagramComponent.getMembers().entrySet().iterator();
            while (it.hasNext()) {
                java.util.Map.Entry pairs = (java.util.Map.Entry) it.next();
                Member member = (Member) pairs.getValue();
                keys.add(member);
            }
        }
        
        setKeys(keys); 
        
    }
    
}
