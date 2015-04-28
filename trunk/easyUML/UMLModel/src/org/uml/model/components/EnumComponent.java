package org.uml.model.components;

import java.util.LinkedHashSet;
import org.uml.model.members.Literal;
import org.uml.model.members.MemberBase;

/**
 * Enumerations (enums) from UML Class diagrams. Used to represent enumeration
 * classes in UML diagrams.
 *
 * @author zoran
 * @see ClassComponent
 * @see InterfaceComponent
 */
public class EnumComponent extends ComponentBase {

    /**
     * Set of literals this enum contains.
     */
    private LinkedHashSet<Literal> literals;

    /**
     * Default constructor. Sets name to default value.
     * <p>
     * Used when deserializing or adding a new component from palette or popup menu.
     */
    public EnumComponent() {
        this("UntitledEnum");
    }

    /**
     * Constructor with parameters which calls its super constructor.
     * Sets the name of the enum.
     * Instantiates literals collection.
     *
     * @param name to be set
     */
    // Used when reverse engineering
    public EnumComponent(String name) {
        super(name);
        literals = new LinkedHashSet<>();
    }

    /**
     * Returns the collection of literals that this enum contains
     *
     * @return HashSet of literals contained
     */
    public LinkedHashSet<Literal> getLiterals() {
        return literals;
    }

    /**
     * Adds the given literal to the enum.
     * Sets the declaring component of the literal to be this enum.
     *
     * @param literal to add
     */
    public void addLiteral(Literal literal) {
        addComponentToContainter(literal);
        literal.setDeclaringComponent(this);
        literals.add(literal);
        // TODO make Literal to contain only name
        // has to be here, because literal does not have a declaring component when it is created
        literal.setType(name);
    }

    /**
     * Removes the member from the associated collection.
     *
     * @param member to be removed
     */
    @Override
    public void removeMember(MemberBase member) {
        removeComponentFromContainer(member);
        if (member instanceof Literal) literals.remove((Literal) member);
//        else throw new RuntimeException("Removing unsupported member: " + member.toString());
    }
}
