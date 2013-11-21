package org.uml.visual.parser;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import org.uml.model.Field;
import org.uml.model.Method;
import org.uml.model.MethodArgument;
import org.uml.model.Visibility;

public class WidgetParser {

	Pattern whitespaces = Pattern.compile("\\s*");
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
        
	public void skipWhitespaces() {
		Matcher m = whitespaces.matcher(stringToParse);
		if(m.lookingAt()) {
			stringToParse = stringToParse.substring(m.end());
		}
	}
	
	public String skipArgumentWhitespaces(String text) {
		Matcher m = whitespaces.matcher(text);
		if(m.lookingAt()) {
			text = text.substring(m.end());
		}
		return text;
	}
	
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
        
        
        public void fillMethodComponents(Method m, String methodWidgetText) {
            stringToParse = methodWidgetText;
            m.setVisibility(getVisibility());
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
            m.setModifiers(modifiers);
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