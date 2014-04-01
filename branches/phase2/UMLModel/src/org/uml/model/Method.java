package org.uml.model;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of Method, one of 4 possible Members.
 * @author Uros
 * @see Member
 * @see Field
 * @see Literal
 * @see Constructor
 */
public class Method extends Member {
    
    private String returnType;
    private HashMap <String,MethodArgument> arguments;
    private int[] modifiers = new int[4];

    public Method(String name, String returnType, HashMap<String,MethodArgument> arguments) {
        super(name);
        this.returnType = returnType;
        this.arguments = arguments;
        visibility = Visibility.PUBLIC;
        //this.returnType="void";
    }

    public Method(String name) {
        super(name);
        arguments = new HashMap<String, MethodArgument>();
    }
    

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public HashMap<String, MethodArgument> getArguments() {
        return arguments;
    }
    
    public void setArguments(HashMap<String, MethodArgument> arguments) {
        this.arguments = arguments;
    }

    public void addModifier(int modifier) {
        boolean isModifierAllreadyAdded = false;
        int emptySpot = -1;
        for(int i = 0; i<modifiers.length; i++) {
            if(modifiers[i] <= 0 && emptySpot == -1) {
                emptySpot = i;
            }
            if(modifiers[i] == modifier) {
                isModifierAllreadyAdded = true;
                break;
            }
        }
        if(!isModifierAllreadyAdded) {
            modifiers[emptySpot] = modifier;
        }
    }
    
    public void deleteModifier(int modifier) {
        for(int i = 0; i<modifiers.length; i++) {
            if(modifiers[i] == modifier) {
                modifiers[i] = -1;
            }
        }
    }
    
    public int[] getModifiers() {
        return modifiers;
    }
    
    public String getModifiersAsString() {
        String result = "";
        for(int i = 0; i < modifiers.length; i++) {
            if(modifiers[i] > 0) {
                result = result.concat(Modifier.toString(modifiers[i]) + " ");
            }
        }
        return result;
    }

    public void setModifiers(int[] modifiers) {
        this.modifiers = modifiers;
    }
    
    public void resetModifiers() {
        for(int i = 0; i< modifiers.length; i++) {
            modifiers[i] = -1;
        }
    }
    
    public boolean hasConcreteModifier(String modifier) {
        boolean hasModifier = false;
        for(int i = 0; i<modifiers.length; i++) {
            if(modifiers[i] > 0 && Modifier.toString(modifiers[i]).equals(modifier)) {
                hasModifier = true;
                break;
            }
        }
        return hasModifier;
    }
    
    public String getSignatureForLabel() {
        String result = "";
        for(int i = 0; i < 4 ; i++) {
            if(modifiers[i] > 0) {
                result = result.concat(Modifier.toString(modifiers[i]) + " ");
            }
        }
        
        result = result.concat(returnType + " ");
        result = result.concat(getName() + "(");
        String args = "";
        if(getArguments()!=null){
            for (Map.Entry<String, MethodArgument> entry1 : getArguments().entrySet()) {
                MethodArgument argument = entry1.getValue();
                args+= argument.getType() + " " + argument.getName()+ ", ";
            }
        }
        if(!args.equals("")){
            args = args.substring(0,args.length()-2);
        }
        result = result.concat(args + ")");
        return result;
    }
    
    public String getSignature() {
        String result = "";
        if(visibility != null && !visibility.PACKAGE.equals(visibility)) {
            result = result.concat(getVisibility().toString() + " ");
        }
        for(int i = 0; i < 4 ; i++) {
            if(modifiers[i] > 0) {
                result = result.concat(Modifier.toString(modifiers[i]) + " ");
            }
        }
        
        result = result.concat(returnType + " ");
        result = result.concat(getName() + "(");
        String args = "";
        if(getArguments()!=null){
            for (Map.Entry<String, MethodArgument> entry1 : getArguments().entrySet()) {
                MethodArgument argument = entry1.getValue();
                args+= argument.getType() + " " + argument.getName()+ ", ";
            }
        }
        if(!args.equals("")){
            args = args.substring(0,args.length()-2);
        }
        result = result.concat(args + ") {} \n");
        return result;
    }

 
}
