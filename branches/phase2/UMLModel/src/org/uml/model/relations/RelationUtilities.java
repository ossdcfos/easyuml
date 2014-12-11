package org.uml.model.relations;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Boris
 */
public class RelationUtilities {
    public static List<RelationBase> allRelations(){
        List<RelationBase> list = new LinkedList<>();
        list.add(new IsRelation());
        list.add(new ImplementsRelation());
        list.add(new AggregationRelation());
        list.add(new CompositionRelation());
        list.add(new UseRelation());
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
