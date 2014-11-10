package org.uml.visual.parser;
 
import java.lang.reflect.Modifier;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import org.uml.model.members.Field;
import org.uml.model.members.Member;
import org.uml.model.members.Method;
import org.uml.model.members.MethodArgument;
import org.uml.model.members.Visibility;

public class WidgetParser {

	Pattern whitespaces = Pattern.compile("\\s*");
        Pattern allMethodModifiers = Pattern.compile("public|private|protected|package|static|final|abstract|synchronized"); 
        Pattern allFieldModifiers = Pattern.compile("public|private|protected|package|static|final|synchronized");
	Pattern visibility = Pattern.compile("public|private|protected|package");
	Pattern methodModifiers = Pattern.compile("static|final|abstract|synchronized");
        Pattern fieldModifiers = Pattern.compile("static|final|synchronized");
	Pattern returnValueType = Pattern.compile("(null|void|int|float|double|boolean)|([A-Z]\\w+\\s*(<\\s*\\w+(\\s*,\\s*\\w+\\s*)*\\s*>\\s*)*)");
	Pattern argumentType = Pattern.compile("(null|int|float|double|boolean)|([A-Z]\\w+\\s*(<\\s*([A-Z]\\w+)(\\s*,\\s*([A-Z]\\w+)\\s*)*\\s*>\\s*))|([A-Z]\\w+)");
	Pattern arrayCheck = Pattern.compile("(\\[\\s*\\])?");
	Pattern name = Pattern.compile("\\w+");
	Pattern argumentBlock = Pattern.compile("\\(.*\\)");
	String  stringToParse = null;
	int[] niz = new int[3];
        Method m;
        Field f;
        
        /**
         * Edits stringToParse field. 
         * Removes white spaces from the start of the stringToParse field.
         */
	public void skipWhitespaces() {
		Matcher m = whitespaces.matcher(stringToParse);
		if(m.lookingAt()) {
			stringToParse = stringToParse.substring(m.end());
		}
	}
	/**
         * Edits one method argument and returns argument's type or name.
         * Removes white spaces from the start of the text argument.
         * @param text represents method argument's type and name or just name 
         * @return method argument's type or name
         */
	public String skipArgumentWhitespaces(String text) {
		Matcher m = whitespaces.matcher(text);
		if(m.lookingAt()) {
			text = text.substring(m.end());
		}
		return text;
	}
        
        private int convertStringToModifier(String modifier) {
            if("final".equals(modifier)) {
                return Modifier.FINAL;
            }
            if("static".equals(modifier)) {
                return Modifier.STATIC;
            }
            if("abstract".equals(modifier)) {
                return Modifier.ABSTRACT;
            }
            if("synchronized".equals(modifier)) {
                return Modifier.SYNCHRONIZED;
            }
            return -1;
        }
	/**
         * Parses stringToParse to extract all method modifiers from it.
         * @return string array that represents found modifiers
         */
        public String[] getAllMethodModifiers() {
            String[] result = new String[4];
            boolean hasMoreModifiers = true;
            while(hasMoreModifiers) {
                skipWhitespaces();
                Matcher m = allMethodModifiers.matcher(stringToParse);
                if(m.lookingAt()) {
                    for(int i = 0; i<4; i++) {
                        if(result[i] == null){
                            result[i] = stringToParse.substring(m.start(), m.end());
                            break;
                        }
                    }
                    stringToParse = stringToParse.substring(m.end());
                }else {
                    hasMoreModifiers = false;
                }
            }
            return result;
        }
        /**
         * Parses stringToParse to extract all Field modifiers from it.
         * @return string array that represents found modifiers
         */
        public String[] getAllFieldModifiers() {
            String[] result = new String[3];
            boolean hasMoreModifiers = true;
            while(hasMoreModifiers) {
                skipWhitespaces();
                Matcher m = allFieldModifiers.matcher(stringToParse);
                if(m.lookingAt()) {
                    for(int i = 0; i<3; i++) {
                        if(result[i] == null){
                            result[i] = stringToParse.substring(m.start(), m.end());
                            break;
                        }
                    }
                    stringToParse = stringToParse.substring(m.end());
                }else {
                    hasMoreModifiers = false;
                }
            }
            return result;
        }
        /**
         * Sets m object's  visibility and modifiers fields 
         * @param allModifiers represents all modifiers to put into object m
         */
        public void setMethodModifiers(String[] allModifiers) {
            m.resetModifiers();
            for(int i = 0; i<allModifiers.length;i++) {
                if(allModifiers[i] != null){
                    Matcher m = visibility.matcher(allModifiers[i]);
                    if(m.lookingAt()) {
                        setVisibility(this.m, allModifiers[i]);
                    }else {
                        this.m.addModifier(convertStringToModifier(allModifiers[i]));
                    }
                }
            }
        }
        /**
         * Sets f object's  visibility and modifiers fields 
         * @param allModifiers represents all modifiers to put into object f
         */
        public void setFieldModifiers(String[] allModifiers) {
            f.resetModifiers();
            for(int i = 0; i<allModifiers.length;i++) {
                if(allModifiers[i] != null){
                    Matcher m = visibility.matcher(allModifiers[i]);
                    if(m.lookingAt()) {
                        setVisibility(this.f, allModifiers[i]);
                    }else {
                        f.setModifier(allModifiers[i]);
                    }
                }
            }
        }
        /**
         * Sets object m's visibility field based on the input parameter.
         * @param visibility represents value to put into object m's visibility field.
         */
        public void setVisibility(Member m, String visibility) {
            if(visibility.equals("private")) {
                m.setVisibility(Visibility.PRIVATE);
                return;
            }
            if(visibility.equals("public")) {
                m.setVisibility(Visibility.PUBLIC);
                return;
            }
            if(visibility.equals("protected")) {
                m.setVisibility(Visibility.PROTECTED);
                return;
            }
            m.setVisibility(Visibility.PACKAGE);
        }
        /**
         * Parses stringToParse string to extract visibility. 
         * @return visibility 
         */
	public Visibility getVisibility() {
		String vis = "";
                skipWhitespaces();
		Matcher m = visibility.matcher(stringToParse);
		if(m.lookingAt()) {
			vis = stringToParse.substring(m.start(),m.end());
			stringToParse = stringToParse.substring(m.end());
		}
                skipWhitespaces();
                if(vis.equals("private")) {
                    return Visibility.PRIVATE;
                }
                if(vis.equals("public")) {
                    return Visibility.PUBLIC;
                }
                if(vis.equals("protected")) {
                    return Visibility.PROTECTED;
                }
                if(vis.equals("")) {
                    return Visibility.PACKAGE;
                }
                return null;
	}
	
        /**
         * Parses stringToParse string to extract modifiers.
         * @return modifiers separated by " "
         */
	public String getModifier() {
		String result = "";
                skipWhitespaces();
		Matcher m = methodModifiers.matcher(stringToParse);
		if(m.lookingAt()) {
			result = stringToParse.substring(m.start(),m.end()) + " ";
			stringToParse = stringToParse.substring(m.end());
		}
                skipWhitespaces();
		return result;
	}
	
        /**
         * Parses stringToParse string to extract name.
         * @return name
         */
	public String getName() {
		String result = "";
                skipWhitespaces();
		Matcher m = name.matcher(stringToParse);
		if(m.lookingAt()) {
			result = stringToParse.substring(m.start(),m.end());
			stringToParse = stringToParse.substring(m.end());
		}
                skipWhitespaces();
		return result.trim();
	}
	/**
         * Parses stringToParse to extract return type of a method.
         * @return return type of a method
         */
	public String getReturnType() {
		String result = "";
                skipWhitespaces();
		Matcher m = returnValueType.matcher(stringToParse);
		if(m.lookingAt()) {
			result = stringToParse.substring(m.start(),m.end());
			stringToParse = stringToParse.substring(m.end());
			skipWhitespaces();
                        if (!result.equals("null")) {
                            m = arrayCheck.matcher(stringToParse);
                            if(m.lookingAt()) {
				result = result.concat(stringToParse.substring(m.start(), m.end())) + "";
				stringToParse = stringToParse.substring(m.end());
                            }else {
				result = "void";
                            }
                        }else {
                            result = "void";
                        }
		}else {
                    result = "void";
                }               
		skipWhitespaces();
		return result.trim();
	}
        
        /**
         * Parses stringToParse to extract type of a field
         * @return type of a field
         */
        public String getArgumentType() {
            String result = "";
            Matcher m = argumentType.matcher(stringToParse);
            if(m.lookingAt()) {
		result = stringToParse.substring(m.start(),m.end());
		stringToParse = stringToParse.substring(m.end());
		skipWhitespaces();
                if (!result.equals("null")) {
                    m = arrayCheck.matcher(stringToParse);
                    if(m.lookingAt()) {
                        result = result.concat(stringToParse.substring(m.start(), m.end()));
                        stringToParse = stringToParse.substring(m.end());
                    }else {
                        result = "Object";
                    }
                }else {
                    result = "Object";
                }
            }else {
                result = "Object";
            }               
            skipWhitespaces();
            return result.trim();
        }
	
        /**
         * Parses stringToParse to extract method arguments
         * @return string between parentheses
         */
	public String getArguments() {
		String result = "";
		Matcher m = argumentBlock.matcher(stringToParse);
		if(m.lookingAt()) {
			result = stringToParse.substring(m.start() + 1, m.end() - 1);
			stringToParse = stringToParse.substring(m.end());
		}else {
			result = "";
		}
		return result;
	}
	
        /**
         * Parses the result of the getArguments() method to extract all arguments
         * @param arguments represents all method arguments
         * @return arranged method arguments
         */
	public String formatArguments(String arguments) {
		String result = "";
		if(arguments.equals("")) {
			return result;
		}
		String[] args = arguments.split(",");
		for (int i = 0; i<args.length; i++) {
                    if (i+1 < args.length && args[i].contains("<") && !args[i].contains(">") && args[i+1].contains(">") && !args[i+1].contains("<")) {
                        result = result.concat(parseArgument(args[i] +"," + args[i+1]) + ", ");
                        i++;
                    }else {
                        result = result.concat(parseArgument(args[i])) + ", ";
                    }
                        
			
		}
                //if (!args[args.length - 1].contains(">") || (args[args.length - 1].contains("<") && args[args.length - 1].contains(">"))) result = result.concat(parseArgument(args[args.length - 1]));
                try {
                    result = result.substring(0, result.length() - 2);
                }catch (Exception ex) {
                    
                }
		return result;
	}
	
        /**
         * Parses unordered argument to extract argument name and/or type.
         * If argument type is not found, sets it to Object
         * @param argument represents one method argument
         * @return arranged method argument
         */
	public String parseArgument(String argument) {
		String result = "";
		if(argument.equals("")) {
			return result;
		}
		argument = skipArgumentWhitespaces(argument);
		Matcher m = argumentType.matcher(argument);
		if(m.lookingAt()) {
			result = argument.substring(m.start(), m.end());
                        result = result.trim() + " ";
			argument = argument.substring(m.end());
			argument = skipArgumentWhitespaces(argument);
		}else {
			result = "Object ";
		}
		argument = skipArgumentWhitespaces(argument);
		m = name.matcher(argument);
		if(m.lookingAt()) {
			result = result.concat(argument.substring(m.start(), m.end()));
		}else {
			result = result.concat("ArgumentNameMissing.");
		}
		return result;
	}
        
        /**
         * Sets attributes of the Field object by parsing the fieldWidgetText.
         * @param f represents Field object passed from FieldWidget
         * @param fieldWidgetText represents string typed in the class diagram
         */
        public void fillFieldComponents(Field f, String fieldWidgetText) {
            stringToParse = fieldWidgetText;
            this.f = f;
            setFieldModifiers(getAllFieldModifiers());
            f.setType(getArgumentType());
            f.setName(getName());
            //f.getDeclaringClass().notifyMemberNameChanged(f, f.getName());
        }
        
        /**
         * Sets attributes of the Method object by parsing the methodWidgetText.
         * @param m represents Method object passed from MethodWidget
         * @param methodWidgetText represents string typed in the class diagram
         */
        public void fillMethodComponents(Method m, String methodWidgetText) {
            stringToParse = methodWidgetText;
            this.m = m;
            setMethodModifiers(getAllMethodModifiers());
            m.setReturnType(getReturnType());
            m.setName(getName());
            //m.getDeclaringClass().notifyMemberNameChanged(m, m.getName());
            String arguments = getArguments();
            if(arguments.equals("")) {
		
            }else{
                arguments = formatArguments(arguments);
                String[] args = arguments.split(",");
                if(m.getArguments() != null) {
                    m.getArguments().clear();
                }
                for(int i = 0; i < args.length; i++) {
                    String arg = args[i];
                    if (arg.contains("<") && !args[i].contains(">")) {
                        arg = arg + "," + args[i+1];
                        i++;
                    }
                    skipArgumentWhitespaces(arg);
                    Random r = new Random();
                    int Low = 0;
                    int High = 100;
                    int R = r.nextInt(High - Low) + Low;
                    m.getArguments().put(Integer.toString(R), new MethodArgument(arg.substring(0,arg.indexOf(" ")).trim(), arg.substring(arg.indexOf(" ") + 1).trim()));
                }
            }
        }
}