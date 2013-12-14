package org.uml.visual.parser;
 
import java.util.Random;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import org.uml.model.Field;
import org.uml.model.Method;
import org.uml.model.MethodArgument;
import org.uml.model.Visibility;

public class WidgetParser {

	Pattern whitespaces = Pattern.compile("\\s*");
        Pattern allMethodModifiers = Pattern.compile("public|private|protected|package|static|final|abstract|synchronized"); 
	Pattern visibility = Pattern.compile("public|private|protected|package");
	Pattern methodModifiers = Pattern.compile("static|final|abstract|synchronized");
        Pattern fieldModifiers = Pattern.compile("static|final|synchronized");
	Pattern returnValueType = Pattern.compile("(void|int|float|double|boolean)|([A-Z]\\w+)");
	Pattern argumentType = Pattern.compile("(int|float|double|boolean)|([A-Z]\\w+)");
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
	
        public String[] getAllMethodModifiers() {
            String[] result = new String[5];
            boolean hasMoreModifiers = true;
            while(hasMoreModifiers) {
                skipWhitespaces();
                Matcher m = allMethodModifiers.matcher(stringToParse);
                if(m.lookingAt()) {
                    for(int i = 0; i<5; i++) {
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
        
        public void setMethodModifiers() {
            String[] allModifiers = getAllMethodModifiers();
            String modifiers = "";
            for(int i = 0; i<allModifiers.length;i++) {
                if(allModifiers[i] != null){
                    Matcher m = visibility.matcher(allModifiers[i]);
                    if(m.lookingAt()) {
                        setVisibility(allModifiers[i]);
                    }else {
                        
                        modifiers = modifiers.concat(allModifiers[i]) + " ";
                    }
                }
                
            }
            m.setModifiers(modifiers);
        }
        public void setVisibility(String visibility) {
            if(visibility.equals("private")) {
                m.setVisibility(Visibility.PRIVATE);
            }
            if(visibility.equals("public")) {
                m.setVisibility(Visibility.PUBLIC);
            }
            if(visibility.equals("protected")) {
                m.setVisibility(Visibility.PROTECTED);
            }
            if(visibility.equals("")) {
                m.setVisibility(Visibility.PACKAGE);
            }
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
		return result;
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
			m = arrayCheck.matcher(stringToParse);
			if(m.lookingAt()) {
				result = result.concat(stringToParse.substring(m.start(), m.end())) + " ";
				stringToParse = stringToParse.substring(m.end());
			}else {
				result = result.concat(" ");
			}
		}else {
                    result = "void ";
                }               
		skipWhitespaces();
		return result;
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
		m = arrayCheck.matcher(stringToParse);
		if(m.lookingAt()) {
                    result = result.concat(stringToParse.substring(m.start(), m.end())) + " ";
                    stringToParse = stringToParse.substring(m.end());
		}else {
                    result = result.concat(" ");
		}
            }else {
                result = "Object ";
            }               
            skipWhitespaces();
            return result;
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
		for (int i = 0; i<args.length - 1; i++) {
			result = result.concat(parseArgument(args[i])) + ", ";
		}
		result = result.concat(parseArgument(args[args.length - 1]));
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
			result = argument.substring(m.start(), m.end()) + " ";
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
			result = result.concat("FaliNazivArgumenta ");
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
            f.setVisibility(getVisibility());
            String modifiers = "";
            boolean imaModifier = true;
            while(imaModifier) {
		String mod = getModifier();
		if(mod.equals("")){
                    imaModifier = false;
                    modifiers += mod;
                    break;
		}
		modifiers += mod + " ";
            }
            f.setModifiers(modifiers);
            getArgumentType();//ne znam kako da konvertujem string u Type?
            f.setName(getName());
        }
        
        /**
         * Sets attributes of the Method object by parsing the methodWidgetText.
         * @param m represents Method object passed from MethodWidget
         * @param methodWidgetText represents string typed in the class diagram
         */
        public void fillMethodComponents(Method m, String methodWidgetText) {
            stringToParse = methodWidgetText;
            this.m = m;
//            m.setVisibility(getVisibility());
//            String modifiers = "";
//            boolean imaModifier = true;
//            while(imaModifier) {
//		String mod = getModifier();
//		if(mod.equals("")){
//                    imaModifier = false;
//                    modifiers += mod;
//                    break;
//		}
//		modifiers += mod + " ";
//            }
//            m.setModifiers(modifiers);
            setMethodModifiers();
            m.setReturnType(getReturnType());
            m.setName(getName());
            String argumenti = getArguments();
            if(argumenti.equals("")) {
		m.setArguments(null);
            }else{
                argumenti = formatArguments(argumenti);
                String[] args = argumenti.split(", ");
                for(String arg : args) {
                    Random r = new Random();
                    int Low = 0;
                    int High = 100;
                    int R = r.nextInt(High - Low) + Low;
                    m.getArguments().put(Integer.toString(R), new MethodArgument(arg.substring(0,arg.indexOf(" ")), arg.substring(arg.indexOf(" ") + 1)));
                }
            }
        }
}