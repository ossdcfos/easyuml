/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package deprecated;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.openide.util.Exceptions;

/**
 *
 * @author intellex
 */
public class PreMapper {

    private HashMap<String, String[]> classMap = new HashMap<String, String[]>();

    /*public static HashMap<String, HashMap<String, String[]>> getAdditionalData(List<String> filePaths) {

        for (String filePath : filePaths) {
            try {
                lineContentParser(filePath);



            } catch (FileNotFoundException ex) {
                Exceptions.printStackTrace(ex);
                System.out.println("File provided for reading could not be found!");
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
                System.out.println("File provided for line counting could not be found or BufferedReader stream could not be closed!");
            }
        }
    }*/

    private static void lineContentParser(String filePath) throws FileNotFoundException, IOException {
        String tempClassLines = "";
        String tempMethodLines;
        //String tempClassLines;
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;
        int lineCount = countLines(filePath);
        String packageName = "";
        boolean packageFound = false;

        List<String> importList = new ArrayList<String>();
        String tempImport = "";
        boolean importBegun = false;

        for (int i = 0; i <= lineCount; i++) {
            
            line = br.readLine();
            String[] lineElements = line.split(" ");
            List<String> lineElementsList = new ArrayList<String>();
            lineElementsList.addAll(Arrays.asList(lineElements));


            if (!packageFound) {
                if (lineElementsList.contains("package")) {
                    packageName = line.split("package")[1];
                    packageName.trim();
                } else {
                    packageName += line.trim().replace('\n', ' ');
                }
                if (packageName.endsWith(";")) {
                    packageName.replace(";", "");
                    packageFound = true;
                }
                continue;
            }

            if (lineElementsList.contains("import") || importBegun) {
                importBegun = true;
                if (line.contains(";")) {
                    String tempPlus = tempImport + line;
                    String[] imports = tempPlus.split(";");
                    for (int i1 = 0; i < imports.length - 1; i++) {

                        String[] importSubEl = imports[i].split(" ");
                        List<String> importSubElList = new ArrayList<String>();
                        importSubElList.addAll(Arrays.asList(importSubEl));

                        if (importSubElList.contains("import")) {
                            importList.add(imports[i].trim().split("import")[1]);
                            continue;
                        }
                    }
                    if (!imports[imports.length - 1].equals("")) {
                        tempImport = imports[imports.length - 1].replace('\n', ' ');
                    } else {
                        importBegun = false;
                    }
                } else {
                    tempImport += line.trim().replace('\n', ' ');
                }
                continue;
            }

            if (lineElementsList.contains("class")) {
                if (lineElementsList.contains("{")) {
                    if (lineElementsList.contains("implements")) {
                        String implementing = line.split("implements")[1].split(" ")[1];
                        //
                    }
                }
            }

            if (!line.contains(".class") && (line.contains("class") || tempClassLines.length() != 0)) {
                if (line.contains("{")) {
                    String completeLine = tempClassLines + line;
                    classProcessor(line);
                    tempClassLines = "";
                } else {
                    tempClassLines += line;
                    continue;
                }
            }

        }
        br.close();
    }

//    private static String decommenter(String decomment){
//        if (decomment.matches(".*(.*//.*).*"))
//        //SKIP MULTIPLE COMMENT LINES
//        int commentBegun = decomment.indexOf("/*");
//        int commentEnded = decomment.indexOf("*/");
//    }
    private static void classProcessor(String classLine) {
    }

    public static int countLines(String filename) throws IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(filename));
        try {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars = 0;
            boolean empty = true;
            while ((readChars = is.read(c)) != -1) {
                empty = false;
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
            }
            return (count == 0 && !empty) ? 1 : count;
        } finally {
            is.close();
        }
    }
}
