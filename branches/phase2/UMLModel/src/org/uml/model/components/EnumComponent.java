package org.uml.model.components;

//import com.thoughtworks.xstream.annotations.XStreamAlias;
import java.util.LinkedHashSet;
import org.uml.model.members.Literal;
import org.uml.model.members.MemberBase;

/**
 * Enumerations (Enums) from UML Class diagrams. Used to represent enumeration
 * classes in UML diagrams.
 *
 * @author zoran
 * @see ClassDiagramComponents
 * @see ClassComponent
 * @see InterfaceComponent
 * @see PackageComponent
 */
//@XStreamAlias("Enum")
public class EnumComponent extends ComponentBase {
    
    private LinkedHashSet<Literal> literals;

    /**
     * Default constructor only specifying parent diagram. Sets name to default value.
     */
    public EnumComponent() {
        this("UntitledEnum");
    }

    /**
     * Parameterized Constructor, calls the default constructor (sets the name
     * to default value). Instantiates fields, methods, constructors and
     * literals collections.
     *
     * @param name of EnumComponent
     * @see ComponentBase
     */
    public EnumComponent(String name) {
        super(name);
        literals = new LinkedHashSet<>();
    }

    /**
     * Returns the collection of literals that this enum contains
     *
     * @return HashSet of enum's literals
     */
    public LinkedHashSet<Literal> getLiterals() {
        return literals;
    }

    /**
     * Adds a literal to EnumComponent's collection of literals
     * <p>
     * Automatically generates first parameter of literals collection (the
     * second is literal supplied as a parameter).
     *
     * @param literal that will be added to collection
     */
    public void addLiteral(Literal literal) {
        addPartToContainter(literal);
        literal.setDeclaringComponent(this);
        // TODO make Literal to contain only name
        // has to be here, because literal does not have a declaring component when it is created
        literal.setType(name);
        literals.add(literal);
    }

    @Override
    public void removeMember(MemberBase member) {
        if (member instanceof Literal) literals.remove((Literal)member);
        else throw new RuntimeException("Removing unsupported member: "+member.toString());
    }
    
}
