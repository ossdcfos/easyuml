package org.uml.model;

//import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.uml.model.components.ComponentBase;
import org.uml.model.relations.RelationBase;
import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * UML Class Diagrams which can contain class, interface and enum components,
 * along with relations among them.
 *
 * @author Uros
 * @version 1.0
 * @see ComponentBase
 * @see RelationBase
 */
//@XStreamAlias("ClassDiagram")
public class ClassDiagram extends ContainerBase<ComponentBase> implements Serializable {

//    @XStreamAlias("ClassDiagramRelations")
    private HashSet<RelationBase> relations;
//    private HashMap<String, PackageComponent> packages;

    /**
     * Standard ClassDiagram constructor without arguments.
     * <p>
     * Sets name to default value and instantiates components, relations and
     * packages hash maps.
     */
    public ClassDiagram() {
        super("UML Class Diagram");
        this.relations = new HashSet<>();
//        this.packages = new HashMap<>();
    }

    @Override
    public void addPartToContainter(ComponentBase component) {
        super.addPartToContainter(component);
        component.setParentDiagram(this);
    }

    /**
     * Adds new RealtionComponent into collection of existing relations. If
     * relationComponent with same name already exists in relations, a counter
     * is concatenated to its name and is then added. The counter is then
     * incremented by one.
     *
     * @param relationComponent that will be added to the collection of
     * relations
     */
    public void addRelation(RelationBase relationComponent) {
        relations.add(relationComponent);
    }

    /**
     * Removes the given relation from this diagram's collection of relations.
     *
     * @param relation relation to be removed
     */
    public void removeRelation(RelationBase relation) {
        relations.remove(relation);
    }

    public void removeRelationsForAComponent(INameable component) {
        List<RelationBase> toRemove = new LinkedList<>();
        for (RelationBase relation : relations) {
            if (relation.getSource().equals(component) || relation.getTarget().equals(component)) {
                toRemove.add(relation);
            }
        }
        for (RelationBase rc : toRemove) {
            relations.remove(rc);
        }
    }

    /**
     * Returns the collection of components that this diagram has.
     *
     * @return collection of components
     */
    @SuppressWarnings("unchecked")
    public LinkedHashSet<ComponentBase> getComponents() {
        return new LinkedHashSet<>(parts);
    }

    /**
     * Returns the collection of this diagram's relations.
     *
     * @return collection of relations
     */
    public HashSet<RelationBase> getRelations() {
        return relations;
    }

    /**
     * Sets the collection of relations that exist in this diagram.
     *
     * @param relations to be set
     */
    public void setRelations(HashSet<RelationBase> relations) {
        this.relations = relations;
    }    
    
    /**
     * Sets the name of this Container
     *
     * @param newName of Container
     */
    @Override
    public void setName(String newName) {
        String oldName = name;
        name = newName;
        pcs.firePropertyChange("name", oldName, newName);
    }

//    /**
//     * Adds the PackageComponent into package collection.
//     *
//     * @param pc package to be added
//     */
//    public void addPackage(PackageComponent pc) {
//        packages.put(pc.getName(), pc);
//    }
//
//    /**
//     * Removes the package from package collection.
//     *
//     * @param pc package to be removed
//     */
//    public void removePackage(PackageComponent pc) {
//        packages.remove(pc.getName());
//    }
//
//    /**
//     * Gets the packages collection of this ClassDiagram.
//     *
//     * @return packages collection
//     */
//    public HashMap<String, PackageComponent> getPackages() {
//        return packages;
//    }
//    @Override
//    public String toString() {
//        return getName();
//    }
}
