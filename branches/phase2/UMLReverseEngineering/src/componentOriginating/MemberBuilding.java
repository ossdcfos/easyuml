package componentOriginating;

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import javax.lang.model.element.Element;
import org.uml.model.components.ClassComponent;
import org.uml.model.components.ComponentBase;
import org.uml.model.members.Field;
import org.uml.model.members.MethodBase;
import org.uml.model.members.MethodArgument;
import org.uml.model.Visibility;
import org.apache.commons.lang.StringUtils;
import org.uml.model.components.PackageComponent;
import org.uml.model.members.Constructor;
import org.uml.model.members.Literal;
import org.uml.model.members.MemberBase;
import org.uml.model.members.Method;

/**
 * Creates Class diagram component's (Class, Enum od Interface) members (fields,
 * methods, constructors or literals).
 *
 * @author Milan
 */
public class MemberBuilding {

    /**
     * Selects and sets the right Package component for the Class diagram
     * component given, if one exists; if not, it is created and coupled with
     * the component.
     *
     * @param cdc for which a package should be found
     * @param packageName of Class diagram component
     * @see PackageComponent
     */
    public static void packageSelector(ComponentBase cdc, String packageName) {
//        if (CompilationProcessor.generatedDiagram.getPackages().containsKey(packageName)) {
//            PackageComponent tempPack = CompilationProcessor.generatedDiagram.getPackages().get(packageName);
//            CompilationProcessor.generatedDiagram.getPackages().remove(packageName);
//            tempPack.addComponent(cdc);
//            CompilationProcessor.generatedDiagram.addPackage(tempPack);
//            cdc.setParentPackage(tempPack);
//        } else {
//            PackageComponent cpckg = new PackageComponent(CompilationProcessor.generatedDiagram, packageName);
//            cpckg.addComponent(cdc);
//            CompilationProcessor.generatedDiagram.addPackage(cpckg);
//            cdc.setParentPackage(cpckg);
//        }
    }

    /**
     * Fully builds a Field component out of given Element object and creates a
     * Has relationship component if needed.
     *
     * @param element based on which a Field component is created
     * @param modifierElemnts visibility and other modifiers (public, static...)
     * @return fully built Field component
     * @see Field
     */
    public static Field fieldBuilder(Element element, Object[] modifierElemnts) {
        String fieldName = element.getSimpleName().toString();
        String type = element.asType().toString();
        String typeShort = getShorterArguments(type);
//        Element superE = element.getEnclosingElement();
//        String fieldClassPath = superE.getEnclosingElement() + "." + superE.getSimpleName();
//        RelationshipResolver.relationshipHasCreator(type, fieldClassPath, "", fieldName);
        Field createdField = new Field(fieldName, typeShort, Visibility.PACKAGE);
        setModifiers(createdField, modifierElemnts);
        return createdField;
    }

    /**
     * Makes method's arguments representation shorter - takes packages out of
     * their class names.
     *
     * @param type - full type of argument (i.e. java.util.HashMap)
     * @return shortened type of argument (i.e. HashMap)
     * @see MethodBase
     * @see MethodArgument
     */
    private static String getShorterArguments(String type) {
        String typeOfField = type;
        String[] types = type.split("<|>|,");
        for (String typeForReplacement : types) {
            String[] fType = typeForReplacement.split("\\.");
            String shortType = fType[fType.length - 1];
            typeOfField = typeOfField.replace(typeForReplacement, shortType);
        }
        return typeOfField;
    }

    /**
     * Fully builds a Method or Constructor component out of given Element
     * object and creates an Uses relationship component if needed.
     *
     * @param element based on which a Method or Constructor component is
     * created
     * @param modifierElemnts visibility and other modifiers (public, static...)
     * @return fully built Method or Constructor component
     * @see MethodBase
     * @see Constructor
     */
    public static Object methodAndConstructorBuilder(Element element, Object[] modifierElemnts, boolean isMethod) {
        String name = element.getSimpleName().toString();
        if (name.equals("<init>")) {
            return "Default constructor";
        }
        String[] allTypes = element.asType().toString().split("\\)");
        String returnType = allTypes[1];
        Element topEl = element.getEnclosingElement();
        String elementPath = topEl.toString();
        HashMap<String, MethodArgument> generatedArgumens = new HashMap<>();
        if (allTypes[0].length() > 1) {
            String argumentTypes = allTypes[0].substring(1);
            RelationshipResolver.relationshipUsesCreator(argumentTypes, elementPath, "", name);
            argumentsPopulation(argumentTypes, generatedArgumens, false);
        }
        if (isMethod) {
            Method createdMethod = new Method(name, returnType, generatedArgumens);
            setModifiers(createdMethod, modifierElemnts);
            return createdMethod;
        } else {
            String className = element.getEnclosingElement().getSimpleName().toString();
            Constructor createdConstructor = new Constructor(className, generatedArgumens);
            setModifiers(createdConstructor, modifierElemnts);
            return createdConstructor;
        }
    }

    /**
     * Fully builds a Literal component out of given Element object and creates
     * an Uses relationship component if needed.
     *
     * @param element based on which a Literal component is created
     * @param modifierElemnts visibility and other modifiers (public, static...)
     * @return fully built Literal component
     * @see Literal
     */
    public static Literal literalBuilder(Element element, Object[] modifierElemnts) {
        Literal createdLiteral = new Literal(element.getSimpleName().toString());
        setModifiers(element, modifierElemnts);
        return createdLiteral;
    }

    /**
     * Sets modifiers (public, static, abstract...) to the given Member object.
     *
     * @param modifierElements earlier found modifiers
     * @param elementToProcess Member object for modifiers to be set
     * @see Member
     */
    public static void setModifiers(Object elementToProcess, Object[] modifierElements) {
        MemberBase member = null;
        ComponentBase component = null;
        if (elementToProcess instanceof MemberBase) {
            member = (MemberBase) elementToProcess;
        } else if (elementToProcess instanceof ComponentBase) {
            component = (ComponentBase) elementToProcess;
        }
        //String visibility = "package"; Package returns "" and diagram can not be recreated from .cdg file when written as so
        String visibility = "package";
        //Java does not currently support modifier int for packages, public is used instead
        for (Object modifier : modifierElements) {
            int modifierInt;
            String modString = modifier.toString().toLowerCase();
            if (modString.equals(Modifier.toString(Modifier.PUBLIC))) {
                visibility = modString;
            }
            if (modString.equals(Modifier.toString(Modifier.PRIVATE))) {
                visibility = modString;
            }
            if (modString.equals(Modifier.toString(Modifier.PROTECTED))) {
                visibility = modString;
            }
            Visibility vis = Visibility.valueOf(visibility.toUpperCase());
            if (member != null) member.setVisibility(vis);
            else if (component != null) component.setVisibility(vis);

            if (modString.equals(Modifier.toString(Modifier.STATIC))) {
                modifierInt = Modifier.STATIC;
                if (member instanceof Field) {
                    ((Field) member).setStatic(true);
                } else if (member instanceof Method) {
                    ((Method) member).setStatic(true);
                }
            }
            if (modString.equals(Modifier.toString(Modifier.FINAL))) {
                modifierInt = Modifier.FINAL;
                if (member instanceof Field) {
                    ((Field) member).setFinal(true);
                } else if (member instanceof Method) {
                    ((Method) member).setFinal(true);
                }
            }
            if (modString.equals(Modifier.toString(Modifier.SYNCHRONIZED))) {
                modifierInt = Modifier.SYNCHRONIZED;
                if (member instanceof Method) {
                    ((Method) member).setSynchronized(true);
                }
            }
            if (modString.equals(Modifier.toString(Modifier.TRANSIENT))) {
                modifierInt = Modifier.TRANSIENT;
                if (member instanceof Field) {
                    ((Field) member).setTransient(true);
                }
            }
            if (modString.equals(Modifier.toString(Modifier.ABSTRACT))) {
                modifierInt = Modifier.ABSTRACT;
                if (member instanceof Method) {
                    ((Method) member).setAbstract(true);
                }
                if (component instanceof ClassComponent) {
                    ((ClassComponent) component).setAbstract(true);
                }
            }
        }
    }

    /**
     * Creates Method arguments.
     *
     * @param argumentTypes types (classes) of method arguments
     * @param generatedArgumens arguments created
     * @param truePaths if arguments's classes should be represented without
     * their packages
     */
    private static void argumentsPopulation(String argumentTypes, HashMap<String, MethodArgument> generatedArgumens, boolean truePaths) {
        generatedArgumens.clear();
        if (argumentTypes.contains(",")) {
            String argument = "";
            String[] arguments = argumentTypes.split(",");
            int argumentNoCounter = 0;
            for (int i = 0; i < arguments.length; i++) {
                argument += arguments[i];
                int openSharpBrackets = StringUtils.countMatches(argument, "<");
                int closedSharpBrackets = StringUtils.countMatches(argument, ">");
                if (openSharpBrackets == closedSharpBrackets) {
                    int offset = arguments.length - i - 1;
                    if (truePaths) {
                        generatedArgumens.put("arg" + offset, new MethodArgument(argument, "Argument" + argumentNoCounter));
                    } else {
                        generatedArgumens.put("arg" + offset, new MethodArgument(getShorterArguments(argument), "Argument" + argumentNoCounter));
                    }
                    argumentNoCounter++;
                    argument = "";
                } else {
                    argument += ", ";
                }
                openSharpBrackets = 0;
                closedSharpBrackets = 0;
            }
        } else {
            if (truePaths) {
                generatedArgumens.put("arg1", new MethodArgument(argumentTypes, "Argument0"));
            } else {
                generatedArgumens.put("arg1", new MethodArgument(getShorterArguments(argumentTypes), "Argument0"));
            }
        }
    }
}
