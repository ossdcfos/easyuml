/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package componentOriginating;

import javax.lang.model.element.Element;
import static javax.lang.model.element.ElementKind.CLASS;
import static javax.lang.model.element.ElementKind.CONSTRUCTOR;
import static javax.lang.model.element.ElementKind.ENUM;
import static javax.lang.model.element.ElementKind.ENUM_CONSTANT;
import static javax.lang.model.element.ElementKind.FIELD;
import static javax.lang.model.element.ElementKind.INTERFACE;
import static javax.lang.model.element.ElementKind.METHOD;
import org.uml.model.ClassComponent;
import org.uml.model.Constructor;
import org.uml.model.EnumComponent;
import org.uml.model.Field;
import org.uml.model.InterfaceComponent;
import org.uml.model.Literal;
import org.uml.model.Method;
import org.uml.reveng.CompilationProcessor;
import org.uml.reveng.GeneratedDiagramManager;

/**
 *
 * @author Milan
 */
public class ComponentCreation {

    public static void createTopElementComponent(Element tlcc) {
        Object[] rootModifiers = tlcc.getModifiers().toArray();
        switch (tlcc.getKind()) {
            case CLASS:
                CompilationProcessor.genClass = classBuilder(tlcc, rootModifiers);
                CompilationProcessor.allFoundClasses.add(CompilationProcessor.genClass);
                try {
                    CompilationProcessor.generatedDiagram.addComponent(CompilationProcessor.genClass);
                } catch (RuntimeException rtex) {
                    System.out.println("Greška");
                }
                break;
            case ENUM:
                CompilationProcessor.genEnum = enumBuilder(tlcc, rootModifiers);
                CompilationProcessor.allFoundClasses.add(CompilationProcessor.genEnum);
                try {
                    CompilationProcessor.generatedDiagram.addComponent(CompilationProcessor.genEnum);
                } catch (RuntimeException rtex) {
                    System.out.println("Greška");
                }
                break;
            case INTERFACE:
                CompilationProcessor.genInterface = interfaceBuilder(tlcc, rootModifiers);
                CompilationProcessor.allFoundClasses.add(CompilationProcessor.genInterface);
                try {
                    CompilationProcessor.generatedDiagram.addComponent(CompilationProcessor.genInterface);
                } catch (RuntimeException rtex) {
                    System.out.println("Greška");
                }
                break;
        }
    }

    private static ClassComponent classBuilder(Element elem, Object[] modifierElemnts) {
        String projectPath = elem.toString();
        String className = elem.getSimpleName().toString();
        String packages = projectPath.split(className)[0].substring(0, projectPath.length() - className.length() - 1);
        ClassComponent createdClass = new ClassComponent();
        createdClass.setName(className);
        MemberBuilding.setModifiers(modifierElemnts, createdClass);
        MemberBuilding.packageSelector(createdClass, packages);
        createdClass.setParentDiagram(CompilationProcessor.generatedDiagram);
        createdClass.setPosition(GeneratedDiagramManager.getDefault().getComponentPosition());
        return createdClass;
    }

    private static InterfaceComponent interfaceBuilder(Element elem, Object[] modifierElemnts) {
        String projectPath = elem.toString();
        InterfaceComponent createdInterface = new InterfaceComponent();
        String interfaceName = elem.getSimpleName().toString();
        String packages = projectPath.split(interfaceName)[0].substring(0, projectPath.length() - interfaceName.length() - 1);
        createdInterface.setName(interfaceName);
        MemberBuilding.setModifiers(modifierElemnts, elem);
        MemberBuilding.packageSelector(createdInterface, packages);
        createdInterface.setParentDiagram(CompilationProcessor.generatedDiagram);
        createdInterface.setPosition(GeneratedDiagramManager.getDefault().getComponentPosition());
        return createdInterface;
    }

    private static EnumComponent enumBuilder(Element elem, Object[] modifierElemnts) {
        String projectPath = elem.toString();
        String enumName = elem.getSimpleName().toString();
        String packages = projectPath.split(enumName)[0].substring(0, projectPath.length() - enumName.length() - 1);
        EnumComponent createdEnum = new EnumComponent();
        createdEnum.setName(enumName);
        MemberBuilding.setModifiers(modifierElemnts, elem);
        MemberBuilding.packageSelector(createdEnum, packages);
        createdEnum.setParentDiagram(CompilationProcessor.generatedDiagram);
        createdEnum.setPosition(GeneratedDiagramManager.getDefault().getComponentPosition());
        return createdEnum;
    }

    public static void populateTopElementComponent(Element tlc) {
        int methodCounter = 1;
        int construstorCounter = 1;
        GeneratedDiagramManager.getDefault().resetRelationCounter();
        for (Element el : tlc.getEnclosedElements()) {
            Object[] modifierElemnts = el.getModifiers().toArray();
            switch (tlc.getKind()) {
                case CLASS:
                    switch (el.getKind()) {
                        case FIELD:
                            Field genField = MemberBuilding.fieldBuilder(el, modifierElemnts);
                            CompilationProcessor.genClass.addField(genField);
                            break;
                        case METHOD:
                            Method genMethod = (Method) MemberBuilding.methodAndConstructorBuilder(el, modifierElemnts, true);
                            try {
                                CompilationProcessor.genClass.addMethod(genMethod);
                            } catch (RuntimeException rtex) {
                                genMethod.setName(genMethod.getName() + "/*" + methodCounter + "*/");
                                CompilationProcessor.genClass.addMethod(genMethod);
                                methodCounter++;
                            }
                            break;
                        case CONSTRUCTOR:
                            Object newConstructor = MemberBuilding.methodAndConstructorBuilder(el, modifierElemnts, false);
                            if (newConstructor instanceof String){
                                continue;
                            }
                            Constructor genConstr = (Constructor) newConstructor;
                            try {
                                CompilationProcessor.genClass.addConstructor(genConstr);
                            } catch (RuntimeException rtex) {
                                genConstr.setName(genConstr.getName() + "/*" + construstorCounter + "*/");
                                CompilationProcessor.genClass.addConstructor(genConstr);
                                construstorCounter++;
                            }
                            break;
                    }
                    break;
                case ENUM:
                    switch (el.getKind()) {
                        case ENUM_CONSTANT:
                            Literal genliteral = MemberBuilding.literalBuilder(el, modifierElemnts);
                            CompilationProcessor.genEnum.addLiteral(genliteral);
                            break;
                        case FIELD:
                            Field genField = MemberBuilding.fieldBuilder(el, modifierElemnts);
                            CompilationProcessor.genEnum.addField(genField);
                            break;
                        case METHOD:
                            Method genMethod = (Method) MemberBuilding.methodAndConstructorBuilder(el, modifierElemnts, true);
                            try {
                                CompilationProcessor.genEnum.addMethod(genMethod);
                            } catch (RuntimeException rtex) {
                                genMethod.setName(genMethod.getName() + "/*" + methodCounter + "*/");
                                CompilationProcessor.genEnum.addMethod(genMethod);
                                methodCounter++;
                            }
                            break;
                        case CONSTRUCTOR:
                            Object newConstructor = MemberBuilding.methodAndConstructorBuilder(el, modifierElemnts, false);
                            if (newConstructor instanceof String){
                                continue;
                            }
                            Constructor genConstructor = (Constructor) newConstructor;
                            try {
                                CompilationProcessor.genEnum.addConstructor(genConstructor);
                            } catch (RuntimeException rtex) {
                                genConstructor.setName(genConstructor.getName() + "/*" + construstorCounter + "*/");
                                CompilationProcessor.genEnum.addConstructor(genConstructor);
                                construstorCounter++;
                            }
                    }
                    break;
                case INTERFACE:
                    Method genMethod = (Method) MemberBuilding.methodAndConstructorBuilder(el, modifierElemnts, true);
                    try {
                        CompilationProcessor.genInterface.addMethod(genMethod);
                    } catch (RuntimeException rtex) {
                        genMethod.setName(genMethod.getName() + "/*" + methodCounter + "*/");
                        CompilationProcessor.genInterface.addMethod(genMethod);
                        methodCounter++;
                    }
                    break;
            }
        }
    }
}
