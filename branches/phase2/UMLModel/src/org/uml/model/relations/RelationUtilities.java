/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.model.relations;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Boris
 */
public class RelationUtilities {
    public static List<RelationComponent> allRelations(){
        List<RelationComponent> list = new LinkedList<>();
        list.add(new IsRelationComponent());
        list.add(new AggregationRelationComponent());
        list.add(new CompositionRelationComponent());
        list.add(new UseRelationComponent());
        list.add(new ImplementsRelationComponent());
        return list;
    }
    
    
//    JarFile jar;
//    if (isProject) {
//        jar = new JarFile(new File("build/cluster/modules/org-uml-model.jar"));
//    } else {
//        jar = new JarFile(new File("../build/cluster/modules/org-uml-model.jar"));
//    }
//    Enumeration<JarEntry> entries = jar.entries();
//    while (entries.hasMoreElements()) {
//        String fullUrl = entries.nextElement().toString();
//        if (fullUrl.startsWith("org/uml/model/relations/") && fullUrl.contains("RelationComponent") && !fullUrl.equals("org/uml/model/relations/RelationComponent.class")) {
//            try {
//                fullUrl = fullUrl.replace(".class", "");
//                fullUrl = fullUrl.replace("/", ".");
//                Class<? extends RelationComponent> forName = (Class<? extends RelationComponent>) Class.forName(fullUrl);
//                cbxRelations.addItem(forName.newInstance());
//            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
//                Exceptions.printStackTrace(ex);
//            }
//        }
//    }
}
