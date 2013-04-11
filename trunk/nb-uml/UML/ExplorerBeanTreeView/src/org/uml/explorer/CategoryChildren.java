/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.explorer;

import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author Jelena
 */
public class CategoryChildren extends Children.Keys {
    
    private String[] Categories = new String[]{
        "Classes",
        "Interfaces",
        "Enumerations"};
    
    public CategoryChildren() {
    }
    
     protected Node[] createNodes(Object key) {
        Category obj = (Category) key;
        return new Node[] { new CategoryNode( obj ) };
    }
    
    protected void addNotify() {
        super.addNotify();
        Category[] objs = new Category[Categories.length];
        for (int i = 0; i < objs.length; i++) {
            Category cat = new Category();
            cat.setName(Categories[i]);
            objs[i] = cat;
        }
        setKeys(objs);
    }
    
}
