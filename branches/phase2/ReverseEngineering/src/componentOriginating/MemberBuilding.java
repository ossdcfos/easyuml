/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package componentOriginating;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import javax.lang.model.element.Element;
import org.uml.model.ClassComponent;
import org.uml.model.ClassDiagramComponent;
import org.uml.model.Constructor;
import org.uml.model.Field;
import org.uml.model.InterfaceComponent;
import org.uml.model.Literal;
import org.uml.model.Method;
import org.uml.model.MethodArgument;
import org.uml.model.PackageComponent;
import org.uml.model.Visibility;
import org.uml.reveng.CompilationProcessor;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Milan
 */
public class MemberBuilding {

    public static void packageSelector(ClassDiagramComponent cdc, String packageName) {
        if (CompilationProcessor.generatedDiagram.getPackages().containsKey(packageName)) {
            PackageComponent tempPack = CompilationProcessor.generatedDiagram.getPackages().get(packageName);
            CompilationProcessor.generatedDiagram.getPackages().remove(packageName);
            tempPack.addComponent(cdc);
            CompilationProcessor.generatedDiagram.addPackage(tempPack);
            cdc.setParentPackage(tempPack);
        } else {
            PackageComponent cpckg = new PackageComponent(packageName);
            cpckg.addComponent(cdc);
            cpckg.setParentDiagram(CompilationProcessor.generatedDiagram);
            CompilationProcessor.generatedDiagram.addPackage(cpckg);
            cdc.setParentPackage(cpckg);
        }
    }

    public static Field fieldBuilder(Element element, Object[] modifierElemnts) {
        String fName = element.getSimpleName().toString();
        String type = element.asType().toString();
        String typeShort = getShorterArguments(type);
        Element superE = element.getEnclosingElement();
        String fieldClassPath = superE.getEnclosingElement() + "." + superE.getSimpleName();
        RelationshipResolver.relationshipHasCreator(type, fieldClassPath, "", fName);
        Field createdField = new Field(fName, typeShort, Visibility.stringToVisibility("public"));
        setModifiers(modifierElemnts, createdField);
        return createdField;
    }

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
    
    public static Object methodAndConstructorBuilder(Element element, Object[] modifierElemnts, boolean isMethod) {
        String name = element.getSimpleName().toString();
        if (name.equals("<init>")){
            return "Default constructor";
        }
        String[] allTypes = element.asType().toString().split("\\)");
        String returnType = allTypes[1];
        Element topEl = element.getEnclosingElement();
        String elementPath = topEl.toString();
        HashMap<String, MethodArgument> generatedArgumens = new HashMap<String, MethodArgument>();
        if (allTypes[0].length() > 1) {
            String argumentTypes = allTypes[0].substring(1);
            RelationshipResolver.relationshipUsesCreator(argumentTypes, elementPath, "", name);
            argumentsPopulation(argumentTypes, generatedArgumens, false);
        }
        if (isMethod) {
            Method createdMethod = new Method(name, returnType, generatedArgumens);
            setModifiers(modifierElemnts, createdMethod);
            return createdMethod;
        } else {
            String className = element.getEnclosingElement().getSimpleName().toString();
            Constructor createdConstructor = new Constructor(className, returnType, generatedArgumens);
            setModifiers(modifierElemnts, createdConstructor);
            return createdConstructor;
        }
    }

    public static Literal literalBuilder(Element element, Object[] modifierElemnts) {
        Literal createdLiteral = new Literal(element.getSimpleName().toString());
        setModifiers(modifierElemnts, element);
        return createdLiteral;
    }

    public static void setModifiers(Object[] modifierElemnts, Object elementToProcess) {
        Field fElement = null;
        Method mElement = null;
        Constructor coElement = null;
        ClassComponent clElement = null;
        InterfaceComponent iElement = null;
        Literal lElement = null;
        if (elementToProcess instanceof Field) {
            fElement = (Field) elementToProcess;
        }
        if (elementToProcess instanceof Method) {
            mElement = (Method) elementToProcess;
        }
        if (elementToProcess instanceof Constructor) {
            coElement = (Constructor) elementToProcess;
        }
        if (elementToProcess instanceof ClassComponent) {
            clElement = (ClassComponent) elementToProcess;
        }
        if (elementToProcess instanceof Literal) {
            lElement = (Literal) elementToProcess;
        }
        if (elementToProcess instanceof InterfaceComponent) {
            iElement = (InterfaceComponent) elementToProcess;
        }
        //String visibility = "package"; Package returns "" and diagram can not be recreated from .cdg file when written as so
        String visibility = "public";
        int modifierInt = Modifier.PUBLIC; //Java does not currently support modifier int for packages, public is used instead
        for (Object modifier : modifierElemnts) {
            String modif = modifier.toString().toLowerCase();
            if (modif.equals("public")) {
                visibility = modif;
                modifierInt = Modifier.PUBLIC;
            }
            if (modif.matches("private")) {
                visibility = modif;
                modifierInt = Modifier.PRIVATE;
            }
            if (modif.matches("protected")) {
                visibility = modif;
                modifierInt = Modifier.PROTECTED;
            }
            if (modif.equals("static")) {
                modifierInt = Modifier.STATIC;
                if (fElement != null) {
                    fElement.setStatic(true);
                }
                if (mElement != null) {
                    mElement.addModifier(Modifier.STATIC);
                }
            }
            if (modif.equals("final")) {
                modifierInt = Modifier.FINAL;
                if (fElement != null) {
                    fElement.setFinal(true);
                }
                if (mElement != null) {
                    mElement.addModifier(Modifier.FINAL);
                }
            }
            if (modif.equals("synchronized")) {
                modifierInt = Modifier.SYNCHRONIZED;
                if (fElement != null) {
                    fElement.setSynchronized(true);
                }
                if (mElement != null) {
                    mElement.addModifier(Modifier.SYNCHRONIZED);
                }
            }
            if (modif.equals("abstract")) {
                if (clElement != null) {
                    clElement.setIsAbstract(true);
                }
                if (mElement != null) {
                    mElement.addModifier(Modifier.ABSTRACT);
                }
            }
        }
        if (fElement != null) {
            Visibility vis = Visibility.stringToVisibility(visibility);
            Visibility visibility1 = fElement.getVisibility();
            visibility1 = vis;
            fElement.setVisibility(vis);
            fElement.setModifier(modifierInt);
        }
        if (mElement != null) {
            Visibility vis = Visibility.stringToVisibility(visibility);
            mElement.setVisibility(vis);
        }
        if (coElement != null) {
            //coElement.setVisibility(Visibility.stringToVisibility(visibility));
            coElement.setModifier(modifierInt);
        }
        if (lElement != null) {
            lElement.setVisibility(Visibility.stringToVisibility(visibility));
            lElement.setModifier(modifierInt);
        }
        if (iElement != null) {
            iElement.setVisibility(Visibility.stringToVisibility(visibility));
        }
        if (clElement != null) {
            clElement.setVisibility(Visibility.stringToVisibility(visibility));
        }
    }

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
