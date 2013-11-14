package org.uml.visual.parser;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import org.uml.model.Visibility;

public class ParserZaWidget {

	Pattern whitespaces = Pattern.compile("\\s*");
	Pattern visibility = Pattern.compile("public|private|protected|package");
	Pattern methodModifiers = Pattern.compile("static|final|abstract|synchronized");
        Pattern fieldModifiers = Pattern.compile("static|final|synchronized");
	Pattern returnValueType = Pattern.compile("(void|int|float|double|boolean)|([A-Z]\\w+)");
	Pattern argumentType = Pattern.compile("(int|float|double|boolean)|([A-Z]\\w+)");
	Pattern arrayCheck = Pattern.compile("(\\[\\s*\\])?");
	Pattern name = Pattern.compile("\\w+");
	Pattern argumentBlock = Pattern.compile("\\(.*\\)");
	String stringToParse = null;
	int[] niz = new int[3];

    public ParserZaWidget(String stringToParse) {
        this.stringToParse = stringToParse;
    }
	
        
        
        
	public void preskociWhitespace() {
		Matcher m = whitespaces.matcher(stringToParse);
		if(m.lookingAt()) {
			stringToParse = stringToParse.substring(m.end());
		}
	}
	
	public String preskociWhitespaceArgs(String text) {
		Matcher m = whitespaces.matcher(text);
		if(m.lookingAt()) {
			text = text.substring(m.end());
		}
		return text;
	}
	
	public Visibility vratiVisibility() {
		String vis = "";
		Matcher m = visibility.matcher(stringToParse);
		if(m.lookingAt()) {
			vis = stringToParse.substring(m.start(),m.end());
			stringToParse = stringToParse.substring(m.end());
		}
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
	
	public String vratiModifier() {
		String result = "";
		Matcher m = methodModifiers.matcher(stringToParse);
		if(m.lookingAt()) {
			result = stringToParse.substring(m.start(),m.end()) + " ";
			stringToParse = stringToParse.substring(m.end());
		}
		return result;
	}
	
	public String vratiName() {
		String result = "";
		Matcher m = name.matcher(stringToParse);
		if(m.lookingAt()) {
			result = stringToParse.substring(m.start(),m.end());
			stringToParse = stringToParse.substring(m.end());
		}
		return result;
	}
	
	
	
	public String vratiReturnType() {
		String result = "";
		Matcher m = returnValueType.matcher(stringToParse);
		if(m.lookingAt()) {
			result = stringToParse.substring(m.start(),m.end());
			stringToParse = stringToParse.substring(m.end());
			preskociWhitespace();
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
		
		return result;
	}
        
        public String vratiArgumentType() {
            String result = "";
		Matcher m = argumentType.matcher(stringToParse);
		if(m.lookingAt()) {
			result = stringToParse.substring(m.start(),m.end());
			stringToParse = stringToParse.substring(m.end());
			preskociWhitespace();
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
		
		return result;
        }
	
	public String vratiArgumente() {
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
	
	public String urediArgumente(String argumenti) {
		String result = "";
		if(argumenti.equals("")) {
			return result;
		}
		String[] args = argumenti.split(",");
		for (int i = 0; i<args.length - 1; i++) {
			result = result.concat(parsirajArgument(args[i])) + ", ";
		}
		result = result.concat(parsirajArgument(args[args.length - 1]));
		return result;
	}
	
	public String parsirajArgument(String argument) {
		String result = "";
		if(argument.equals("")) {
			return result;
		}
		argument = preskociWhitespaceArgs(argument);
		Matcher m = argumentType.matcher(argument);
		if(m.lookingAt()) {
			result = argument.substring(m.start(), m.end()) + " ";
			argument = argument.substring(m.end());
			argument = preskociWhitespaceArgs(argument);
		}else {
			result = "Object ";
		}
		argument = preskociWhitespaceArgs(argument);
		m = name.matcher(argument);
		if(m.lookingAt()) {
			result = result.concat(argument.substring(m.start(), m.end()));
		}else {
			result = result.concat("FaliNazivArgumenta ");
		}
		return result;
	}
	
	public String isparsiraj() {
		String result = "";
		preskociWhitespace();
		result = result.concat(vratiVisibility().toString());
		preskociWhitespace();
		boolean imaModifier = true;
		while(imaModifier) {
			String mod = vratiModifier();
			if(mod.equals("")){
				imaModifier = false;
				result = result.concat(mod);
				break;
			}
			result = result.concat(mod);
			preskociWhitespace();
		}
		result = result.concat(vratiReturnType());
		preskociWhitespace();
		result = result.concat(vratiName());
		preskociWhitespace();
		String argumenti = vratiArgumente();
		if(argumenti.equals("()")) {
			result = result.concat("()");
		}else {
			result = result.concat(urediArgumente(argumenti));
		}
		
		
		
		
		return result;
	}
        
	
   public static void main(String[] args){
    	ParserZaWidget rth = new ParserZaWidget("");
    	
    	Pattern p1 = Pattern.compile("\\s*(public|private|protected)\\s*\\w+\\s*");
    	Matcher m  = p1.matcher(" public void");
    	System.out.println(m.matches());
    	System.out.println("a".substring(1) + "+");
        System.out.println(("a".split(" "))[0]);
        while (true) {
        	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        		
           
			try {
				
  			
				
				 System.out.println("Unesi tekst");
				 rth.stringToParse =br.readLine();
                                 
				System.out.println("Parsirano " + rth.isparsiraj());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
           
            
            

            
       }
    }
}